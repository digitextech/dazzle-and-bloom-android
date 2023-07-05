package com.dazzlebloom.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import com.dazzlebloom.R
import com.dazzlebloom.onclick.OnClickLogin
import com.dazzlebloom.viewbind.LogInViewBind

class LoginActivity : AppCompatActivity() {
    var loginViewBind : LogInViewBind? = null
    var loginClick : OnClickLogin?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = LayoutInflater.from(this).inflate(R.layout.activity_login,null)
        loginViewBind = LogInViewBind(this,view)
        loginClick = OnClickLogin(this,loginViewBind!!)
        setContentView(view)
    }
}