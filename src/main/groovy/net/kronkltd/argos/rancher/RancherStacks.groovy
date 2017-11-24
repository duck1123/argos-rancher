package net.kronkltd.argos.rancher

import io.rancher.Rancher
import io.rancher.service.StackService
import io.rancher.type.Stack

class RancherStacks extends Panel {
    private final Rancher rancher
    private final String baseUrl

    private final StackService stackService

    RancherStacks(String baseUrl, Rancher rancher) {
        this.rancher = rancher
        this.baseUrl = baseUrl
        this.stackService = rancher.type(StackService)
        this.title = 'Rancher Stacks'

        try {
            def response = stackService.list().execute()
            def data = response.body().data
            data.sort {a, b -> (a.name <=> b.name) }.each { formatStack it }
        } catch (Exception ignored) {
            def item = new Item('failed')
            item.iconName = 'network-error'
            addItem(item)
        }
    }

    def formatStack(Stack stack) {
        def healthy = stack.healthState == 'healthy'

        if (stack.state == 'active') {
            def item = new Item(stack.name)
            item.color = healthy ? 'green' : 'red'
            item.href = "${baseUrl}env/1a5/apps/stacks/${stack.id}"
            addItem(item)

            addItem(new Item('--stop'))
            addItem(new Item('--start'))
        }
    }
}
