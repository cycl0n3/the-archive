package com.app.secondhand.runner

import com.app.secondhand.role.Role
import com.app.secondhand.role.RoleRepository
import com.app.secondhand.user.User
import com.app.secondhand.user.UserRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@Transactional
class InitialService {

    @Autowired
    UserRepository userRepository

    @Autowired
    RoleRepository roleRepository

    void init() {
        println("InitialService.init()")

        /*def adminRole = new Role(name: "ROLE_ADMIN", description: "Admin")
        def userRole = new Role(name: "ROLE_USER", description: "User")
        def guestRole = new Role(name: "ROLE_GUEST", description: "Guest")
        def moderatorRole = new Role(name: "ROLE_MODERATOR", description: "Moderator")

        def admin = new User(username: "admin", email: "admin@localhost", firstName: "Admin", lastName: "Admin")
        def user = new User(username: "user", email: "user@localhost", firstName: "User", lastName: "User")
        def guest = new User(username: "guest", email: "guest@localhost", firstName: "Guest", lastName: "Guest")
        def sam = new User(username: "sam", email: "sam@localhost", firstName: "Sam", lastName: "Sam")

        admin.roles.add(adminRole)
        user.roles.add(userRole)
        guest.roles.add(guestRole)
        sam.roles.add(userRole)
        sam.roles.add(moderatorRole)

        roleRepository.save(adminRole)
        roleRepository.save(userRole)
        roleRepository.save(guestRole)
        roleRepository.save(moderatorRole)

        userRepository.save(admin)
        userRepository.save(user)
        userRepository.save(guest)
        userRepository.save(sam)

        println("InitialService.init() - admin: ${admin}")
        println("InitialService.init() - user: ${user}")
        println("InitialService.init() - guest: ${guest}")
        println("InitialService.init() - sam: ${sam}")*/
    }
}
