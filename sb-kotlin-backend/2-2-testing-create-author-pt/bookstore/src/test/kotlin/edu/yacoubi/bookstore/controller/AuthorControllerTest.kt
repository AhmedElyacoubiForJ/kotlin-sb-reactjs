package edu.yacoubi.bookstore.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import edu.yacoubi.bookstore.domain.dto.AuthorDto
import edu.yacoubi.bookstore.domain.entities.AuthorEntity
import edu.yacoubi.bookstore.service.IAuthorService
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class AuthorControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    @MockkBean val authorService: IAuthorService
) {

    val objectMapper: ObjectMapper get() = ObjectMapper()

    @BeforeEach
    fun beforeEach() {
        every { authorService.save(any()) } answers { firstArg() }
    }

    @Test
    fun `test that create Author returns a HTTP 201 as status`() {
        mockMvc.post("/v1/authors") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                AuthorDto(
                    id = null,
                    name = "John Doe",
                    age = 42,
                    description = "Author of the best books ever written.",
                    image = "https://example.com/author-image.jpg"
                )
            )
        }.andExpect {
            status { isCreated() }
        }
    }

    @Test
    fun `test create Author saves the Author`() {
        // Given
        val authorDto = AuthorDto(
            id = null,
            name = "John Doe",
            age = 42,
            description = "Author of the best books ever written.",
            image = "https://example.com/author-image.jpg"
        )

        // When
        mockMvc.post("/v1/authors") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(authorDto)
        }

        val expected = AuthorEntity(
            id = null,
            name = "John Doe",
            age = 42,
            description = "Author of the best books ever written.",
            image = "https://example.com/author-image.jpg"
        )

        // Then
        verify { authorService.save(eq(expected)) }
    }
}