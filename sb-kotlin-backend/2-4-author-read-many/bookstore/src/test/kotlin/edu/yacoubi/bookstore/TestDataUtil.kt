package edu.yacoubi.bookstore

import edu.yacoubi.bookstore.domain.dto.AuthorDto
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

//fun testAuthorDtoA(id: Long? = null): AuthorDto {
//    return AuthorDto(
//        id = id,
//        name = "John Doe",
//        age = 30,
//        description = "Author A description",
//        image = "author-a-image.jpeg"
//    )
//}