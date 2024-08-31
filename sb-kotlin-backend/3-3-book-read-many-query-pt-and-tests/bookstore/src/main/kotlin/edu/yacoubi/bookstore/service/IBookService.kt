package edu.yacoubi.bookstore.service

import edu.yacoubi.bookstore.domain.BookSummary
import edu.yacoubi.bookstore.domain.entities.BookEntity

interface IBookService {
    fun createOrUpdate(isbn: String, bookSummary: BookSummary): Pair<BookEntity, Boolean>
    fun getAllBooks(authorId: Long?): List<BookEntity>
}