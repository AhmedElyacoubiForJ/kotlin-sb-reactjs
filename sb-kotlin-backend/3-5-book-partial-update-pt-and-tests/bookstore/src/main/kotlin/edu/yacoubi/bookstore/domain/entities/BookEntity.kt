package edu.yacoubi.bookstore.domain.entities

import jakarta.persistence.*

@Entity
@Table(name = "books")
data class BookEntity(
    @Id
    @Column(name = "isbn")
    val isbn: String,

    @Column(name = "title")
    val title: String,

    @Column(name = "description", length = -1)
    val description: String,

    @Column(name = "image")
    val image: String,

    @ManyToOne(cascade = [CascadeType.DETACH])
    @JoinColumn(name = "author_id")
    val authorEntity: AuthorEntity
)