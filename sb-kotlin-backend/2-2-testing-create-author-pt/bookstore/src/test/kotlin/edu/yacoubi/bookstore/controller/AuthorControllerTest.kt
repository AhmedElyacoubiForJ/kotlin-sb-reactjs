package edu.yacoubi.bookstore.controller

import com.fasterxml.jackson.databind.ObjectMapper
import edu.yacoubi.bookstore.domain.dto.AuthorDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

// This is a test class for the AuthorController in the Bookstore application.
// It is annotated with @SpringBootTest to load the entire application context,
// which is necessary for testing controllers that rely on Spring Boot's features.
// @AutoConfigureMockMvc is used to automatically configure MockMvc,
// which is a powerful tool for testing Spring MVC applications.
// MockMvc can be thought of as a client that simulates HTTP requests and responses
@SpringBootTest
@AutoConfigureMockMvc
class AuthorControllerTest @Autowired constructor(private val mockMvc: MockMvc) {

    val objectMapper: ObjectMapper get() = ObjectMapper()

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
}