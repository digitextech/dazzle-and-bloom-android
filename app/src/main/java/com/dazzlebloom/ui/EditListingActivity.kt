package com.dazzlebloom.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dazzlebloom.R
import com.dazzlebloom.databinding.ActivityEditListingBinding

class EditListingActivity : AppCompatActivity() {
    var activityEditListingBinding : ActivityEditListingBinding ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityEditListingBinding = ActivityEditListingBinding.inflate(layoutInflater)
        setContentView(activityEditListingBinding?.root)
    }
}