package com.dazzlebloom.ui

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

import com.dazzlebloom.databinding.ActivityMobileVerificationBinding
import com.dazzlebloom.utiles.Constants
import com.dazzlebloom.utiles.ViewUtils
import com.dazzlebloom.utiles.customtypeface.CustomTypeface


class MobileVerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMobileVerificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMobileVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding?.tvDazzle?.typeface = CustomTypeface(this).ralewayRegular
        binding?.etmobilenumber?.typeface = CustomTypeface(this).ralewayMedium
        binding?.btnSubmit?.typeface = CustomTypeface(this).ralewaySemiBold


        binding.btnSubmit.setOnClickListener {
            if (!binding.etmobilenumber.text.toString().equals("")) {
                if(binding.etmobilenumber.text?.length!! >= 10 ) {
                    Constants.LOGINMODE = "login"
                    Constants.OTPPHONE = binding.etmobilenumber.text.toString()
                    val intent = Intent(this, VerificationCodeActivity::class.java)
                    startActivity(intent)
                }else
                    ViewUtils.showdialog(this,"Please enter a valid phone number")
            }else
                ViewUtils.showdialog(this,"Please enter phone number")
        }
        binding.backArrow.setOnClickListener {
            finish()
        }
    }


}