package com.dazzlebloom.ui

import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dazzlebloom.R
import com.dazzlebloom.databinding.ActivityMyEditAddressBinding
import com.dazzlebloom.utiles.ViewUtils
import com.dazzlebloom.utiles.customtypeface.CustomTypeface

class MyEditAddressActivity : AppCompatActivity() {
    lateinit var activityMyEditAddressBinding: ActivityMyEditAddressBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMyEditAddressBinding = ActivityMyEditAddressBinding.inflate(layoutInflater)
        ViewUtils.changeStatusBarcolor(this!!,this?.resources!!.getColor(R.color.status_bar_color))

        activityMyEditAddressBinding.backArrow.setOnClickListener {
            finish()
        }



        activityMyEditAddressBinding.tvBillingAddress?.typeface = CustomTypeface(this).ralewaySemiBold
        activityMyEditAddressBinding.tvCountry.typeface = CustomTypeface(this).ralewaySemiBold
        activityMyEditAddressBinding.etCountryregion.typeface = CustomTypeface(this).ralewayRegular
        activityMyEditAddressBinding.tvStreetaddress?.typeface = CustomTypeface(this).ralewaySemiBold
        activityMyEditAddressBinding.etStreetaddress.typeface = CustomTypeface(this).ralewayRegular
        activityMyEditAddressBinding.tvTown.typeface = CustomTypeface(this).ralewaySemiBold
        activityMyEditAddressBinding.etTowncity.typeface = CustomTypeface(this).ralewayRegular

        activityMyEditAddressBinding.tvCountryopt.typeface = CustomTypeface(this).ralewaySemiBold
        activityMyEditAddressBinding.etCountry.typeface = CustomTypeface(this).ralewayRegular
        activityMyEditAddressBinding.tvPostcode.typeface = CustomTypeface(this).ralewaySemiBold
        activityMyEditAddressBinding.etPostcode.typeface = CustomTypeface(this).ralewayRegular
        activityMyEditAddressBinding.tvEmail.typeface = CustomTypeface(this).ralewaySemiBold
        activityMyEditAddressBinding.etEmail.typeface = CustomTypeface(this).ralewayRegular
        activityMyEditAddressBinding.tvPhonenum.typeface = CustomTypeface(this).ralewaySemiBold
        activityMyEditAddressBinding.etPhonrnumbr.typeface = CustomTypeface(this).ralewayRegular
        setContentView(activityMyEditAddressBinding!!.root)
    }
}