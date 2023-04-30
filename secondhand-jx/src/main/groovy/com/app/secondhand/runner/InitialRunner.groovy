package com.app.secondhand.runner

import com.app.secondhand.role.Role
import com.app.secondhand.user.User

import groovy.util.logging.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.springframework.beans.factory.annotation.Autowired

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Slf4j
@Component
class InitialRunner implements ApplicationRunner {

    @Autowired
    InitialService initialService

    Logger logger = LoggerFactory.getLogger(InitialRunner.class)

    @Override
    void run(ApplicationArguments args) throws Exception {
        logger.info("InitialRunner.run()")
        initialService.init()
    }
}
