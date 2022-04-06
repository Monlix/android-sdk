package com.monlixv2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.monlixv2.ui.Main
import com.monlixv2.util.PreferenceHelper
import com.monlixv2.util.PreferenceHelper.MonlixAppId
import com.monlixv2.util.PreferenceHelper.MonlixPrefs
import com.monlixv2.util.PreferenceHelper.MonlixUserId
import com.monlixv2.util.PreferenceHelper.set
import java.lang.Exception


object MonlixOffers {
    private var instance: MonlixOffers? = null
    private lateinit var prefs: SharedPreferences

    @Synchronized
    fun createInstance(
        context: Context,
        appId: String,
        userId: String
    ): MonlixOffers? {
        if (instance == null) {
            instance = MonlixOffers
            prefs = PreferenceHelper.customPrefs(context, MonlixPrefs);
            prefs[MonlixAppId] = appId
            prefs[MonlixUserId] = userId
        }
        return instance;
    }


    fun showWall(context: Context) {
        if (instance == null) {
            throw Exception("createInstance() must be called before accessing the instance.")
        }
        val intent = Intent(context, Main::class.java)
        context.startActivity(intent)
    }

}
