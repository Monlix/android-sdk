package com.monlixv2.adapters

import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.monlixv2.service.models.ads.Ad
import com.monlixv2.R


class OfferAdapter(
    private val dataSource: ArrayList<Ad>
) : RecyclerView.Adapter<OfferAdapter.OfferHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OfferHolder {
        val inflatedView = parent.inflate(R.layout.offer_item, false)
        return OfferHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: OfferHolder, position: Int) {
        holder.title.text = dataSource[position].name
        holder.description.text = dataSource[position].description
        holder.payout.text = "${dataSource[position].payout}"
        holder.currency.text = "${dataSource[position].currency}"
        holder.offer = dataSource[position]
        Glide.with(holder.image.context).load(dataSource[position].logo)
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    class OfferHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        val title: TextView
        val description: TextView
        val payout: TextView
        val currency: TextView
        val image: ImageView
        lateinit var offer: Ad

        init {
            title = v.findViewById(R.id.title)
            payout = v.findViewById(R.id.payout);
            description = v.findViewById(R.id.description)
            image = v.findViewById(R.id.image)
            currency = v.findViewById(R.id.currency)
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(offer.link)
            v?.let { startActivity(it.context,i, null) }
        }
    }
}
