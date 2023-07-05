package com.dazzlebloom.onclick

import android.content.Intent
import android.view.View
import com.dazzlebloom.R
import com.dazzlebloom.ui.*
import com.dazzlebloom.utiles.Constants
import com.dazzlebloom.utiles.sheardpreference.AppSheardPreference
import com.dazzlebloom.viewbind.EmailLoginViewBind

class OnClickEmailLogin(val emailLoginActivity: EmailLoginActivity,val emailLoginViewBind: EmailLoginViewBind) : View.OnClickListener
{
    init {
        emailLoginViewBind.btnContinueEmail?.setOnClickListener(this)
        emailLoginViewBind.btnskip?.setOnClickListener(this)
        emailLoginViewBind.btnContinueMobile?.setOnClickListener(this)
        emailLoginViewBind.tvSignup?.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when(v?.id){
          R.id.btn_continue_email->{
              val intent = Intent(emailLoginActivity, LoginActivity::class.java)
              emailLoginActivity.startActivity(intent)
          }
          R.id.btn_skip->{
              val intent = Intent(emailLoginActivity, MainActivity::class.java)
              intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
              emailLoginActivity.startActivity(intent)
              AppSheardPreference.storeIntAppPreference(emailLoginActivity,Constants.USERID,0)
          }
          R.id.btn_continue_mobile->{
              val intent = Intent(emailLoginActivity, MobileVerificationActivity::class.java)
              emailLoginActivity.startActivity(intent)
          }
          R.id.tv_signup->{
              val signupIntent = Intent(emailLoginActivity,SignUpActivity::class.java)
              emailLoginActivity.startActivity(signupIntent)
          }
        }
    }
}