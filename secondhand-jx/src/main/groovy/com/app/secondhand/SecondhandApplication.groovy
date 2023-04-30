package com.app.secondhand

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.web.server.GracefulShutdownCallback

@SpringBootApplication
class SecondhandApplication {

	static void main(String[] args) {
		def app = new SpringApplication(SecondhandApplication)
		app.registerShutdownHook = false

		def ctx = app.run(args)

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			ctx.getBean(GracefulShutdownCallback).execute()
		}))
	}
}
