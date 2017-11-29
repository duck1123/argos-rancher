package net.kronkltd.argos.rancher

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "rancher")
class RancherConfiguration {

    /**
     * The mode
     */
    String mode

    /**
     * Rancher Access Key
     */
    String accessKey

    /**
     * Rancher Secret Key
     */
    String secretKey

    String baseUrl
}
