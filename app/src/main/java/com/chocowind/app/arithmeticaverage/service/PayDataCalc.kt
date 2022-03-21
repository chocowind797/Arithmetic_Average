package com.chocowind.app.arithmeticaverage.service

import android.util.Log
import com.chocowind.app.arithmeticaverage.db.PayDatabase
import com.chocowind.app.arithmeticaverage.model.PayResult
import com.chocowind.app.arithmeticaverage.model.Payer
import com.chocowind.app.arithmeticaverage.model.Receiver
import com.chocowind.app.arithmeticaverage.model.ReceiverTemp
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.abs
import kotlin.math.round

class PayDataCalc(val db: PayDatabase, val amount: Int) {
    fun mapReceiver(other: String, payers: ArrayList<Payer>): ArrayList<PayResult> {
        var zero = 0
        val total = db.payDao().getTotal()
        val price = HashMap<Int, Receiver>()
        val receiversNegative = ArrayList<Receiver>()
        val receiversPositive = ArrayList<Receiver>()
        val correspond = ArrayList<ArrayList<Receiver>>()

        val totalPerPerson = round((total / amount).toDouble()).toInt()
        payers.forEach {
            val havepaid = db.payDao().getTotalByPayer(it.uuid)
            val needToPay = totalPerPerson - havepaid
            val receiver = Receiver(it.name, havepaid, needToPay)

            if (price[abs(needToPay)] == null) {
                price[abs(needToPay)] = receiver

                if (needToPay > 0)
                    receiversPositive.add(receiver)
                else if (needToPay < 0)
                    receiversNegative.add(receiver)
                else
                    zero++
            } else {
                if (price[abs(needToPay)]?.needToPay?.plus(needToPay) ?: 0 == 0) {
                    val removeItem =
                        price.remove(abs(needToPay)) ?: receiver
                    correspond.add(arrayListOf(receiver, removeItem))
//                    Log.d("CalcActivity", removeItem.toString())

                    receiversNegative.remove(removeItem)
                    receiversPositive.remove(removeItem)
                    zero += 2
                }
            }
//            Log.d("CalcActivity", receiver.toString())
        }

        for (i in 0 until (amount - receiversNegative.size - receiversPositive.size - zero))
            receiversPositive.add(Receiver(other, 0, totalPerPerson))

        Collections.sort(receiversNegative, Comparator.comparingInt(Receiver::needToPay))
        Collections.sort(receiversPositive, Comparator.comparingInt(Receiver::needToPay).reversed())

        return calcrepayer(receiversNegative, receiversPositive, correspond)
    }

    private fun calcrepayer(
        receiversNegative: ArrayList<Receiver>,
        receiversPositive: ArrayList<Receiver>,
        correspond: ArrayList<ArrayList<Receiver>>,
    ): ArrayList<PayResult> {
        val payResults = ArrayList<PayResult>()

        correspond.forEach {
            if (it[0].needToPay > 0)
                payResults.add(PayResult(it[0], arrayListOf(ReceiverTemp(it[1], it[0].needToPay))))
            else
                payResults.add(PayResult(it[1], arrayListOf(ReceiverTemp(it[0], it[1].needToPay))))
        }

//        Log.d("CalcActivity", payResults.toString())
//        Log.d("CalcActivity", receiversNegative.toString())
//        Log.d("CalcActivity", receiversPositive.toString())

        receiversPositive.forEach { pos ->
            val receivers = ArrayList<ReceiverTemp>()

            receiversNegative.forEach inner@ { nev ->
                if(pos.needToPay != 0) {
                    if (pos.needToPay + nev.needToPay < 0) {
                        receivers.add(ReceiverTemp(nev, pos.needToPay))
                        nev.needToPay += pos.needToPay
                        return@inner
                    } else if (pos.needToPay + nev.needToPay == 0) {
                        receivers.add(ReceiverTemp(nev, pos.needToPay))
                        receiversNegative.remove(nev)
                        return@inner
                    } else {
                        receivers.add(ReceiverTemp(nev, nev.needToPay))
                        pos.needToPay += nev.needToPay
                        receiversNegative.remove(nev)
                    }
                }
            }
            receivers.sortedWith(Comparator { o1, o2 ->
                o1.receiver.name.hashCode() - o2.receiver.name.hashCode()
            })
            payResults.add(PayResult(pos, receivers))
        }
        return payResults
    }
}