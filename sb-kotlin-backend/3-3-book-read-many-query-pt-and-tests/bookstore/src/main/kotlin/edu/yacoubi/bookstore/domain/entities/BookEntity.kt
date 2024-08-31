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
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BookEntity

        if (isbn != other.isbn) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (image != other.image) return false
        if (authorEntity != other.authorEntity) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isbn.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + image.hashCode()
        result = 31 * result + authorEntity.hashCode()
        return result
    }
}