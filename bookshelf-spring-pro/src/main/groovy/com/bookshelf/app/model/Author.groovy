package com.bookshelf.app.model


import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull

@Entity
@Table
class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    long authorId

    @Column
    @NotNull
    String name

    Author() { }

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = [CascadeType.PERSIST, CascadeType.MERGE],
            mappedBy = "authors")
    @JsonIgnore
    Set<Book> books = []
}
