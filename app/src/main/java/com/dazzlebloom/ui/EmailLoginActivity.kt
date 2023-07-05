package com.dazzlebloom.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.dazzlebloom.R
import com.dazzlebloom.onclick.OnClickEmailLogin
import com.dazzlebloom.viewbind.EmailLoginViewBind

class EmailLoginActivity : AppCompatActivity() {
    lateinit var onClickEmailLogin :OnClickEmailLogin
    lateinit var emailLoginViewBind: EmailLoginViewBind
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val emailLoginview = LayoutInflater.from(this).inflate(R.layout.activity_email_login,null)
        setContentView(emailLoginview)
        emailLoginViewBind = EmailLoginViewBind(this, emailLoginview)
        onClickEmailLogin = OnClickEmailLogin(this, emailLoginViewBind)
    }
}