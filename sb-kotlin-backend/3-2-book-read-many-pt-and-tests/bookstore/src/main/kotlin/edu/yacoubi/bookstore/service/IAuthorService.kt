package edu.yacoubi.bookstore.service

import edu.yacoubi.bookstore.domain.AuthorUpdateRequest
import edu.yacoubi.bookstore.domain.entities.AuthorEntity

interface IAuthorService {
    fun create(authorEntity: AuthorEntity): AuthorEntity
    fun list(): List<AuthorEntity>
    fun getAuthor(id: Long): AuthorEntity?
    fun fullUpdate(id: Long, authorEntity: AuthorEntity): AuthorEntity
    fun partialUpdate(id: Long, authorUpdateRequest: AuthorUpdateRequest): AuthorEntity
    fun delete(id: Long)
}