package edu.yacoubi.bookstore.service.impl

import edu.yacoubi.bookstore.domain.BookSummary
import edu.yacoubi.bookstore.domain.BookUpdateRequest
import edu.yacoubi.bookstore.domain.entities.BookEntity
import edu.yacoubi.bookstore.repository.AuthorRepository
import edu.yacoubi.bookstore.repository.BookRepository
import edu.yacoubi.bookstore.service.IBookService
import edu.yacoubi.bookstore.toBookEntity
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class BookServiceImpl(
    private val bookRepository: BookRepository,
    private val authorRepository: AuthorRepository
) : IBookService {

    @Transactional
    override fun createOrUpdate(
        isbn: String,
        bookSammary: BookSummary): Pair<BookEntity, Boolean> {

        val normalisedBook = bookSammary.copy(isbn = isbn)
        val isExists = bookRepository.existsById(isbn)
        val author = authorRepository.findByIdOrNull(normalisedBook.author.id)
        checkNotNull(author)

        val savedBook = bookRepository.save(normalisedBook.toBookEntity(author))
        return Pair(savedBook, !isExists)
    }

    override fun getAllBooks(authorId: Long?): List<BookEntity> {
        return authorId?.let {
            bookRepository.findAllByAuthorEntityId(it)
        } ?: bookRepository.findAll()
    }

    override fun get(isbn: String): BookEntity? {
        return bookRepository.findByIdOrNull(isbn)
    }

    override fun partialUpdate(
        isbn: String,
        bookUpdateRequest: BookUpdateRequest): BookEntity {

        val existingBook = bookRepository.findByIdOrNull(isbn)
        checkNotNull(existingBook)

        val updatedBook = existingBook.copy(
            title = bookUpdateRequest.title ?: existingBook.title,
            description = bookUpdateRequest.description ?: existingBook.description,
            image = bookUpdateRequest.image ?: existingBook.image
        )
        return bookRepository.save(updatedBook)
    }

    override fun delete(isbn: String) {
        bookRepository.deleteById(isbn)
    }
}