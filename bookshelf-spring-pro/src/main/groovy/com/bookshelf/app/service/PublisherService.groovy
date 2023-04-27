package com.bookshelf.app.service

import com.bookshelf.app.model.Publisher
import com.bookshelf.app.repo.PublisherRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service

@Service("publisherService")
class PublisherService extends SimpleService<Publisher, Long> {

    @Autowired
    PublisherRepository publisherRepository

    @Override
    JpaRepository<Publisher, Long> getRepository() {
        publisherRepository
    }

    Optional<Publisher> findPublisherByName(String text) {
        publisherRepository.findPublisherByName(text)
    }
}
