package com.chocowind.app.arithmeticaverage.model

data class ReceiverTemp(val receiver: Receiver, val needToPay: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ReceiverTemp

        if (receiver != other.receiver) return false
        if (needToPay != other.needToPay) return false

        return true
    }

    override fun hashCode(): Int {
        var result = receiver.hashCode()
        result = 31 * result + needToPay
        return result
    }
}
