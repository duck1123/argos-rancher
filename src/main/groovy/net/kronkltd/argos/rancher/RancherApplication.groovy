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

        def rs = new RancherStacks(baseUrl, accessKey, secretKey)
        def ra = new RancherApplication(rs)
        ra.run()
    }

    def run() {
        rancherStacks.listStacks()
    }
}
