package com.boat.notifications.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Column

@Entity
class Customer {
 
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  long id

  @Column(nullable = false)
  String firstName

  @Column(nullable = false)
  String lastName
}
