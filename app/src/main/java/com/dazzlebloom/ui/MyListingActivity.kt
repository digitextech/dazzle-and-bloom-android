package com.dazzlebloom.ui

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dazzlebloom.R
import com.dazzlebloom.adapter.ListingItemAdapter
import com.dazzlebloom.apiresponse.ListingItem
import com.dazzlebloom.apiresponse.MyListingProductData
import com.dazzlebloom.apiresponse.ProductData
import com.dazzlebloom.apiresponse.TagData
import com.dazzlebloom.databinding.ActivityMyListingBinding

import com.dazzlebloom.retrofit.ApiInterface
import com.dazzlebloom.retrofit.RetrofitHelper
import com.dazzlebloom.utiles.Constants
import com.dazzlebloom.utiles.ViewUtils
import com.dazzlebloom.utiles.customtypeface.CustomTypeface
import com.dazzlebloom.utiles.sheardpreference.AppSheardPreference
import org.json.JSONArray
import org.json.JSONObject

class MyListingActivity : AppCompatActivity() {
    var myListingBinding : ActivityMyListingBinding?= null
    var listArray : ArrayList<ListingItem> = ArrayList()
    var listingItemAdapter : ListingItemAdapter ?= null
    var totalItem = 0
    var totalPage = 0
    var pageNumber  : Int = 1
    var isloading = false
    var ordered = "ASC"
    var searchString = ""
    var orderBy = ""
    var Perpage = 20
    var prodList : ArrayList<MyListingProductData> = ArrayList()
    var selectedCatID : Int = 0
    var selectedstatus = "all"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myListingBinding = ActivityMyListingBinding.inflate(layoutInflater)
        ViewUtils.changeStatusBarcolor(this!!,resources!!.getColor(R.color.status_bar_color))
        setContentView(myListingBinding?.root)
        myListingBinding?.backArrow?.setOnClickListener {
            finish()
        }
        myListingBinding?.tvNewlist?.setOnClickListener {
            val intent = Intent(this,CreateListingActivity::class.java )
            intent.putExtra("isedit","0")
            startActivity(intent)
        }
        myListingBinding?.tvAlllist?.setOnClickListener {
            callApimylisting("any")
        }
        myListingBinding?.r1?.setOnClickListener {
           // callApiforListing("")
            callApimylisting("any")
            selectedstatus = "any"
        }
        myListingBinding?.r2?.setOnClickListener {
           // callApiforListing(Constants.ACTIVE)
            callApimylisting(Constants.ACTIVE)
            selectedstatus = Constants.ACTIVE
        }
        myListingBinding?.r3?.setOnClickListener {
           // callApiforListing(Constants.PENDING)
            callApimylisting(Constants.PENDING)
            selectedstatus = Constants.PENDING
        }
        myListingBinding?.r4?.setOnClickListener {
           // callApiforListing(Constants.REVIWED)
            callApimylisting(Constants.REVIWED)
            selectedstatus = Constants.REVIWED
        }
        settypeface()
        //if(AppSheardPreference.fetchIntFromAppPreference(Constants.USERID)!= 0)

