package edu.yacoubi.bookstore.service

import edu.yacoubi.bookstore.domain.BookSummary
import edu.yacoubi.bookstore.domain.BookUpdateRequest
import edu.yacoubi.bookstore.domain.entities.BookEntity

interface IBookService {
    fun createOrUpdate(isbn: String, bookSummary: BookSummary): Pair<BookEntity, Boolean>
    fun getAllBooks(authorId: Long?=null): List<BookEntity>
    fun get(isbn: String): BookEntity?
    fun partialUpdate(isbn: String, bookUpdateRequest: BookUpdateRequest): BookEntity
}