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


class AdsAdapter(
    private val dataSource: ArrayList<Ad>
) : RecyclerView.Adapter<AdsAdapter.AdHolder>() {


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
        holder.title.text = dataSource[position].name
        holder.points.text = "${dataSource[position].payout} \n ${dataSource[position].currency}"
        holder.description.text = dataSource[position].description
        holder.ad = dataSource[position]

        Glide.with(holder.itemView.context).load(dataSource[position].logo)
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .error(ContextCompat.getDrawable(holder.itemView.context, R.drawable.offer_placeholder))
            .into(holder.offerImage)
        holder.itemView.alpha = if(dataSource[position].visited) 0.3f else 1f
    }


    class AdHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        val title: TextView = v.findViewById(R.id.adTitle)
        val description: TextView = v.findViewById(R.id.adDescription)
        val points: TextView = v.findViewById(R.id.adPoints)
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

