package com.monlixv2.adapters

import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.google.android.material.progressindicator.LinearProgressIndicator


@BindingAdapter("mainDataLoading")
fun mainDataLoading(layout: ConstraintLayout, isLoading: LiveData<Boolean>?) {
    if(isLoading?.value == true) {
        layout.visibility = View.VISIBLE

        return
    }
    val transition: Transition = Fade()
    transition.duration = 300;
    transition.addTarget(layout);
    TransitionManager.beginDelayedTransition(layout.parent as ViewGroup?, transition);
    layout.visibility = View.GONE
}

@BindingAdapter("transactionDataLoader")
fun transactionDataLoader(progressBar: ProgressBar, isLoading: LiveData<Boolean>?) {
    progressBar.visibility = if (isLoading?.value == true) View.VISIBLE else View.GONE
}

@BindingAdapter("transactionsNoDataHandler")
fun transactionsNoDataHandler(linearLayout: LinearLayout, showNoData: LiveData<Boolean>?) {
    linearLayout.visibility = if (showNoData?.value == true) View.VISIBLE else View.GONE
}


@BindingAdapter("showEarningsHandler")
fun showEarnings(view: LinearLayout, isLoadingProgress: LiveData<Boolean>?) {
    view.visibility = if (isLoadingProgress?.value == true) View.GONE else View.VISIBLE
}

@BindingAdapter("showPoints")
fun showPoints(view: TextView, data: LiveData<String>?) {
    view.text = data?.value ?: ""
}


@BindingAdapter("progressbarLoading")
fun progressbarLoading(view: LinearProgressIndicator, data: LiveData<Int>?) {
    if(data?.value !== null) {
        view.progress = data.value!!
    }
}

@BindingAdapter("textLoading")
fun textLoading(view: TextView, data: LiveData<Int>?) {
    if(data?.value !== null) {
        view.text = "Loading...${data.value!!}%"
    }
}
//textLoading
