package net.kronkltd.argos.rancher

import io.rancher.Rancher

class RancherHosts extends Panel {
    private final Rancher rancher
    private final String baseUrl

    RancherHosts(String baseUrl, Rancher rancher) {
        this.baseUrl = baseUrl
        this.rancher = rancher

        this.title = 'Rancher Hosts'

        def i = new Item('Foo')
        i.size = 12

        addItem(i)
    }
}
