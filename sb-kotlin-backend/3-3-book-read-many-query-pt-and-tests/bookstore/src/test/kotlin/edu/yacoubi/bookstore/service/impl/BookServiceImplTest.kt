package edu.yacoubi.bookstore.service.impl

import edu.yacoubi.bookstore.*
import edu.yacoubi.bookstore.domain.entities.BookEntity
import edu.yacoubi.bookstore.repository.AuthorRepository
import edu.yacoubi.bookstore.repository.BookRepository
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest
@DirtiesContext(
    classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD
)
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

    @Test
    fun `test get all books returns an empty list when no books in the database`() {
        // Given
        // When
        val result = underTest.getAllBooks()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `test get all books returns all books from the database`() {
        // Given
        val existingAuthorA = authorRepository.save(testAuthorEntityA(1))
        assertThat(existingAuthorA).isNotNull()
        val existingBookA = bookRepository.save(
            testBookEntityA("577-812-123548-911", existingAuthorA)
        )
        assertThat(existingBookA).isNotNull()
        val existingAuthorB = authorRepository.save(testAuthorEntityA(id = 2))
        assertThat(existingAuthorB).isNotNull()
        val existingBookB = bookRepository.save(
            testBookEntityB("577-812-123548-912", existingAuthorB)
        )
        assertThat(existingBookB).isNotNull()

        // When
        val result = underTest.getAllBooks()

        // Then
        assertThat(result).hasSize(2)
        assertThat(result).containsExactly(existingBookA, existingBookB)
    }

    @Test
    fun `test that get all books returns no authors when the author ID doesn't match`(){
        // Given
        val existingAuthorA = authorRepository.save(testAuthorEntityA(1L))
        assertThat(existingAuthorA).isNotNull()
        val existingBookA = bookRepository.save(
            testBookEntityA("577-812-123548-911", existingAuthorA)
        )
        assertThat(existingBookA).isNotNull()
        val nonExistingAuthorId = 3L

        // When
        val result = underTest.getAllBooks(authorId = nonExistingAuthorId)

        // Then
        assertThat(result).hasSize(0)
    }

    @Test
    fun `test that get all books returns books for the author when the author ID matches`(){
        // Given
        val existingAuthorA = authorRepository.save(testAuthorEntityA(1))
        assertThat(existingAuthorA).isNotNull()
        val existingBookA = bookRepository.save(
            testBookEntityA("577-812-123548-911", existingAuthorA)
        )
        assertThat(existingBookA).isNotNull()
        val existingAuthorB = authorRepository.save(testAuthorEntityA(id = 2))
        assertThat(existingAuthorB).isNotNull()
        val existingBookB = bookRepository.save(
            testBookEntityB("577-812-123548-912", existingAuthorB)
        )
        assertThat(existingBookB).isNotNull()

        // When
        val resultA = underTest.getAllBooks(authorId = existingAuthorA.id!!)
        val resultB = underTest.getAllBooks(authorId = existingAuthorB.id!!)

        // Then
        assertThat(resultA).hasSize(1)
        assertThat(resultA[0]).isEqualTo(existingBookA)
        assertThat(resultB).hasSize(1)
        assertThat(resultB[0]).isEqualTo(existingBookB)
    }
}