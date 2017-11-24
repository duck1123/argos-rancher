package net.kronkltd.argos.rancher

import io.rancher.Rancher

class RancherApplication {
    private final Rancher rancher
    private final Properties properties

    def baseUrl = 'http://rancher.dev.int.eprize.net/'
    def accessKey = '4D7746F25549E43DEBDD'
    def secretKey = 'KSNmFnfV3VThzojLvTLfPkt1fbcUozC3MFGG8XCx'

    RancherApplication() {
        def config = new Rancher.Config(new URL("${baseUrl}v2-beta/"), accessKey, secretKey)
        this.rancher = new Rancher(config)
        this.properties = new Properties()
        loadProperties()
    }

    static void main(String[] args) {
        new RancherApplication().run()
    }

    def loadProperties() {
        File propFile = new File('argos.properties')

        if (propFile.exists()) {
            properties.load(propFile.newDataInputStream())
        } else {
            throw new RuntimeException('No Properties file')
        }
    }

    def run() {
        def mode = properties.getProperty('mode')

        switch (mode) {
            case 'stacks':
                println new RancherStacks(baseUrl, rancher)
                break
            case 'hosts':
                def hosts = new RancherHosts()
                println hosts.toPanel()
                break
            default:
                def panel = new Panel('Rancher')

                def i = new Item('Unknown')
                panel.items.add(i)

                println(panel)
                break
        }
    }
}
