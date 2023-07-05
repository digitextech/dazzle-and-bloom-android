package com.dazzlebloom.onclick

import android.content.Intent
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.dazzlebloom.R
import com.dazzlebloom.retrofit.ApiInterface
import com.dazzlebloom.retrofit.RetrofitHelper
import com.dazzlebloom.ui.LoginActivity
import com.dazzlebloom.ui.SignUpActivity
import com.dazzlebloom.ui.VerificationCodeActivity
import com.dazzlebloom.utiles.Constants
import com.dazzlebloom.utiles.ViewUtils
import com.dazzlebloom.viewbind.SignUpViewBind
import org.json.JSONObject

class OnClickSignUp(val signUpActivity: SignUpActivity,val  signUpViewBind: SignUpViewBind) : View.OnClickListener {
    init {
        signUpViewBind.btnSignup?.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_signup ->{
                 if(checkValidation())
                     callApiForSignUp()
            }

        }
    }

    private fun callApiForSignUp() {
        ViewUtils.showDialog(signUpActivity)
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        signUpActivity.lifecycleScope.launchWhenCreated {
            try{
                val data: MutableMap<String, String> = HashMap()
                data.put("username",signUpViewBind.username?.text.toString())
                data.put("email", signUpViewBind.emailaddress?.text.toString())
                data.put("password",signUpViewBind.password?.text?.trim().toString())
                data.put("phone_number",signUpViewBind.phonenumber?.text?.trim().toString())

               val response =  apiInterface.callSignUpApi(data)
                ViewUtils.dismissDialog()
                if(response.isSuccessful){
                    if(response.code() == 200 ) {
                        Constants.LOGINMODE = "register"
                        Constants.OTPEMAIL = signUpViewBind.emailaddress?.text.toString()
                        Constants.OTPPHONE = signUpViewBind.phonenumber?.text?.trim().toString()
                        val verification = Intent(signUpActivity, VerificationCodeActivity::class.java)
                        signUpActivity.startActivity(verification)
                        signUpActivity.finish()
                       // ViewUtils.showdialog(signUpActivity, response.message())
                    }
                }else {
                    try {
                        val jsonobj: JSONObject = JSONObject(response.errorBody()?.string())
                         val message = jsonobj.optString("message")
                        ViewUtils.showdialog(signUpActivity,message)
                    }catch (e : Exception){
                        e.printStackTrace()
                        ViewUtils.showdialog(signUpActivity,"Something wrong. Try again later.")
                    }

                }

            }catch (e: Exception){
                e.printStackTrace()
                ViewUtils.showdialog(signUpActivity,"Something wrong. Try again later.")
                ViewUtils.dismissDialog()
            }

        }


    }

    private fun checkValidation(): Boolean {
        if (signUpViewBind.username?.text.toString().equals("")) {
            ViewUtils.showdialog(signUpActivity, "Enter username")
            return false
        }
        if (signUpViewBind.emailaddress?.text.toString().equals("")) {
            ViewUtils.showdialog(signUpActivity, "Enter email address")
            return false
        }
        if (signUpViewBind.phonenumber?.text.toString().equals("")) {
            ViewUtils.showdialog(signUpActivity, "Enter Phone number")
            return false
        }
        if (signUpViewBind.password?.text.toString().trim().equals("")) {
            ViewUtils.showdialog(signUpActivity, "Enter Password")
            return false
        }
        if (signUpViewBind.conpassword?.text.toString().trim().equals("")) {
            ViewUtils.showdialog(signUpActivity, "Enter Confirm password")
            return false
        }
        if(!signUpViewBind.password?.text.toString().trim().equals(signUpViewBind.conpassword?.text.toString().trim()))
        { ViewUtils.showdialog(signUpActivity,"Password and confirm password dose not match")
            return false}
        return true
    }
}