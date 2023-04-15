package com.boat.notifications.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Column

@Entity
class Notification {
 
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  long id

  @Column(nullable = false)
  long customerId

  @Column(nullable = false)
  Date createdDate
  
  @Column(nullable = true)
  Date readDate

  @Column(nullable = false)
  String title

  @Column(nullable = false)
  String description
}
