package edu.yacoubi.bookstore

import edu.yacoubi.bookstore.domain.dto.AuthorDto

fun testAuthorDtoA(id: Long? = null) = AuthorDto(
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