package edu.yacoubi.bookstore.domain.dto

data class BookUpdateRequestDto(
    val title: String?,
    val description: String?,
    val image: String?
)