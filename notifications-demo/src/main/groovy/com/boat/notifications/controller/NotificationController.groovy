package com.boat.notifications.controller

import com.boat.notifications.model.Customer
import com.boat.notifications.model.Notification

import com.boat.notifications.repo.CustomerRepository
import com.boat.notifications.repo.NotificationRepository

import org.springframework.ui.Model

import org.springframework.stereotype.Controller

import org.springframework.http.ResponseEntity

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PathVariable

import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.annotation.Autowired

@RestController
@RequestMapping("/notifications")
class NotificationRESTController {
  @Autowired
  CustomerRepository customerRepository

  @Autowired
  NotificationRepository notificationRepository

  @PostMapping(path=["/add", "/add/"], produces="application/json")
  def add(@RequestBody Map m) {
    def title = m.title
    def description = m.description
    def customers = m.customers

    def notifications = []
    
    customers.each { c ->
      def id = c.split("-")[1].toLong()
      def customer = customerRepository.findById(id)
      
      def notification = new Notification()
      notification.customerId = id
      notification.createdDate = new Date()
      notification.title = title
      notification.description = description

      notificationRepository.save(notification)

      notifications << notification
    }

    return notifications
  }

  @PostMapping(path=["/dismiss", "/dismiss/"], produces="application/json")
  def dismiss(@RequestBody Map m) {
    def notification = notificationRepository.findById(m.id)

    notification.readDate = new Date()
    notificationRepository.save(notification)
    
    return notification
  }
}
