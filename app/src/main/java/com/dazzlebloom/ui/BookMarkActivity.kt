package com.dazzlebloom.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.dazzlebloom.R
import com.dazzlebloom.adapter.BookMarkListAdapter
import com.dazzlebloom.apiresponse.ProductData
import com.dazzlebloom.databinding.ActivityBookMarkBinding
import com.dazzlebloom.utiles.ViewUtils
import com.dazzlebloom.utiles.customtypeface.CustomTypeface
import com.dazzlebloom.utiles.sheardpreference.AppSheardPreference

class BookMarkActivity : AppCompatActivity() {
    var activityBookMarkBinding: ActivityBookMarkBinding?= null
    var bookMarkListAdapter : BookMarkListAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBookMarkBinding = ActivityBookMarkBinding.inflate(layoutInflater)
        setContentView(activityBookMarkBinding?.root)
    }

    override fun onResume() {
        super.onResume()
        ViewUtils.changeStatusBarcolor(this!!,resources!!.getColor(R.color.status_bar_color))
        activityBookMarkBinding?.recBookmark?.layoutManager = LinearLayoutManager(this)

        activityBookMarkBinding?.tvBookmark?.typeface = CustomTypeface(this).ralewaySemiBold
        activityBookMarkBinding?.backArrow?.setOnClickListener {
            finish()
        }
        var prodlist :  ArrayList<ProductData> = ArrayList()
        prodlist = AppSheardPreference.getProductList(this)
        if(prodlist.size > 0) {
            bookMarkListAdapter = BookMarkListAdapter(this, prodlist!!)
            activityBookMarkBinding?.recBookmark?.adapter = bookMarkListAdapter
        }else
            ViewUtils.showdialog(this,"your wishlist is empty.")

    }

}