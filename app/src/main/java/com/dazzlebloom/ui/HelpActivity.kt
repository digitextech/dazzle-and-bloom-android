package com.dazzlebloom.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dazzlebloom.R
import com.dazzlebloom.databinding.ActivityHelpBinding
import com.dazzlebloom.utiles.ViewUtils

class HelpActivity : AppCompatActivity() {
    var activityHelpBinding : ActivityHelpBinding ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityHelpBinding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(activityHelpBinding!!.root)
        ViewUtils.changeStatusBarcolor(this!!,resources!!.getColor(R.color.status_bar_color))
        activityHelpBinding!!.backArrow.setOnClickListener {
            finish()
        }

    }
}