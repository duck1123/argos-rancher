package net.kronkltd.argos.rancher

import io.rancher.Rancher

class RancherApplication {
    private Rancher rancher
    private final Properties properties

    private String baseUrl
    private String mode

    static void main(String... args) {
        def application = new RancherApplication()
        def arguments = args.toList().findAll { (it != null) && (!it.empty) }

        println("${arguments} ${arguments.size()}")
        application.run(arguments)
    }

    RancherApplication() {
        this.properties = new Properties()
        File propFile = new File('argos.properties')

        if (propFile.exists()) {
            properties.load(propFile.newDataInputStream())
        } else {
            throw new RuntimeException('No Properties file')
        }

        def accessKey = properties.getProperty('access-key')
        def secretKey = properties.getProperty('secret-key')
        this.baseUrl = properties.getProperty('base-url')
        this.mode = properties.getProperty('mode')

        def url = new URL("${baseUrl}v2-beta/")
        def config = new Rancher.Config(url, accessKey, secretKey)
        this.rancher = new Rancher(config)
    }

    def run(List<String> args) {
        Panel panel

        if (!args.empty) {
            def command = args.first()

            println("Command: ${command}")
        } else {
            println('No command')

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
