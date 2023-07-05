package com.dazzlebloom.onclick

import android.app.ProgressDialog
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.dazzlebloom.R
import com.dazzlebloom.retrofit.ApiInterface
import com.dazzlebloom.retrofit.RetrofitHelper
import com.dazzlebloom.ui.ForgotPasswordActivity
import com.dazzlebloom.ui.LoginActivity
import com.dazzlebloom.ui.MainActivity
import com.dazzlebloom.ui.VerificationCodeActivity
import com.dazzlebloom.utiles.Constants
import com.dazzlebloom.utiles.ViewUtils
import com.dazzlebloom.utiles.data.LoginInputData
import com.dazzlebloom.utiles.sheardpreference.AppSheardPreference
import com.dazzlebloom.viewbind.LogInViewBind
import com.google.gson.JsonObject
import org.json.JSONObject



class OnClickLogin(val loginActivity: LoginActivity,val  loginViewBind: LogInViewBind): View.OnClickListener {
    var userid = 0
    init {
       loginViewBind.btnVerify?.setOnClickListener(this)
        loginViewBind.tvforgotPassword?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_verify ->{
                /*val verificationIntent = Intent(loginActivity,VerificationCodeActivity::class.java)
                loginActivity.startActivity(verificationIntent)*/
                if( loginViewBind.phone?.visibility == View.GONE) {
                    if (!loginViewBind?.email?.text.toString().equals("")) {
                        if (!loginViewBind?.password?.text.toString().equals("")) {
                            callApiforLogin()
                        } else
                            ViewUtils.showdialog(loginActivity, "Enter password")
                    } else
                        ViewUtils.showdialog(loginActivity, "Enter email address")
                }else{
                    if(!loginViewBind.phone?.text?.equals("")!!)
                       callApiforaddphone()
                    else
                        ViewUtils.showdialog(loginActivity, "Enter Phone number")
                }

            }
            R.id.tv_forgotpassword->{
                val forgotpassIntent = Intent(loginActivity , ForgotPasswordActivity::class.java)
                loginActivity?.startActivity(forgotpassIntent)
            }
        }
    }

    private fun callApiforaddphone() {
        ViewUtils.showDialog(loginActivity)
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        loginActivity.lifecycleScope.launchWhenCreated {
            try{
                val phno = loginViewBind.phone?.text?.trim().toString()
                val data: MutableMap<String, String> = HashMap()
                data.put("uid",userid?.toString()!!)
                data.put("phone", phno)
                val response = apiInterface.callApiforAddPhone(data)
                ViewUtils.dismissDialog()
                if(response.isSuccessful){

                    Constants.LOGINMODE = "login"
                    Constants.OTPEMAIL = loginViewBind.email?.text.toString()
                    Constants.OTPPHONE = loginViewBind.phone?.text?.trim().toString()
                    val varification = Intent(loginActivity, VerificationCodeActivity::class.java)
                    loginActivity.startActivity(varification)
                }else{
                    ViewUtils.showdialog(loginActivity,"Something wrong, Try again.")
                }
            }catch (e : Exception){
                ViewUtils.dismissDialog()
                e.printStackTrace()
            }
        }
    }

    private fun callApiforLogin() {
        ViewUtils.showDialog(loginActivity)

        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        loginActivity.lifecycleScope.launchWhenCreated {
            try {
                val data: MutableMap<String, String> = HashMap()
                data.put("username",loginViewBind.email?.text.toString())
                data.put("password", loginViewBind.password?.text.toString())

                val response = apiInterface.callLoginApi(data)
                ViewUtils.dismissDialog()
                if (response.isSuccessful()) {
                    Constants.LOGINMODE = "login"
                    Constants.OTPEMAIL = loginViewBind.email?.text.toString()
                  //  Constants.OTPPHONE = signUpViewBind.phonenumber?.text?.trim().toString()

                    response?.body()?.ID?.let { AppSheardPreference.storeIntAppPreference(loginActivity,Constants.USERID, it)
                    }
                    response?.body()?.data?.display_name?.let { AppSheardPreference.storeStringAppPreference(loginActivity,Constants.USERNAME, it) }
                    response.body()?.data?.user_nicename?.let { AppSheardPreference.storeStringAppPreference(loginActivity,Constants.FRISTNAME, it) }
                    response.body()?.data?.user_email?.let { AppSheardPreference.storeStringAppPreference(loginActivity,Constants.EMAIL, it) }
                    response.body()?.data?.user_url?.let { AppSheardPreference.storeStringAppPreference(loginActivity,Constants.USER_URL, it)}
                    val mainIntent = Intent(loginActivity, MainActivity::class.java)
                    mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                    loginActivity.startActivity(mainIntent)

                }else {
                    try {
                        val jsonobj: JSONObject = JSONObject(response.errorBody()?.string())
                        val message = jsonobj.optString("message")
                        if(message.equals("Please add your mobile number")){
                            userid = jsonobj.optInt("uid")
                           // AppSheardPreference.storeIntAppPreference(loginActivity,Constants.USERID, uid)
                            loginViewBind.phone?.visibility = View.VISIBLE
                            loginViewBind.phone?.requestFocus()
                            loginViewBind.email?.isEnabled = false
                            loginViewBind.password?.isEnabled = false
                            ViewUtils.showdialog(loginActivity,message)
                        }
                        else if(message.equals("User is not verified please verify your account")){
                            val phone = jsonobj.optString("phone")
                            val email = jsonobj.optString("email")
                            val uid = jsonobj.optInt("uid")
                           // AppSheardPreference.storeIntAppPreference(loginActivity,Constants.USERID, uid)
                            Constants.LOGINMODE = "login"
                            Constants.OTPEMAIL = email
                            Constants.OTPPHONE = phone
                            val varification = Intent(loginActivity, VerificationCodeActivity::class.java)
                            loginActivity.startActivity(varification)

                        }else
                            ViewUtils.showdialog(loginActivity,message)
                    }catch (e: Exception){
                        e.printStackTrace()
                        ViewUtils.showdialog(loginActivity,"Something wrong, Try again.")
                    }

                }

            }catch (e: Exception){
                e.printStackTrace()
                ViewUtils.dismissDialog()
                ViewUtils.showdialog(loginActivity,"Something wrong, Try again.")
            }
        }

    }
}