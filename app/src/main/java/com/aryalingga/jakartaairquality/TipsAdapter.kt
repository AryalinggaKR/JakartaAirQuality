package com.aryalingga.jakartaairquality

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class TipsAdapter(private val tips: List<Tip>) : RecyclerView.Adapter<TipsAdapter.TipViewHolder>() {

    class TipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.cardView)
        val tvTipTitle: TextView = itemView.findViewById(R.id.tvTipTitle)
        val tvTipDescription: TextView = itemView.findViewById(R.id.tvTipDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tip, parent, false)
        return TipViewHolder(view)
    }

    override fun onBindViewHolder(holder: TipViewHolder, position: Int) {
        val tip = tips[position]
        holder.tvTipTitle.text = tip.title
        holder.tvTipDescription.text = tip.description

        holder.cardView.setOnClickListener {
            if (holder.tvTipDescription.visibility == View.GONE) {
                holder.tvTipDescription.visibility = View.VISIBLE
            } else {
                holder.tvTipDescription.visibility = View.GONE
            }
        }
    }

    override fun getItemCount() = tips.size
}
