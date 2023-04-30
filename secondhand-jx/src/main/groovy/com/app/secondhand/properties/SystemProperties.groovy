package com.app.secondhand.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "system")
class SystemProperties {

    private String anonUrl
}
