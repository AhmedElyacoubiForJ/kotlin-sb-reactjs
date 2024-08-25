package edu.yacoubi.bookstore.service.impl

import edu.yacoubi.bookstore.domain.entities.AuthorEntity
import edu.yacoubi.bookstore.repository.AuthorRepository
import edu.yacoubi.bookstore.service.IAuthorService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AuthorServiceImpl(private val authorRepository: AuthorRepository) : IAuthorService {
    override fun save(authorEntity: AuthorEntity): AuthorEntity {
        return authorRepository.save(authorEntity)
    }

    override fun list(): List<AuthorEntity> {
        return authorRepository.findAll()
    }

    override fun getAuthor(id: Long): AuthorEntity? {
        return authorRepository.findByIdOrNull(id)
    }
}