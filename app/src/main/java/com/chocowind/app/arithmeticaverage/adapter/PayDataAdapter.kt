package com.chocowind.app.arithmeticaverage.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chocowind.app.arithmeticaverage.R
import com.chocowind.app.arithmeticaverage.model.PayData
import kotlinx.android.synthetic.main.querecycle_list.view.*

class PayDataAdapter(val listener: QueryRowOnClickListener) :
    RecyclerView.Adapter<PayDataAdapter.MyViewHolder>() {
    var payDatas = ArrayList<PayData>()
    var tag = ""
    var reverse = 1

    interface QueryRowOnClickListener {
        fun onItemClickListener(payData: PayData)
        fun onItemLongClickListener(payData: PayData)
        fun onRemarkDetailClickListener(payData: PayData)
        fun setPayer(textView: TextView, uuid: Int)
    }

    class MyViewHolder(view: View, val listener: QueryRowOnClickListener) :
        RecyclerView.ViewHolder(view) {
        val tv_date = view.tv_query_date
        val tv_payer = view.tv_query_payer
        val tv_item = view.tv_query_item
        val tv_price = view.tv_query_price
        val tv_remark = view.tv_query_remark

        fun bind(payData: PayData) {
            tv_date.text = payData.date
            listener.setPayer(tv_payer, payData.payer)
            tv_item.text = payData.item
            tv_price.text = payData.price.toString()
            tv_remark.setOnClickListener {
                listener.onRemarkDetailClickListener(payData)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.querecycle_list, parent, false)
        return MyViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val payData = payDatas[position]
        holder.bind(payData)
        // holder.itemView -> 整列資料
        holder.itemView.setOnClickListener {
            listener.onItemClickListener(payData)
        }

        holder.itemView.setOnLongClickListener {
            listener.onItemLongClickListener(payData)
            false
        }
    }

    override fun getItemCount(): Int {
        return payDatas.size
    }
}