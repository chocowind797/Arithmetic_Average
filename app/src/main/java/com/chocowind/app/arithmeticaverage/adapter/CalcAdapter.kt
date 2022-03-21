package com.chocowind.app.arithmeticaverage.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chocowind.app.arithmeticaverage.R
import com.chocowind.app.arithmeticaverage.model.PayResult
import com.chocowind.app.arithmeticaverage.model.Receiver
import com.chocowind.app.arithmeticaverage.model.ReceiverTemp
import kotlinx.android.synthetic.main.calcycle_list.view.*

class CalcAdapter(val listener: CalcRowOnClickListener, val nested_listener: ReceiverAdapter.ReceiverRowOnClickListener) :
    RecyclerView.Adapter<CalcAdapter.CalcViewHolder>() {
    private var payers = ArrayList<PayResult>()
    val viewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()

    fun setPayers(payers: ArrayList<PayResult>) {
        this.payers = payers
    }

    interface CalcRowOnClickListener {
        fun onItemClickListener(payResult: PayResult)
        fun onItemLongClickListener(payResult: PayResult)
    }

    class CalcViewHolder(view: View, val listener: CalcRowOnClickListener) :
        RecyclerView.ViewHolder(view) {
        val recyclerView = view.calc_receiver_recyclerview
        val tv_calc_payer = view.tv_calc_payer

        fun bind(receiver: Receiver) {
            tv_calc_payer.text = receiver.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalcViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.calcycle_list, parent, false)
        return CalcViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: CalcViewHolder, position: Int) {
        val payer = payers[position]
        holder.bind(payer.payer)

        // holder.itemView -> 整列資料
        holder.itemView.setOnClickListener {
            listener.onItemClickListener(payer)
        }

        holder.itemView.setOnLongClickListener {
            listener.onItemLongClickListener(payer)
            false
        }

        holder.recyclerView.apply {
            adapter = ReceiverAdapter(payer.receivers, nested_listener)
            layoutManager = LinearLayoutManager(context)
            setRecycledViewPool(viewPool)
        }
    }

    override fun getItemCount(): Int {
        return payers.size
    }
}