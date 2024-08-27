package edu.yacoubi.bookstore.service.impl

import edu.yacoubi.bookstore.domain.AuthorUpdateRequest
import edu.yacoubi.bookstore.domain.entities.AuthorEntity
import edu.yacoubi.bookstore.repository.AuthorRepository
import edu.yacoubi.bookstore.testAuthorEntityA
import edu.yacoubi.bookstore.testAuthorEntityB
import edu.yacoubi.bookstore.testAuthorUpdateRequestA
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
class AuthorServiceImplTest @Autowired constructor(
    private val underTest: AuthorServiceImpl,
    private val authorRepository: AuthorRepository) {

    @Test
    fun `test that create author should persist the author in the database`() {
        val savedAuthor = underTest.create(testAuthorEntityA())
        assertThat(savedAuthor.id).isNotNull()

        val retrievedAuthor = authorRepository.findByIdOrNull(savedAuthor.id!!)
        assertThat(retrievedAuthor).isNotNull()
        assertThat(retrievedAuthor).isEqualTo(testAuthorEntityA(savedAuthor.id))
    }

    @Test
    fun `test that create author with an ID should throws an IllegalArgumentException`() {
        assertThrows<IllegalArgumentException> {
            underTest.create(testAuthorEntityA(999))
        }
    }

    @Test
    fun `test that list authors should returns empty list when no authors in the database`() {
        val result = underTest.list()
        // assertThat(result).isNotEmpty() failing test
        assertThat(result).isEmpty()
    }

    @Test
    fun `test that list authors should return all authors from the database`() {
        val savedAuthorA = underTest.create(testAuthorEntityA())
        val savedAuthorB = underTest.create(testAuthorEntityB())
        val expected = listOf(savedAuthorA, savedAuthorB)

        val result = underTest.list();
        assertThat(result).hasSize(2)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `test that get author by id returns null when author no present in the database`() {
        val result = underTest.getAuthor(-1)
        assertThat(result).isNull()
    }

    @Test
    fun `test that get author by id returns the author when author is present in the database`() {
        // Given
        val savedAuthor = authorRepository.save(testAuthorEntityA())

        // when
        val result = underTest.getAuthor(savedAuthor.id!!)

        // then
        assertThat(result).isEqualTo(savedAuthor)
    }

    @Test
    fun `test that full update author should update the author in the database`() {
        // Given
        val existingAuthor = authorRepository.save(testAuthorEntityA())
        val existingAuthorId = existingAuthor.id!!
        val updatedAuthor = AuthorEntity(
            id = existingAuthorId,
            name = "Updated name",
            age = 99,
            description = "This is an updated description",
            image = "updated-image.jpg"
        )

        // When
        var result = underTest.fullUpdate(existingAuthorId, updatedAuthor)
        val retrievedAuthor = authorRepository.findByIdOrNull(existingAuthorId)

        // Then
        assertThat(result).isEqualTo(updatedAuthor)
        assertThat(retrievedAuthor).isNotNull()
    }

    @Test
    fun `test that full update author should throws an IllegalStateException when author doesn't exist in the database`() {
        // Given
        val nonExistingAuthorId = -1L
        val updatedAuthor = testAuthorEntityB(id = nonExistingAuthorId)

        // When & Then
        assertThrows<IllegalStateException> {
            underTest.fullUpdate(nonExistingAuthorId, updatedAuthor)
        }
    }

    @Test
    fun `test that partial update author throws an IllegalStateException when author doesn't exist in the database`() {
        // Given
        val nonExistingAuthorId = -1L
        val updatedRequest = testAuthorUpdateRequestA(id = nonExistingAuthorId)

        // When & Then
        assertThrows<IllegalStateException> {
            underTest.partialUpdate(nonExistingAuthorId, updatedRequest)
        }
    }

    @Test
    fun `test that partial author does not update Author when all values are null`() {
        // Given
        val existingAuthor = authorRepository.save(testAuthorEntityA())
        val existingAuthorId = existingAuthor.id!!

        // When
        var result = underTest.partialUpdate(existingAuthorId, AuthorUpdateRequest())
        // Then
        assertThat(result).isEqualTo(existingAuthor)
    }

    @Test
    fun `test that partial update author should update only the specified name field`() {
        // Given
        val existingAuthor = authorRepository.save(testAuthorEntityA())
        val existingAuthorId = existingAuthor.id!!
        val newName = "UPDATED"
        val expected = existingAuthor.copy(
            name= newName
        )

        // When
        var result = underTest.partialUpdate(existingAuthorId, AuthorUpdateRequest(
            name= newName
        ))

        // Then
        assertThat(result).isEqualTo(expected)

        val retrievedAuthor = authorRepository.findByIdOrNull(existingAuthorId)
        assertThat(retrievedAuthor).isNotNull()

        assertThat(retrievedAuthor).isEqualTo(expected)
    }

    @Test
    fun `test that partial update author should update only the specified age field`() {
        // Given
        val existingAuthor = authorRepository.save(testAuthorEntityA())
        val existingAuthorId = existingAuthor.id!!
        val newAge = 99
        val expected = existingAuthor.copy(
            age= newAge
        )

        // When
        var result = underTest.partialUpdate(existingAuthorId, AuthorUpdateRequest(
            age= newAge
        ))

        // Then
        assertThat(result).isEqualTo(expected)

        val retrievedAuthor = authorRepository.findByIdOrNull(existingAuthorId)
        assertThat(retrievedAuthor).isNotNull()

        assertThat(retrievedAuthor).isEqualTo(expected)
    }

    @Test
    fun `test that partial update author should update only the specified description field`() {
        // Given
        val existingAuthor = authorRepository.save(testAuthorEntityA())
        val existingAuthorId = existingAuthor.id!!
        val newDescription = "This is an updated description"
        val expected = existingAuthor.copy(
            description= newDescription
        )

        // When
        var result = underTest.partialUpdate(existingAuthorId, AuthorUpdateRequest(
            description= newDescription
        ))

        // Then
        assertThat(result).isEqualTo(expected)

        val retrievedAuthor = authorRepository.findByIdOrNull(existingAuthorId)
        assertThat(retrievedAuthor).isNotNull()

        assertThat(retrievedAuthor).isEqualTo(expected)
    }

    @Test
    fun `test that partial update author should update only the specified image field`() {
        // Given
        val existingAuthor = authorRepository.save(testAuthorEntityA())
        val existingAuthorId = existingAuthor.id!!
        val newImage = "updated-image.jpg"
        val expected = existingAuthor.copy(
            image= newImage
        )

        // When
        var result = underTest.partialUpdate(existingAuthorId, AuthorUpdateRequest(
            image= newImage
        ))

        // Then
        assertThat(result).isEqualTo(expected)

        val retrievedAuthor = authorRepository.findByIdOrNull(existingAuthorId)
        assertThat(retrievedAuthor).isNotNull()

        assertThat(retrievedAuthor).isEqualTo(expected)
    }

    @Test
    fun `test that partial update author should update the author in the database`() {
        // Given
        val existingAuthor = authorRepository.save(testAuthorEntityA())
        val existingAuthorId = existingAuthor.id!!
        val updatedRequest = testAuthorUpdateRequestA(id = existingAuthorId).copy(name="UPDATED")
        val expected = testAuthorEntityA(id = existingAuthorId).copy(name="UPDATED")

        // When
        var result = underTest.partialUpdate(existingAuthorId, updatedRequest)
        val retrievedAuthor = authorRepository.findByIdOrNull(existingAuthorId)

        // Then
        assertThat(result).isEqualTo(expected)
        assertThat(retrievedAuthor).isNotNull()
    }
}