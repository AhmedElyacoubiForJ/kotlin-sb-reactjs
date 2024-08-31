package edu.yacoubi.bookstore.controller

import edu.yacoubi.bookstore.domain.BookUpdateRequest
import edu.yacoubi.bookstore.domain.dto.BookSummaryDto
import edu.yacoubi.bookstore.domain.dto.BookUpdateRequestDto
import edu.yacoubi.bookstore.exception.InvalidAuthorException
import edu.yacoubi.bookstore.service.IBookService
import edu.yacoubi.bookstore.toBookSummary
import edu.yacoubi.bookstore.toBookSummaryDto
import edu.yacoubi.bookstore.toBookUpdateRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/books")
class BookController(val bookService: IBookService) {

    @PutMapping(path = ["/{isbn}"])
    fun createOrFullUpdateBook(
        @PathVariable("isbn") isbn: String,
        @RequestBody book: BookSummaryDto
    ): ResponseEntity<BookSummaryDto> {
        try {
            val (savedBook, isCreated) = bookService.createOrUpdate(isbn, book.toBookSummary())
            val responseCode = if (isCreated) HttpStatus.CREATED else HttpStatus.OK
            return ResponseEntity(savedBook.toBookSummaryDto(), responseCode)

        } catch (ex: InvalidAuthorException) {
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (ex: IllegalStateException) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping()
    fun getAllBooks(@RequestParam("author") authorId: Long?): List<BookSummaryDto> {
        return bookService.getAllBooks(authorId).map { it.toBookSummaryDto() }
    }

    @GetMapping(path = ["/{isbn}"])
    fun getBookByIsbn(@PathVariable("isbn") isbn: String): ResponseEntity<BookSummaryDto> {
        return bookService.get(isbn)?.let {
            ResponseEntity(it.toBookSummaryDto(), HttpStatus.OK)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PatchMapping(path = ["/{isbn}"])
    fun partialUpdateBook(
        @PathVariable("isbn") isbn: String,
        @RequestBody bookUpdateRequestDto: BookUpdateRequestDto
    ): ResponseEntity<BookSummaryDto> {
        return try {
            val updatedBook = bookService.partialUpdate(
                isbn,
                bookUpdateRequestDto.toBookUpdateRequest()
            )
            ResponseEntity(
                updatedBook.toBookSummaryDto(),
                HttpStatus.OK
            )
        } catch (ex: IllegalStateException) {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }
}


