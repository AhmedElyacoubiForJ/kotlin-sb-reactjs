package edu.yacoubi.bookstore.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import edu.yacoubi.bookstore.*
import edu.yacoubi.bookstore.service.IBookService
import io.mockk.every
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.result.StatusResultMatchersDsl

private const val BOOKS_BASE_URL = "/v1/books"

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    @MockkBean val bookService: IBookService
) {
    val objectMapper = ObjectMapper()

    @Test
    fun `test that create or full update returns HTTP status 201 when book is created`() {
        assertThatBookCreatedOrUpdated(true) { isCreated() }
    }

    @Test
    fun `test that create or full update returns HTTP status 200 when book is successfully updated`() {
        assertThatBookCreatedOrUpdated(false) { isOk() }
    }

    private fun assertThatBookCreatedOrUpdated(
        isCreated: Boolean,
        statusCodeAssertion: StatusResultMatchersDsl.() -> Unit
    ) {
        // Given
        val isbn = "577-812-123548-911"
        val author = testAuthorEntityA(id = 1)
        val savedBook = testBookEntityA(isbn, author)

        val authorSummaryDto = testAuthorSummaryDtoA(id = 1)
        val bookSummaryDto = testBookSummaryDtoA(isbn, authorSummaryDto)

        every {
            bookService.createOrUpdate(isbn, any())
        } answers {
            Pair(savedBook, isCreated)
        }

        // When
        val result = mockMvc.put("$BOOKS_BASE_URL/{isbn}", isbn) {
            contentType = APPLICATION_JSON
            accept = APPLICATION_JSON
            content = objectMapper.writeValueAsString(bookSummaryDto)
        }

        // Then
        result.andExpect {
            status { statusCodeAssertion() }
        }
    }

    @Test
    fun `test that create or full update returns HTTP status 500 when author in the database doesn't have an ID`() {
        // Given
        val isbn = "577-812-123548-911"
        val author = testAuthorEntityA()
        val savedBook = testBookEntityA(isbn, author)

        every {
            bookService.createOrUpdate(isbn, any())
        } answers {
            Pair(savedBook, true)
        }

        val authorSummaryDto = testAuthorSummaryDtoA(id = 1)
        val bookSummaryDto = testBookSummaryDtoA(isbn, authorSummaryDto)

        // When
        val result = mockMvc.put("$BOOKS_BASE_URL/{isbn}", isbn) {
            contentType = APPLICATION_JSON
            accept = APPLICATION_JSON
            content = objectMapper.writeValueAsString(bookSummaryDto)
        }

        // Then
        result.andExpect {
            status { isInternalServerError() }
        }
    }

    @Test
    fun `test create or update book returns HTTP 400 when author doesn't exist in the database`() {
        // Given
        val isbn = "577-812-123548-911"

        every {
            bookService.createOrUpdate(isbn, any())
        } throws IllegalStateException()

        val authorSummaryDto = testAuthorSummaryDtoA(id = -1)
        val bookSummaryDto = testBookSummaryDtoA(isbn, authorSummaryDto)

        // When
        val result = mockMvc.put("$BOOKS_BASE_URL/{isbn}", isbn) {
            contentType = APPLICATION_JSON
            accept = APPLICATION_JSON
            content = objectMapper.writeValueAsString(bookSummaryDto)
        }

        // Then
        result.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `test that get all books returns a list of books`() {
        // Given
        val books = listOf(
            testBookEntityA(isbn = "577-812-123548-911", testAuthorEntityA(id = 1)),
            testBookEntityB(isbn = "577-812-123548-912", testAuthorEntityB(id = 2))
        )

        every {
            bookService.getAllBooks()
        } answers { books }

        // When
        val result = mockMvc.get(BOOKS_BASE_URL) {
            contentType = APPLICATION_JSON
            accept = APPLICATION_JSON
        }

        // Then
        result.andExpect {
            status { isOk() }
            content {
                jsonPath("$[0].isbn", equalTo("577-812-123548-911"))
                jsonPath("$[0].title", equalTo("Test Book A"))
                jsonPath("$[0].image", equalTo("book-a-image.jpeg"))
                jsonPath("$[0].author.id", equalTo(1))
                jsonPath("$[0].author.name", equalTo("John Doe"))
                jsonPath("$[0].author.image", equalTo("author-a-image.jpeg"))
                //
                jsonPath("$[1].isbn", equalTo("577-812-123548-912"))
                jsonPath("$[1].title", equalTo("Test Book B"))
                jsonPath("$[1].image", equalTo("book-b-image.jpeg"))
                jsonPath("$[1].author.id", equalTo(2))
                jsonPath("$[1].author.name", equalTo("Don Joe"))
                jsonPath("$[1].author.image", equalTo("author-b-image.jpeg"))
            }
        }
    }

    @Test
    fun `test that get all books returns no books when the author ID doesn't exists`(){
        // Given
        every {
            bookService.getAllBooks(any())
        } answers { emptyList() }

        // When
        val result = mockMvc.get("$BOOKS_BASE_URL?authorId=-1") {
            contentType = APPLICATION_JSON
            accept = APPLICATION_JSON
        }

        // Then
        result.andExpect {
            status { isOk() }
            content {
                json("[]")
            }
        }
    }

    @Test
    fun `test that get all books returns books when the author ID is provided`(){
        // Given
        val books = listOf(
            testBookEntityA(isbn = "577-812-123548-911", testAuthorEntityA(id = 1L))
        )

        every {
            bookService.getAllBooks( authorId = 1L)
        } answers { books }

        // When
        val result = mockMvc.get("$BOOKS_BASE_URL?author=1") {
            contentType = APPLICATION_JSON
            accept = APPLICATION_JSON
        }

        // Then
        result.andExpect {
            status { isOk() }
            content {
                jsonPath("$[0].isbn", equalTo("577-812-123548-911"))
                jsonPath("$[0].title", equalTo("Test Book A"))
                jsonPath("$[0].image", equalTo("book-a-image.jpeg"))
                jsonPath("$[0].author.id", equalTo(1))
                jsonPath("$[0].author.name", equalTo("John Doe"))
                jsonPath("$[0].author.image", equalTo("author-a-image.jpeg"))
            }
        }
    }
}