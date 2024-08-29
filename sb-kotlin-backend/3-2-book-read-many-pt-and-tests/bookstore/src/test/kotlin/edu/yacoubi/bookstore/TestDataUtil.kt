package edu.yacoubi.bookstore

import edu.yacoubi.bookstore.domain.AuthorSummary
import edu.yacoubi.bookstore.domain.AuthorUpdateRequest
import edu.yacoubi.bookstore.domain.BookSummary
import edu.yacoubi.bookstore.domain.dto.AuthorDto
import edu.yacoubi.bookstore.domain.dto.AuthorSummaryDto
import edu.yacoubi.bookstore.domain.dto.AuthorUpdateRequestDto
import edu.yacoubi.bookstore.domain.dto.BookSummaryDto
import edu.yacoubi.bookstore.domain.entities.AuthorEntity
import edu.yacoubi.bookstore.domain.entities.BookEntity

fun testAuthorDtoA(id: Long? = null) = AuthorDto(
    id = id,
    name = "John Doe",
    age = 30,
    description = "Some description",
    image = "author-image.jpeg",
)

fun testAuthorEntityA(id: Long? = null) = AuthorEntity(
    id = id,
    name = "John Doe",
    age = 30,
    description = "Some description",
    image = "author-image.jpeg",
)

fun testAuthorEntityB(id: Long? = null) = AuthorEntity(
    id=id,
    name="Don Joe",
    age=65,
    description = "Some other description",
    image = "some-other-image.jpeg"
)

fun testAuthorSummaryDtoA(id: Long) = AuthorSummaryDto(
    id = id,
    name = "John Doe",
    image = "author-image.jpeg")

fun testAuthorSummaryA(id: Long) = AuthorSummary(
    id = id,
    name = "John Doe",
    image = "author-image.jpeg")

fun testAuthorUpdateRequestDtoA(id: Long? = null) = AuthorUpdateRequestDto(
    id=id,
    name = "John Doe",
    age = 30,
    description = "Some description",
    image =  "author-image.jpeg",
)

fun testAuthorUpdateRequestA(id: Long? = null) = AuthorUpdateRequest(
    id=id,
    name = "John Doe",
    age = 30,
    description = "Some description",
    image =  "author-image.jpeg",
)

fun testBookEntityA(isbn: String, author: AuthorEntity) = BookEntity(
    isbn=isbn,
    title = "Test Book A",
    description = "A test book",
    image = "book-image.jpeg",
    authorEntity = author
)

fun testBookSummaryDtoA(isbn: String, author: AuthorSummaryDto) = BookSummaryDto(
    isbn=isbn,
    title = "Test Book A",
    description = "A test book",
    image = "book-image.jpeg",
    author=author
)

fun testBookSummaryA(isbn: String, author: AuthorSummary) = BookSummary(
    isbn=isbn,
    title = "Test Book A",
    description = "A test book",
    image = "book-image.jpeg",
    author=author
)

fun testBookSummaryB(isbn: String, author: AuthorSummary) = BookSummary(
    isbn=isbn,
    title = "Test Book B",
    description = "Another test book",
    image = "book-image-b.jpeg",
    author=author
)