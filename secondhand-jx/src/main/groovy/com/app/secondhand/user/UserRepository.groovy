package com.app.secondhand.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username)

    User findByEmail(String email)

    User findByUsernameOrEmail(String username, String email)

    User findByUsernameOrEmailAndIdNot(String username, String email, Long id)

    User findByUsernameAndIdNot(String username, Long id)

    User findByEmailAndIdNot(String email, Long id)

    boolean existsByUsername(String username)

    boolean existsByEmail(String email)

    boolean existsByUsernameAndIdNot(String username, Long id)

    boolean existsByEmailAndIdNot(String email, Long id)

    boolean existsByUsernameOrEmail(String username, String email)

    boolean existsByUsernameOrEmailAndIdNot(String username, String email, Long id)
}
