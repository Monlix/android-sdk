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
import androidx.navigation.fragment.NavHostFragment
import com.monlixv2.R
import com.monlixv2.databinding.MonlixMainActivityBinding
import com.monlixv2.util.Constants
import com.monlixv2.util.PreferenceHelper
import com.monlixv2.util.PreferenceHelper.MonlixUserId
import com.monlixv2.util.PreferenceHelper.get


class MainActivity : AppCompatActivity() {
    private lateinit var prefs: SharedPreferences
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var binding: MonlixMainActivityBinding

    private var isUserPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val availableFragments = intent.getIntArrayExtra(Constants.AVAILABLE_FRAGMENTS)

        binding = DataBindingUtil.setContentView(this, R.layout.monlix_main_activity)
        binding.lifecycleOwner = this
        prefs = PreferenceHelper.customPrefs(this, PreferenceHelper.MonlixPrefs);
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val bundle = Bundle()
        bundle.putIntArray(Constants.AVAILABLE_FRAGMENTS, availableFragments)
        navHostFragment.navController.setGraph(R.navigation.monlix_nav_graph, bundle)
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
        if (!isUserPage) {
            navHostFragment.navController.navigate(R.id.action_homeFragment_to_transactionFragment)
            isUserPage = true
            (view as AppCompatImageView).setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.monlix_arrow_left
                )
            )
            return
        }
        goToLoadingScreen()
    }

    private fun goToLoadingScreen() {
        val intent = Intent(this, LoadingScreenActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    override fun onBackPressed() {
        if (!isUserPage) {
            finish()
        }
        profileAction(binding.profileActionBtn)
    }
}
