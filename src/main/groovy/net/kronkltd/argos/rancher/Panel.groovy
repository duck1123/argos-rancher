package net.kronkltd.argos.rancher

class Panel {
    String title

    List<Item> items = []

    Panel() { }

    Panel(String title) {
        this.title = title
    }

    def addItem(Item item) {
        items.add(item)
    }

    String toString() {

        def parts = [title, '---']
        parts.addAll(items as List<String>)

        def message = parts.join('\n')
        return message
    }
}
