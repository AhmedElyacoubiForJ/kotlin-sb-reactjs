package edu.yacoubi.bookstore

import edu.yacoubi.bookstore.domain.AuthorUpdateRequest
import edu.yacoubi.bookstore.domain.dto.AuthorDto
import edu.yacoubi.bookstore.domain.dto.AuthorUpdateRequestDto
import edu.yacoubi.bookstore.domain.entities.AuthorEntity

fun AuthorEntity.toAuthorDto() = AuthorDto(
    id = this.id,
    name = this.name,
    age = this.age,
    description = this.description,
    image = this.image
)

fun AuthorDto.toAuthorEntity() = AuthorEntity(
    id = this.id,
    name = this.name,
    age = this.age,
    description = this.description,
    image = this.image
)

fun AuthorUpdateRequestDto.toAuthorUpdateRequest() = AuthorUpdateRequest(
    id = this.id,
    name = this.name,
    age = this.age,
    description = this.description,
    image = this.image
)
/*
*   fun [receiverType].[functionName]([arguments]) : [returnType] {
*       ....
*       [Body]
*       ....
*   }
*/

/*
fun AuthorEntity.toAuthorDto() : AuthorDto {
    return AuthorDto(
        id = this.id,
        name = this.name,
        age = this.age,
        description = this.description,
        image = this.image
    )
}

fun AuthorDto.toAuthorEntity() : AuthorEntity {
    return AuthorEntity(
        id = this.id,
        name = this.name,
        age = this.age,
        description = this.description,
        image = this.image
    )
}*/
