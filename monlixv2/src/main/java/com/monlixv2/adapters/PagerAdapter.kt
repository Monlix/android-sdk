package com.monlixv2.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.monlixv2.R
import com.monlixv2.service.models.ads.Ad

const val VIEW_TYPE_SURVEY_LIST = 0
const val VIEW_TYPE_OFFER_LIST = 1
const val VIEW_TYPE_ADS = 2

class PagerAdapter(private val context: Context, private val offers: Array<ArrayList<Ad>>) :
    RecyclerView.Adapter<PagerAdapter.PageHolder>() {

    override fun getItemViewType(position: Int): Int {

        return when (position) {
            VIEW_TYPE_SURVEY_LIST -> VIEW_TYPE_SURVEY_LIST
            VIEW_TYPE_OFFER_LIST -> VIEW_TYPE_OFFER_LIST
            else -> {
                VIEW_TYPE_ADS
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageHolder {
        return when (viewType) {
            VIEW_TYPE_SURVEY_LIST ->  PageHolder(LayoutInflater.from(context).inflate(R.layout.survey_page, parent, false) as RecyclerView)
            VIEW_TYPE_OFFER_LIST -> PageHolder(LayoutInflater.from(context).inflate(R.layout.transaction_fragment_container, parent, false) as LinearLayout)
            else -> {
                PageHolder(LayoutInflater.from(context).inflate(R.layout.transaction_fragment_container, parent, false) as LinearLayout)
            }
        }
    }

    override fun onBindViewHolder(holder: PageHolder, position: Int) {
//        if (position != offers.size){
//            val adapter = OfferAdapter(offers[position]);
//            (holder.offerView as RecyclerView).addItemDecoration(
//                UIHelpers.MarginItemDecoration(holder.itemView.context.resources.getDimensionPixelSize(R.dimen.offer_row_margin))
//            )
//            holder.offerView.adapter = adapter
//        }
    }

    override fun getItemCount(): Int = offers.size

    inner class PageHolder(view: View) : RecyclerView.ViewHolder(view) {
        val offerView = view
    }
}
