package com.dazzlebloom.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.lifecycle.lifecycleScope
import com.dazzlebloom.R
import com.dazzlebloom.databinding.ActivityForgotPasswordBinding
import com.dazzlebloom.retrofit.ApiInterface
import com.dazzlebloom.retrofit.RetrofitHelper
import com.dazzlebloom.utiles.ViewUtils
import com.dazzlebloom.utiles.customtypeface.CustomTypeface

class ForgotPasswordActivity : AppCompatActivity() {
    var activityForgotPasswordBinding : ActivityForgotPasswordBinding? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityForgotPasswordBinding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(activityForgotPasswordBinding?.root)
        activityForgotPasswordBinding?.backArrow?.setOnClickListener {
            finish()
        }
        activityForgotPasswordBinding?.btnSubmit?.setOnClickListener {
            if(!activityForgotPasswordBinding!!.etRecoveryEmail.text.toString().equals("")){
                callApiforForgotPassword()
            }
        }
        settypeface()
    }

    private fun callApiforForgotPassword() {
        ViewUtils.showDialog(this)
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
       lifecycleScope?.launchWhenCreated {
          val response =  apiInterface.forgotPassword(activityForgotPasswordBinding?.etRecoveryEmail?.text.toString())
          ViewUtils.dismissDialog()
           if(response.isSuccessful){
               ViewUtils.showdialog(this@ForgotPasswordActivity,"Password reset link has been sent to your registered email")
           }else
               ViewUtils.showdialog(this@ForgotPasswordActivity,"Something wrong, try again later.")
        }
    }

    private fun settypeface() {
        activityForgotPasswordBinding?.tvDazzle?.typeface = CustomTypeface(this).ralewayRegular
        activityForgotPasswordBinding?.etRecoveryEmail?.typeface = CustomTypeface(this).ralewayMedium
        activityForgotPasswordBinding?.btnSubmit?.typeface = CustomTypeface(this).ralewaySemiBold
    }
}