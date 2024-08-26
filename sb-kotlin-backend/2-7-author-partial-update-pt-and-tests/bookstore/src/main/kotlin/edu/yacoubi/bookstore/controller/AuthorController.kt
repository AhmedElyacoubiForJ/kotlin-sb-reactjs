package edu.yacoubi.bookstore.controller

import edu.yacoubi.bookstore.domain.dto.AuthorDto
import edu.yacoubi.bookstore.service.IAuthorService
import edu.yacoubi.bookstore.toAuthorDto
import edu.yacoubi.bookstore.toAuthorEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["http://localhost:3000/"])
@RequestMapping(path = ["/v1/authors"])
class AuthorController(private val authorService: IAuthorService) {

    @PostMapping
    fun createAuthor(@RequestBody authorDto: AuthorDto): ResponseEntity<AuthorDto> {
        try {
            val createdAuthor = authorService.create(
                authorDto.toAuthorEntity()
            ).toAuthorDto()

            return ResponseEntity(createdAuthor, HttpStatus.CREATED)
        } catch (ex: IllegalArgumentException) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping
    fun getAllAuthors(): ResponseEntity<List<AuthorDto>> {
        val authors = authorService.list()
        val authorDtoS = authors.map { it.toAuthorDto() }
        return ResponseEntity(authorDtoS, HttpStatus.OK)
    }

    // get author by id endpoint
    @GetMapping(path = ["/{id}"])
    fun getAuthorById(@PathVariable id: Long): ResponseEntity<AuthorDto> {
        val foundAuthor = authorService.getAuthor(id)?.toAuthorDto()
        // return if (foundAuthor!= null) ResponseEntity(foundAuthor, HttpStatus.OK)
        // else ResponseEntity.notFound().build()
        return foundAuthor?.let {
            ResponseEntity(it, HttpStatus.OK)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PutMapping(path = ["/{id}"])
    fun fullUpdateAuthor(
        @PathVariable id: Long,
        @RequestBody authorDto: AuthorDto
    ): ResponseEntity<AuthorDto> {

        return try {
            val updatedAuthor = authorService.fullUpdate(id, authorDto.toAuthorEntity())
            ResponseEntity(updatedAuthor.toAuthorDto(), HttpStatus.OK)
        } catch (ex: IllegalStateException) {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }
}