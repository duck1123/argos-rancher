package net.kronkltd.argos.rancher

import io.rancher.Rancher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class RancherApplication implements CommandLineRunner {
    private Rancher rancher

    private String baseUrl
    private String mode

    static void main(String... args) {
        SpringApplication.run(RancherApplication, args)
    }

    @Autowired
    RancherApplication(RancherConfiguration configuration) {
        def accessKey = configuration.accessKey
        def secretKey = configuration.secretKey
        this.baseUrl = configuration.baseUrl
        this.mode = configuration.mode

        def url = new URL("${baseUrl}v2-beta/")
        def config = new Rancher.Config(url, accessKey, secretKey)
        this.rancher = new Rancher(config)
    }

    @Override
    void run(String... args) {
        Panel panel

        def arguments = args.toList().findAll { (it != null) && (!it.empty) }

        // println("${arguments} ${arguments.size()}")

        if (!arguments.empty) {
            def command = arguments.first()

            println("Command: ${command}")
        } else {
            // println('xNo command')

            switch (mode) {
                case 'stacks':
                    panel = new RancherStacks(baseUrl, rancher)
                    break
                case 'hosts':
                    panel = new RancherHosts(baseUrl, rancher)
                    break
                default:
                    panel = new Panel('Rancher')
                    panel.addItem(new Item('Unknown'))
                    break
            }

            println(panel)
        }
    }
}
