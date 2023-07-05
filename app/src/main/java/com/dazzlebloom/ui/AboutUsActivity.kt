package com.dazzlebloom.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.dazzlebloom.databinding.ActivityAboutUsBinding
import com.dazzlebloom.retrofit.ApiInterface
import com.dazzlebloom.retrofit.RetrofitHelper
import com.dazzlebloom.utiles.ViewUtils
import org.json.JSONObject

class AboutUsActivity : AppCompatActivity() {
    var activityAboutUsBinding : ActivityAboutUsBinding ? =  null
    val mimeType = "text/html"
    val encoding = "UTF-8"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAboutUsBinding  = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(activityAboutUsBinding?.root)
        activityAboutUsBinding?.backArrow?.setOnClickListener {
            finish()
        }
       // callApiForAboutUs()
    }

    private fun callApiForAboutUs() {
        ViewUtils.showDialog(this@AboutUsActivity)
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        lifecycleScope.launchWhenCreated {
           val response= apiInterface.callApiforAboutus()
            ViewUtils.dismissDialog()
            if(response.isSuccessful){
               try{
                   val jsonobj : JSONObject = JSONObject(response.body()?.string())
                   val content : JSONObject  = jsonobj.optJSONObject("content")
                  val post_content = content.optString("post_content")
                   activityAboutUsBinding?.tvweb?.loadDataWithBaseURL("", post_content, mimeType, encoding, "");

               }catch (e: Exception){
                   e.printStackTrace()
               }
            }

        }
    }
}