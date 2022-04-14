package com.monlixv2.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.monlixv2.R
import com.monlixv2.databinding.MainActivityBinding
import com.monlixv2.ui.fragments.HomeFragment
import com.monlixv2.util.Constants.viewModelFactory
import com.monlixv2.util.PreferenceHelper
import com.monlixv2.util.PreferenceHelper.MonlixAppId
import com.monlixv2.util.PreferenceHelper.MonlixUserId
import com.monlixv2.util.PreferenceHelper.get
import com.monlixv2.viewmodels.GroupedResponse
import com.monlixv2.viewmodels.MainViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var prefs: SharedPreferences
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainActivityBinding

    private var isUserPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        binding.lifecycleOwner = this
        prefs = PreferenceHelper.customPrefs(this, PreferenceHelper.MonlixPrefs);
        viewModel =
            ViewModelProvider(
                this,
                viewModelFactory {
                    MainViewModel(
                        prefs[MonlixAppId, ""]!!,
                        prefs[MonlixUserId, ""]!!,
                        application
                    )
                }).get(
                MainViewModel::class.java
            )
        binding.viewModel = viewModel
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        setupListeners()
    }


    fun setupListeners() {
        viewModel.groupedResponse.observe(this, Observer {
            if (it.mergedSurveys !== null) {
                val currentFragment =
                    if (navHostFragment.childFragmentManager.fragments.size > 0) navHostFragment.childFragmentManager.fragments[0] else null
                if (currentFragment is HomeFragment) {
                     currentFragment.displayData(it);
                }
            }
        })
    }


    fun refClick(view: View) {
        val userId = prefs[MonlixUserId, ""]
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse("https://monlix.com/?ref=offerwall-${userId}")
        this.startActivity(i)
    }


    fun close(view: View) {
        finish();
    }

    fun profileAction(view: View) {
        navHostFragment.navController.navigate(if (!isUserPage) R.id.action_homeFragment_to_transactionFragment else R.id.action_transactionFragment_to_homeFragment)
        isUserPage = !isUserPage
        (view as AppCompatImageView).setImageDrawable(
            ContextCompat.getDrawable(
                this,
                if (isUserPage) R.drawable.arrow_left else R.drawable.user_fill
            )
        )
        if (!isUserPage) {
            viewModel.makeRequest()
        }
    }

    override fun onBackPressed() {
        if(!isUserPage){
            finish()
        }
        profileAction(binding.profileActionBtn)
    }
}
