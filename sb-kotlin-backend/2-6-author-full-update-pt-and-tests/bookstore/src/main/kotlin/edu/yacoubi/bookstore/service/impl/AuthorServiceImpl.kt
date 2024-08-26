package edu.yacoubi.bookstore.service.impl

import edu.yacoubi.bookstore.domain.entities.AuthorEntity
import edu.yacoubi.bookstore.repository.AuthorRepository
import edu.yacoubi.bookstore.service.IAuthorService
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AuthorServiceImpl(private val authorRepository: AuthorRepository) : IAuthorService {
    override fun create(authorEntity: AuthorEntity): AuthorEntity {
        // Check if the author's ID is null before saving.
        // If it's not null, it means the author already exists in the database,
        // and we should throw an IllegalStateException to prevent duplicate entries.
        require(null == authorEntity.id)
        return authorRepository.save(authorEntity)
    }

    override fun list(): List<AuthorEntity> {
        // Fetch and return all author entities from the database.
        return authorRepository.findAll()
    }

    override fun getAuthor(id: Long): AuthorEntity? {
        // Find and return the author with the given ID.
        // If no author is found, return null.
        return authorRepository.findByIdOrNull(id)
    }

    // The @Transactional annotation ensures that the fullUpdate method is executed within a transaction.
    // This ensures that the database is updated correctly even if there are multiple concurrent requests.
    // This means if any part of the method fails, the entire transaction will be rolled back,
    // and any changes made to the database will be reverted.
    // This is important to maintain data integrity and consistency.
    @Transactional
    override fun fullUpdate(id: Long, authorEntity: AuthorEntity): AuthorEntity {
        // Check if the author with the given ID exists in the database.
        // If it doesn't exist, throw a check error to indicate that the author doesn't exist.
        check(authorRepository.existsById(id))
        // Create a new author entity with the given ID and the  updated details.
        // This ensures that the ID remains unchanged during the update operation.
        val normalisedAuthor = authorEntity.copy(id = id)
        // Save the updated author entity and return it.
        return authorRepository.save(normalisedAuthor)
    }
}