package com.monlixv2.adapters

import android.content.Intent
import android.net.Uri
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.monlixv2.R
import com.monlixv2.service.models.surveys.Survey
import com.monlixv2.util.UIHelpers


const val LARGE_CARD = 0
const val SMALL_CARD = 1

class SurveysAdapter(
    private val dataSource: ArrayList<Survey>
) : RecyclerView.Adapter<SurveysAdapter.SurveyHolder>() {

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
        UIHelpers.dangerouslySetHTML(dataSource[position].name, holder.title)
        // trick to handle text auto-resizing
        TextViewCompat.setAutoSizeTextTypeWithDefaults(holder.points, TextViewCompat.AUTO_SIZE_TEXT_TYPE_NONE);
        holder.points.text = ""
        if(holder.itemViewType == LARGE_CARD) {
            TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(holder.points, 12,
                60, 2, TypedValue.COMPLEX_UNIT_SP);
        }else {
            TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(holder.points, 12,
                30, 2, TypedValue.COMPLEX_UNIT_SP);
        }

        holder.points.text = if(dataSource[position].payout != null) dataSource[position].payout else "${dataSource[position].minPayout} - ${dataSource[position].maxPayout}"
        holder.currency.text = dataSource[position].currency
        holder.time.text = "~${if(dataSource[position].maxDuration !== null) dataSource[position].maxDuration else "15"} min"
        holder.survey = dataSource[position]
        holder.ratingContainer.visibility = if(dataSource[position].rank !== null) View.VISIBLE else View.GONE
        if(dataSource[position].rank !== null) {
            holder.rating!!.text = "%.1f".format(dataSource[position].rank!!*5)
        }

        if(holder.itemViewType == LARGE_CARD) {
            holder.timerContainer.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.border_radius_item)
            holder.ratingContainer.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.border_radius_item_transparent_20)
        }else {
            holder.timerContainer.background = ContextCompat.getDrawable(holder.itemView.context,
            if(dataSource[position].rank !== null) R.drawable.border_radius_item_half_corners else R.drawable.border_radius_item)
            holder.ratingContainer.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.border_radius_item_transparent_20_half_corners)
        }
    }


    class SurveyHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        val title: TextView = v.findViewById(R.id.surveyTitle)
        val points: TextView = v.findViewById(R.id.surveyPoints)
        val time: TextView = v.findViewById(R.id.surveyTime)
        val ratingContainer: LinearLayout = v.findViewById(R.id.ratingContainer)
        val timerContainer: LinearLayout = v.findViewById(R.id.surveyTimerContainer)
        val rating: TextView? = v.findViewById(R.id.surveyRating)
        val currency: TextView = v.findViewById(R.id.surveyCurrency)
        lateinit var survey: Survey


        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(survey.link)
            v?.let { ContextCompat.startActivity(it.context, i, null) }
        }
    }
}

