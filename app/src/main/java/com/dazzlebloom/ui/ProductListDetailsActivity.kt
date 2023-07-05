package com.dazzlebloom.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import com.bumptech.glide.Glide
import com.dazzlebloom.R
import com.dazzlebloom.apiresponse.ProductData
import com.dazzlebloom.databinding.ActivityProductListDetailsBinding
import com.dazzlebloom.utiles.ViewUtils
import com.dazzlebloom.utiles.customtypeface.CustomTypeface
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.dazzlebloom.OnitemCLickIterface
import com.dazzlebloom.adapter.HomeProductAdapter
import com.dazzlebloom.adapter.ReleatedProductAdapter
import com.dazzlebloom.apiresponse.ReleatedProductData
import com.dazzlebloom.retrofit.ApiInterface
import com.dazzlebloom.retrofit.RetrofitHelper
import com.dazzlebloom.utiles.Constants
import com.dazzlebloom.utiles.sheardpreference.AppSheardPreference
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import org.json.JSONObject
import java.io.Serializable
import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.PersistableBundle

import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dazzlebloom.apiresponse.TagData
import com.dazzlebloom.ui.fragments.myaccount.MyAccountFragment
import com.dazzlebloom.ui.fragments.sell.SellFragment


class ProductListDetailsActivity : AppCompatActivity() {
    var activityProductListDetailsBinding: ActivityProductListDetailsBinding? = null
    var product: ProductData? = null
    var prodList: ArrayList<ProductData> = ArrayList()
    val MY_PERMISSIONS_REQUEST_CALL_PHONE = 222
    val taglist: ArrayList<TagData> = ArrayList()
    var releatedProductAdapter: ReleatedProductAdapter? = null
    var prodlist: ArrayList<ProductData> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityProductListDetailsBinding =
            ActivityProductListDetailsBinding.inflate(layoutInflater)
        setContentView(activityProductListDetailsBinding?.root)
        prodlist = AppSheardPreference.getProductList(this)
        activityProductListDetailsBinding?.imgBack?.setOnClickListener {
            finish()
        }
        activityProductListDetailsBinding?.imgMenu?.setOnClickListener {
           // MainActivity.getInstance()?.drawerLayout?.openDrawer(Gravity.LEFT)
            finish()
        }

        activityProductListDetailsBinding?.tvSubmitListing?.setOnClickListener {
            finish()
            MainActivity.getInstance()?.showsubmitlisting()

        }


        activityProductListDetailsBinding?.imgAccount?.setOnClickListener {
            finish()
            MainActivity.getInstance()?.showaccount()

        }
        product = intent.extras?.get("product") as ProductData

        activityProductListDetailsBinding?.imgShare?.setOnClickListener {

            val sharingIntent = Intent(Intent.ACTION_SEND)
            //val screenshotUri: Uri = Uri.parse(path)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(Intent.EXTRA_TEXT, product?.websitelink)
            startActivityForResult(Intent.createChooser(sharingIntent, "Share "),303)
        }

        activityProductListDetailsBinding?.btnContactSaller?.setOnClickListener {

            product?.ID?.let { it1 -> ShowAlertsendmessage(it1) }
        }

        activityProductListDetailsBinding?.btnAddFav?.setOnClickListener {
            //product?.ID?.let { it1 -> ShowAlertBid(it1) }
            if (!product?.phonenumber.equals("")) {
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:" + product?.phonenumber)
                //  startActivity(callIntent)
                if (ContextCompat.checkSelfPermission(
                        this, Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this, arrayOf(Manifest.permission.CALL_PHONE),
                        MY_PERMISSIONS_REQUEST_CALL_PHONE
                    )

                    // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                } else {
                    //You already have permission
                    try {
                        startActivity(callIntent)
                    } catch (e: SecurityException) {
                        e.printStackTrace()
                    }
                }
            } else
                ViewUtils.showdialog(this, "No phone number for seller")

        }

