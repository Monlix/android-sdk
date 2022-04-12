package com.monlixv2.adapters

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
import com.monlixv2.service.models.transactions.Transaction
import com.monlixv2.ui.components.squareprogressbar.SquareProgressView
import com.monlixv2.util.Constants.CLICKED_QUERY_PARAM
import com.monlixv2.util.Constants.TRANSACTION_ITEM_STATUS_DRAWABLE
import com.monlixv2.util.Constants.TRANSACTION_ITEM_STATUS_TEXT_COLOR


const val SIMPLE_TRANSCATION_CARD = 0
const val STEPS_TRANSCATION_CARD = 1

class TransactionAdapter(
    private var dataSource: ArrayList<Transaction>
) : RecyclerView.Adapter<TransactionAdapter.TransactionHolder>() {

    private var expandedIds = mutableSetOf<String>();


    public fun updateDataSource(arrayList: ArrayList<Transaction>) {
        val oldSize = dataSource.size
        dataSource = arrayList
        notifyItemRangeInserted(oldSize, arrayList.size)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionHolder {
        val inflatedView = parent.inflate(
            when (viewType) {
                SIMPLE_TRANSCATION_CARD -> R.layout.transaction_item_v2
                else -> {
                    R.layout.transaction_item_v2_with_steps
                }
            }, false
        )

        return TransactionHolder(inflatedView)

    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (dataSource[position].goals.size == 0) SIMPLE_TRANSCATION_CARD else STEPS_TRANSCATION_CARD
    }


    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {

        holder.title.text = dataSource[position].name
        holder.payout.text =
            "${dataSource[position].reward} ${dataSource[position].currency}"
        holder.status.text = "${dataSource[position].status}"

        TRANSACTION_ITEM_STATUS_DRAWABLE[dataSource[position].status]?.let {
            holder.status.background = ContextCompat.getDrawable(
                holder.itemView.context,
                it
            )
        }
        TRANSACTION_ITEM_STATUS_TEXT_COLOR[dataSource[position].status]?.let {
            holder.status.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    it
                )
            )

        }
        holder.id = dataSource[position].id
        holder.transactionId.text = "${dataSource[position].id}"


        Glide.with(holder.itemView.context).load(dataSource[position].image)
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .error(ContextCompat.getDrawable(holder.itemView.context, R.drawable.offer_placeholder))
            .into(holder.imageView)

        when (holder.itemViewType) {
            SIMPLE_TRANSCATION_CARD -> {
            }
            STEPS_TRANSCATION_CARD -> {

                // make inner scrollview scrollable
                holder.stepsToggle?.visibility = View.VISIBLE
                val finishedSteps =
                    dataSource[position].goals.count { !it.status.contentEquals(CLICKED_QUERY_PARAM) }
                holder.stepsCount?.text = "${finishedSteps}/${dataSource[position].goals.size}"
                val percentageDone =
                    Math.floor((finishedSteps.toDouble() / dataSource[position].goals.size.toDouble() * 100))
                holder.progressView?.setProgress(percentageDone)

                val expandedCheck = expandedIds.contains(dataSource[position].id)

                manageMultiStepItemsAutomatically(holder, dataSource[position], expandedCheck)
                manageMultiStepContainer(holder, expandedCheck)

                holder.stepsToggle?.setOnClickListener {

                    val shouldReveal = !expandedIds.contains(dataSource[position].id)

                    holder.stepsContainer?.visibility =
                        if (shouldReveal) View.VISIBLE else View.GONE
                    holder.stepsScroller?.visibility =
                        if (shouldReveal) View.VISIBLE else View.GONE

                    val drawable = ContextCompat.getDrawable(
                        holder.itemView.context,
                        if (shouldReveal) R.drawable.arrow_down_reversed else
                            R.drawable.arrow_down
                    )
                    holder.stepsToggle.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        drawable,
                        null
                    )


                    holder.stepsScroller?.apply {
                        setOnTouchListener(View.OnTouchListener { v, _ ->
                            v.parent.parent.parent.requestDisallowInterceptTouchEvent(true);
                            performClick()
                            false
                        })
                    }
                    if (shouldReveal) {
                        expandedIds.add(dataSource[position].id)
                    } else {
                        expandedIds.remove(dataSource[position].id)
                    }
                    manageMultiStepItemsAutomatically(holder, dataSource[position], shouldReveal)
                }
            }
        }
    }

    fun manageMultiStepContainer(
        holder: TransactionHolder,
        isExpanded: Boolean
    ) {
        holder.stepsContainer?.visibility =
            if (isExpanded) View.VISIBLE else View.GONE
        holder.stepsScroller?.visibility =
            if (isExpanded) View.VISIBLE else View.GONE
        val drawable = ContextCompat.getDrawable(
            holder.itemView.context,
            if (isExpanded) R.drawable.arrow_down_reversed else
                R.drawable.arrow_down
        )
        holder.stepsToggle?.setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            drawable,
            null
        )
    }

    fun manageMultiStepItemsAutomatically(
        holder: TransactionHolder,
        transaction: Transaction,
        isExpanded: Boolean
    ) {
        holder.stepsContainer?.removeAllViews()

        if (isExpanded) {
            for (i in 0 until transaction.goals.size) {
                val requirementItem = LayoutInflater.from(holder.itemView.context)
                    .inflate(R.layout.requrement_item, null);

                val textview =
                    requirementItem.findViewById<TextView>(R.id.requirementTitle)
                val stepCheck = requirementItem.findViewById<ImageView>(R.id.stepCheck)
                val goalPayout =
                    requirementItem.findViewById<TextView>(R.id.goalPayout);
                textview.text = transaction.goals[i].name
                goalPayout.text = transaction.goals[i].payout

                Glide.with(holder.itemView.context).load(
                    ContextCompat.getDrawable(
                        holder.itemView.context,
                        if (transaction.goals[i].status.contentEquals(
                                CLICKED_QUERY_PARAM
                            )
                        )
                            R.drawable.circle_radio else R.drawable.circle_radio_checked
                    )
                ).into(stepCheck)
                if (i == transaction.goals.size - 1) {
                    requirementItem.findViewById<ImageView>(R.id.bottomDots).visibility =
                        View.GONE
                }
                holder.stepsContainer?.addView(requirementItem)
            }
        }
    }

    class TransactionHolder(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.findViewById(R.id.title)
        val transactionId: TextView = v.findViewById(R.id.transactionId)
        val status: TextView = v.findViewById(R.id.status)
        val payout: TextView = v.findViewById(R.id.payout)
        val progressView: SquareProgressView? = v.findViewById(R.id.transactionProgress)
        val stepsToggle: TextView? = v.findViewById(R.id.stepsToggle)
        val imageView: ImageView = v.findViewById(R.id.transactionImage)
        val stepsContainer: LinearLayout? = v.findViewById(R.id.stepsContainer);
        val stepsCount: TextView? = v.findViewById(R.id.stepsCount);
        val stepsScroller: ScrollView? = v.findViewById(R.id.stepsScroller);
        var id = ""

        init {
            progressView?.setWidthInDp(3)
            progressView?.setColor(ContextCompat.getColor(v.context, R.color.orangeV1))
            progressView?.setRoundedCorners(true, 8f)
        }
    }
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}
