package com.dazzlebloom.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dazzlebloom.R
import com.dazzlebloom.adapter.MyPackageViewPagerAdapter
import com.dazzlebloom.databinding.ActivityMyPackageBinding
import com.dazzlebloom.ui.fragments.mypackage.AddNewPackageFragment
import com.dazzlebloom.ui.fragments.mypackage.SubscribeFragment
import com.dazzlebloom.utiles.ViewUtils
import com.dazzlebloom.utiles.customtypeface.CustomTypeface

class MyPackageActivity : AppCompatActivity() {
    var activityMyPackageBinding : ActivityMyPackageBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMyPackageBinding = ActivityMyPackageBinding.inflate(layoutInflater)
        setContentView(activityMyPackageBinding?.root)
    }

    override fun onResume() {
        super.onResume()
        ViewUtils.changeStatusBarcolor(this!!,resources!!.getColor(R.color.status_bar_color))
        // Initializing the ViewPagerAdapter
        val adapter = supportFragmentManager?.let { MyPackageViewPagerAdapter(it) }
        // add fragment to the list
        adapter?.addFragment(SubscribeFragment(this), "Subscribe Package")
       // adapter?.addFragment(AddNewPackageFragment(), "Add new package")

        // Adding the Adapter to the ViewPager
        activityMyPackageBinding?.viewPager?.adapter = adapter

        // bind the viewPager with the TabLayout.
        activityMyPackageBinding?.tabs?.setupWithViewPager(activityMyPackageBinding?.viewPager)
        activityMyPackageBinding?.tvMypackage?.typeface = CustomTypeface(this).ralewaySemiBold
        activityMyPackageBinding?.backArrow?.setOnClickListener {
            finish()
        }
    }
}