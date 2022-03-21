package com.chocowind.app.arithmeticaverage.model

data class PayResult(val payer: Receiver, val receivers: ArrayList<ReceiverTemp>) {
    override fun equals(other: Any?): Boolean {
        if(other !is PayResult)
            return false

        val payResult = other as PayResult
        if (payResult.payer.name == this.payer.name)
            return true
        return false
    }

    override fun hashCode(): Int {
        return payer.hashCode()
    }
}
