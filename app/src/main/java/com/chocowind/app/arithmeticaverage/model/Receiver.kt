package com.chocowind.app.arithmeticaverage.model

data class Receiver(
    var name: String,
    var havepaid: Int,
    var needToPay: Int
) {
    override fun equals(other: Any?): Boolean {
        if (other !is Receiver) {
            return false
        }

        val rec = other as Receiver
        if(this.name == rec.name && this.havepaid == rec.havepaid && this.needToPay == rec.needToPay) {
            return true
        }

        return false
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + havepaid
        result = 31 * result + needToPay
        return result
    }
}