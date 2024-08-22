package edu.yacoubi.bookstore.controller

import edu.yacoubi.bookstore.domain.dto.AuthorDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthorController {

    @PostMapping(path = ["/v1/authors"])
    fun createAuthor(@RequestBody authorDto: AuthorDto) {
        // Implement author creation logic here
    }
}