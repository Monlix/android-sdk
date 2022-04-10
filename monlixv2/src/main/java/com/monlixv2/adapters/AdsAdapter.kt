package com.monlixv2.adapters

import android.content.Intent
import android.net.Uri
import android.support.v4.app.INotificationSideChannel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.monlixv2.R
import com.monlixv2.service.models.ads.Ad
import com.monlixv2.service.models.surveys.Survey
import com.monlixv2.service.models.transactions.Transaction
import com.monlixv2.ui.components.squareprogressbar.SquareProgressView
import com.monlixv2.util.Constants.CLICKED_QUERY_PARAM
import com.monlixv2.util.Constants.TRANSACTION_ITEM_STATUS_DRAWABLE
import com.monlixv2.util.Constants.TRANSACTION_ITEM_STATUS_TEXT_COLOR



class AdsAdapter(
    private val dataSource: ArrayList<Ad>
) : RecyclerView.Adapter<AdsAdapter.AdHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdHolder {
        return AdHolder(parent.inflate(R.layout.offer_item_v2,false))

    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) LARGE_CARD else SMALL_CARD
    }


    override fun onBindViewHolder(holder: AdHolder, position: Int) {
        holder.title.text = dataSource[position].name
        holder.points.text = "${dataSource[position].payout} \n ${dataSource[position].currency}"
        holder.description.text = dataSource[position].description
        holder.ad = dataSource[position]

        Glide.with(holder.itemView.context).load(dataSource[position].logo)
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .error(ContextCompat.getDrawable(holder.itemView.context, R.drawable.offer_placeholder))
            .into(holder.offerImage)
    }


    class AdHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        val title: TextView = v.findViewById(R.id.offerTitle)
        val description: TextView = v.findViewById(R.id.offerDescription)
        val points: TextView = v.findViewById(R.id.offerPoints)
        lateinit var ad: Ad
        val progressView: SquareProgressView? = v.findViewById(R.id.transactionProgress)
        val offerImage: ImageView = v.findViewById(R.id.offerImage)


        init {
            v.setOnClickListener(this)
            progressView?.setWidthInDp(3)
            progressView?.setColor(ContextCompat.getColor(v.context, R.color.orangeV1))
            progressView?.setRoundedCorners(true, 8f)
        }

        override fun onClick(v: View?) {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(ad.link)
            v?.let { ContextCompat.startActivity(it.context, i, null) }
        }
    }
}

