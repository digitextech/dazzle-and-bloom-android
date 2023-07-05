package com.dazzlebloom.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.dazzlebloom.R
import com.dazzlebloom.onclick.OnClickSignUp
import com.dazzlebloom.viewbind.SignUpViewBind

class SignUpActivity : AppCompatActivity() {
    var signUpViewBind : SignUpViewBind ? = null
    var onClickSignUp : OnClickSignUp ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val signupView = LayoutInflater.from(this).inflate(R.layout.activity_sign_up,null)
        signUpViewBind = SignUpViewBind(this,signupView)
        onClickSignUp = OnClickSignUp(this, signUpViewBind!!)
        setContentView(signupView)
        /*signUpViewBind?.btnSignup?.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }*/
    }
}