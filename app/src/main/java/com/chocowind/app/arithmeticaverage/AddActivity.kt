package com.chocowind.app.arithmeticaverage

import com.chocowind.app.arithmeticaverage.db.PayDatabase
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.text.isDigitsOnly
import com.chocowind.app.arithmeticaverage.model.PayData
import com.chocowind.app.arithmeticaverage.model.Payer
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.confirm_add.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddActivity : AppCompatActivity() {
    lateinit var db: PayDatabase
    lateinit var context: Context
    lateinit var date: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        setTitle(R.string.activity_add_title_txt)

        context = this
        date = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())

        db = PayDatabase.getDatabase(context)

        et_add_date.setText(date)

        btn_add_add.setOnClickListener {
            if (checkPayForm()) {
                val name = et_add_payer.text.toString().trim()
                val item = et_add_item.text.toString().trim()
                val price = et_add_price.text.toString().trim().toInt()
                val date = date
                val remark = et_add_remark.text.toString().trim()

                val confirmAlert: View = View.inflate(context, R.layout.confirm_add, null)
                confirmAlert.confirm_date.text = date
                confirmAlert.confirm_payer.text = name
                confirmAlert.confirm_item.text = item
                confirmAlert.confirm_remark.text = remark
                confirmAlert.confirm_price.text = price.toString()

                AlertDialog.Builder(context)
                    .setTitle(R.string.add_alert_title)
                    .setView(confirmAlert)
                    .setPositiveButton(R.string.add_alert_confirm_btn) { _, _ ->
                        GlobalScope.launch {
                            val payer = Payer(name)
                            if (db.payDao().getPayer(name) != payer)
                                db.payDao().insertPayers(payer)

                            val payData = PayData(db.payDao().getPayer(name).uuid, item, price, date, remark)
                            db.payDao().insertPayDatas(payData)
                        }
                        clearAll()
                        Toast.makeText(context, R.string.add_successful_txt, Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton(R.string.add_alert_cancel_btn, null)
                    .create()
                    .show()
            }
        }
    }

    fun checkPayForm(): Boolean {
        if (et_add_payer.text.isNotEmpty() && et_add_item.text.isNotEmpty() && et_add_price.text.isNotEmpty()) {
            if (et_add_price.text.toString().isDigitsOnly())
                return true
            else
                Toast.makeText(context, R.string.add_fail_not_digital_txt, Toast.LENGTH_SHORT).show()
        }
        Toast.makeText(context, R.string.add_fail_empty_txt, Toast.LENGTH_SHORT).show()
        return false
    }

    fun clearAll() {
        date = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
        et_add_date.setText(date)
        et_add_item.setText("")
        et_add_payer.setText("")
        et_add_price.setText("")
        et_add_remark.setText("")
    }

    override fun onResume() {
        super.onResume()
        date = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
    }
}