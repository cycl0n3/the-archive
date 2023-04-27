package com.bookshelf.app.repo

import com.bookshelf.app.model.Publisher
import org.springframework.data.jpa.repository.JpaRepository

interface PublisherRepository extends JpaRepository<Publisher, Long> {

    Optional<Publisher> findPublisherByName(String text)
}
