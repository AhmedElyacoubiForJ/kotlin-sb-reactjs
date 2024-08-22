package edu.yacoubi.bookstore.service

import edu.yacoubi.bookstore.domain.entities.AuthorEntity

interface IAuthorService {
    fun save(authorEntity: AuthorEntity): AuthorEntity
}