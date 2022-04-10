
package com.monlixv2.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.monlixv2.service.models.offers.OfferResponse
import com.monlixv2.R
import com.monlixv2.adapters.PagerAdapter
import com.monlixv2.ui.Main
import com.monlixv2.util.PreferenceHelper
import com.monlixv2.util.PreferenceHelper.get

class HomeFragment : Fragment() {

    private var pager: ViewPager2? = null
    private var tabLayout: TabLayout? = null

    private lateinit var data: OfferResponse;
    private lateinit var prefs: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prefs = PreferenceHelper.customPrefs(context as Main, PreferenceHelper.MonlixPrefs);
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (view as ViewGroup).layoutTransition.setAnimateParentHierarchy(false);
        this.loadViews(view)
        this.fetchData()
    }

    private fun loadViews(view: View) {
        pager = view.findViewById<ViewPager2>(R.id.monlixPager)
        pager!!.offscreenPageLimit = 1
        tabLayout = view.findViewById<TabLayout>(R.id.monlixTablayout)
    }


    private fun fetchData() {
        prefs = PreferenceHelper.customPrefs((context as Main), PreferenceHelper.MonlixPrefs);
        val appId = prefs[PreferenceHelper.MonlixAppId, ""]
        val userId = prefs[PreferenceHelper.MonlixUserId, ""]
//        val url = "${Api.ENDPOINT}/offers?appid=${appId}&userid=${userId}"
//        thread {
//            val json = try {
//                URL(url).readText()
//            } catch (e: Exception) {
//                return@thread
//            }
////            data = Api.parseOffers(json)
////            (context as Main).runOnUiThread {
////                displayData(data)
////            }
//        }
    }

    private fun displayData(data: OfferResponse) {
        val dataArray = arrayOf(data.surveys, data.offers, data.ads)
//        pager?.adapter = PagerAdapter(requireContext(), dataArray)
//        TabLayoutMediator(tabLayout!!, pager!!) { tab, position ->
//            tab.text = tabs[position]
//        }.attach()
    }


    companion object {
        private val tabs = arrayOf("Surveys", "Offers", "Ads")
    }


}
