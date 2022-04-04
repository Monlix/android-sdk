package com.monlixv2.ui

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.monlix.service.models.OfferResponse
import com.monlixv2.R
import com.monlixv2.adapters.PagerAdapter
import com.monlixv2.service.Api
import com.monlixv2.service.Api.ENDPOINT
import com.monlixv2.util.PreferenceHelper
import com.monlixv2.util.PreferenceHelper.MonlixAppId
import com.monlixv2.util.PreferenceHelper.MonlixUserId
import com.monlixv2.util.PreferenceHelper.get
import java.net.URL
import kotlin.concurrent.thread

class
Offers : AppCompatActivity() {

    private lateinit var loader: ProgressBar;
    private lateinit var loaderWindow: ConstraintLayout;
    private var transition: Transition = Fade()

    private var pager: ViewPager2? = null
    private var tabLayout: TabLayout? = null

    private lateinit var data: OfferResponse;
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.offer_activity)

        this.loadViews()
        this.fetchData()
    }

    private fun loadViews() {
        loaderWindow = findViewById(R.id.loaderWindow);
        transition.duration = 300;
        transition.addTarget(loaderWindow);

        loader = findViewById(R.id.loader);
        pager = findViewById<ViewPager2>(R.id.monlixPager)
        pager!!.offscreenPageLimit = 1
        tabLayout = findViewById<TabLayout>(R.id.monlixTablayout)
        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                triggerTransactionFetch(tab);
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                triggerTransactionFetch(tab);
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun triggerTransactionFetch(tab: TabLayout.Tab?) {
        if (tab?.text == "Account" && supportFragmentManager.fragments.size > 0) {
            (supportFragmentManager.fragments[0] as TransactionFragment).fetchData()
        }
    }

    private fun fetchData() {
        prefs = PreferenceHelper.customPrefs(this, PreferenceHelper.MonlixPrefs);
        val appId = prefs[MonlixAppId, ""]
        val userId = prefs[MonlixUserId, ""]
        val url = "${ENDPOINT}/offers?appid=${appId}&userid=${userId}"
        thread {
            val json = try {
                URL(url).readText()
            } catch (e: Exception) {
                return@thread
            }
            data = Api.parseOffers(json)
            runOnUiThread {
                TransitionManager.beginDelayedTransition(loaderWindow.parent as ViewGroup?, transition);
                loaderWindow.visibility = View.GONE
                loader.visibility = View.GONE
                displayData(data)
            }
        }
    }


    private fun displayData(data: OfferResponse) {
        val dataArray = arrayOf(data.surveys, data.offers, data.ads)
        pager?.adapter = PagerAdapter(this, dataArray)
        TabLayoutMediator(tabLayout!!, pager!!) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }

    fun refClick(view: View) {
        val userId = prefs[MonlixUserId, ""]
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse("https://monlix.com/?ref=offerwall-${userId}")
        this.startActivity(i)
    }

    companion object {
        private val tabs = arrayOf("Surveys", "Offers", "Ads")
    }

    fun close(view: View) {
        finish();
    }

    fun showProfile(view: View) {}

}
