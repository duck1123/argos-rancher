package net.kronkltd.argos.rancher

class RancherApplication {
    private final RancherStacks rancherStacks

    RancherApplication(RancherStacks rancherStacks) {
        this.rancherStacks = rancherStacks
    }

    static void main(String[] args) {
        def baseUrl = 'http://rancher.dev.int.eprize.net/'
        def accessKey = '4D7746F25549E43DEBDD'
        def secretKey = 'KSNmFnfV3VThzojLvTLfPkt1fbcUozC3MFGG8XCx'

        System.getenv()

        def rs = new RancherStacks(baseUrl, accessKey, secretKey)
        def ra = new RancherApplication(rs)
        ra.run()
    }

    def run() {
        Properties properties = new Properties()
        File propFile = new File('argos.properties')

        if (propFile.exists()) {
            properties.load(propFile.newDataInputStream())

            def mode = properties.getProperty('mode')

            switch (mode) {
                case 'stacks':
                    rancherStacks.listStacks()
                    break
                case 'hosts':
                    def hosts = new RancherHosts()
                    break
                default:
                    def panel = new Panel('Rancher')

                    def i = new Item('Unknown')
                    panel.items.add(i)

                    println(panel)
                    break
            }
        } else {
            throw new RuntimeException('No Properties file')
        }
    }
}
