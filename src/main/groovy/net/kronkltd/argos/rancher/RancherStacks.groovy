package net.kronkltd.argos.rancher

import io.rancher.Rancher
import io.rancher.service.StackService
import io.rancher.type.Stack

class RancherStacks {
    private final Rancher rancher
    private final String baseUrl

    private final StackService stackService

    RancherStacks(String baseUrl, Rancher rancher) {
        this.rancher = rancher
        this.baseUrl = baseUrl
        this.stackService = rancher.type(StackService)
    }

    def formatStack(Panel panel, Stack stack) {
        def healthy = stack.healthState == 'healthy'

        if (stack.state == 'active') {
            def item = new Item(stack.name)
            item.color = healthy ? 'green' : 'red'
            item.href = "${baseUrl}env/1a5/apps/stacks/${stack.id}"
            panel.addItem(item)

            panel.addItem(new Item('--stop'))
            panel.addItem(new Item('--start'))
        }
    }

    String toString() {
        def p = new Panel('Rancher Stacks')

        try {
            def response = stackService.list().execute()
            def data = response.body().data
            data.sort {a, b -> (a.name <=> b.name) }.each { formatStack(p, it) }
        } catch (Exception ignored) {
            def item = new Item('failed')
            item.iconName = 'network-error'
            p.addItem(item)
        }

        p
    }
}
