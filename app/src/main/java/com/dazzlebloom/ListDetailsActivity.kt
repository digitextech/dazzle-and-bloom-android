package com.dazzlebloom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dazzlebloom.databinding.ActivityListDetailsBinding
import com.dazzlebloom.databinding.ActivityProductListDetailsBinding
import com.dazzlebloom.ui.CreateListingActivity
import com.dazzlebloom.ui.EmailLoginActivity
import com.dazzlebloom.ui.MainActivity
import com.dazzlebloom.utiles.Constants
import com.dazzlebloom.utiles.ViewUtils
import com.dazzlebloom.utiles.customtypeface.CustomTypeface
import com.dazzlebloom.utiles.sheardpreference.AppSheardPreference

class ListDetailsActivity : AppCompatActivity() {
    var activityListDetailsBinding: ActivityListDetailsBinding? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityListDetailsBinding = ActivityListDetailsBinding.inflate(layoutInflater)
        setContentView(activityListDetailsBinding?.root)
        ViewUtils.changeStatusBarcolor(this!!,resources!!.getColor(R.color.status_bar_color))
        settypeface()
        activityListDetailsBinding?.backArrow?.setOnClickListener {
            finish()
        }
        activityListDetailsBinding?.btnSubPackagelist?.setOnClickListener {
            if(AppSheardPreference.fetchIntFromAppPreference(Constants.USERID)!=0) {
                val intent = Intent(this, CreateListingActivity::class.java)
                intent.putExtra("isedit", "0")
                startActivity(intent)
            }else{
                val intent = Intent(MainActivity.mainActivity, EmailLoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }

        }
    }

    private fun settypeface() {
        activityListDetailsBinding?.tvListTitle?.typeface = CustomTypeface(this).ralewayMedium
        activityListDetailsBinding?.tvFree?.typeface = CustomTypeface(this).ralewaySemiBold
        activityListDetailsBinding?.tvFeature?.typeface = CustomTypeface(this).ralewayRegular
        activityListDetailsBinding?.tvImageFeature?.typeface = CustomTypeface(this).ralewayRegular
        activityListDetailsBinding?.tvOneVideo?.typeface = CustomTypeface(this).ralewayRegular
        activityListDetailsBinding?.tvListSold?.typeface = CustomTypeface(this).ralewayRegular
        activityListDetailsBinding?.tvNohiddencost?.typeface = CustomTypeface(this).ralewayRegular
        activityListDetailsBinding?.btnSubPackagelist?.typeface = CustomTypeface(this).ralewayMedium
    }
}