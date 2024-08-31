package edu.yacoubi.bookstore.domain

data class BookUpdateRequest(
    val title: String?,
    val description: String?,
    val image: String?
)