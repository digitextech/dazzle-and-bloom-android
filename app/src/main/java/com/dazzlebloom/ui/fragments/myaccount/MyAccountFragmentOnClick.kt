package com.dazzlebloom.ui.fragments.myaccount

import android.content.Intent
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.dazzlebloom.R
import com.dazzlebloom.retrofit.ApiInterface
import com.dazzlebloom.retrofit.RetrofitHelper
import com.dazzlebloom.ui.EditProfileActivity
import com.dazzlebloom.ui.*
import com.dazzlebloom.utiles.Constants
import com.dazzlebloom.utiles.ViewUtils
import com.dazzlebloom.utiles.sheardpreference.AppSheardPreference
import java.util.HashMap


class MyAccountFragmentOnClick(
    val mainActivity: MainActivity,
    val myAccountFragmentViewBind: MyAccountFragmentViewBind,
) : View.OnClickListener {
   init {
       if(AppSheardPreference.fetchIntFromAppPreference(Constants.USERID)==0){
           myAccountFragmentViewBind.cons_mypackage?.setOnClickListener(null)
           myAccountFragmentViewBind.cons_listing?.setOnClickListener(null)
           myAccountFragmentViewBind.tv_editProfile?.setOnClickListener(null)
           myAccountFragmentViewBind.tv_my_package?.text = "No Package"
           myAccountFragmentViewBind.tv_my_listing?.text = "No List Found"
           myAccountFragmentViewBind.tv_user_name?.text = "No User Name"
           myAccountFragmentViewBind.tv_editProfile?.visibility = View.INVISIBLE

       }else{
           myAccountFragmentViewBind.cons_mypackage?.setOnClickListener(this)
           myAccountFragmentViewBind.cons_listing?.setOnClickListener(this)
           myAccountFragmentViewBind.tv_editProfile?.setOnClickListener(this)
           myAccountFragmentViewBind.tv_user_name?.text = "Hello, "+AppSheardPreference.fetchStringFromAppPreference(Constants.USERNAME)
           Glide.with(mainActivity)
               .load(AppSheardPreference.fetchStringFromAppPreference(Constants.USER_URL)!!)
               .error(R.drawable.ic_profile)
               .apply(RequestOptions.bitmapTransform( RoundedCorners(10)))
               .into(myAccountFragmentViewBind?.img_profile!!);
       }
       myAccountFragmentViewBind.cons_address?.setOnClickListener(this)
       myAccountFragmentViewBind.cons_fav?.setOnClickListener(this)
       myAccountFragmentViewBind.cons_feedback?.setOnClickListener(this)
       myAccountFragmentViewBind.cons_helps?.setOnClickListener(this)
       myAccountFragmentViewBind.cons_logout?.setOnClickListener(this)
       myAccountFragmentViewBind.cons_message?.setOnClickListener(this)
       myAccountFragmentViewBind.cons_deleteaccount?.setOnClickListener(this)


   }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.cons_mypackage->{
                /*val manager: FragmentManager = mainActivity.supportFragmentManager
                val transaction: FragmentTransaction = manager.beginTransaction()
                transaction.add(R.id.nav_host_fragment_content_main, MyPackageFragment(mainActivity), Constants.MYPACKAGE_FRAGMENT)
                transaction.addToBackStack(null)
                transaction.commit()*/
                val packageIntent = Intent(mainActivity, MyPackageActivity::class.java)
                mainActivity.startActivity(packageIntent)
            }
            R.id.cons_address ->{
                val addressIntent = Intent(mainActivity, MyAddressActivity::class.java)
                mainActivity.startActivity(addressIntent)
            }
            R.id.cons_listing ->{
                val listingIntent = Intent(mainActivity, MyListingActivity::class.java)
                mainActivity.startActivity(listingIntent)
            }
            R.id.cons_logout ->{
                AppSheardPreference.clearSheardpreference(mainActivity)
                val intent = Intent(mainActivity, EmailLoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                mainActivity.startActivity(intent)
            }
            R.id.cons_fav ->{
                val bookmarkIntent = Intent(mainActivity, BookMarkActivity::class.java)
                mainActivity.startActivity(bookmarkIntent)
            }
            R.id.cons_feedback ->{
                val feedbackIntent = Intent(mainActivity, FeedbackActivity::class.java)
                mainActivity.startActivity(feedbackIntent)

            }
            R.id.cons_message ->{
                val messageIntent = Intent(mainActivity, MessageActivity::class.java)
                mainActivity.startActivity(messageIntent)

            }
            R.id.cons_helps ->{
                val helpIntent = Intent(mainActivity, HelpActivity::class.java)
                mainActivity.startActivity(helpIntent)
            }
            R.id.tv_editProfile ->{
                val editprofIntent = Intent(mainActivity, EditProfileActivity::class.java)
                mainActivity.startActivity(editprofIntent)
            }
            R.id.cons_deleteaccount ->{

                callApiforDeleteAccount(AppSheardPreference.fetchIntFromAppPreference(Constants.USERID))

            }
        }
    }

    private fun callApiforDeleteAccount(userid: Int) {
        ViewUtils.showDialog(mainActivity)
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        mainActivity.lifecycleScope.launchWhenCreated {
            val data: MutableMap<String, Int> = HashMap()
            data.put("uid",userid)
            val response = apiInterface.callApiforDeleteuser(data)
            ViewUtils.dismissDialog()
            if (response?.isSuccessful!! || response.code() == 403) {
                AppSheardPreference.clearSheardpreference(mainActivity)
                val intent = Intent(mainActivity, EmailLoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                mainActivity.startActivity(intent)
            }
            else
                ViewUtils.showdialog(mainActivity,"Something Wrong, Try later")
        }
    }
}