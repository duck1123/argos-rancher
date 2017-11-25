package net.kronkltd.argos.rancher

class Panel {
    Item title

    List<Item> items = []

    Panel() { }

    Panel(String titleText) {
        this(new Item(titleText))
    }

    Panel(Item title) {
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
