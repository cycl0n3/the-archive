package com.app.secondhand.exception

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
class GenericException extends Exception {

    Logger logger = LoggerFactory.getLogger(GenericException.class)

    GenericException() {
        super()

        logger.error("GenericException: {}", this.message)
    }

    GenericException(String message) {
        super(message)

        logger.error("GenericException: {}", message)
    }

    GenericException(String message, Throwable cause) {
        super(message, cause)

        logger.error("GenericException: {} {}", message, cause.message)
    }

    GenericException(Throwable cause) {
        super(cause)

        logger.error("GenericException: {}", cause.message)
    }

    GenericException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace)

        logger.error("GenericException: {} {}", message, cause.message)
    }
}
