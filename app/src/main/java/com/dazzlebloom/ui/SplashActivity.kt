package com.dazzlebloom.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import com.dazzlebloom.R
import com.dazzlebloom.databinding.ActivitySplashBinding
import com.dazzlebloom.utiles.Constants
import com.dazzlebloom.utiles.customtypeface.CustomTypeface
import com.dazzlebloom.utiles.sheardpreference.AppSheardPreference

class SplashActivity : AppCompatActivity() {
    var activitySplashBinding : ActivitySplashBinding?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        activitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(activitySplashBinding?.root)
        AppSheardPreference.getsheardPreference(this)

        activitySplashBinding?.dazzbloom?.typeface = CustomTypeface(this).ralewayMedium
        Handler().postDelayed({
            if(AppSheardPreference.fetchIntFromAppPreference(Constants.USERID)==0) {
                val loginIntent = Intent(this, EmailLoginActivity::class.java)
                startActivity(loginIntent)
                finish()
            }else{
                val mainIntent = Intent(this, MainActivity::class.java)
                startActivity(mainIntent)
                finish()
            }
        }, Constants.SPLASHSCREEN_TIME)
    }
}