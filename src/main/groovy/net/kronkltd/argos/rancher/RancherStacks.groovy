package net.kronkltd.argos.rancher

import io.rancher.Rancher
import io.rancher.service.StackService
import io.rancher.type.Stack

class RancherStacks extends Panel {
    private final Rancher rancher
    private final String baseUrl

    private final StackService stackService

    RancherStacks(String baseUrl, Rancher rancher) {
        super('Rancher Stacks')
        this.rancher = rancher
        this.baseUrl = baseUrl
        this.stackService = rancher.type(StackService)

        try {
            def response = stackService.list().execute()
            def data = response.body().data
            data.sort {a, b -> (a.name <=> b.name) }.each { formatStack it }
        } catch (Exception ignored) {
            title.iconName = 'network-error'
            addItem(new Item('failed', [iconName: 'network-error']))
        }
    }

    def formatStack(Stack stack) {
        def healthy = stack.healthState == 'healthy'

        if (stack.state == 'active') {
            def color = healthy ? 'green' : 'red'
            def href = "${baseUrl}env/1a5/apps/stacks/${stack.id}"
            addItem(new Item(stack.name, [color: color, href: href]))
            addItem(new Item('--stop'))
            addItem(new Item('--start'))
        }
    }
}
