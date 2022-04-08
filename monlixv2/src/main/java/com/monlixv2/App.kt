package com.monlixv2

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDexApplication

class App: MultiDexApplication() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }
}