        listingItemAdapter = ListingItemAdapter(this, prodList)
        val layoutManager = LinearLayoutManager(this)
        myListingBinding?.recListing?.layoutManager = layoutManager
        myListingBinding?.recListing?.adapter = listingItemAdapter
        myListingBinding?.recListing?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val  totalItemCount = layoutManager.getItemCount()
                val  lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                if ( totalItemCount < lastVisibleItem && !isloading) {
                     callApimylisting(selectedstatus)
                    //End of the items
                    // if (onLoadMoreListener != null) {
                    // onLoadMoreListener.LoadItems()
                    //  }

                }
            }
        })
       // callApiforListing("")
        listingcount()
        callApimylisting(selectedstatus)
        myListingBinding?.recListing?.setNestedScrollingEnabled(false);

    }

    private  fun listingcount(){
        //ViewUtils.showDialog(this)
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        lifecycleScope?.launchWhenCreated {
            val response = apiInterface.callApiforListingcount(AppSheardPreference.fetchIntFromAppPreference(Constants.USERID))
            //ViewUtils.dismissDialog()
            if(response.isSuccessful){
                try{
                    val jsonobj : JSONObject = JSONObject(response.body()?.string())
                    val jsoobjmessage = jsonobj.getJSONObject("message")
                    val publish = jsoobjmessage.optInt("publish")
                    val pending = jsoobjmessage.optInt("pending")
                    val reviewed = jsoobjmessage.optInt("reviewed")
                    val temp = jsoobjmessage.optInt("temp")
                    val trash = jsoobjmessage.optInt("trash")
                    val all = jsoobjmessage.optInt("all")
                    myListingBinding?.tvActivelistingcount?.text = publish.toString()
                    myListingBinding?.tvTotalPendingcount?.text = pending.toString()
                    myListingBinding?.tvTotalListingcount?.text = all.toString()
                    myListingBinding?.tvExpiredcount?.text = trash.toString()

                }catch (e : java.lang.Exception){
                    e.printStackTrace()
                }
            }
        }
    }

    private fun callApimylisting(listcategory : String) {
        ViewUtils.showDialog(this)
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        lifecycleScope?.launchWhenCreated {
            val response = apiInterface.callApiformylisting(selectedCatID, pageNumber,ordered, searchString, Perpage,orderBy,
                AppSheardPreference.fetchIntFromAppPreference(Constants.USERID).toString(),listcategory)
            //prodList.clear()
            if(response.isSuccessful){
                //var status = ""
                prodList.clear()


                try {
                    val jsonobj : JSONObject = JSONObject(response.body()?.string())
                    val jsonArraylisting = jsonobj.getJSONArray("listings")
                    totalItem = jsonobj.optInt("total_listing")
                    totalPage = jsonobj.optInt("total_pages")
                    for (i in 0 until jsonArraylisting.length()) {
                        val objlist = jsonArraylisting.getJSONObject(i)

                        val ID =  objlist.optInt("ID")
                        val title =  objlist.optString("title")
                        val content = objlist.optString("post_content")
                         val status = objlist.optString("status")
                        val categories = objlist.optString("categories")
                        val displayPrice = objlist.optString("price")
                        val imgarrary: Any = objlist.get("images")
                        val attechimagesId : Any = objlist.get("images_attachment_id")
                        var image = JSONArray()
                        if (imgarrary is JSONArray)
                            image = objlist.optJSONArray("images")
                        var attachimageId = JSONArray()
                        if (attechimagesId is JSONArray)
                            attachimageId = objlist.optJSONArray("images_attachment_id")
                        val location = objlist.optString("location")
                        val phone_number = objlist.optString("phone_number")
                        val website_link = objlist.optString("website_link")
                        val categori = objlist.optString("categories")
                        val whatsappNumber = objlist.optString("whatsapp_number")
                        val addressline1 = objlist.optString("address_line_1")
                        val addressline2 = objlist.optString("address_line_2")
                        val zip = objlist.optString("zip")
                        val lat = objlist.optString("lat")
                        val lang = objlist.optString("long")
                        val linkedinlink = objlist.optString("linkedin_link")
                        val instagram_link = objlist.optString("instagram_link")
                        val youtubelink =objlist.optString("youtube_link")
                        val twitterlink = objlist.optString("twitter_link")
                        val facebooklink = objlist.optString("facebook_link")
                        val summery = objlist.optString("post_excerpt")
                        val listingtype = objlist.optString("listing_type")
                        val additionalInfo = objlist.optString("additional_info")


                        val tags = objlist.optJSONArray("tags")
                        val taglist: ArrayList<TagData> = ArrayList()
                        if(tags.length()>0){
                            for ( i in 0 until  tags.length()) {
                                taglist.add(
                                    TagData(
                                        tags.getJSONObject(i).optInt("term_id"),
                                        tags.getJSONObject(i).optString("slug")))
                            }

                        }
                        val attahimagelist : ArrayList<String> = ArrayList()
                        if(attachimageId.length()>0){
                        for (i in 0 until attachimageId.length()){
                            attahimagelist.add((attachimageId.getInt(i).toString()))
                        }}

                        val imagedata: ArrayList<String> = ArrayList()
                        if(image.length()>0) {
                            for (i in 0 until image.length()){
                                imagedata.add((image.getString(i)))
                            }
                            val prodItem = MyListingProductData(
                                ID, title, content, location,
                                imagedata, categories, displayPrice.substring(1),phone_number,website_link,categori, status, taglist,addressline1,addressline2,zip,
                            lat, lang,linkedinlink,instagram_link,youtubelink,twitterlink,facebooklink,whatsappNumber,summery,listingtype,additionalInfo, attahimagelist)
                            prodList.add(prodItem)
                        }else{
                            val prodItem = MyListingProductData(
                                ID, title, content, location,
                                imagedata, categories, displayPrice.substring(1),phone_number,website_link, categori,status , taglist,addressline1,addressline2,zip,
                                lat, lang,linkedinlink,instagram_link,youtubelink,twitterlink,facebooklink,whatsappNumber,summery,listingtype,additionalInfo, attahimagelist)
                            prodList.add(prodItem)
                        }
                        //ViewUtils.dismissDialog()

                    }

                    if(totalPage > pageNumber)
                        pageNumber++
                    isloading = false


                    if(prodList.size >0)
                        listingItemAdapter?.notifyDataSetChanged()
                    else {
                        listingItemAdapter?.notifyDataSetChanged()
                        ViewUtils.showdialog(this@MyListingActivity, "No list found.")
                    }
                   /* if( status == Constants.ACTIVE)
                        myListingBinding?.tvActivelistingcount?.text = if (prodList?.size.toString()!= null) prodList.size.toString() else "0"
                    else if( status == Constants.PENDING)
                        myListingBinding?.tvTotalPendingcount?.text = if (prodList?.size.toString()!= null) prodList.size.toString() else "0"
                    else if( status == Constants.EXPIRED)
                        myListingBinding?.tvExpiredcount?.text = if (prodList?.size.toString()!= null) prodList.size.toString() else "0"
*/
                  //  myListingBinding?.tvTotalListingcount?.text = if (prodList?.size.toString()!= null) prodList.size.toString() else "0"
                   Handler().postDelayed({

                       ViewUtils.dismissDialog()
                   },Constants.SPLASHSCREEN_TIME)

                    //  }
                }catch (e :Exception){
                    e.printStackTrace()
                    ViewUtils.dismissDialog()
                }
            }else
                ViewUtils.showdialog(this@MyListingActivity,"Something wrong. try again later.")

        }
    }


    private fun callApiforListing( status : String){
        ViewUtils.showDialog(this)
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        lifecycleScope.launchWhenCreated {
           // val response = apiInterface.getlisting(20, 1,AppSheardPreference.fetchIntFromAppPreference(Constants.USERID), status)
            val response = apiInterface.getlisting(20, 1,AppSheardPreference.fetchIntFromAppPreference(Constants.USERID), status)

            if (response.isSuccessful){
                listArray.clear()
                listingItemAdapter?.notifyDataSetChanged()
                   try{
                       val jsonobj : JSONObject = JSONObject(response.body()?.string())
                       val jsonArrayMessage = jsonobj.getJSONArray("message")
                       for (i in 0 until jsonArrayMessage.length()) {
                           var imageUrl = ""
                           val objlist = jsonArrayMessage.getJSONObject(i)
                           val locationJsonArray = objlist.getJSONArray("locations")
                           val logo_image =  objlist.optString("logo_image")
                           if(!logo_image.equals("")) {
                               val ImageObj = objlist.getJSONObject("images")
                                imageUrl = ImageObj.getJSONObject(logo_image).optString("guid")
                           }
                           if(locationJsonArray.length()>0) {
                               val listItem = ListingItem(
                                   objlist.getInt("ID"),
                                   objlist.optString("post_title"),
                                   objlist.getString("post_excerpt"),
                                   locationJsonArray.getJSONObject(0).getString("name"),
                                   imageUrl,
                                   objlist.optString("status")
                               )
                               listArray.add(listItem)
                           }else{
                               val listItem = ListingItem(
                                   objlist.getInt("ID"),
                                   objlist.optString("post_title"),
                                   objlist.getString("post_excerpt"),
                                   "",
                                   imageUrl,
                                   objlist.optString("status")
                               )
                               listArray.add(listItem)
                           }

                       }
                       ViewUtils.dismissDialog()
                       if(listArray.size >0)
                          listingItemAdapter?.notifyDataSetChanged()
                       else
                           ViewUtils.showdialog(this@MyListingActivity,"No list found.")

                       if( status == Constants.ACTIVE)
                           myListingBinding?.tvActivelistingcount?.text = if (listArray?.size.toString()!= null) listArray.size.toString() else "0"
                       else if( status == Constants.PENDING)
                           myListingBinding?.tvTotalPendingcount?.text = if (listArray?.size.toString()!= null) listArray.size.toString() else "0"
                       else if( status == Constants.EXPIRED)
                           myListingBinding?.tvExpiredcount?.text = if (listArray?.size.toString()!= null) listArray.size.toString() else "0"

                           myListingBinding?.tvTotalListingcount?.text = if (listArray?.size.toString()!= null) listArray.size.toString() else "0"

                   }catch (e : Exception){
                       ViewUtils.dismissDialog()
                       e.printStackTrace()
                   }
            }else{
                ViewUtils.dismissDialog()
                ViewUtils.showdialog(this@MyListingActivity,"Something wrong")
            }
        }

    }

    public fun callApiforDelete(listingID: Int, reason: String, position: Int) {
        ViewUtils.showDialog(this)
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        lifecycleScope.launchWhenCreated {
            val data: MutableMap<String, String> = HashMap()
            data.put("uid",AppSheardPreference.fetchIntFromAppPreference(Constants.USERID).toString())
            data.put("listing_id",listingID.toString())
            data.put("reason",reason)
            val response = apiInterface.callApiforDelete(data)
          ViewUtils.dismissDialog()
          if (response.isSuccessful){
              // dialog.dismiss()
             //val item = listArray.get(position)
              //listArray?.removeAt(position)
             // listingItemAdapter?.notifyDataSetChanged()
                prodList.clear()
               listingItemAdapter?.notifyDataSetChanged()
              listingcount()
                callApimylisting(selectedstatus)


          }else{
              ViewUtils.showdialog(this@MyListingActivity,"Something error, try later")
          }

        }
    }

    public fun callApinotetoAdmin(listingID: Int, reason: String, position: Int) {
        ViewUtils.showDialog(this)
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        lifecycleScope.launchWhenCreated {
            val response = apiInterface.callApifornotetoadmin(AppSheardPreference.fetchIntFromAppPreference(Constants.USERID),listingID,reason)
            ViewUtils.dismissDialog()
            if (response.isSuccessful){
               // val item = listArray.get(position)
               // listArray.remove(item)
               // listingItemAdapter?.notifyDataSetChanged()
                ViewUtils.showdialog(this@MyListingActivity,"Note added to Admin")

            }else{
                ViewUtils.showdialog(this@MyListingActivity,"Something error, try later")
            }

        }
    }

    private fun settypeface() {
        myListingBinding?.tvListing?.typeface = CustomTypeface(this).ralewaySemiBold
        myListingBinding?.tvTotallisting?.typeface = CustomTypeface(this).ralewaySemiBold
        myListingBinding?.tvTotalListingcount?.typeface = CustomTypeface(this).ralewaySemiBold
        myListingBinding?.tvActivelisting?.typeface = CustomTypeface(this).ralewaySemiBold
        myListingBinding?.tvActivelistingcount?.typeface = CustomTypeface(this).ralewaySemiBold
        myListingBinding?.tvPendingapproval?.typeface = CustomTypeface(this).ralewaySemiBold
        myListingBinding?.tvTotalPendingcount?.typeface = CustomTypeface(this).ralewaySemiBold
        myListingBinding?.tvExpriedlisting?.typeface = CustomTypeface(this).ralewaySemiBold
        myListingBinding?.tvExpiredcount?.typeface = CustomTypeface(this).ralewaySemiBold
        myListingBinding?.tvAlllist?.typeface = CustomTypeface(this).ralewayMedium
        myListingBinding?.tvNewlist?.typeface = CustomTypeface(this).ralewayMedium

        /*myListingBinding?.tvBlueby?.typeface = CustomTypeface(this).ralewaySemiBold
        myListingBinding?.tvExpiry?.typeface = CustomTypeface(this).ralewaySemiBold
        myListingBinding?.tvExprirydate?.typeface = CustomTypeface(this).ralewayMedium
        myListingBinding?.tvListingid?.typeface = CustomTypeface(this).ralewaySemiBold
        myListingBinding?.tvListingidNumber?.typeface = CustomTypeface(this).ralewayMedium
        myListingBinding?.tvStatus?.typeface = CustomTypeface(this).ralewaySemiBold*/
    }
}