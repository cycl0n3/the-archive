package com.bookshelf.app.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull

@Entity
@Table
class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "book_id")
	long bookId
	
	@Column(length = 1024)
	@NotNull
	String title

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "publisher_fk_id")
	Publisher publisher

	@Column
	String year

	@Column
	String language

	Book() { }

	@ManyToMany(fetch = FetchType.EAGER,
			cascade = [CascadeType.PERSIST, CascadeType.MERGE])
	@JoinTable(name = "book_authors",
			joinColumns = [ @JoinColumn(name = "book_fk_id") ],
			inverseJoinColumns = [ @JoinColumn(name = "author_fk_id") ])
	// @JsonIgnore
	Set<Author> authors = []
}
