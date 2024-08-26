package edu.yacoubi.bookstore.service.impl

import edu.yacoubi.bookstore.repository.AuthorRepository
import edu.yacoubi.bookstore.testAuthorEntityA
import edu.yacoubi.bookstore.testAuthorEntityB
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
}