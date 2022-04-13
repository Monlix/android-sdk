package com.test.monlixv2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.monlixv2.MonlixOffers

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MonlixOffers.createInstance(this, "aa7e6c192d65bf39fdaae3ea480fe5dc", "tdsavsadas")
//        MonlixOffers.createInstance(this, "cad69646a4ed159c0a4e78f5ccd643b1", "tdsavsadas")
        MonlixOffers.showWall(this)

    }
}
