package com.monlixv2.ui.components.squareprogressbar.utils

import android.R.color

class ColourUtil {
    fun getColourArray(): ArrayList<Int>? {
        val colourArray = ArrayList<Int>()
        colourArray.add(color.holo_blue_bright)
        colourArray.add(color.holo_blue_dark)
        colourArray.add(color.holo_blue_light)
        colourArray.add(color.holo_green_dark)
        colourArray.add(color.holo_green_light)
        colourArray.add(color.holo_orange_dark)
        colourArray.add(color.holo_orange_light)
        colourArray.add(color.holo_purple)
        colourArray.add(color.holo_red_dark)
        colourArray.add(color.holo_red_light)
        return colourArray
    }
}
