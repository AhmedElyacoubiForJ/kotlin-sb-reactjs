package edu.yacoubi.bookstore.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import edu.yacoubi.bookstore.service.IBookService
import edu.yacoubi.bookstore.testAuthorEntityA
import edu.yacoubi.bookstore.testAuthorSummaryDtoA
import edu.yacoubi.bookstore.testBookEntityA
import edu.yacoubi.bookstore.testBookSummaryDtoA
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.put

private const val BOOKS_BASE_URL = "/v1/books"

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    @MockkBean val bookService: IBookService
) {
    val objectMapper = ObjectMapper()

    @Test
    fun `test that create or full update returns HTTP status 201 when book is created` () {
        // Given
        val isbn = "577-812-123548-911"
        val author = testAuthorEntityA(id=1)
        val savedBook = testBookEntityA(isbn, author)

        val authorSummaryDto = testAuthorSummaryDtoA(id=1)
        val bookSummaryDto = testBookSummaryDtoA(isbn, authorSummaryDto)

        every {
            bookService.createOrUpdate(isbn, any())
        } answers {
            Pair(savedBook, true)
        }

        // When
        val result = mockMvc.put("$BOOKS_BASE_URL/{isbn}", isbn) {
            contentType = APPLICATION_JSON
            accept = APPLICATION_JSON
            content = objectMapper.writeValueAsString(bookSummaryDto)
        }

        // Then
        result.andExpect {
            status { isCreated() }
        }
    }

    @Test
    fun `test that create or full update returns HTTP status 500 when author in the database doesn't have an ID` () {
        // Given
        val isbn = "577-812-123548-911"
        val author = testAuthorEntityA()
        val savedBook = testBookEntityA(isbn, author)

        every {
            bookService.createOrUpdate(isbn, any())
        } answers {
            Pair(savedBook, true)
        }

        val authorSummaryDto = testAuthorSummaryDtoA(id=1)
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
}