package com.app.secondhand.user

import com.app.secondhand.role.Role

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany

@Entity
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false, updatable = false)
    long id

    @Column(nullable = false, unique = true)
    String username

    @Column(nullable = false, unique = true)
    String email

    @Column(nullable = false)
    String firstName

    @Column(nullable = false)
    String lastName

    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE], fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
            joinColumns = [@JoinColumn(name = "user_id", referencedColumnName = "id")],
            inverseJoinColumns = [@JoinColumn(name = "role_id", referencedColumnName = "id")])
    Set<Role> roles = new HashSet<>()

    User() {
    }

    User(String username, String email, String firstName, String lastName) {
        this.username = username
        this.email = email
        this.firstName = firstName
        this.lastName = lastName
    }

    String toString() {
        return "User(id: ${id}, firstName: ${firstName}, lastName: ${lastName})"
    }
}
