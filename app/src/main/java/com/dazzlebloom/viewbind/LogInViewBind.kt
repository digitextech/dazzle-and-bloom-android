package com.dazzlebloom.viewbind

import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import com.dazzlebloom.R
import com.dazzlebloom.ui.LoginActivity
import com.dazzlebloom.utiles.customtypeface.CustomTypeface
import com.google.android.material.textfield.TextInputEditText

class LogInViewBind(val loginActivity: LoginActivity,val view: View) {
    var email : AppCompatEditText ?= null
    var phone : AppCompatEditText ? = null
    var password : AppCompatEditText ? = null
    var tvforgotPassword : AppCompatTextView ? = null
    var btnVerify : AppCompatButton ? = null
    var tvbloom : AppCompatTextView ? = null
    var tvdazzle : AppCompatTextView ?= null
    init {
        email = view.findViewById(R.id.email)
        phone = view.findViewById(R.id.phone)
        password = view.findViewById(R.id.password)
        tvforgotPassword = view.findViewById(R.id.tv_forgotpassword)
        btnVerify = view.findViewById(R.id.btn_verify)
        tvdazzle = view.findViewById(R.id.tv_dazzle)
        tvbloom = view.findViewById(R.id.tv_bloom)

        tvdazzle?.typeface = CustomTypeface(loginActivity).ralewayRegular

        tvbloom?.typeface = CustomTypeface(loginActivity).ralewayMedium
        email?.typeface = CustomTypeface(loginActivity).ralewayMedium
        password?.typeface = CustomTypeface(loginActivity).ralewayMedium
        tvdazzle?.typeface = CustomTypeface(loginActivity).ralewayMedium
        tvforgotPassword?.typeface = CustomTypeface(loginActivity).ralewayLight
        btnVerify?.typeface =CustomTypeface(loginActivity).ralewayBold

    }
}