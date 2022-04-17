package com.monlixv2.adapters

import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.monlixv2.R
import com.monlixv2.service.models.ads.Ad
import com.monlixv2.service.models.campaigns.Campaign
import com.monlixv2.ui.fragments.AdsFragment
import com.monlixv2.util.UIHelpers


class AdsAdapter : RecyclerView.Adapter<AdsAdapter.AdHolder>() {

    private var dataSource: ArrayList<Ad> = ArrayList<Ad>()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdHolder {
        return AdHolder(parent.inflate(R.layout.ad_item,false))

    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) LARGE_CARD else SMALL_CARD
    }


    override fun onBindViewHolder(holder: AdHolder, position: Int) {
        UIHelpers.dangerouslySetHTML(dataSource[position].name, holder.title)
        UIHelpers.dangerouslySetHTML(dataSource[position].description, holder.description)
        holder.points.text = "+${dataSource[position].payout}"
        holder.currency.text = dataSource[position].currency
        holder.ad = dataSource[position]

        Glide.with(holder.itemView.context).load(dataSource[position].logo)
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .error(ContextCompat.getDrawable(holder.itemView.context, R.drawable.offer_placeholder))
            .into(holder.offerImage)
        holder.itemView.alpha = if(dataSource[position].visited) 0.3f else 1f
    }

    fun appendData(it: List<Ad>) {
        val currentPosition = dataSource.size
        dataSource.addAll(it)
        notifyItemRangeInserted(currentPosition, it.size)
    }



    class AdHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        val title: TextView = v.findViewById(R.id.adTitle)
        val description: TextView = v.findViewById(R.id.adDescription)
        val points: TextView = v.findViewById(R.id.adPoints)
        val currency: TextView = v.findViewById(R.id.adCurrency)
        val offerImage: ImageView = v.findViewById(R.id.adImage)
        lateinit var ad: Ad

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(ad.link)
            v?.let { ContextCompat.startActivity(it.context, i, null) }
        }
    }
}

