package com.dazzlebloom.viewbind

import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.dazzlebloom.R
import com.dazzlebloom.ui.VerificationCodeActivity
import com.dazzlebloom.utiles.customtypeface.CustomTypeface
import `in`.aabhasjindal.otptextview.OtpTextView




class VerificationViewBind(val verificationCodeActivity: VerificationCodeActivity,val  verifyView: View) {
    var tvbloom : AppCompatTextView? = null
    var tvdazzle : AppCompatTextView?= null
    var tvVerificationCode : AppCompatTextView? = null
    var one:EditText ?= null
    var two :  EditText ? =null
    var three : EditText? = null
    var four : EditText? = null
    var five : EditText ? = null
    var six : EditText ? = null
    var tvResendcode : AppCompatTextView ? = null
    var btnVerify : AppCompatButton ?= null
     var otpTextView: OtpTextView? = null
    var otpPhone : AppCompatTextView ?= null
    var resendagain : AppCompatTextView ?= null
  //  var tv_phone : AppCompatTextView ?= null

    init {
        tvdazzle = verifyView.findViewById(R.id.tv_dazzle)
        tvbloom = verifyView.findViewById(R.id.tv_bloom)
       // tv_phone = verifyView.findViewById(R.id.tv_phone)
        tvVerificationCode = verifyView.findViewById(R.id.tv_verification_code)
        one = verifyView.findViewById(R.id.one)
        two = verifyView.findViewById(R.id.two)
        three = verifyView.findViewById(R.id.three)
        four = verifyView.findViewById(R.id.four)
        five = verifyView.findViewById(R.id.five)
        six = verifyView.findViewById(R.id.six)
        resendagain = verifyView.findViewById(R.id.resendagain)
        tvResendcode = verifyView.findViewById(R.id.tv_resendcode)
        btnVerify = verifyView.findViewById(R.id.btn_verify)
        otpTextView = verifyView.findViewById(R.id.otp_tp)
        otpPhone = verifyView.findViewById(R.id.tv_phone)


        tvdazzle?.typeface = CustomTypeface(verificationCodeActivity).ralewayMedium
        tvbloom?.typeface = CustomTypeface(verificationCodeActivity).ralewayMedium

        tvVerificationCode?.typeface = CustomTypeface(verificationCodeActivity).ralewayMedium
        one?.typeface = CustomTypeface(verificationCodeActivity).ralewayMedium
        two?.typeface = CustomTypeface(verificationCodeActivity).ralewayMedium
        four?.typeface = CustomTypeface(verificationCodeActivity).ralewayMedium
        three?.typeface = CustomTypeface(verificationCodeActivity).ralewayMedium
        five?.typeface = CustomTypeface(verificationCodeActivity).ralewayMedium
        six?.typeface = CustomTypeface(verificationCodeActivity).ralewayMedium
        resendagain?.typeface = CustomTypeface(verificationCodeActivity).ralewayMedium
        btnVerify?.typeface = CustomTypeface(verificationCodeActivity).ralewayBold
        tvResendcode?.typeface =CustomTypeface(verificationCodeActivity).ralewayThin
        otpPhone?.typeface = CustomTypeface(verificationCodeActivity).ralewayMedium

    }
}