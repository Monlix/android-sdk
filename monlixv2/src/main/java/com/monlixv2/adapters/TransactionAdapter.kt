package com.monlixv2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.monlixv2.R
import com.monlix.service.models.Transaction


class TransactionAdapter(
    private val dataSource: ArrayList<Transaction>
) : RecyclerView.Adapter<TransactionAdapter.TransactionHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionHolder {
        val inflatedView = parent.inflate(R.layout.transaction_item, false)
        return TransactionHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        holder.title.text = dataSource[position].name
        holder.payout.text = "${dataSource[position].reward}"
        holder.currency.text = "${dataSource[position].currency}"
        holder.status.text = "${dataSource[position].status}"
        holder.transactionId.text = "${dataSource[position].id}"
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    class TransactionHolder(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView
        val transactionId: TextView
        val status: TextView
        val payout: TextView
        val currency: TextView

        init {
            title = v.findViewById(R.id.title)
            transactionId = v.findViewById(R.id.transaction_id)
            status = v.findViewById(R.id.status)
            payout = v.findViewById(R.id.payout);
            currency = v.findViewById(R.id.currency)
        }
    }
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}
