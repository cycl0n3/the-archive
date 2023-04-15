package com.boat.notifications.repo

import com.boat.notifications.model.Notification

import org.springframework.data.repository.CrudRepository

interface NotificationRepository extends CrudRepository<Notification, Long> {
  Notification findById(long id)
  
  List<Notification> findByCustomerId(long customerId)
  List<Notification> findByCustomerIdAndReadDateIsNull(long customerId)
  List<Notification> findByCustomerIdAndReadDateIsNotNull(long customerId)
}
