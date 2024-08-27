package edu.yacoubi.bookstore.service.impl

import edu.yacoubi.bookstore.domain.AuthorUpdateRequest
import edu.yacoubi.bookstore.domain.entities.AuthorEntity
import edu.yacoubi.bookstore.repository.AuthorRepository
import edu.yacoubi.bookstore.service.IAuthorService
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AuthorServiceImpl(private val authorRepository: AuthorRepository) : IAuthorService {
    override fun create(authorEntity: AuthorEntity): AuthorEntity {
        require(null == authorEntity.id)
        return authorRepository.save(authorEntity)
    }

    override fun list(): List<AuthorEntity> {
        return authorRepository.findAll()
    }

    override fun getAuthor(id: Long): AuthorEntity? {
        return authorRepository.findByIdOrNull(id)
    }

    @Transactional
    override fun fullUpdate(id: Long, authorEntity: AuthorEntity): AuthorEntity {
        check(authorRepository.existsById(id))
        val normalisedAuthor = authorEntity.copy(id = id)
        return authorRepository.save(normalisedAuthor)
    }

    @Transactional
    override fun partialUpdate(id: Long, authorUpdateRequest: AuthorUpdateRequest): AuthorEntity {
        val existingAuthor = authorRepository.findByIdOrNull(id)
        checkNotNull(existingAuthor)

        val updatedAuthor = existingAuthor.copy(
            name = authorUpdateRequest.name ?: existingAuthor.name,
            age = authorUpdateRequest.age ?: existingAuthor.age,
            description = authorUpdateRequest.description ?: existingAuthor.description,
            image = authorUpdateRequest.image ?: existingAuthor.image
        )

        return authorRepository.save(updatedAuthor)
    }
}