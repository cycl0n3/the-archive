package com.app.secondhand.user

import com.app.secondhand.role.Role
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {

    @Autowired
    UserRepository userRepository

    List<User> getAllUsers() {
        return userRepository.findAll()
    }

    User getUserById(Long id) {
        return userRepository.findById(id).orElse(null)
    }

    User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
    }

    User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
    }

    User getUserByUsernameOrEmail(String username, String email) {
        return userRepository.findByUsernameOrEmail(username, email)
    }

    User getUserByUsernameOrEmailAndIdNot(String username, String email, Long id) {
        return userRepository.findByUsernameOrEmailAndIdNot(username, email, id)
    }

    User getUserByUsernameAndIdNot(String username, Long id) {
        return userRepository.findByUsernameAndIdNot(username, id)
    }

    User getUserByEmailAndIdNot(String email, Long id) {
        return userRepository.findByEmailAndIdNot(email, id)
    }

    User saveUser(User user) {
        return userRepository.save(user)
    }

    void deleteUserById(Long id) {
        userRepository.deleteById(id)
    }

    void deleteUser(User user) {
        userRepository.delete(user)
    }

    void deleteAllUsers() {
        userRepository.deleteAll()
    }

    boolean existsUserById(Long id) {
        return userRepository.existsById(id)
    }

    boolean existsUserByUsername(String username) {
        return userRepository.existsByUsername(username)
    }

    boolean existsUserByEmail(String email) {
        return userRepository.existsByEmail(email)
    }

    boolean existsUserByUsernameOrEmail(String username, String email) {
        return userRepository.existsByUsernameOrEmail(username, email)
    }

    boolean existsUserByUsernameOrEmailAndIdNot(String username, String email, Long id) {
        return userRepository.existsByUsernameOrEmailAndIdNot(username, email, id)
    }

    boolean existsUserByUsernameAndIdNot(String username, Long id) {
        return userRepository.existsByUsernameAndIdNot(username, id)
    }

    boolean existsUserByEmailAndIdNot(String email, Long id) {
        return userRepository.existsByEmailAndIdNot(email, id)
    }

    long countUsers() {
        return userRepository.count()
    }

    Set<Role> getUserRolesByUsername(String username) {
        return userRepository.findByUsername(username).roles
    }

    void addRoleToUser(User user, Role role) {
        user.roles.add(role)
        userRepository.save(user)
    }

    void removeRoleFromUser(User user, Role role) {
        user.roles.remove(role)
        userRepository.save(user)
    }

    void removeAllRolesFromUser(User user) {
        user.roles.clear()
        userRepository.save(user)
    }
}
