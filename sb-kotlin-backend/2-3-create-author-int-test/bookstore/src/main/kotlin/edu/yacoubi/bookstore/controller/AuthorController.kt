package edu.yacoubi.bookstore.controller

import edu.yacoubi.bookstore.domain.dto.AuthorDto
import edu.yacoubi.bookstore.service.IAuthorService
import edu.yacoubi.bookstore.toAuthorDto
import edu.yacoubi.bookstore.toAuthorEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthorController(private val authorService: IAuthorService) {

    @PostMapping(path = ["/v1/authors"])
    fun createAuthor(@RequestBody authorDto: AuthorDto): ResponseEntity<AuthorDto> {
        val createdAuthor = authorService.save(
            authorDto.toAuthorEntity()
        ).toAuthorDto()

        return ResponseEntity(createdAuthor, HttpStatus.CREATED)
    }
}