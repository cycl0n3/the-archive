package com.app.secondhand.role

import com.app.secondhand.user.User
import com.app.secondhand.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RoleService {

    @Autowired
    UserRepository userRepository

    @Autowired
    RoleRepository roleRepository

    Role createRole(String name, String description) {
        Role role = new Role(name: name, description: description)
        roleRepository.save(role)
        return role
    }

    Role getRole(long id) {
        return roleRepository.findById(id).orElse(null)
    }

    Role findRoleByName(String name) {
        return roleRepository.findByName(name)
    }

    List<Role> getRoles() {
        return roleRepository.findAll()
    }

    void deleteRole(long id) {
        roleRepository.deleteById(id)
    }

    void deleteRole(Role role) {
        roleRepository.delete(role)
    }

    void deleteRoles() {
        roleRepository.deleteAll()
    }

    Role updateRole(long id, String name, String description) {
        Role role = getRole(id)
        if(role != null) {
            role.name = name
            role.description = description
            roleRepository.save(role)
        }
        return role
    }

    Role updateRole(Role role, String name, String description) {
        if(role != null) {
            role.name = name
            role.description = description
            roleRepository.save(role)
        }
        return role
    }

    Role addRoleToUser(long roleId, long userId) {
        Role role = getRole(roleId)
        User user = getUser(userId)
        if(role != null && user != null) {
            user.roles.add(role)
            userRepository.save(user)
        }
        return role
    }

    Role addRoleToUser(Role role, User user) {
        if(role != null && user != null) {
            user.roles.add(role)
            userRepository.save(user)
        }
        return role
    }

    Role removeRoleFromUser(long roleId, long userId) {
        Role role = getRole(roleId)
        User user = getUser(userId)
        if(role != null && user != null) {
            user.roles.remove(role)
            userRepository.save(user)
        }
        return role
    }

    Role removeRoleFromUser(Role role, User user) {
        if(role != null && user != null) {
            user.roles.remove(role)
            userRepository.save(user)
        }
        return role
    }

    User getUser(long id) {
        return userRepository.findById(id).orElse(null)
    }
}
