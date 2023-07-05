package com.dazzlebloom.viewbind

import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.dazzlebloom.R
import com.dazzlebloom.ui.EmailLoginActivity


import com.dazzlebloom.utiles.customtypeface.CustomTypeface


class EmailLoginViewBind(val emailLoginActivity: EmailLoginActivity, emailLoginview: View) {
    var btnContinueEmail: AppCompatButton? = null
    var btnContinueMobile : AppCompatButton ? =null
    var btnskip : AppCompatButton ? = null
    var tvSignup : AppCompatTextView ? = null
    var tvDazzle : AppCompatTextView ? = null
    var tvBloom : AppCompatTextView ?= null
    var tvWelcome : AppCompatTextView ? = null
    init {
        btnContinueEmail = emailLoginview.findViewById(R.id.btn_continue_email)
        btnContinueMobile = emailLoginview.findViewById(R.id.btn_continue_mobile)
        btnskip = emailLoginview.findViewById(R.id.btn_skip)
        tvSignup = emailLoginview.findViewById(R.id.tv_signup)
        tvBloom = emailLoginview.findViewById(R.id.tv_bloom)
        tvDazzle = emailLoginview.findViewById(R.id.tv_dazzle)
        tvWelcome = emailLoginview.findViewById(R.id.tv_welcome)
        btnskip?.typeface = CustomTypeface(emailLoginActivity).ralewayMedium
        tvDazzle?.typeface = CustomTypeface(emailLoginActivity).ralewayMedium
        tvBloom?.typeface = CustomTypeface(emailLoginActivity).ralewayRegular
        tvWelcome?.typeface =CustomTypeface(emailLoginActivity).ralewayBold
        btnContinueMobile?.typeface =CustomTypeface(emailLoginActivity).ralewayBold
        btnContinueEmail?.typeface = CustomTypeface(emailLoginActivity).ralewayBold
        tvSignup?.typeface = CustomTypeface(emailLoginActivity).ralewayLight
    }
}