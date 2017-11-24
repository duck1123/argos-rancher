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
        def healthy = stack.healthState == 'healthy'

        if (stack.state == 'active') {
            def msg = "${stack.name}"
            def color = healthy ? 'green' : 'red'
            def href = "${baseUrl}env/1a5/apps/stacks/${stack.id}"
            printItem(msg, [href: href, color: color])
            println('--stop')
            println('--start')
            // printItem(msg + "\\n${stack.state}", opts + ([alternate: 'true'] as Map<String, GString>))
        }
    }

    def listStacks() {
        def call = rancher.type(StackService).list()

        def code, response
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
        def optionsString = opts.collect { "${it.key}=${it.value}" }.join(' ')
        def line = [msg, optionsString].join(' | ')
        println(line)
    }
}
