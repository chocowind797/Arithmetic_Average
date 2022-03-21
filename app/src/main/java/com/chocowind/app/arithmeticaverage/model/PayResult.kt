package com.chocowind.app.arithmeticaverage.model

data class PayResult(val payer: Receiver, val receivers: ArrayList<ReceiverTemp>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PayResult

        if (payer != other.payer) return false
        if (receivers != other.receivers) return false

        return true
    }

    override fun hashCode(): Int {
        var result = payer.hashCode()
        result = 31 * result + receivers.hashCode()
        return result
    }
}
