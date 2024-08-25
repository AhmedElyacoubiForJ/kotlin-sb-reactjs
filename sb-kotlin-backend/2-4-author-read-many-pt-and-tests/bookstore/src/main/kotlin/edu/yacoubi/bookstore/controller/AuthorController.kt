package edu.yacoubi.bookstore.controller

import edu.yacoubi.bookstore.domain.dto.AuthorDto
import edu.yacoubi.bookstore.service.IAuthorService
import edu.yacoubi.bookstore.toAuthorDto
import edu.yacoubi.bookstore.toAuthorEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin(origins = ["http://localhost:3000/"])
@RequestMapping(path = ["/v1/authors"])
class AuthorController(private val authorService: IAuthorService) {

    @PostMapping
    fun createAuthor(@RequestBody authorDto: AuthorDto): ResponseEntity<AuthorDto> {
        val createdAuthor = authorService.save(
            authorDto.toAuthorEntity()
        ).toAuthorDto()

        return ResponseEntity(createdAuthor, HttpStatus.CREATED)
    }

    @GetMapping
    fun getAllAuthors(): ResponseEntity<List<AuthorDto>> {
        val authors = authorService.list()
        val authorDtoS = authors.map { it.toAuthorDto() }
        return ResponseEntity(authorDtoS, HttpStatus.OK)
    }
}