        activityProductListDetailsBinding?.imgFav?.setOnClickListener {




                if (!prodlist.contains(product)) {
                    activityProductListDetailsBinding?.imgFav?.setBackgroundResource(R.drawable.fav_red)
                    prodlist.add(product!!)
                    AppSheardPreference.saveProductList(this, prodlist)
                    Toast.makeText(this, "Product added in your favourite successfully", Toast.LENGTH_LONG).show()
                }else
                    Toast.makeText(this, "Product already added in your favourite list", Toast.LENGTH_LONG).show()


        }
        val layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        activityProductListDetailsBinding?.recReleatedProd?.layoutManager = layoutManager
        releatedProductAdapter =
            ReleatedProductAdapter(this, prodList!!, object : OnitemCLickIterface {
                override fun OnitemClick(prodData: ProductData) {
                    val intent = Intent(
                        this@ProductListDetailsActivity,
                        ProductListDetailsActivity::class.java
                    )
                    intent.putExtra("product", prodData as Serializable)
                    startActivity(intent)
                    finish()
                }
            })
        activityProductListDetailsBinding?.recReleatedProd?.adapter = releatedProductAdapter
        callApiforrelatedProduct()
        settypeface()
        setvalue()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    private fun callApiforrelatedProduct() {
        ViewUtils.showDialog(this)
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        lifecycleScope.launchWhenCreated {
            val response = apiInterface.callApiforreleatedProduct(product?.ID!!)
            ViewUtils.dismissDialog()
            if (response?.isSuccessful!!) {
                try {
                    val jsonobj: JSONObject = JSONObject(response.body()?.string())
                    val jsonArraylisting = jsonobj.getJSONArray("listings")
                    for (i in 0 until jsonArraylisting.length()) {
                        val objlist = jsonArraylisting.getJSONObject(i)

                        val ID = objlist.optInt("ID")
                        val title = objlist.optString("title")
                        val content = objlist.optString("post_content")
                        val status = objlist.optString("status")
                        val categories = objlist.optString("categories")
                        val displayPrice = objlist.optString("price")
                        val image = objlist.getJSONArray("images")
                        val location = objlist.optString("location")
                        val phone_number = objlist.optString("phone_number")
                        val website_link = objlist.optString("website_link")
                        val categori = objlist.optString("categories")
                        val imagedata: ArrayList<String> = ArrayList()
                        if (image.length() > 0) {
                            for (i in 0 until image.length()) {
                                imagedata.add((image.getString(i)))
                            }
                            val prodItem = ProductData(
                                ID,
                                title,
                                content,
                                location,
                                imagedata,
                                categories,
                                displayPrice.substring(1),
                                phone_number,
                                website_link,
                                categori,""
                            ,taglist)
                            prodList.add(prodItem)
                        } else {
                            val prodItem = ProductData(
                                ID,
                                title,
                                content,
                                location,
                                imagedata,
                                categories,
                                displayPrice.substring(1),
                                phone_number,
                                website_link,
                                categori,"", taglist)
                            prodList.add(prodItem)
                        }

                    }

                    releatedProductAdapter?.notifyDataSetChanged()
                    //  }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }

    private fun ShowAlertBid(listingID: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        var dialog = builder.create()
        val customLayout: View = LayoutInflater.from(this).inflate(R.layout.dialog_bid, null)
        builder.setView(customLayout)
        val tvwhydelete: AppCompatTextView = customLayout.findViewById(R.id.why_delete)
        val cancel: AppCompatTextView = customLayout.findViewById(R.id.tv_cancel)
        val etwriteHere: AppCompatEditText = customLayout.findViewById(R.id.et_typehere)
        val etbid: AppCompatEditText = customLayout.findViewById(R.id.et_bid)
        val btndelete: AppCompatButton = customLayout.findViewById(R.id.btn_delete)
        tvwhydelete.typeface = CustomTypeface(this).ralewaySemiBold
        etwriteHere.typeface = CustomTypeface(this).ralewayMedium
        btndelete.typeface = CustomTypeface(this).ralewaySemiBold
        etwriteHere.typeface = CustomTypeface(this).ralewayMedium
        cancel.typeface = CustomTypeface(this).ralewaySemiBold

        // tvwhydelete.text = "Give note to admin"
        // btndelete.text = " Send"

        btndelete.setOnClickListener {
            if (!etwriteHere.text.toString().equals("")) {
                if (!etbid.text?.equals("")!!) {
                    callApiSendoffer(listingID, etwriteHere.text.toString(), etbid?.text.toString())
                } else
                    ViewUtils.showdialog(this, "Please enter Bid")
            } else
                ViewUtils.showdialog(this, "Please enter Message")


        }
        cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }

    private fun ShowAlertsendmessage(listingID: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        var dialog = builder.create()
        val customLayout: View = LayoutInflater.from(this).inflate(R.layout.dialog_delete, null)
        builder.setView(customLayout)
        val tvwhydelete: AppCompatTextView = customLayout.findViewById(R.id.why_delete)
        val cancel: AppCompatTextView = customLayout.findViewById(R.id.tv_cancel)
        val etwriteHere: AppCompatEditText = customLayout.findViewById(R.id.et_typehere)
        val btndelete: AppCompatButton = customLayout.findViewById(R.id.btn_delete)
        tvwhydelete.typeface = CustomTypeface(this).ralewaySemiBold
        etwriteHere.typeface = CustomTypeface(this).ralewayMedium
        btndelete.typeface = CustomTypeface(this).ralewaySemiBold
        etwriteHere.typeface = CustomTypeface(this).ralewayMedium
        cancel.typeface = CustomTypeface(this).ralewaySemiBold

        tvwhydelete.text = "Send Message"
        btndelete.text = " Send"

        btndelete.setOnClickListener {
            if (!etwriteHere.text.toString().equals(""))
                callApiSendMessage(listingID, etwriteHere.text.toString(),dialog)
            else
                ViewUtils.showdialog(this, "Please enter Message")


        }
        cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }

    private fun callApiSendMessage(listingID: Int, message: String, dialog: AlertDialog) {

        ViewUtils.showDialog(this)
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        lifecycleScope.launchWhenCreated {
           /* val response = apiInterface.callApiforSendmessage(
                listingID,
                AppSheardPreference.fetchIntFromAppPreference(Constants.USERID),
                message,
                ""
            )*/
            val data: MutableMap<String, String> = HashMap()
            data.put("uid",AppSheardPreference.fetchIntFromAppPreference(Constants.USERID).toString())
            data.put("listing_id",listingID.toString())
            data.put("message",message)

            val response = apiInterface.callApiformessageSend(data)

            ViewUtils.dismissDialog()
            if (response.isSuccessful) {
                dialog.dismiss()
                ViewUtils.showdialog(this@ProductListDetailsActivity, "Message send")

            } else {
                ViewUtils.showdialog(this@ProductListDetailsActivity, "Something error, try later")
            }

        }
    }

    private fun callApiSendoffer(listingID: Int, message: String, bid: String) {

        ViewUtils.showDialog(this)
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        lifecycleScope.launchWhenCreated {
            val response = apiInterface.callApiforSendmessage(
                listingID,
                AppSheardPreference.fetchIntFromAppPreference(Constants.USERID),
                message,
                bid
            )
            ViewUtils.dismissDialog()
            if (response.isSuccessful) {
                ViewUtils.showdialog(this@ProductListDetailsActivity, "Offer send")

            } else {
                ViewUtils.showdialog(this@ProductListDetailsActivity, "Something error, try later")
            }

        }
    }

    private fun setvalue() {

        if (prodlist.contains(product)) {
            activityProductListDetailsBinding?.imgFav?.setBackgroundResource(R.drawable.fav_red)
        }
      //  else
          //  activityProductListDetailsBinding?.imgFav?.setColorFilter(resources.getColor(R.color.grey));

        val mimeType = "text/html"
        val encoding = "UTF-8"
        activityProductListDetailsBinding?.tvProdTitle?.text = product?.post_name
       // activityProductListDetailsBinding?.tvProdDesc?.text = product?.post_content
        product?.post_content?.let { activityProductListDetailsBinding?.webPostcontent?.loadDataWithBaseURL(null, it, mimeType, encoding, null) };
        activityProductListDetailsBinding?.tvProdPrice?.text = "£ " + product?.displayPrice
        activityProductListDetailsBinding?.tvLocation?.text = product?.locatioin
        /*activityProductListDetailsBinding?.imgDetails?.let {
            Glide.with(this)
                .load(product?.post_image)
                .placeholder(R.drawable.placeholder)
                .into(it)
        };*/


        val imageList = ArrayList<SlideModel>() // Create image list

// imageList.add(SlideModel("String Url" or R.drawable)
// imageList.add(SlideModel("String Url" or R.drawable, "title") You can add title
        for (i in 0 until product?.post_image!!.size) {
            imageList.add(SlideModel(product?.post_image?.get(i), "", ScaleTypes.CENTER_INSIDE))
        }

        // imageList.add(SlideModel("https://bit.ly/2YoJ77H", "The animal population decreased by 58 percent in 42 years."))
        // imageList.add(SlideModel("https://bit.ly/2BteuF2", "Elephants and tigers may become extinct."))
        // imageList.add(SlideModel("https://bit.ly/3fLJf72", "And people do that."))
        activityProductListDetailsBinding?.imageSlider?.setImageList(imageList,ScaleTypes.CENTER_INSIDE)
        activityProductListDetailsBinding?.imageSlider?.setBackgroundColor(resources.getColor(R.color.white))
        activityProductListDetailsBinding?.tvProdDesc?.setMovementMethod(ScrollingMovementMethod())

    }

    private fun settypeface() {
        activityProductListDetailsBinding?.tvDazzle?.typeface = CustomTypeface(this).ralewayRegular
        activityProductListDetailsBinding?.tvSubmitListing?.typeface =
            CustomTypeface(this).ralewayMedium
        activityProductListDetailsBinding?.tvProdTitle?.typeface = CustomTypeface(this).ralewayBold
        activityProductListDetailsBinding?.tvProdPrice?.typeface =
            CustomTypeface(this).ralewayRegular
        activityProductListDetailsBinding?.tvDazzle?.typeface = CustomTypeface(this).ralewayRegular
        activityProductListDetailsBinding?.tvDes?.typeface = CustomTypeface(this).ralewaySemiBold
        activityProductListDetailsBinding?.tvProdDesc?.typeface =
            CustomTypeface(this).ralewayRegular
        activityProductListDetailsBinding?.btnContactSaller?.typeface = CustomTypeface(this).ralewaySemiBold
        activityProductListDetailsBinding?.tvLocation?.typeface = CustomTypeface(this).ralewaySemiBold
        activityProductListDetailsBinding?.btnAddFav?.typeface = CustomTypeface(this).ralewaySemiBold
        activityProductListDetailsBinding?.tvRocomnded?.typeface = CustomTypeface(this).ralewayBold
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults.size > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:" + product?.phonenumber)
                  startActivity(callIntent)
            }
        }
        return
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==303){

        }
    }

}