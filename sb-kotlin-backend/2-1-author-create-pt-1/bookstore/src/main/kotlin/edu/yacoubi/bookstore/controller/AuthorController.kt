package edu.yacoubi.bookstore.controller

import edu.yacoubi.bookstore.domain.entities.Author
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthorController {

    @PostMapping(path = ["/v1/authors"])
    fun createAuthor(@RequestBody author: Author) {
        // Implement author creation logic here
    }
}