package com.app.secondhand.role

import com.app.secondhand.user.User

import jakarta.persistence.Column
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany

@Entity
class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id

    @Column(nullable = false)
    String name

    @Column(nullable = false)
    String description

    @ManyToMany(mappedBy = "roles", cascade = [CascadeType.PERSIST, CascadeType.MERGE], fetch = FetchType.LAZY)
    Set<User> users = new HashSet<>()

    Role() {
    }

    Role(String name, String description) {
        this.name = name
        this.description = description
    }

    String toString() {
        return "Role(id: ${id}, name: ${name}, description: ${description})"
    }
}
