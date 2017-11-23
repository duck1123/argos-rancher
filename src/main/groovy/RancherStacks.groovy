import io.rancher.Rancher
import io.rancher.service.StackService

class RancherStacks {
    Map<String, String> commands = [serviceName: 'rancher ps --format {{.Name}}',
                                    stackJson: 'rancher stacks --format json',
                                    stackName: 'rancher stacks --format {{.Stack.Name}}']

    Rancher rancher

    RancherStacks(String baseUrl, String accessKey, String secretKey) {
        this(new Rancher.Config(new URL("${baseUrl}v2-beta/"), accessKey, secretKey))
    }

    RancherStacks(Rancher.Config config) {
        this(new Rancher(config))
    }

    RancherStacks(Rancher rancher) {
        this.rancher = rancher
    }

    def listStacks() {
        def call = rancher.type(StackService).list()
        def data = call.execute().body().data

        data
                .sort {a, b ->
            (a.name <=> b.name)
        }
        .each {
            if (it.state == 'active') {
                def msg = "${it.healthState} ${it.state} ${it.name}"
                def opts = [href: "${baseUrl}env/1a5/apps/stacks/${it.id}"]
                printItem(msg, opts)
            }
        }
    }

    def printItem(msg, opts) {
        def o = opts.collect { "${it.key}=${it.value}" }.join(' ')

        def l = [msg, o].join(' | ')

        println(l)
    }

    def rancherStacks(String baseUrl) {
        def psProc = commands.stackName.execute()
        def rancherText = psProc.text
        printItem(rancherText, [:])
    }

    def run() {
        listStacks()
    }

    static void main(String[] args) {
        def baseUrl = 'REDACTED'
        def accessKey = 'REDACTED'
        def secretKey = 'REDACTED'

        def rs = new RancherStacks(baseUrl, accessKey, secretKey)
        rs.run()
    }
}
