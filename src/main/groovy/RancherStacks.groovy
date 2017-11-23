import io.rancher.Rancher
import io.rancher.service.StackService

class RancherStacks {
    def baseUrl = 'REDACTED'
    def url = new URL("${baseUrl}v2-beta/")
    def accessKey = 'REDACTED'
    def secretKey = 'REDACTED'

    Map<String, String> commands = [serviceName: 'rancher ps --format {{.Name}}',
                                    stackJson: 'rancher stacks --format json',
                                    stackName: 'rancher stacks --format {{.Stack.Name}}']

    Rancher rancher

    RancherStacks() {
        def config = new Rancher.Config(url, accessKey, secretKey)
        this.rancher = new Rancher(config)
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

    def rancherStacks() {
        def psProc = commands.stackName.execute()
        def rancherText = psProc.text
        printItem(rancherText, [:])
    }

    def run() {
        println('listStacks')
        println('---')
        listStacks()
    }

    static void main(String[] args) {
        def rs = new RancherStacks()
        rs.run()
    }
}
