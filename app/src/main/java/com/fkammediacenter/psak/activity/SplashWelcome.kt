package com.fkammediacenter.psak.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.fkammediacenter.psak.R
import com.fkammediacenter.psak.utils.SharedPrefManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_splash_welcome.*

class SplashWelcome : AppCompatActivity() {
    lateinit var sharedPrefManager: SharedPrefManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_welcome)
        sharedPrefManager = SharedPrefManager(this)
        tv_welcome.text = "Selamat Datang " + sharedPrefManager.getSPNama()


        val handler = Handler()
        handler.postDelayed({
            startActivity(Intent(applicationContext, HomeActivity::class.java))
            finish()
        }, 2000L) //3000 L = 3 detik
    }
}
