package com.chocowind.app.arithmeticaverage.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chocowind.app.arithmeticaverage.R
import com.chocowind.app.arithmeticaverage.model.Receiver
import com.chocowind.app.arithmeticaverage.model.ReceiverTemp
import kotlinx.android.synthetic.main.calcycle_receiver_list.view.*

class ReceiverAdapter(val receivers: ArrayList<ReceiverTemp>, val listener: ReceiverRowOnClickListener) :
    RecyclerView.Adapter<ReceiverAdapter.ReceiverViewHolder>() {

        interface ReceiverRowOnClickListener {
            fun onItemClickListener(receiverTemp: ReceiverTemp)
            fun onItemLongClickListener(receiverTemp: ReceiverTemp)
        }

        class ReceiverViewHolder(view: View, val listener: ReceiverRowOnClickListener) :
            RecyclerView.ViewHolder(view) {
            val tv_calc_receiver = view.tv_calc_receiver
            val tv_calc_needtopay = view.tv_calc_needtopay

            fun bind(receiverTemp: ReceiverTemp) {
                tv_calc_receiver.text = receiverTemp.receiver.name
                tv_calc_needtopay.text = receiverTemp.needToPay.toString()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiverViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.calcycle_receiver_list, parent, false)
            return ReceiverViewHolder(view, listener)
        }

        override fun onBindViewHolder(holder: ReceiverViewHolder, position: Int) {
            val receiver = receivers[position]
            holder.bind(receiver)
            // holder.itemView -> 整列資料
            holder.itemView.setOnClickListener {
                listener.onItemClickListener(receiver)
            }

            holder.itemView.setOnLongClickListener {
                listener.onItemLongClickListener(receiver)
                false
            }
        }

        override fun getItemCount(): Int {
            return receivers.size
        }
}