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
    description = "Author A description",
    image = "author-a-image.jpeg",
)

fun testAuthorEntityA(id: Long? = null) = AuthorEntity(
    id = id,
    name = "John Doe",
    age = 30,
    description = "Author A description",
    image = "author-a-image.jpeg",
)

fun testAuthorEntityB(id: Long? = null) = AuthorEntity(
    id=id,
    name="Don Joe",
    age=65,
    description = "Author B description",
    image = "author-b-image.jpeg"
)

fun testAuthorSummaryDtoA(id: Long) = AuthorSummaryDto(
    id = id,
    name = "John Doe",
    image = "author-a-image.jpeg")

fun testAuthorSummaryA(id: Long) = AuthorSummary(
    id = id,
    name = "John Doe",
    image = "author-a-image.jpeg")

fun testAuthorUpdateRequestDtoA(id: Long? = null) = AuthorUpdateRequestDto(
    id=id,
    name = "John Doe",
    age = 30,
    description = "Author A description",
    image =  "author-a-image.jpeg",
)

fun testAuthorUpdateRequestA(id: Long? = null) = AuthorUpdateRequest(
    id=id,
    name = "John Doe",
    age = 30,
    description = "Author A description",
    image =  "author-a-image.jpeg",
)

fun testBookEntityA(isbn: String, author: AuthorEntity) = BookEntity(
    isbn=isbn,
    title = "Test Book A",
    description = "Book A description",
    image = "book-a-image.jpeg",
    authorEntity = author
)
fun testBookEntityB(isbn: String, author: AuthorEntity) = BookEntity(
    isbn=isbn,
    title = "Test Book B",
    description = "Book B description",
    image = "book-b-image.jpeg",
    authorEntity = author
)

fun testBookSummaryDtoA(isbn: String, author: AuthorSummaryDto) = BookSummaryDto(
    isbn=isbn,
    title = "Test Book A",
    description = "Book A description",
    image = "book-a-image.jpeg",
    author=author
)

fun testBookSummaryA(isbn: String, author: AuthorSummary) = BookSummary(
    isbn=isbn,
    title = "Test Book A",
    description = "Book A description",
    image = "book-a-image.jpeg",
    author=author
)

fun testBookSummaryB(isbn: String, author: AuthorSummary) = BookSummary(
    isbn=isbn,
    title = "Test Book B",
    description = "Book B description",
    image = "book-b-image.jpeg",
    author=author
)