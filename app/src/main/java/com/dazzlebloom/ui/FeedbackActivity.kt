package com.dazzlebloom.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dazzlebloom.R
import com.dazzlebloom.databinding.ActivityFeedbackBinding
import com.dazzlebloom.utiles.ViewUtils

class FeedbackActivity : AppCompatActivity() {
    var activityFeedbackBinding : ActivityFeedbackBinding ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityFeedbackBinding = ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(activityFeedbackBinding!!.root)
        ViewUtils.changeStatusBarcolor(this!!,resources!!.getColor(R.color.status_bar_color))
        activityFeedbackBinding!!.backArrow.setOnClickListener {
            finish()
        }
    }
}