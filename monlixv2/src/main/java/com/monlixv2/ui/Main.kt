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
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.monlix.service.models.OfferResponse
import com.monlixv2.R
import com.monlixv2.databinding.MainActivityBinding
import com.monlixv2.service.ApiInterface
import com.monlixv2.service.models.TransactionResponse
import com.monlixv2.util.Constants.viewModelFactory
import com.monlixv2.util.PreferenceHelper
import com.monlixv2.util.PreferenceHelper.MonlixAppId
import com.monlixv2.util.PreferenceHelper.MonlixUserId
import com.monlixv2.util.PreferenceHelper.get
import com.monlixv2.viewmodels.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.util.*


class Main : AppCompatActivity() {

    private lateinit var loader: ProgressBar;
//    private var transition: Transition = Fade()

    private lateinit var data: OfferResponse;
    private lateinit var prefs: SharedPreferences
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainActivityBinding

    private  var isUserPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        binding.lifecycleOwner = this
        prefs = PreferenceHelper.customPrefs(this, PreferenceHelper.MonlixPrefs);
        viewModel =
            ViewModelProvider(
                this,
                viewModelFactory { MainViewModel(prefs[MonlixAppId,""]!!,prefs[MonlixUserId, ""]!!, application) }).get(
                MainViewModel::class.java
            )
        binding.viewModel = viewModel
        loadViews()
    }

    private fun loadViews() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

//        Timer().schedule(object : TimerTask() {
//            override fun run() {
//                runOnUiThread {
//                    val bottomSheetDialog = BottomSheetDialog(this@Main)
//                    bottomSheetDialog.setContentView(R.layout.filter_bottom_sheet)
//                    bottomSheetDialog.show()
//                }
//            }
//        }, 5000)

//        CoroutineScope(Dispatchers.IO).launch {
//            println("aaaaaaaaaaaa")
//            val response = ApiInterface.getInstance().getTransactions(prefs[MonlixAppId,""]!!, prefs[MonlixUserId, ""]!!,"","","")
//            withContext(Dispatchers.Main) {
//                try {
//                    println("response")
//                   println(response.body()?.transactions?.size)
//                } catch (e: HttpException) {
//                    println("Exception ${e.message}")
//                } catch (e: Throwable) {
//                    println("Ooops: Something else went wrong")
//                }
//            }
//        }
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
