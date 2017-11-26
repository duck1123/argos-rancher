package net.kronkltd.argos.rancher

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = 'rancher')
class RancherConfiguration {
    String mode

    String accessKey

    @NotBlank
    String secretKey

}
