package com.dazzlebloom.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Paint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.dazzlebloom.R
import com.dazzlebloom.databinding.ActivityEditProfileBinding
import com.dazzlebloom.retrofit.ApiInterface
import com.dazzlebloom.retrofit.RetrofitHelper
import com.dazzlebloom.utiles.Constants
import com.dazzlebloom.utiles.ViewUtils
import com.dazzlebloom.utiles.customtypeface.CustomTypeface
import com.dazzlebloom.utiles.sheardpreference.AppSheardPreference
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import kotlin.random.Random

class EditProfileActivity : AppCompatActivity() {
    lateinit var activityEditProfilebinding : ActivityEditProfileBinding
    val PICK_IMAGE = 111
    private val EXTERNAL_STORAGE_PERMISSION_CODE = 23
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityEditProfilebinding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(activityEditProfilebinding.root)
        ViewUtils.changeStatusBarcolor(this!!,this?.resources!!.getColor(R.color.status_bar_color))
        activityEditProfilebinding.imgCross.setOnClickListener {
            finish()
        }

        activityEditProfilebinding.tvTupadte.setOnClickListener {
            checkApiForUpdateProfile()
        }
        activityEditProfilebinding.imgProfile.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectImage()
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

        activityEditProfilebinding.tvTupadte.setPaintFlags(activityEditProfilebinding.tvTupadte.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        activityEditProfilebinding.tvTupadte.setText("Update")
        activityEditProfilebinding.tvTupadte.typeface = CustomTypeface(this).ralewayMediumItalic
        activityEditProfilebinding.tvEditprofile.typeface = CustomTypeface(this).ralewaySemiBold
        activityEditProfilebinding.tvUname.typeface = CustomTypeface(this).ralewaySemiBold
        activityEditProfilebinding.etUname.typeface = CustomTypeface(this).ralewayRegular
        activityEditProfilebinding.tvFname.typeface = CustomTypeface(this).ralewaySemiBold
        activityEditProfilebinding.etFname.typeface = CustomTypeface(this).ralewayRegular
        activityEditProfilebinding.tvLname.typeface = CustomTypeface(this).ralewaySemiBold
        activityEditProfilebinding.etLname.typeface = CustomTypeface(this).ralewayRegular

        activityEditProfilebinding.tvEmail.typeface = CustomTypeface(this).ralewaySemiBold
        activityEditProfilebinding.etEmail.typeface = CustomTypeface(this).ralewayRegular
        activityEditProfilebinding.tvPhonenum.typeface = CustomTypeface(this).ralewaySemiBold
        activityEditProfilebinding.etPhonrnumbr.typeface = CustomTypeface(this).ralewayRegular
        //activityEditProfilebinding.tvChangepass.typeface = CustomTypeface(this).ralewaySemiBold
        //activityEditProfilebinding.etChangepass.typeface = CustomTypeface(this).ralewayRegular

        //setvalue()
        callApiforProfileget()
    }

    private fun callApiforProfileget() {
        ViewUtils.showDialog(this)
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        lifecycleScope.launchWhenCreated {
            val response = apiInterface.callApiforGetProfile(AppSheardPreference.fetchIntFromAppPreference(Constants.USERID))
           ViewUtils.dismissDialog()
             if(response.isSuccessful){
                 try{
                     val jsonobj : JSONObject = JSONObject(response.body()?.string())
                     val profile : JSONObject = jsonobj.optJSONObject("profile")
                     val username = profile.optString("username")
                     val email = profile.optString("email")
                     val first_name = profile.optString("first_name")
                     val last_name = profile.optString("last_name")
                     val user_phone = profile.optString("user_phone")
                     val avatar_id = profile.optString("avatar_id")
                     val nickname = profile.optString("nickname")
                     activityEditProfilebinding.etUname.setText(nickname)
                     activityEditProfilebinding.etFname.setText(first_name)
                     activityEditProfilebinding.etLname.setText(last_name)
                     activityEditProfilebinding.etEmail.setText(email)
                     activityEditProfilebinding.etPhonrnumbr.setText(user_phone)
                    /* Glide.with(this@EditProfileActivity)
                         .load(avatar_id)
                         .error(R.drawable.placeholder)
                         .apply(RequestOptions.bitmapTransform( RoundedCorners(10)))
                         .into(activityEditProfilebinding.imgProfile);*/
                     AppSheardPreference.storeStringAppPreference(this@EditProfileActivity,Constants.USERNAME, username)
                     AppSheardPreference.storeStringAppPreference(this@EditProfileActivity,Constants.FRISTNAME, first_name)
                     AppSheardPreference.storeStringAppPreference(this@EditProfileActivity,Constants.LASTNAME, last_name)

                     AppSheardPreference.storeStringAppPreference(this@EditProfileActivity,Constants.EMAIL, email)
                     AppSheardPreference.storeStringAppPreference(this@EditProfileActivity,Constants.PHONENUMBER, user_phone)

                     AppSheardPreference.storeStringAppPreference(this@EditProfileActivity,Constants.USER_URL, avatar_id)

                 }catch (e: Exception){
                     e.printStackTrace()
                 }
             }
        }
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
    }


