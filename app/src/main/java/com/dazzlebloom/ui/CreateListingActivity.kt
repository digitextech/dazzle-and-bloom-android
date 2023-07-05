package com.dazzlebloom.ui

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope


import com.dazzlebloom.retrofit.ApiInterface
import com.dazzlebloom.retrofit.RetrofitHelper
import com.dazzlebloom.utiles.Constants
import com.dazzlebloom.utiles.ViewUtils
import com.dazzlebloom.utiles.customtypeface.CustomTypeface
import com.dazzlebloom.utiles.sheardpreference.AppSheardPreference
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter

import androidx.core.app.ActivityCompat.startActivityForResult

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import kotlin.random.Random

import android.content.DialogInterface.OnMultiChoiceClickListener
import androidx.appcompat.app.AlertDialog
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.size
import com.bumptech.glide.Glide
import com.dazzlebloom.databinding.ActivityCreateListingBinding
import java.lang.StringBuilder
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.dazzlebloom.R
import com.dazzlebloom.adapter.SelectedImageAdapter
import com.dazzlebloom.apiresponse.*
import android.graphics.BitmapFactory
import androidx.core.content.FileProvider
import com.dazzlebloom.BuildConfig
import java.io.*
import java.text.SimpleDateFormat


class CreateListingActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    var activityCreateListingBinding: ActivityCreateListingBinding? = null
    var listingID = 0
    var listingTypeId :String= "0"
    var attachImageID = ""
    var attachedImagename = ""
    val PICK_IMAGE = 111
    var videoId : Any? = null
    private val EXTERNAL_STORAGE_PERMISSION_CODE = 23
    var locationList : ArrayList<String> = ArrayList()
    var categoryList : ArrayList<String> = ArrayList()
    var categoryKeyID : ArrayList<String> = ArrayList()
    var cityKeyID : ArrayList<String> = ArrayList()
    var tagList : ArrayList<String> = ArrayList()
    var tagKeyID : ArrayList<String> = ArrayList()
    var typeList : ArrayList<String> = ArrayList()
    var typeKeyID : ArrayList<String> = ArrayList()
    var langList: ArrayList<Int> = ArrayList()
    var selectedtagList: ArrayList<String> = ArrayList()
    var selectedcategory: ArrayList<Int> = ArrayList()
    var attachedImageIds: ArrayList<String> = ArrayList()
    var selectedCity : String ="0"
    lateinit var selectedTag : BooleanArray
    var array = arrayOfNulls<String>(tagList.size);
    var product: MyListingProductData? = null
    var listitem : ListingItem ?= null
    var isedit : String = ""
    var imagecount = 0
    var listimage : ArrayList<View> = ArrayList()
    val selecteimagelist : ArrayList<ImageModel> = ArrayList()
    var selectedImageAdapter : SelectedImageAdapter ?= null
    private val REQUEST_PERMISSION = 100
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_PICK_IMAGE = 2
    var photoFile: File? = null
    var mCurrentPhotoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCreateListingBinding = ActivityCreateListingBinding.inflate(layoutInflater)
        setContentView(activityCreateListingBinding?.root)
        ViewUtils.changeStatusBarcolor(this!!, resources!!.getColor(R.color.status_bar_color))

        selectedImageAdapter = SelectedImageAdapter(selecteimagelist , this)

        val layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        activityCreateListingBinding?.recImage?.layoutManager = layoutManager
        activityCreateListingBinding?.recImage?.adapter = selectedImageAdapter

        setTypeface()
        ViewUtils.showDialog(this)
         isedit = intent.getStringExtra("isedit")!!
        if (isedit.equals("1")){
            product = intent.extras?.get("listing") as MyListingProductData
            listingID = product?.ID!!
            setvalue()
        }else
            callApiforCreateListingFormID()

        activityCreateListingBinding?.backArrow?.setOnClickListener {
            finish()
        }

        activityCreateListingBinding?.tvSelectTag?.setOnClickListener {
            if (tagList.size > 0) {
                showSelectTagDialog()
            }
        }

        activityCreateListingBinding?.btnSubmit?.setOnClickListener {
            // ViewUtils.showdialog(this, "Under Development")
            if (listingID != 0) {
                if (cheakvalidation())
                    callApiForCreateList()
            }
        }

        activityCreateListingBinding?.btnAddVido?.setOnClickListener {
            if (!activityCreateListingBinding?.etYoutube?.text.toString().equals("")) {
                try {
                     videoId = Uri.parse(activityCreateListingBinding?.etYoutube?.text.toString()).getQueryParameter("v")!!
                   if(videoId!=null) {
                       val thumbnailUri = Uri.parse("https://img.youtube.com/vi/${videoId}/0.jpg")
                       val inner : ConstraintLayout  = layoutInflater.inflate(R.layout.item_youtube_thumb, activityCreateListingBinding?.constVideo, false) as ConstraintLayout;
                       val youtubeimageView = inner.findViewById<ImageView>(R.id.img_youtube_video)
                       val deleteImage = inner.findViewById<ImageView>(R.id.img_bin)
                       deleteImage.setOnClickListener {
                           activityCreateListingBinding?.constVideo?.removeAllViews()
                       }
                       youtubeimageView?.let {

                           if(thumbnailUri != null) {
                               activityCreateListingBinding?.constVideo?.addView(inner)
                               Glide.with(this).load(thumbnailUri).into(it)
                           }
                       }

                   }else
                       ViewUtils.showdialog(this,"This you tube video url is not Correct")

                }catch (e : Exception){
                    e.printStackTrace()
                    ViewUtils.showdialog(this,"This you tube video url is not Correct")
                }
            } else
                ViewUtils.showdialog(this, "Enter Youtube Link")
        }


        callApiforListingTag()
        callApiForCategories()
        callApiForLocation()
        callApiForListingType()
        activityCreateListingBinding?.imgUpload?.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                           if(imagecount<= 8)
                            //selectImage()
                                selectImageDialog()
                } else {
                    ActivityCompat.requestPermissions(this,
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        EXTERNAL_STORAGE_PERMISSION_CODE
                    );
                }
            }

        }
        activityCreateListingBinding?.autolistingType?.setOnItemClickListener(this)
        activityCreateListingBinding?.autolistingCity?.setOnItemClickListener(this)
        activityCreateListingBinding?.category?.setOnItemClickListener(this)
       // activityCreateListingBinding?.llImg?.orientation = LinearLayout.HORIZONTAL

    }

    private fun setvalue() {
        activityCreateListingBinding?.etTitle?.setText(product?.post_name)
        activityCreateListingBinding?.autolistingCity?.setText( product?.locatioin.toString())

        activityCreateListingBinding?.tvCreateNewListing?.setText("Edit Listing")

        val stringBuilder = StringBuilder()
        // use for loop
        for (j in  0 until product?.taglist?.size!!) {
            // concat array value
            stringBuilder.append(product?.taglist?.get(j)?.tagname)
            product?.taglist?.get(j)?.tagname?.let { selectedtagList.add(it) }
            // check condition
            if (j != product?.taglist?.size!! - 1) {
                stringBuilder.append(", ")
            }
        }
        // set text on textView
        activityCreateListingBinding?.tvSelectTag?.setText(stringBuilder.toString())
        if(product?.category!= null)
          activityCreateListingBinding?.category?.setText(product?.category)
       // activityCreateListingBinding?.llImg?.orientation = LinearLayout.HORIZONTAL
        for (i in 0 until product?.post_image?.size!!){

                val imagemodel = ImageModel(product?.imageIDS?.get(i)!!,product?.post_image!!.get(i))
                selecteimagelist.add(imagemodel)
                selectedImageAdapter?.notifyDataSetChanged()
               attachedImageIds  = product?.imageIDS!!

        }

        activityCreateListingBinding?.etPrice?.setText(product?.displayPrice!!)
        activityCreateListingBinding?.etPhone?.setText(product?.phonenumber!!)
        activityCreateListingBinding?.etDescription?.setText(product?.post_content!!)
        activityCreateListingBinding?.etSummry?.setText(product?.summery)
        activityCreateListingBinding?.etAdd1?.setText(product?.address_line_1)
        activityCreateListingBinding?.etAdd2?.setText(product?.address_line_2)
        activityCreateListingBinding?.etZipcode?.setText(product?.zip)
        activityCreateListingBinding?.etWhatsapp?.setText(product?.whatsapp_number)
        activityCreateListingBinding?.etTwitter?.setText(product?.twitter_link)
        activityCreateListingBinding?.etYoutubelink?.setText(product?.youtube_link)
        activityCreateListingBinding?.etInstra?.setText(product?.instagram_link)
        activityCreateListingBinding?.etFacebook?.setText(product?.facebook_link)
        activityCreateListingBinding?.etAdditionalinfo?.setText(product?.additionalinfo)

    }

    private fun callApiForCreateList() {
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        lifecycleScope.launchWhenCreated {
            ViewUtils.showDialog(this@CreateListingActivity)
            try{
                val createlistmodel = CreateListModel(listingID.toString(),activityCreateListingBinding?.etDescription?.text.toString(),activityCreateListingBinding?.etSummry?.text.toString(),AppSheardPreference.fetchIntFromAppPreference(Constants.USERID),
                    activityCreateListingBinding?.etTitle?.text.toString(),selectedtagList.toTypedArray(),selectedcategory.toTypedArray(),
                    activityCreateListingBinding?.etPhone?.text.toString(),activityCreateListingBinding?.etWhatsapp?.text.toString(),listingTypeId.toString(),
                "","",activityCreateListingBinding?.etPrice?.text.toString(),"",selectedCity,
                    activityCreateListingBinding?.etAdd1?.text.toString(),activityCreateListingBinding?.etAdd2?.text.toString(),
                    activityCreateListingBinding?.etZipcode?.text.toString(),activityCreateListingBinding?.etAdditionalinfo?.text.toString(), "","",
                    attachedImageIds?.toTypedArray()!!,attachedImagename,"",videoId.toString(),"", activityCreateListingBinding?.etInstra?.text.toString(),
                    activityCreateListingBinding?.etYoutubelink?.text.toString(),activityCreateListingBinding?.etTwitter?.text.toString(),
                     activityCreateListingBinding?.etFacebook?.text.toString())


                if(isedit.equals("1")) {
                    val response = apiInterface.callApiForUpdateList(createlistmodel)
                    ViewUtils.dismissDialog()
                    if(response.isSuccessful){
                    ViewUtils.showdialog(this@CreateListingActivity,"Listing  Updated successfully")
                        val mylisting = Intent(this@CreateListingActivity,MyListingActivity::class.java)
                        startActivity(mylisting)
                        finish()
                }else{
                    ViewUtils.showdialog(this@CreateListingActivity,"Something wrong. Try again later")
                }

                }else {
                    val response = apiInterface.callApiForCreateList(createlistmodel)
                    ViewUtils.dismissDialog()
                    if (response.isSuccessful) {
                        ViewUtils.showdialog(this@CreateListingActivity, "Listing  added successfully")

                      val mylisting = Intent(this@CreateListingActivity,MyListingActivity::class.java)
                      startActivity(mylisting)
                      finish()
                    } else {
                        ViewUtils.showdialog(this@CreateListingActivity, "Something wrong. Try again later")
                    }
                }

            }catch (e : Exception){
                e.printStackTrace()
                ViewUtils.dismissDialog()
            }

        }

    }

    private fun cheakvalidation() : Boolean {
        if(activityCreateListingBinding?.etTitle?.text.toString().equals("")){
            ViewUtils.showdialog(this, "Please Enter Title")
            return false
        }
         if(activityCreateListingBinding?.tvSelectTag?.text.toString().equals("")){
             ViewUtils.showdialog(this, "Please Select Tag")
             return false
         }
        if (activityCreateListingBinding?.category?.text?.toString().equals("")){
            ViewUtils.showdialog(this, "Please Select Category")
            return false
        }
        if (activityCreateListingBinding?.etPhone?.text?.trim().toString().isNullOrEmpty()){
            ViewUtils.showdialog(this, "Please Enter phone number")
            return false
        }
        if (activityCreateListingBinding?.autolistingType?.text.toString().equals("")){
            ViewUtils.showdialog(this, "Please Select listing type")
            return false
        }
        if (activityCreateListingBinding?.etPrice?.text.toString().equals("")){
            ViewUtils.showdialog(this, "Please enter price")
            return false
        }
        if (activityCreateListingBinding?.etDescription?.text.toString().equals("")){
            ViewUtils.showdialog(this, "Please enter Description")
            return false
        }
        if (activityCreateListingBinding?.autolistingCity?.text.toString().equals("")){
            ViewUtils.showdialog(this, "Please select city")
            return false
        }
        return true
    }

    private fun showSelectTagDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Select Tag");
        builder.setCancelable(false)
        builder.setMultiChoiceItems(tagList.toArray(array), selectedTag,
            OnMultiChoiceClickListener { dialogInterface, i, b ->
                // check condition
                if (b) {
                    // when checkbox selected
                    // Add position  in lang list
                    langList.add(i)

                    // Sort array list
                    Collections.sort(langList)
                } else {
                    // when checkbox unselected
                    // Remove position from langList
                    langList.remove(Integer.valueOf(i))
                }
            })

        builder.setPositiveButton("OK") { dialogInterface, i -> // Initialize string builder
            val stringBuilder = StringBuilder()
            selectedtagList.clear()
            // use for loop
            for (j in langList.indices) {
                // concat array value
                stringBuilder.append(tagList.get(langList[j]))
                selectedtagList.add(tagList.get(langList[j]))
                // check condition
                if (j != langList.size - 1) {
                    // When j value  not equal
                    // to lang list size - 1
                    // add comma
                    stringBuilder.append(", ")
                }
            }
            // set text on textView
            activityCreateListingBinding?.tvSelectTag?.setText(stringBuilder.toString())
        }

        builder.setNegativeButton("Cancel") { dialogInterface, i -> // dismiss dialog
            dialogInterface.dismiss()
        }

        builder.setNeutralButton("Clear All") { dialogInterface, i ->
            // use for loop
            for (j in 0 until selectedTag.size) {
                // remove all selection
                selectedTag[j] = false
                // clear language list
                langList.clear()
                // clear text view value
                activityCreateListingBinding?.tvSelectTag?.setHint("Select Listing Tag")
            }
        }

        builder.show();


    }

    private fun callApiForListingType() {
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        lifecycleScope.launchWhenCreated {
            try {
                val response = apiInterface.callApiForType()
                ViewUtils.dismissDialog()
                if(response.isSuccessful){
                    val  mainJSONObj : JSONObject = JSONObject(response.body()?.string())
                    val jsonAobjMessage = mainJSONObj.optJSONArray("message")
                    //val selectionObj = jsonAobjMessage.getJSONObject("selection_items")

                    for(i in 0 until jsonAobjMessage.length()){
                        typeList.add(jsonAobjMessage.optJSONObject(i).optString("name"))
                        typeKeyID.add(jsonAobjMessage.optJSONObject(i).optString("id"))
                    }


                    var listTypeAdapter = ArrayAdapter(this@CreateListingActivity, R.layout.item_dropdown, typeList)
                    activityCreateListingBinding?.autolistingType?.setAdapter(listTypeAdapter)
                    if(isedit.equals("1"))
                    {
                        for(i in 0 until typeKeyID.size){
                            if(typeKeyID.get(i).toString().equals(product?.listingtype)){
                                listingTypeId = typeKeyID.get(i)
                                activityCreateListingBinding?.autolistingType?.setText(typeList.get(i))
                                break
                            }

                        }

                    }
                }

            }catch (e : java.lang.Exception){
                e.printStackTrace()
            }
        }
    }

    private fun callApiForLocation() {
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        lifecycleScope.launchWhenCreated {
            try {
                val response = apiInterface.callApiForLocation()
                if(response.isSuccessful){
                    val  mainJSONObj : JSONObject = JSONObject(response.body()?.string())

                    val jsonAobjMessage = mainJSONObj.optJSONArray("message")

                    for ( i in 0 until jsonAobjMessage.length()){
                        val location : JSONObject = jsonAobjMessage.getJSONObject(i)
                        locationList.add(location.optString("name"))
                        cityKeyID.add(location.optString("id"))
                    }


                     var locationAdapter = ArrayAdapter(this@CreateListingActivity, R.layout.item_dropdown, locationList)
                    activityCreateListingBinding?.autolistingCity?.setAdapter(locationAdapter)

                    if (isedit.equals("1")){
                        for(i in 0 until locationList.size){
                            if(locationList.get(i).equals(product?.locatioin)){
                                activityCreateListingBinding?.autolistingCity?.setText(product?.locatioin)
                                selectedCity = cityKeyID.get(i)
                                break
                            }
                        }
                    }
                }

            }catch (e : java.lang.Exception){
                e.printStackTrace()
            }
        }
    }

    private fun callApiForCategories() {
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        lifecycleScope.launchWhenCreated {
            try {
                val response = apiInterface.callApiForCategories()
                if(response.isSuccessful){
                    val  mainJSONObj : JSONObject = JSONObject(response.body()?.string())
                    val jsonAobjMessage = mainJSONObj.optJSONArray("message")
                    for (i in 0 until jsonAobjMessage.length()){
                        categoryList.add(jsonAobjMessage.getJSONObject(i).optString("name"))
                        categoryKeyID.add(jsonAobjMessage.getJSONObject(i).optString("id"))

                    }


                    var categoryAdapter = ArrayAdapter(this@CreateListingActivity, R.layout.item_dropdown, categoryList)
                    activityCreateListingBinding?.category?.setAdapter(categoryAdapter)

                    if (isedit.equals("1")){
                        for(i in 0 until categoryList.size){
                            if(categoryList.get(i).equals(product?.category)){
                                activityCreateListingBinding?.category?.setText(product?.category)
                                selectedcategory.add(categoryKeyID.get(i).toInt())
                                break
                            }
                        }
                    }

                }


            }catch (e : java.lang.Exception){
                e.printStackTrace()
            }
        }
    }

    private fun callApiforListingTag() {
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        lifecycleScope.launchWhenCreated {
            try {
                val response = apiInterface.callApiForListingTags()
                if(response.isSuccessful){
                    val  mainJSONObj : JSONObject = JSONObject(response.body()?.string())
                    val jsonAobjMessage = mainJSONObj.getJSONArray("message")
                    for (i in 0 until jsonAobjMessage.length()){
                        tagList.add( jsonAobjMessage.getJSONObject(i).optString("name"))
                        tagKeyID.add(jsonAobjMessage.getJSONObject(i).optString("id"))
                    }

                    array = arrayOfNulls<String>(tagList.size);
                    selectedTag = BooleanArray(tagList.size)

                }

            }catch (e : java.lang.Exception){
                e.printStackTrace()
            }
        }


    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
    }

    private fun callApiforCreateListingFormID() {

        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        lifecycleScope.launchWhenCreated {
            try {
                val data: MutableMap<String, Int> = HashMap()
                data.put("uid", AppSheardPreference.fetchIntFromAppPreference(Constants.USERID))
                val response = apiInterface.callApiForPrepareForm(data)

                if (response.isSuccessful) {
                    // if(response.body()?.message?.listing_id!=null)
                    response?.body()?.message!!?.listing_id?.let {
                        listingID = it
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    private fun setTypeface() {
        activityCreateListingBinding?.autolistingType?.typeface = CustomTypeface(this).ralewayRegular
        activityCreateListingBinding?.category?.typeface = CustomTypeface(this).ralewayRegular
        activityCreateListingBinding?.tvCreateNewListing?.typeface = CustomTypeface(this).ralewaySemiBold
        activityCreateListingBinding?.tvDetails?.typeface = CustomTypeface(this).ralewaySemiBold
        activityCreateListingBinding?.tvTitle?.typeface = CustomTypeface(this).ralewayMedium
        activityCreateListingBinding?.etTitle?.typeface = CustomTypeface(this).ralewayRegular
        activityCreateListingBinding?.tvListingTag?.typeface = CustomTypeface(this).ralewayMedium
        activityCreateListingBinding?.tvSelectTag?.typeface = CustomTypeface(this).ralewayRegular
        activityCreateListingBinding?.tvCategories?.typeface = CustomTypeface(this).ralewayMedium
        //activityCreateListingBinding?.tvCategoryTag?.typeface = CustomTypeface(this).ralewayRegular
        activityCreateListingBinding?.tvExtradetails?.typeface = CustomTypeface(this).ralewaySemiBold
        activityCreateListingBinding?.tvPhone?.typeface = CustomTypeface(this).ralewayMedium
        activityCreateListingBinding?.etPhone?.typeface = CustomTypeface(this).ralewayRegular
        activityCreateListingBinding?.tvWhatsappnumber?.typeface = CustomTypeface(this).ralewayMedium
        activityCreateListingBinding?.etWhatsapp?.typeface = CustomTypeface(this).ralewayRegular
        activityCreateListingBinding?.tvListingtype?.typeface = CustomTypeface(this).ralewayMedium
        //activityCreateListingBinding?.tvListingtypeVal?.typeface = CustomTypeface(this).ralewayRegular
        activityCreateListingBinding?.tvPrice?.typeface = CustomTypeface(this).ralewayMedium
        activityCreateListingBinding?.etPhone?.typeface = CustomTypeface(this).ralewayRegular
        activityCreateListingBinding?.tvDescriptionDetails?.typeface = CustomTypeface(this).ralewaySemiBold
        activityCreateListingBinding?.tvSocialMedia?.typeface = CustomTypeface(this).ralewaySemiBold
        activityCreateListingBinding?.tvFacebooklink?.typeface = CustomTypeface(this).ralewayRegular
        activityCreateListingBinding?.etFacebook?.typeface = CustomTypeface(this).ralewayRegular
        activityCreateListingBinding?.tvTwitterlink?.typeface = CustomTypeface(this).ralewayMedium
        activityCreateListingBinding?.etTwitter?.typeface = CustomTypeface(this).ralewayRegular
        activityCreateListingBinding?.tvYoutubelink?.typeface = CustomTypeface(this).ralewayMedium
        activityCreateListingBinding?.etYoutubelink?.typeface = CustomTypeface(this).ralewayRegular
        activityCreateListingBinding?.tvIntralink?.typeface = CustomTypeface(this).ralewayMedium
        activityCreateListingBinding?.etInstra?.typeface = CustomTypeface(this).ralewayRegular
        activityCreateListingBinding?.tvDescription?.typeface = CustomTypeface(this).ralewaySemiBold
        activityCreateListingBinding?.tvSummery?.typeface = CustomTypeface(this).ralewaySemiBold
        activityCreateListingBinding?.etDescription?.typeface = CustomTypeface(this).ralewayRegular
        activityCreateListingBinding?.etSummry?.typeface = CustomTypeface(this).ralewayMedium
        activityCreateListingBinding?.tvMedia?.typeface = CustomTypeface(this).ralewaySemiBold
        activityCreateListingBinding?.tvListingImage?.typeface = CustomTypeface(this).ralewaySemiBold
        activityCreateListingBinding?.tvUploadimage?.typeface = CustomTypeface(this).ralewayRegular
        activityCreateListingBinding?.tvListingVideo?.typeface = CustomTypeface(this).ralewaySemiBold
        activityCreateListingBinding?.tvUploadvideo?.typeface = CustomTypeface(this).ralewayRegular
        activityCreateListingBinding?.etYoutubelink?.typeface = CustomTypeface(this).ralewayRegular
        activityCreateListingBinding?.btnAddVido?.typeface = CustomTypeface(this).ralewayMedium
       activityCreateListingBinding?.tvLocation?.typeface = CustomTypeface(this).ralewaySemiBold
        activityCreateListingBinding?.tvListinglocation?.typeface = CustomTypeface(this).ralewayMedium
        activityCreateListingBinding?.autolistingCity?.typeface = CustomTypeface(this).ralewayRegular
        activityCreateListingBinding?.tvAdd1?.typeface = CustomTypeface(this).ralewayMedium
        activityCreateListingBinding?.etAdd1?.typeface = CustomTypeface(this).ralewayRegular
        activityCreateListingBinding?.tvAdd2?.typeface = CustomTypeface(this).ralewayMedium
        activityCreateListingBinding?.etAdd2?.typeface = CustomTypeface(this).ralewayRegular
        activityCreateListingBinding?.tvZipcode?.typeface = CustomTypeface(this).ralewayMedium
        activityCreateListingBinding?.etZipcode?.typeface = CustomTypeface(this).ralewayRegular
        activityCreateListingBinding?.tvAddtionalinfo?.typeface = CustomTypeface(this).ralewayMedium
        activityCreateListingBinding?.etAdditionalinfo?.typeface = CustomTypeface(this).ralewayRegular

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            EXTERNAL_STORAGE_PERMISSION_CODE -> {
                if(imagecount<= 8)
                    selectImageDialog()
            }
            REQUEST_PERMISSION -> {
                if(imagecount<= 8) {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
                }

            }
        }
    }

    @Throws(IOException::class)
    private fun createCapturedPhoto(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageFileName, /* prefix */".jpg", /* suffix */
            storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE ) {
            if (resultCode != Activity.RESULT_OK) {
                return
            }

            if (data != null) {
                try {
                    val bm = MediaStore.Images.Media.getBitmap(
                        applicationContext.contentResolver,
                        data.data
                    )

                    val externalStorageVolumes: Array<out File> =
                        ContextCompat.getExternalFilesDirs(applicationContext, null)
                    val root = externalStorageVolumes[0]
                   // val root = Environment.getExternalStorageDirectory().toString()
                    val myDir = File("$root/Dazzle/createlist")
                    myDir.mkdirs()
                    val generator = Random
                    var n = 100
                    n = generator.nextInt(n)
                    val fname = "listimage_" + n.toString() + ".jpg"
                    val file = File(myDir, fname)
                    val fo: FileOutputStream
                    if (file.exists())
                        file.delete()
                    try {
                        val out = FileOutputStream(file)
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, out)
                        out.flush()
                        out.close()

                        imagecount ++

                        callApiUploadImage(file)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }else  if (requestCode == REQUEST_IMAGE_CAPTURE ){

            if (resultCode != Activity.RESULT_OK) {
                return
            }
            if (data != null) {
                try {
                    val thumbnail = data!!.extras!!.get("data") as Bitmap
                    val externalStorageVolumes: Array<out File> =
                        ContextCompat.getExternalFilesDirs(applicationContext, null)
                    val root = externalStorageVolumes[0]
                    // val root = Environment.getExternalStorageDirectory().toString()
                    val myDir = File("$root/Dazzle/createlist")
                    myDir.mkdirs()
                    val generator = Random
                    var n = 100
                    n = generator.nextInt(n)
                    val fname = "listimage_" + n.toString() + ".jpg"
                    val file = File(myDir, fname)
                    val fo: FileOutputStream
                    if (file.exists())
                        file.delete()
                    try {
                        val out = FileOutputStream(file)
                        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, out)
                        out.flush()
                        out.close()

                        imagecount ++

                        callApiUploadImage(file)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }


        }
    }

    private fun selectImageDialog() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Photo!")
        builder.setItems(options) { dialog, item ->
            if (options[item] == "Take Photo") {

                checkCameraPermission()
            } else if (options[item] == "Choose from Gallery") {

                selectImage()
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_PERMISSION)
        }else{
            if(imagecount<= 8)
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
                    intent.resolveActivity(packageManager)?.also {
                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
                    }
                }
        }

    }

    private fun callApiUploadImage(file: File) {
        ViewUtils.showDialog(this)
        try {
            val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
            builder.addFormDataPart("listing_id", listingID.toString())
            builder.addFormDataPart("image", file.name, RequestBody.create("image/jpeg".toMediaTypeOrNull(),file))
            val requestBody = builder.build()
            var request: Request? = null
            request = Request.Builder()
               // .addHeader("Authorization", AppSheardPreference(this).getvalue_in_preference(PreferenceConstent.loginuser_token))
                //.addHeader("site_id",AppSheardPreference(this).getvalue_in_preference(PreferenceConstent.site_id))
                .addHeader("Content-Type","application/json")
                .url(Constants.BASE_URL + Constants.IMAGE_UPLOAD_URL)
                .post(requestBody)
                .build()

            val client = okhttp3.OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .build()

            val call = client.newCall(request)

            call.enqueue(object  : okhttp3.Callback{
                override fun onFailure(call: Call, e: IOException) {
                    ViewUtils.dismissDialog()
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        val jsonobj: JSONObject = JSONObject(response.body?.string())
                        val jsonMessage = jsonobj.getJSONObject("message")
                        attachImageID =  jsonMessage.optString("attachment_id")
                        attachedImagename  = jsonMessage.optString("uploaded_file")
                        attachedImageIds.add(attachImageID!!.toString())
                        val imageModel = ImageModel(attachImageID,attachedImagename )
                        selecteimagelist.add(imageModel)
                        runOnUiThread {
                            selectedImageAdapter?.notifyDataSetChanged()
                            ViewUtils.dismissDialog()
                        }


                    }catch (e : java.lang.Exception){
                        e.printStackTrace()
                        ViewUtils.dismissDialog()
                    }
                }

            })
        }catch (e : Exception){
            e.printStackTrace()
            ViewUtils.dismissDialog()
        }


        }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(parent?.count == locationList.size){
            selectedCity =  cityKeyID?.get(position).toString()
        }else if(parent?.count == typeList.size){
            listingTypeId = typeKeyID?.get(position).toString()
        }
        else if(parent?.count == categoryList.size){
            selectedcategory.add(categoryKeyID?.get(position)!!.toInt())
        }

    }
}


