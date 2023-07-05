package com.dazzlebloom.onclick

import android.content.Intent
import android.view.View
import com.dazzlebloom.R
import com.dazzlebloom.ui.MainActivity
import com.dazzlebloom.ui.VerificationCodeActivity
import com.dazzlebloom.viewbind.VerificationViewBind

class VerificationOnclick(
   val verificationCodeActivity: VerificationCodeActivity,
    val varificationViewBind: VerificationViewBind
) : View.OnClickListener
{
    init {
        varificationViewBind.btnVerify?.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
       when(v?.id){
            R.id.btn_verify->{
                val main = Intent(verificationCodeActivity,MainActivity::class.java)
                verificationCodeActivity.startActivity(main)
            }

       }
    }
}