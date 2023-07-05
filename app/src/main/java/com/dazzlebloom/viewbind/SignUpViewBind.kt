package com.dazzlebloom.viewbind

import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import com.dazzlebloom.R
import com.dazzlebloom.ui.SignUpActivity
import com.dazzlebloom.utiles.customtypeface.CustomTypeface
import com.google.android.material.textfield.TextInputEditText

class SignUpViewBind(val signUpActivity: SignUpActivity, val signUpViewBind: View) {
    var username : AppCompatEditText ? = null
    var emailaddress : AppCompatEditText ? =null
    var phonenumber :  AppCompatEditText ? = null
    var password : AppCompatEditText ? = null
    var conpassword : AppCompatEditText ? = null
    var btnSignup : AppCompatButton ? =null
    var tvbloom : AppCompatTextView? = null
    var tvdazzle : AppCompatTextView?= null
    init {
        username = signUpViewBind.findViewById(R.id.username)
        emailaddress = signUpViewBind.findViewById(R.id.emailaddress)
        phonenumber = signUpViewBind.findViewById(R.id.phonenumber)
        password = signUpViewBind.findViewById(R.id.password)
        conpassword = signUpViewBind.findViewById(R.id.conpassword)
        btnSignup = signUpViewBind.findViewById(R.id.btn_signup)
        tvdazzle = signUpViewBind.findViewById(R.id.tv_dazzle)
        tvbloom = signUpViewBind.findViewById(R.id.tv_bloom)

        tvdazzle?.typeface = CustomTypeface(signUpActivity).ralewayMedium
        tvbloom?.typeface = CustomTypeface(signUpActivity).ralewayMedium
        btnSignup?.typeface = CustomTypeface(signUpActivity).ralewayBold
        username?.typeface = CustomTypeface(signUpActivity).ralewayMedium
        emailaddress?.typeface = CustomTypeface(signUpActivity).ralewayMedium
        phonenumber?.typeface = CustomTypeface(signUpActivity).ralewayMedium
        password?.typeface = CustomTypeface(signUpActivity).ralewayMedium
        conpassword?.typeface = CustomTypeface(signUpActivity).ralewayMedium

    }
}