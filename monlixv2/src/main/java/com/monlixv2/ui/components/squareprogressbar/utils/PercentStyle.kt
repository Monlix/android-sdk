package com.monlixv2.ui.components.squareprogressbar.utils

import android.graphics.Color
import android.graphics.Paint.Align

class PercentStyle {
    var align: Align? = null
    var textSize = 0f
    var isPercentSign = false

    var customText = "%"

    var textColor = Color.BLACK

    constructor() {}

    constructor(align: Align?, textSize: Float, percentSign: Boolean) : super() {
        this.align = align
        this.textSize = textSize
        isPercentSign = percentSign
    }
}
