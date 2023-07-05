package com.dazzlebloom.ui.fragments.myaccount

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.dazzlebloom.R
import com.dazzlebloom.ui.MainActivity
import com.dazzlebloom.utiles.Constants
import com.dazzlebloom.utiles.customtypeface.CustomTypeface
import com.dazzlebloom.utiles.sheardpreference.AppSheardPreference

class MyAccountFragmentViewBind(val activity: MainActivity?,val  accountview: View) {
   var tv_dazzle : AppCompatTextView ? = null
    var tv_bloom : AppCompatTextView? = null
    var tv_user_name : AppCompatTextView ? = null
    var tv_editProfile : AppCompatTextView? = null
    var img_profile : AppCompatImageView ? = null
    var cons_mypackage : ConstraintLayout? = null
    var tv_my_package : AppCompatTextView ? = null
    var cons_listing : ConstraintLayout? = null
    var tv_my_listing : AppCompatTextView ? = null
    var cons_fav : ConstraintLayout? = null
    var tv_my_fav : AppCompatTextView? = null
    var cons_address : ConstraintLayout ? = null
    var tv_location : AppCompatTextView? = null
    var cons_message : ConstraintLayout? = null
    var tv_message : AppCompatTextView ? = null
    var cons_helps : ConstraintLayout? = null
    var tv_help : AppCompatTextView ? = null
    var cons_feedback : ConstraintLayout? = null
    var tv_feedback : AppCompatTextView ? = null
    var cons_logout : ConstraintLayout? = null
    var tv_logout : AppCompatTextView ? = null
    var tv_privacy : AppCompatTextView ? = null
    var tv_deleteaccount : AppCompatTextView ? = null
    var cons_deleteaccount : ConstraintLayout? = null

    var div9 : View ?= null
    var div8 : View ?= null
    init {
      tv_dazzle = accountview.findViewById(R.id.tv_dazzle)
        tv_bloom = accountview.findViewById(R.id.tv_bloom)
        tv_user_name = accountview.findViewById(R.id.tv_user_name)
        tv_editProfile = accountview.findViewById(R.id.tv_editProfile)
        img_profile = accountview.findViewById(R.id.img_profile)
        cons_mypackage = accountview.findViewById(R.id.cons_mypackage)
        tv_my_package = accountview.findViewById(R.id.tv_my_package)
        cons_listing = accountview.findViewById(R.id.cons_listing)
        tv_my_listing = accountview.findViewById(R.id.tv_my_listing)
        cons_fav = accountview.findViewById(R.id.cons_fav)
        tv_my_fav = accountview.findViewById(R.id.tv_my_fav)
        cons_address = accountview.findViewById(R.id.cons_address)
        tv_location = accountview.findViewById(R.id.tv_location)
        cons_message = accountview.findViewById(R.id.cons_message)
        tv_message = accountview.findViewById(R.id.tv_message)
        cons_helps = accountview.findViewById(R.id.cons_helps)
        tv_help = accountview.findViewById(R.id.tv_help)
        cons_feedback = accountview.findViewById(R.id.cons_feedback)
        tv_feedback = accountview.findViewById(R.id.tv_feedback)
        cons_logout = accountview.findViewById(R.id.cons_logout)
        tv_logout = accountview.findViewById(R.id.tv_logout)
        tv_privacy = accountview.findViewById(R.id.tv_privacy)
        div9 = accountview.findViewById(R.id.div9)
        div8 = accountview.findViewById(R.id.div8)
        tv_deleteaccount = accountview.findViewById(R.id.tv_deleteaccount)
        cons_deleteaccount = accountview.findViewById(R.id.cons_deleteaccount)

        tv_dazzle?.typeface= activity?.let { CustomTypeface(it).ralewayRegular }
        tv_bloom?.typeface= activity?.let { CustomTypeface(it).ralewayRegular }
        tv_user_name?.typeface= activity?.let { CustomTypeface(it).ralewaySemiBold }
        tv_editProfile?.typeface= activity?.let { CustomTypeface(it).ralewayLight }
        tv_my_listing?.typeface= activity?.let { CustomTypeface(it).ralewaySemiBold }
        tv_my_package?.typeface= activity?.let { CustomTypeface(it).ralewaySemiBold }
        tv_my_fav?.typeface= activity?.let { CustomTypeface(it).ralewaySemiBold }
        tv_location?.typeface= activity?.let { CustomTypeface(it).ralewaySemiBold }
        tv_message?.typeface= activity?.let { CustomTypeface(it).ralewaySemiBold }
        tv_help?.typeface= activity?.let { CustomTypeface(it).ralewaySemiBold }
        tv_feedback?.typeface= activity?.let { CustomTypeface(it).ralewaySemiBold }
        tv_logout?.typeface= activity?.let { CustomTypeface(it).ralewaySemiBold }
        tv_deleteaccount?.typeface= activity?.let { CustomTypeface(it).ralewaySemiBold }
        tv_privacy?.typeface= activity?.let { CustomTypeface(it).ralewayLight }

        if(AppSheardPreference.fetchIntFromAppPreference(Constants.USERID)==0){
            cons_logout?.visibility = View.GONE
            cons_deleteaccount?.visibility = View.GONE
            div9?.visibility = View.GONE
            div8?.visibility = View.GONE
        }

    }
}