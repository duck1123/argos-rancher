package net.kronkltd.argos.rancher

class Item {
    String message

    String color
    String font
    Integer size
    String iconName
    String href

    Item() {}

    Item(String message) {
        this()
        this.message = message
    }

    Item(String message, Map<String, Object> opts) {
        this(message)

        def t = this

        opts.each {i -> t.setProperty(i.key, i.value) }
    }

    String toString() {
        def opts = [color: color,
                    font: font,
                    size: size,
                    iconName: iconName,
                    href: href]

        def optString = opts
                .collect { (it.value != null) ? "${it.key}=${it.value}" : null }
                .findAll()
                .join(' ')

        return [message, optString]
                .findAll()
                .join(' | ')
    }
}
