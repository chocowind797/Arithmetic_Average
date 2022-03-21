package com.chocowind.app.arithmeticaverage

import com.chocowind.app.arithmeticaverage.db.PayDatabase
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.chocowind.app.arithmeticaverage.adapter.CalcAdapter
import com.chocowind.app.arithmeticaverage.adapter.ReceiverAdapter
import com.chocowind.app.arithmeticaverage.model.PayResult
import com.chocowind.app.arithmeticaverage.model.Payer
import com.chocowind.app.arithmeticaverage.model.Receiver
import com.chocowind.app.arithmeticaverage.model.ReceiverTemp
import com.chocowind.app.arithmeticaverage.service.PayDataCalc
import kotlinx.android.synthetic.main.activity_calc.*
import kotlinx.android.synthetic.main.calcycle_list.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class CalcActivity : AppCompatActivity() {
    var amount = 1
    lateinit var calc: PayDataCalc
    lateinit var context: Context
    lateinit var db: PayDatabase
    lateinit var calcAdapter: CalcAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc)
        setTitle(R.string.activity_calc_title_txt)

        amount = intent.getIntExtra("amount", 1)

        context = this
        db = PayDatabase.getDatabase(context)

        calc_recyclerview.apply {
            calcAdapter = CalcAdapter(onCalcClickEvent, onReceiverClickEvent)
            layoutManager = LinearLayoutManager(context)
            adapter = calcAdapter
        }

        GlobalScope.launch {
            val payers = db.payDao().getAllPayers()
            calc = PayDataCalc(db, amount)
//            Log.d("CalcActivity", payers.toString())
            val other = resources.getString(R.string.calc_no_pay_txt)
            var results_temp: List<PayResult> = calc.mapReceiver(other, payers as ArrayList<Payer>)
//            Log.d("CalcActivity", results.toString())
            val count = amount - payers.size - 1

            results_temp = results_temp.toSet().toList()

            val results = ArrayList<PayResult>(results_temp)

            results.sortWith(Comparator { o1, o2 ->
                o1.payer.name.hashCode() - o2.payer.name.hashCode()
            })

            calcAdapter.setPayers(results)
            runOnUiThread {
                calcAdapter.notifyDataSetChanged()
            }
        }
    }

    private val onCalcClickEvent = object : CalcAdapter.CalcRowOnClickListener {
        override fun onItemClickListener(payResult: PayResult) {

        }

        override fun onItemLongClickListener(payResult: PayResult) {

        }
    }

    private val onReceiverClickEvent = object : ReceiverAdapter.ReceiverRowOnClickListener {
        override fun onItemClickListener(receiverTemp: ReceiverTemp) {

        }

        override fun onItemLongClickListener(receiverTemp: ReceiverTemp) {

        }
    }
}