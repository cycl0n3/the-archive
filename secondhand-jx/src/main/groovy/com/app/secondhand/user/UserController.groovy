package com.app.secondhand.user

import org.springframework.beans.factory.annotation.Autowired

import org.springframework.http.ResponseEntity

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController {

    @Autowired
    UserService userService

    @GetMapping("/all")
    ResponseEntity<List<User>> getAllUsers() {
        def users = userService.getAllUsers()
        return ResponseEntity.ok(users)
    }
}
