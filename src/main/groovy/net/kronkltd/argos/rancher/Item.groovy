package net.kronkltd.argos.rancher

class Item {
    String message

    String color
    String font
    Integer size
    String iconName

    Item() {}

    Item(String message) {
        this.message = message
    }

    String toString() {
        def opts = [color: color,
                    font: font,
                    size: size,
                    iconName: iconName]

        def optString = opts
                .collect { (it.value != null) ? "${it.key}=${it.value}" : null }
                .findAll()
                .join(' ')

        return [message, optString]
                .findAll()
                .join(' | ')
    }
}
