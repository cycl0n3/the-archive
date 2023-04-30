package com.app.secondhand.role

import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name)

    Role findByNameLike(String name)

    Role findByNameNotLike(String name)

    Role findByNameStartingWith(String name)

    Role findByNameEndingWith(String name)

    Role findByNameContaining(String name)

    Role findByNameIgnoreCase(String name)

    Role findByNameOrderByNameAsc(String name)
}
