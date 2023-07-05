package com.dazzlebloom.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dazzlebloom.databinding.ActivityContactUsBinding

class ContactUsActivity : AppCompatActivity() {
    var activityContactUsBinding : ActivityContactUsBinding ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityContactUsBinding = ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(activityContactUsBinding?.root)
        activityContactUsBinding?.backArrow?.setOnClickListener {
            finish()
        }
    }
}