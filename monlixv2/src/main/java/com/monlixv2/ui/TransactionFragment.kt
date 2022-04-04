package com.monlixv2.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.monlixv2.R
import com.monlixv2.adapters.TransactionAdapter
import com.monlixv2.service.Api
import com.monlix.service.models.TransactionResponse
import com.monlixv2.util.PreferenceHelper
import com.monlixv2.util.PreferenceHelper.get
import com.monlixv2.util.UIHelpers
import java.net.URL
import kotlin.concurrent.thread

class TransactionFragment : Fragment() {

    private lateinit var loader: ProgressBar;
    private lateinit var recyclerView: RecyclerView;
    private lateinit var ptcClicks: TextView;
    private lateinit var ptcEarnings: TextView;
    private lateinit var ptcContainer: LinearLayout;

    private lateinit var data: TransactionResponse;
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prefs = PreferenceHelper.customPrefs(context as Offers, PreferenceHelper.MonlixPrefs);
        return inflater.inflate(R.layout.transaction_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (view as ViewGroup).layoutTransition.setAnimateParentHierarchy(false);
        this.loadViews(view)
        this.fetchData()
    }

    private fun loadViews(view: View) {
        recyclerView = view.findViewById(R.id.offer_result_list);
        context?.resources?.getDimensionPixelSize(R.dimen.offer_row_margin)?.let {
            UIHelpers.MarginItemDecoration(
                it
            )
        }?.let {
            recyclerView.addItemDecoration(
                it
            )
        }
        loader = view.findViewById(R.id.loader);
        ptcClicks = view.findViewById(R.id.ptcClicks);
        ptcEarnings = view.findViewById(R.id.ptcEarnings);
        ptcContainer = view.findViewById(R.id.ptcContainer);
    }

    fun fetchData() {
        loader.visibility = View.VISIBLE
        ptcContainer.visibility = View.INVISIBLE
        recyclerView.visibility = View.INVISIBLE

        val appId = prefs[PreferenceHelper.MonlixAppId, ""]
        val userId = prefs[PreferenceHelper.MonlixUserId, ""]
        val url = "${Api.ENDPOINT}/user/transactions?appid=${appId}&userid=${userId}";

        thread {
            val json = try {
                URL(url).readText()
            } catch (e: Exception) {
                return@thread
            }
            data = Api.parseTransactions(json)
            (context as Offers).runOnUiThread {
                displayData(data)
            }
        }
    }

    private fun displayData(data: TransactionResponse) {
        ptcClicks.text = data.ptcClicks.toString()
        ptcEarnings.text = data.ptcEarnings.toString()
        loader.visibility = View.GONE
        ptcContainer.visibility = View.VISIBLE
        recyclerView.adapter = TransactionAdapter(data.transactions);
        recyclerView.visibility = View.VISIBLE
    }
}
