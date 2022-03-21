package com.chocowind.app.arithmeticaverage

import com.chocowind.app.arithmeticaverage.db.PayDatabase
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.chocowind.app.arithmeticaverage.adapter.PayDataAdapter
import com.chocowind.app.arithmeticaverage.model.PayData
import kotlinx.android.synthetic.main.activity_query.*
import kotlinx.android.synthetic.main.confirm_add.view.*
import kotlinx.android.synthetic.main.detail_remark.view.*
import kotlinx.android.synthetic.main.querecycle_list_title.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class QueryActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var db: PayDatabase
    lateinit var payDataAdapter: PayDataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query)
        setTitle(R.string.activity_query_title_txt)

        context = this

        db = PayDatabase.getDatabase(context)

        query_recyclerview.apply {
            payDataAdapter = PayDataAdapter(onClickEvent)
            layoutManager = LinearLayoutManager(context)
            adapter = payDataAdapter
        }

        GlobalScope.launch {
            val payDatas = db.payDao().getAllPayDatas()
            payDataAdapter.payDatas = payDatas as ArrayList<PayData>
            runOnUiThread {
                payDataAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(Menu.NONE, 1, 0, resources.getString(R.string.query_menu_clear))
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            1 -> {
                AlertDialog.Builder(context)
                    .setTitle(R.string.query_menu_clear)
                    .setMessage(R.string.query_menu_clear_alert_message)
                    .setPositiveButton(R.string.add_alert_confirm_btn) { _, _ ->
                        GlobalScope.launch {
                            db.payDao().deleteAllPayer()
                            val payDatas = db.payDao().getAllPayDatas()
                            payDataAdapter.payDatas = payDatas as ArrayList<PayData>
                            runOnUiThread {
                                payDataAdapter.notifyDataSetChanged()
                                Toast.makeText(context, R.string.query_menu_clear_successful, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    .create()
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun sort(view: View) {
        var symbol = "▼"
        clearTitle()

        if (view.tag == payDataAdapter.tag) {
            payDataAdapter.reverse *= -1
            symbol = if (payDataAdapter.reverse == 1)
                "▼"
            else
                "▲"
        } else {
            payDataAdapter.reverse = 1
        }

        Collections.sort(payDataAdapter.payDatas, Comparator.comparing {
            val compare: Int = when (view.tag) {
                "date" -> it.date.hashCode()
                "payer" -> it.payer.hashCode()
                "item" -> it.item.hashCode()
                "price" -> it.price
                else -> it.date.hashCode()
            }
            compare * payDataAdapter.reverse
        })

        val view_txt = view as TextView
        view_txt.text = view_txt.text.toString() + symbol
        payDataAdapter.tag = view.tag.toString()
        payDataAdapter.notifyDataSetChanged()
    }

    private fun clearTitle() {
        tv_title_date.setText(R.string.date_txt)
        tv_title_payer.setText(R.string.payer_txt)
        tv_title_item.setText(R.string.item_txt)
        tv_title_price.setText(R.string.price_txt)
    }

    private val onClickEvent = object : PayDataAdapter.QueryRowOnClickListener {
        override fun onItemClickListener(payData: PayData) {
            val confirmAlert: View = View.inflate(context, R.layout.confirm_add, null)

            confirmAlert.confirm_date.text = payData.date
            confirmAlert.confirm_payer.text = db.payDao().getPayer(payData.payer).name
            confirmAlert.confirm_item.text = payData.item
            confirmAlert.confirm_remark.text = payData.remark
            confirmAlert.confirm_price.text = payData.price.toString()

            AlertDialog.Builder(context)
                .setView(confirmAlert)
                .setNegativeButton(R.string.query_paydata_delete_btn){ _, _ ->
                    GlobalScope.launch {
                        db.payDao().deletePayData(payData)
                        val uuid = payData.payer
                        val exists = db.payDao().getPayDataByPayer(uuid)
                        if (exists.isEmpty())
                            db.payDao().deletePayer(uuid)
                        val payDatas = db.payDao().getAllPayDatas()
                        payDataAdapter.payDatas = payDatas as ArrayList<PayData>
                        runOnUiThread {
                            payDataAdapter.notifyDataSetChanged()
                            Toast.makeText(context, R.string.query_menu_clear_successful, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .create()
                .show()
        }

        override fun onRemarkDetailClickListener(payData: PayData) {
            val detail: View = View.inflate(context, R.layout.detail_remark, null)

            detail.detail_remark_et.setText(payData.remark)

            AlertDialog.Builder(context)
                .setView(detail)
                .setPositiveButton(R.string.query_detail_finish_btn, null)
                .setNegativeButton(R.string.query_detail_modify_btn) { _, _ ->
                    val remark = detail.detail_remark_et.text.toString()
                    payData.remark = remark
                    GlobalScope.launch {
                        db.payDao().updatePayData(payData)
                        val payDatas = db.payDao().getAllPayDatas()
                        payDataAdapter.payDatas = payDatas as ArrayList<PayData>
                        runOnUiThread {
                            payDataAdapter.notifyDataSetChanged()
                            Toast.makeText(context, R.string.query_detail_modify_successful, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .create()
                .show()
        }

        override fun setPayer(textView: TextView, uuid: Int) {
            GlobalScope.launch {
                textView.text = db.payDao().getPayer(uuid).name
            }
        }
    }
}