package com.dazzlebloom.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dazzlebloom.R
import com.dazzlebloom.databinding.ActivityMyaddressBinding
import com.dazzlebloom.retrofit.ApiInterface
import com.dazzlebloom.retrofit.RetrofitHelper
import com.dazzlebloom.utiles.Constants
import com.dazzlebloom.utiles.ViewUtils
import com.dazzlebloom.utiles.customtypeface.CustomTypeface
import com.dazzlebloom.utiles.sheardpreference.AppSheardPreference
import java.lang.Exception

class MyAddressActivity : AppCompatActivity() {
    var activityMyaddressBinding : ActivityMyaddressBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMyaddressBinding= ActivityMyaddressBinding.inflate(layoutInflater)
        setContentView(activityMyaddressBinding?.root)
        callApiforAddress()
    }

    private fun callApiforAddress() {
        ViewUtils.showDialog(this)
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        lifecycleScope.launchWhenCreated {

            try{
                val response = apiInterface.callApiforAddressd(AppSheardPreference.fetchIntFromAppPreference(Constants.USERID))
                ViewUtils.dismissDialog()
                if (response.isSuccessful){

                }
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        ViewUtils.changeStatusBarcolor(this!!,resources!!.getColor(R.color.status_bar_color))
        activityMyaddressBinding?.tvFollowing?.typeface = CustomTypeface(this).ralewaySemiBoldItalic
        activityMyaddressBinding?.tvAddress?.typeface = CustomTypeface(this).ralewaySemiBold
        activityMyaddressBinding?.tvAddeditShipping?.typeface = CustomTypeface(this).ralewayLightItalic
        activityMyaddressBinding?.tvAddeditBilling?.typeface = CustomTypeface(this).ralewayLightItalic
        activityMyaddressBinding?.tvBillingAddress?.typeface = CustomTypeface(this).ralewaySemiBold
        activityMyaddressBinding?.tvShippingAddress?.typeface = CustomTypeface(this).ralewaySemiBold

        activityMyaddressBinding?.tvUserBillingAddress?.typeface = CustomTypeface(this).ralewayLightItalic
        activityMyaddressBinding?.tvUserShippingAddress?.typeface = CustomTypeface(this).ralewayLightItalic

        activityMyaddressBinding?.backArrow?.setOnClickListener {
            finish()
        }
        activityMyaddressBinding?.tvAddeditBilling?.setOnClickListener {
            val intent = Intent(this,MyEditAddressActivity::class.java)
            startActivity(intent)
        }
        activityMyaddressBinding?.tvAddeditShipping?.setOnClickListener {
            val intent = Intent(this,MyEditAddressActivity::class.java)
            startActivity(intent)
        }
    }
}