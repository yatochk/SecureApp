package com.yatochk.secure.app.model.images

data class Album(
    var name: String,
    var preview: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Album

        if (name != other.name) return false
        if (!preview.contentEquals(other.preview)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + preview.contentHashCode()
        return result
    }
}