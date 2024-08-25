package edu.yacoubi.bookstore.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import edu.yacoubi.bookstore.domain.entities.AuthorEntity
import edu.yacoubi.bookstore.service.IAuthorService
import edu.yacoubi.bookstore.testAuthorDtoA
import edu.yacoubi.bookstore.testAuthorEntityA
import edu.yacoubi.bookstore.testAuthorEntityB
import io.mockk.every
import io.mockk.verify
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

private const val AUTHORS_BASE_URL = "/v1/authors"

@SpringBootTest
@AutoConfigureMockMvc
class AuthorControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    @MockkBean val authorService: IAuthorService
) {

    val objectMapper: ObjectMapper get() = ObjectMapper()

    @BeforeEach
    fun beforeEach() {
        every {
            authorService.save(any())
        } answers {
            firstArg()
        }
    }

    @Test
    fun `test that create Author returns a HTTP 201 as status`() {
        mockMvc.post(AUTHORS_BASE_URL) {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                testAuthorDtoA()
            )
        }.andExpect {
            status { isCreated() }
        }
    }

    @Test
    fun `test create Author saves the Author`() {
        // Given
        // When
        mockMvc.post(AUTHORS_BASE_URL) {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                testAuthorDtoA()
            )
        }

        val expected = AuthorEntity(
            id = null,
            name = "John Doe",
            age = 30,
            description = "Author A description",
            image = "author-a-image.jpeg"
        )

        // Then
        verify { authorService.save(eq(expected)) }
    }

    @Test
    fun `test that list authors returns an empty list and HTTP status 200 when no authors in the database`() {
        // every { authorService.list() } returns emptyList()
        every {
            authorService.list()
        } answers {
            emptyList()
        }
        // equivalent to
        //// Given
        //    given(authorService.list()).willReturn(Collections.emptyList());

        // When
        mockMvc.get(AUTHORS_BASE_URL) {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { json("[]") }
            jsonPath("$.length()").value(0)
        }
        /*
        * // When
            MvcResult result = mockMvc.perform(get("/v1/authors")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json("[]"))
            .andExpect(jsonPath("$.length()", is(0)))
            .andReturn();
        * */
    }

    @Test
    fun `test that list authors returns a list of authors and HTTP status 200`() {
        // Given
        val authors = listOf(testAuthorEntityA(1), testAuthorEntityB(2))
        every { authorService.list() } answers { authors }

        // When & Then
        mockMvc.get(AUTHORS_BASE_URL) {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content {
                objectMapper.writeValueAsString(authors)
//                json(
//                    """
//                    [
//                      {
//                        "id": 1,
//                        "name": "John Doe",
//                        "age": 30,
//                        "description": "Author A description",
//                        "image": "author-a-image.jpeg"
//                      },
//                      {
//                        "id": 2,
//                        "name": "Paul Franklin",
//                        "age": 35,
//                        "description": "Author B description",
//                        "image": "author-b-image.jpeg"
//                      }
//                    ]
//                    """.trimIndent()
//                )
                jsonPath("$.length()").value(2)

                jsonPath("$.[0].id").value(1)
                jsonPath("$.[0].id", equalTo(1)) // or w. hamcrest Matchers
                jsonPath("$.[0].name").value("John Doe")
                jsonPath("$.[0].name", equalTo("John Doe")) // or w. hamcrest Matchers
                jsonPath("$.[0].age").value(30)
                jsonPath("$.[0].age", equalTo(30)) // or w. hamcrest Matchers
                jsonPath("$.[0].description").value("Author A description")
                jsonPath("$.[0].description", equalTo("Author A description"))  //or w. hamcrest Matchers
                jsonPath("$.[0].image").value("author-a-image.jpeg")
                jsonPath("$.[0].image", equalTo("author-a-image.jpeg"))  // or w. hamcrest Matchers

                jsonPath("$.[1].id").value(2)
                jsonPath("$.[1].id", equalTo(2)) // or w. hamcrest Matchers
                jsonPath("$.[1].name").value("Paul Franklin")
                jsonPath("$.[1].name", equalTo("Paul Franklin")) // or w. hamcrest Matchers
                jsonPath("$.[1].age").value(35)
                jsonPath("$.[1].age", equalTo(35)) // or w. hamcrest Matchers
                jsonPath("$.[1].description").value("Author B description")
                jsonPath("$.[1].description", equalTo("Author B description"))  // or w. hamcrest Matchers
                jsonPath("$.[1].image").value("author-b-image.jpeg")
                jsonPath("$.[1].image", equalTo("author-b-image.jpeg"))  // or w. hamcrest Matchers
            }
        }
    }

    @Test
    fun `test that get Author by id returns HTTP status 404 when author not found in the database`() {
        // Given mock with exceptions
        every { authorService.getAuthor(any()) } answers { null }
        //every { authorService.getAuthor(1) } returns null

        // When & Then
        mockMvc.get("$AUTHORS_BASE_URL/{id}", -1) { // or "${AUTHOR_BASE_URL}/-1"
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNotFound() } // isOk() failing test
        }
    }

    @Test
    fun `test that get Author by id returns the Author and HTTP status 200`() {
        // Given
        val author = testAuthorEntityA(1)
        every { authorService.getAuthor(any()) } answers { author }

        // When & Then
        mockMvc.get("$AUTHORS_BASE_URL/{id}", 1) { // or "${AUTHOR_BASE_URL}/1"
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content {
                objectMapper.writeValueAsString(author)
                //jsonPath("$.id").value(1)
                jsonPath("$.id", equalTo(1)) // or w. hamcrest Matchers
                //jsonPath("$.name").value("John Doe")
                jsonPath("$.name", equalTo("John Doe")) // or w. hamcrest Matchers
                //jsonPath("$.age").value(30)
                jsonPath("$.age", equalTo(30)) // or w. hamcrest Matchers
                //jsonPath("$.description").value("Author A description")
                jsonPath("$.description", equalTo("Author A description"))  // or w. hamcrest Matchers
                //jsonPath("$.image").value("author-a-image.jpeg")
                jsonPath("$.image", equalTo("author-a-image.jpeg"))  // or w. hamcrest Matchers
            }
        }
    }
}