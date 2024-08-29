package edu.yacoubi.bookstore.service.impl

import edu.yacoubi.bookstore.domain.entities.BookEntity
import edu.yacoubi.bookstore.repository.AuthorRepository
import edu.yacoubi.bookstore.repository.BookRepository
import edu.yacoubi.bookstore.testAuthorEntityA
import edu.yacoubi.bookstore.testAuthorSummaryDtoA
import edu.yacoubi.bookstore.testBookSummaryDtoA
import edu.yacoubi.bookstore.toBookSummary
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
//@DirtiesContext(
//    classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD
//)
@Transactional
class BookServiceImplTest @Autowired constructor(
    private val underTest: BookServiceImpl,
    private val bookRepository: BookRepository,
    private val authorRepository: AuthorRepository
) {
    @Test
    fun `test that createOrUpdate returns IllegalStateException when author doesn't exists in the database`() {
        // Given
        val isbn = "577-812-123548-911"
        val nonExistingAuthorId = -1L
        val bookSummaryDto = testBookSummaryDtoA(
            isbn,
            testAuthorSummaryDtoA(id = nonExistingAuthorId)
        )
        val bookSummary = bookSummaryDto.toBookSummary()

        // When & Then
        assertThrows<IllegalStateException> {
            underTest.createOrUpdate(isbn = isbn, bookSummary)
        }
    }

    @Test
    fun `test that createOrUpdate returns a pair of BookEntity and true when book is created`() {
        // Given
        val isbn = "577-812-123548-911"
        val existingAuthor = authorRepository.save(testAuthorEntityA())
        val bookSummaryDto = testBookSummaryDtoA(
            isbn,
            testAuthorSummaryDtoA(id = existingAuthor.id!!)
        )
        val bookSummary = bookSummaryDto.toBookSummary()

        // When
        val result = underTest.createOrUpdate(isbn = isbn, bookSummary)
        val recalledBook = bookRepository.findByIdOrNull(isbn)

        // Then
        assertThat(recalledBook).isNotNull()
        assertThat(recalledBook).isEqualTo(result.first)
        assertThat(result.first).isInstanceOf(BookEntity::class.java)
        assertThat(result.first).isEqualTo(
            BookEntity(
                isbn = isbn,
                title = bookSummary.title,
                description = bookSummary.description,
                image = bookSummary.image,
                authorEntity = existingAuthor
            )
        )
        //assertThat(result.first.authorEntity).isEqualTo(existingAuthor)
        assertThat(result.second).isTrue()
    }

    @Test
    fun `test that createOrUpdate returns a pair of BookEntity and false when book is updated`() {
        // Given
        val isbn = "577-812-123548-911"
        val existingAuthor = authorRepository.save(testAuthorEntityA())
        assertThat(existingAuthor).isNotNull()
        val existingBook = bookRepository.save(
            BookEntity(
                isbn = isbn,
                title = "Original Title",
                description = "Original Description",
                image = "Original Image",
                authorEntity = existingAuthor
            )
        )
        assertThat(existingBook).isNotNull()
        val updatedTitle = "Updated Title"
        val updatedDescription = "Updated Description"
        val updatedImage = "Updated Image"
        val updatedBookSummaryDto = testBookSummaryDtoA(
            isbn,
            testAuthorSummaryDtoA(id = existingAuthor.id!!)
        ).copy(title = updatedTitle, description = updatedDescription, image = updatedImage)
        val updatedBookSummary = updatedBookSummaryDto.toBookSummary()

        // When
        val result = underTest.createOrUpdate(isbn = isbn, updatedBookSummary)
        val recalledBook = bookRepository.findByIdOrNull(isbn)

        // Then
        assertThat(recalledBook).isNotNull()
        assertThat(recalledBook).isEqualTo(result.first)
        assertThat(result.first).isInstanceOf(BookEntity::class.java)
        assertThat(result.first).isEqualTo(
            BookEntity(
                isbn = isbn,
                title = updatedTitle,
                description = updatedDescription,
                image = updatedImage,
                authorEntity = existingAuthor
            )
        )
        assertThat(result.second).isFalse()
    }
}