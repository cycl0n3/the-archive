package com.boat.notifications

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

import org.springframework.data.jpa.repository.config.EnableJpaRepositories

import org.springframework.boot.autoconfigure.domain.EntityScan

@EnableJpaRepositories("com.boat.notifications.repo") 
@EntityScan("com.boat.notifications.model")
@SpringBootApplication
class NotificationsApplication {

	static void main(String[] args) {
		SpringApplication.run(NotificationsApplication, args)
	}
}
