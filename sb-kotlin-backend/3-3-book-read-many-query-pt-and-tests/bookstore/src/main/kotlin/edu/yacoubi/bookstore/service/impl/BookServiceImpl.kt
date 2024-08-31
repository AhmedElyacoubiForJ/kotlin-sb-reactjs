package edu.yacoubi.bookstore.service.impl

import edu.yacoubi.bookstore.domain.BookSummary
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
}