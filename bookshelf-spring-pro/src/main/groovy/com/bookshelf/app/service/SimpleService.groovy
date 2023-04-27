package com.bookshelf.app.service

import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

abstract class SimpleService<S, T> {

    abstract JpaRepository<S, T> getRepository()

    @Transactional
    Page<S> findAll(PageRequest pageRequest) {
        repository.findAll(pageRequest)
    }

    @Transactional
    Optional<S> findById(T id) {
        repository.findById(id)
    }

    @Transactional
    S create(S obj) {
        repository.save(obj)
    }

    @Transactional
    S update(S obj) {
        repository.saveAndFlush(obj)
    }

    @Transactional
    void delete(S obj) {
        repository.delete(obj)
    }
}
