package edu.yacoubi.bookstore

import edu.yacoubi.bookstore.domain.dto.AuthorDto
import edu.yacoubi.bookstore.domain.dto.AuthorUpdateRequestDto
import edu.yacoubi.bookstore.domain.entities.AuthorEntity

fun testAuthorDtoA(id: Long? = null) = AuthorDto(
    id = id,
    name = "John Doe",
    age = 30,
    description = "Author A description",
    image = "author-a-image.jpeg"
)

fun testAuthorEntityA(id: Long? = null) = AuthorEntity(
    id = id,
    name = "John Doe",
    age = 30,
    description = "Author A description",
    image = "author-a-image.jpeg"
)

fun testAuthorEntityB(id: Long? = null) = AuthorEntity(
    id = id,
    name = "Paul Franklin",
    age = 35,
    description = "Author B description",
    image = "author-b-image.jpeg"
)

fun testAuthorUpdateRequestDtoA(id: Long? = null) = AuthorUpdateRequestDto(
    id = id,
    name = "John Doe",
    age = 30,
    description = "Author A description",
    image = "author-a-image.jpeg"
)

//fun testAuthorDtoA(id: Long? = null): AuthorDto {
//    return AuthorDto(
//        id = id,
//        name = "John Doe",
//        age = 30,
//        description = "Author A description",
//        image = "author-a-image.jpeg"
//    )
//}