    private fun checkApiForUpdateProfile() {
        ViewUtils.showDialog(this)
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        lifecycleScope.launchWhenCreated {
            val data: MutableMap<String, String> = HashMap()
            data.put("uid",AppSheardPreference.fetchIntFromAppPreference(Constants.USERID).toString())
            data.put("first_name", activityEditProfilebinding.etFname?.text.toString())
            data.put("last_name", activityEditProfilebinding.etLname?.text.toString())
            data.put("nickname", activityEditProfilebinding.etUname?.text.toString())
            data.put("user_phone", activityEditProfilebinding.etPhonrnumbr?.text.toString())
            data.put("user_whatsapp_number", activityEditProfilebinding.etPhonrnumbr?.text.toString())
            data.put("email", activityEditProfilebinding.etEmail?.text.toString())
            data.put("user_url", AppSheardPreference.fetchStringFromAppPreference(Constants.USER_URL))
            data.put("author_fb", "")
            data.put("author_tw", "")
            data.put("author_linkedin", "")
            data.put("author_pinterest", "")
            data.put("author_dribbble", "")
            data.put("author_ytube", "")
            data.put("author_vimeo", "")
            data.put("author_instagram", "")

            try{
                val response = apiInterface.callApiForUpdateProfile(data)
                ViewUtils.dismissDialog()
                if (response.isSuccessful){
                    ViewUtils.showdialog(this@EditProfileActivity,"Profile Updated")
                    callApiforProfileget()
                }
            }catch (e : Exception){
                e.printStackTrace()
                ViewUtils.dismissDialog()
            }
        }
    }


    private  fun setvalue(){
        activityEditProfilebinding.etUname.setText(AppSheardPreference.fetchStringFromAppPreference(Constants.USERNAME))
        activityEditProfilebinding.etFname.setText(AppSheardPreference.fetchStringFromAppPreference(Constants.FRISTNAME))
        activityEditProfilebinding.etEmail.setText(AppSheardPreference.fetchStringFromAppPreference(Constants.EMAIL))
        Glide.with(this)
            .load(AppSheardPreference.fetchStringFromAppPreference(Constants.USER_URL))
            .error(R.drawable.placeholder)
            .apply(RequestOptions.bitmapTransform( RoundedCorners(10)))
            .into(activityEditProfilebinding.imgProfile);


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            EXTERNAL_STORAGE_PERMISSION_CODE -> {
                selectImage()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE) {
            if (resultCode != Activity.RESULT_OK) {
                return
            }
            /*val uri = data?.data
            if (uri != null) {
                val imageFile = uriToImageFile(uri)
                callApiforImageUpload(imageFile)
            }*/
            if (data != null) {
                try {
                    val bm = MediaStore.Images.Media.getBitmap(
                        applicationContext.contentResolver,
                        data.data
                    )
                    val root = Environment.getExternalStorageDirectory().toString()
                    val myDir = File("$root/Dazzle/profile")
                    myDir.mkdirs()
                    val generator = Random
                    var n = 100
                    n = generator.nextInt(n)
                    val fname = "profile_" + n.toString() + ".jpg"
                    val file = File(myDir, fname)
                    val fo: FileOutputStream
                    if (file.exists())
                        file.delete()
                    try {
                        val out = FileOutputStream(file)
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, out)
                        out.flush()
                        out.close()
                        activityEditProfilebinding?.imgProfile?.setImageDrawable(null);
                        activityEditProfilebinding?.imgProfile?.setImageBitmap(bm)
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
}