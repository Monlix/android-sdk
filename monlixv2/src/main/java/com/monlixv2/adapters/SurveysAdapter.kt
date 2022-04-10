package com.monlixv2.adapters

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
import com.monlixv2.service.models.surveys.Survey
import com.monlixv2.service.models.transactions.Transaction
import com.monlixv2.ui.components.squareprogressbar.SquareProgressView
import com.monlixv2.util.Constants.CLICKED_QUERY_PARAM
import com.monlixv2.util.Constants.TRANSACTION_ITEM_STATUS_DRAWABLE
import com.monlixv2.util.Constants.TRANSACTION_ITEM_STATUS_TEXT_COLOR


const val LARGE_CARD = 0
const val SMALL_CARD = 1

class SurveysAdapter(
    private val dataSource: ArrayList<Survey>
) : RecyclerView.Adapter<SurveysAdapter.SurveyHolder>() {

    private var expandedIds = mutableSetOf<String>();


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SurveyHolder {
        val inflatedView = parent.inflate(
            when (viewType) {
                LARGE_CARD -> R.layout.survey_item_banner
                else -> {
                    R.layout.survey_item
                }
            }, false
        )

        return SurveyHolder(inflatedView)

    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) LARGE_CARD else SMALL_CARD
    }


    override fun onBindViewHolder(holder: SurveyHolder, position: Int) {
        holder.title.text = dataSource[position].name
        holder.points.text = dataSource[position].payout
        holder.currency.text = dataSource[position].currency
        holder.time.text = "~15 min"
    }


    class SurveyHolder(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.findViewById(R.id.surveyTitle)
        val points: TextView = v.findViewById(R.id.surveyPoints)
        val time: TextView = v.findViewById(R.id.surveyTime)
        val rating: TextView = v.findViewById(R.id.surveyRating)
        val currency: TextView = v.findViewById(R.id.surveyCurrency)
    }
}

