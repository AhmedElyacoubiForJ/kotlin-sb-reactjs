package edu.yacoubi.bookstore.repository

import edu.yacoubi.bookstore.domain.entities.BookEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : JpaRepository<BookEntity, String> {
    fun findAllByAuthorEntityId(authorId: Long) : List<BookEntity>
}