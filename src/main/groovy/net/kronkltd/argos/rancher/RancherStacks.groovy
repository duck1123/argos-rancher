package net.kronkltd.argos.rancher

import io.rancher.Rancher
import io.rancher.service.StackService

class RancherStacks {
    Map<String, String> commands = [serviceName: 'rancher ps --format {{.Name}}',
                                    stackJson: 'rancher stacks --format json',
                                    stackName: 'rancher stacks --format {{.Stack.Name}}']

    private final Rancher rancher
    private final String baseUrl

    RancherStacks(String baseUrl, String accessKey, String secretKey) {
        this(baseUrl, new Rancher.Config(new URL("${baseUrl}v2-beta/"), accessKey, secretKey))
    }

    RancherStacks(String baseUrl, Rancher.Config config) {
        this(baseUrl, new Rancher(config))
    }

    RancherStacks(String baseUrl, Rancher rancher) {
        this.rancher = rancher
        this.baseUrl = baseUrl
    }

    def listStacks() {
        def call = rancher.type(StackService).list()

        def code

        try {
            def response = call.execute()

            code = response.code()
        } catch (Exception ex) {
            code = 500
        }

        if (code == 200) {
            println('listStacks')
            println('---')

            def data = response.body().data

            data
                    .sort {a, b ->
                (a.name <=> b.name)
            }
            .each {
                if (it.state == 'active') {
                    def healthy = it.healthState == 'healthy'
                    def msg = "${healthy} ${it.name}"
                    Map<String, String> opts = [href: "${baseUrl}env/1a5/apps/stacks/${it.id}" as String]
                    printItem(msg, opts)
                    printItem(msg + "\\n${it.state}", opts + [alternate: 'true'])
                }
            }
        } else {
            println 'Failed'
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
