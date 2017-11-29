package net.kronkltd.argos.rancher

import io.rancher.Rancher
import io.rancher.service.HostService

class RancherHosts extends Panel {
    private final Rancher rancher
    private final String baseUrl

    RancherHosts(String baseUrl, Rancher rancher) {
        super('Rancher Hosts')
        this.baseUrl = baseUrl
        this.rancher = rancher

        def hosts = rancher.type(HostService).list().execute().body().data

        hosts.each {h ->
            addItem(new Item(h.name))

        }

        addItem(new Item('Foo', [size: 12]))
    }
}
