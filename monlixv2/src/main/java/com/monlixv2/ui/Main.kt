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
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import com.monlix.service.models.OfferResponse
import com.monlixv2.R
import com.monlixv2.util.PreferenceHelper.MonlixUserId
import com.monlixv2.util.PreferenceHelper.get
import java.util.*

class Main : AppCompatActivity() {

    private lateinit var loader: ProgressBar;
    private lateinit var loaderWindow: ConstraintLayout;
    private var transition: Transition = Fade()

    private lateinit var data: OfferResponse;
    private lateinit var prefs: SharedPreferences
    private lateinit var navHostFragment: NavHostFragment

    private  var isUserPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.offer_activity)
        loadViews()
    }

    private fun loadViews() {
        loaderWindow = findViewById(R.id.loaderWindow);
        loaderWindow.visibility = View.VISIBLE
        transition.duration = 300;
        transition.addTarget(loaderWindow);
        loader = findViewById(R.id.loader);
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        Timer().schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    TransitionManager.beginDelayedTransition(loaderWindow.parent as ViewGroup?, transition);
                    loaderWindow.visibility = View.GONE
                    loader.visibility = View.GONE
                }
            }
        }, 2000)
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
        navHostFragment.navController.navigate(if(!isUserPage) R.id.action_homeFragment_to_transactionFragment else R.id.action_transactionFragment_to_homeFragment)
        isUserPage = !isUserPage
        (view as AppCompatImageView).setImageDrawable( ContextCompat.getDrawable(this, if(isUserPage) R.drawable.arrow_left else R.drawable.user_fill)  )
    }

}
