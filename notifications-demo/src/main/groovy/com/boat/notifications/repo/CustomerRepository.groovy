package com.boat.notifications.repo

import com.boat.notifications.model.Customer

import org.springframework.data.repository.CrudRepository

interface CustomerRepository extends CrudRepository<Customer, Long> {
  Customer findById(long id)
}
