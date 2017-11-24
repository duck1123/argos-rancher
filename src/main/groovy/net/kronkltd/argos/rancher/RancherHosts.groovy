package net.kronkltd.argos.rancher

class RancherHosts {
    RancherHosts() {
    }

    Panel toPanel() {
        def panel = new Panel('Rancher Hosts')

        def i = new Item('Foo')
        i.size = 12

        panel.addItem(i)


        return panel
    }
}
