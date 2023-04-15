package com.boat.notifications.controller

import com.boat.notifications.model.Customer

import com.boat.notifications.repo.CustomerRepository
import com.boat.notifications.repo.NotificationRepository

import org.springframework.ui.Model

import org.springframework.stereotype.Controller

import org.springframework.http.ResponseEntity

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PathVariable

import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.annotation.Autowired


@Controller
@RequestMapping("/customers")
class CustomerHTMLController {
  
  @Autowired
  CustomerRepository customerRepository

  @Autowired
  NotificationRepository notificationRepository

  @GetMapping(path=["/{id}/index.html", "/{id}/index.html/"])
  def index(@PathVariable Long id, Model model) {
    def customer = customerRepository.findById(id)
    model.addAttribute("customer", customer)
    return "customer/index"
  }
}

@RestController
@RequestMapping("/customers")
class CustomerRESTController {
  
  @Autowired
  CustomerRepository customerRepository

  @Autowired
  NotificationRepository notificationRepository

  @GetMapping(path=["/json", "/json/"], produces="application/json")
  def findAll() {
    def customers = customerRepository.findAll()
    
    return customers.collect { customer ->
      [
        'customer': customer, 
        'notifications': notificationRepository.findByCustomerIdAndReadDateIsNull(customer.id)
      ]
    }
  }

  @GetMapping(path=["/{id}.json", "/{id}.json/"], produces="application/json")
  def findCustomerById(@PathVariable long id) {
    def customer = customerRepository.findById(id)

    def active_notifications = notificationRepository.findByCustomerIdAndReadDateIsNull(customer.id)
    def old_notifications = notificationRepository.findByCustomerIdAndReadDateIsNotNull(customer.id)

    return [
      "customer": customer,
      "active_notifications": active_notifications,
      "old_notifications": old_notifications,
    ]
  }
}
