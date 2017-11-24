package net.kronkltd.argos.rancher

import io.rancher.Rancher
import io.rancher.service.StackService
import io.rancher.type.Stack

class RancherStacks {
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

    def formatStack(Stack stack) {
        if (stack.state == 'active') {
            def healthy = stack.healthState == 'healthy'

            def msg = "${stack.name}"
            Map<String, String> opts = [href: "${baseUrl}env/1a5/apps/stacks/${stack.id}",
                                        color: healthy ? 'green' : 'red']
            printItem(msg, opts)
            printItem(msg + "\\n${stack.state}", opts + ([alternate: 'true'] as Map<String, GString>))
        }
    }

    def listStacks() {
        def call = rancher.type(StackService).list()

        def code
        def response
        List<Stack> data = []

        try {
            response = call.execute()

            code = response.code()
            data = response.body().data
        } catch (Exception ignored) {
            code = 500
        }

        if (code == 200) {
            println('Rancher Stacks')
            println('---')

            data.sort {a, b -> (a.name <=> b.name) }.each { formatStack it }
        } else {
            printItem('failed', [iconName: 'network-error'])
        }

    }

    static printItem(msg, Map<String, String> opts) {
        def o = opts.collect { "${it.key}=${it.value}" }.join(' ')

        def l = [msg, o].join(' | ')

        println(l)
    }
}
