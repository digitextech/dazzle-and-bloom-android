package com.dazzlebloom.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.dazzlebloom.R
import com.dazzlebloom.onclick.VerificationOnclick
import com.dazzlebloom.viewbind.VerificationViewBind

import `in`.aabhasjindal.otptextview.OTPListener
import android.R.attr
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.dazzlebloom.retrofit.ApiInterface
import com.dazzlebloom.retrofit.RetrofitHelper
import com.dazzlebloom.utiles.Constants
import com.dazzlebloom.utiles.ViewUtils
import com.dazzlebloom.utiles.sheardpreference.AppSheardPreference
import org.json.JSONObject
import android.R.attr.phoneNumber





class VerificationCodeActivity : AppCompatActivity() {
    var varificationViewBind : VerificationViewBind ? =null
    var verificationOnclick : VerificationOnclick ? = null
    var otpStr = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val verifyView = LayoutInflater.from(this).inflate(R.layout.activity_verification_code, null)
        varificationViewBind = VerificationViewBind(this,verifyView)
        verificationOnclick = VerificationOnclick(this, varificationViewBind!!)
        setContentView(verifyView)
        val lastfourdigit = if (Constants.OTPPHONE.length >= 4) Constants.OTPPHONE.substring(Constants.OTPPHONE.length - 4) else ""
        val firsttwodigit = if(Constants.OTPPHONE.length >=4) Constants.OTPPHONE.substring(0, 2) else ""

        varificationViewBind!!.otpTextView?.requestFocusOTP()
        //varificationViewBind!!.otpTextView?.sett

        varificationViewBind!!.otpTextView?.otpListener = object : OTPListener {
            override fun onInteractionListener() {

            }

            override fun onOTPComplete(otp: String) {
                otpStr = otp
                /*val main = Intent(this@VerificationCodeActivity,MainActivity::class.java)
                startActivity(main)*/
            }
        }
        varificationViewBind?.btnVerify?.setOnClickListener {
            /*val main = Intent(this@VerificationCodeActivity,MainActivity::class.java)
            startActivity(main)
            finish()*/
            if(!otpStr.equals(""))
               callApiVerifyOtp()
            else
                ViewUtils.showdialog(this,"Please Enter Valid OTP")
        }
        varificationViewBind?.otpPhone?.setText("We sent a text with a security code to "+firsttwodigit+"*******"+lastfourdigit+ " It may take a moment to arrive")
        callApiforOtp()
    }

    private fun callApiforOtp() {
        ViewUtils.showDialog(this)
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
       lifecycleScope.launchWhenCreated {
            try {
                val data: MutableMap<String, String> = HashMap()
                data.put("email", Constants.OTPEMAIL)
                data.put("phone_number", Constants.OTPPHONE)
                data.put("action", Constants.LOGINMODE)
                data.put("mode", "live")

                val response = apiInterface.callApiForOtp(data)
                ViewUtils.dismissDialog()
                if (response.isSuccessful){
                   try{
                        val jsonobj : JSONObject = JSONObject(response.body()?.string())
                        val code = jsonobj.optInt("code")
                           val message = jsonobj.optString("message")
                         ViewUtils.showdialog(this@VerificationCodeActivity,message)

                   }catch ( e : Exception){
                       e.printStackTrace()
                   }

                }else {
                    val jsonobj : JSONObject = JSONObject(response.errorBody()?.string())
                    val message = jsonobj.optString("message")
                    ViewUtils.showdialog(this@VerificationCodeActivity, message)

                }
            }catch(e : Exception){
                e.printStackTrace()

            }            }
    }


    private fun callApiVerifyOtp() {
        ViewUtils.showDialog(this)
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        lifecycleScope.launchWhenCreated {
            try {
                val data: MutableMap<String, String> = HashMap()
                data.put("otp", otpStr)
                data.put("phone_number", Constants.OTPPHONE)

                val response = apiInterface.callApiVerifyOtp(data)
                ViewUtils.dismissDialog()
                if (response.isSuccessful){
                    response?.body()?.data?.ID?.let { AppSheardPreference.storeIntAppPreference(this@VerificationCodeActivity,Constants.USERID, it) }
                   // AppSheardPreference.storeIntAppPreference(this@VerificationCodeActivity,Constants.USERID, response?.body()?.data.ID)
                    response?.body()?.data?.display_name?.let { AppSheardPreference.storeStringAppPreference(this@VerificationCodeActivity,Constants.USERNAME, it) }
                    response.body()?.data?.user_nicename?.let { AppSheardPreference.storeStringAppPreference(this@VerificationCodeActivity,Constants.FRISTNAME, it) }
                    response.body()?.data?.user_email?.let { AppSheardPreference.storeStringAppPreference(this@VerificationCodeActivity,Constants.EMAIL, it) }
                    response.body()?.data?.user_url?.let { AppSheardPreference.storeStringAppPreference(this@VerificationCodeActivity,Constants.USER_URL, it)}

                    val main = Intent(this@VerificationCodeActivity,MainActivity::class.java)
                    main.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(main)
                }else
                    ViewUtils.showdialog(this@VerificationCodeActivity,"Something wrong, try again layer.")
            }catch(e : Exception){
                e.printStackTrace()

            }            }
    }


}
