package com.monlixv2.adapters

import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData


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
