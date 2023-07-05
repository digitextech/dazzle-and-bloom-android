package com.dazzlebloom.ui.fragments.home

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dazzlebloom.OnitemCLickIterface

import com.dazzlebloom.adapter.HomeProductAdapter

import com.dazzlebloom.apiresponse.ProductData

import com.dazzlebloom.retrofit.ApiInterface
import com.dazzlebloom.retrofit.RetrofitHelper
import com.dazzlebloom.ui.ProductListDetailsActivity
import com.dazzlebloom.utiles.ViewUtils
import com.dazzlebloom.utiles.customtypeface.CustomTypeface
import org.json.JSONArray
import org.json.JSONObject
import java.io.Serializable
import androidx.appcompat.widget.PopupMenu
import com.dazzlebloom.databinding.FragmentHomeBinding
import kotlin.collections.ArrayList
import android.view.inputmethod.EditorInfo
import android.widget.ProgressBar

import android.widget.TextView.OnEditorActionListener
import androidx.core.view.isVisible
import com.dazzlebloom.R
import com.dazzlebloom.apiresponse.TagData
import com.dazzlebloom.ui.MainActivity
import com.dazzlebloom.utiles.customview.CustomProgressDialog
import com.dazzlebloom.utiles.customview.FilterFullScreenDialog


class HomeFragment(val mainActivity: MainActivity) : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
     var _binding: FragmentHomeBinding? = null
    var pageNumber  : Int = 1
    var prodList : ArrayList<ProductData> = ArrayList()
    var groduct_grid : GridView ? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    var selectedCatID : Int = 0
    var searchstr = ""
    private val binding get() = _binding!!
    var homeProductAdapter : HomeProductAdapter ?= null
    var recProd : RecyclerView ?= null
    var totalItem = 0
    var totalPage = 0
    var Perpage = 20
    var isloading = false
    var ordered = "ASC"
    var searchString = ""
    var orderBy = ""
    var isnothome = false
    var minvalue = 0
    var maxvalue = 10000
    private val isLastPage = false
     var isLoader = false
    var selectedlocationArraylist : ArrayList<Int> = ArrayList()
    val taglist: ArrayList<TagData> = ArrayList()
    var locationlistPosition: ArrayList<Int> = ArrayList()
    var progressbar : ProgressBar ?= null
    var locationList : ArrayList<String> = ArrayList()
    var cityKeyID : ArrayList<String> = ArrayList()
    var location = arrayOf<Int>()
    val typefaceregular = CustomTypeface(mainActivity).ralewayRegular
    val typefaSemibold = CustomTypeface(mainActivity).ralewaySemiBold
    // var totalitem =0
  // private val progressDialog by lazy { CustomProgressDialog(mainActivity) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
       // progressDialog.showdialo()



       //  groduct_grid = root.findViewById(R.id.grid_product)
        recProd = root.findViewById(R.id.rec_prod)
        progressbar = root.findViewById(R.id.progress)
        val layoutManager = GridLayoutManager(mainActivity, 2)
        recProd?.layoutManager = layoutManager
        recProd?.isNestedScrollingEnabled = false
        recProd?.setHasFixedSize(true)
        homeProductAdapter = HomeProductAdapter(this,mainActivity,prodList,object :OnitemCLickIterface{
            override fun OnitemClick(prodData: ProductData) {
                val intent = Intent(context, ProductListDetailsActivity::class.java)
                intent.putExtra("product", prodData as Serializable)
                context?.startActivity(intent)
            }
        })
        recProd?.adapter = homeProductAdapter
        recProd?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val  totalItemCount = layoutManager.getItemCount()
                val  lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                if ( totalItemCount <= lastVisibleItem + 1 && !isloading && prodList.size < totalItem ){
                    //progressbar?.visibility = View.VISIBLE

                    isLoader = true
                    val imagedata: ArrayList<String> = ArrayList()
                    val tagdata: ArrayList<TagData> = ArrayList()
                    prodList.add(ProductData(0,"0","0","0",imagedata,"0","0","0","0","","",tagdata))
                    homeProductAdapter?.notifyItemInserted(prodList.size - 1)
                    titleFilter(ordered)
                   // progressbar?.visibility = View.VISIBLE

                   //
                    //End of the items
                   // if (onLoadMoreListener != null) {
                       // onLoadMoreListener.LoadItems()
                  // }
                }

            }
        })


      //  callApiforProduct()
        prodList.clear()
       // titleFilter(ordered)
        callApiwhenpageload()

        ViewUtils.changeStatusBarcolor(activity!!,activity?.resources!!.getColor(R.color.white))
        val imag_menu = root.findViewById<ImageView>(R.id.img_menu)

        imag_menu.setOnClickListener {
            mainActivity.drawerLayout.openDrawer(Gravity.START);
        }
        /*_binding!!.btnFliter.setOnClickListener {

           val view = layoutInflater.inflate(R.layout.filter_popup_layout, null, false)

            val popupWindow = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
            val filterFullScreenDialog = FilterFullScreenDialog(mainActivity,this)
            filterFullScreenDialog.show(mainActivity.supportFragmentManager,"filterfragment")
        }*/
        _binding!!.btnSort.setOnClickListener {
            val popup = PopupMenu(mainActivity, _binding!!.btnSort)
            popup.getMenuInflater().inflate(R.menu.main, popup.getMenu());
            popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {

                override fun onMenuItemClick(item: android.view.MenuItem?): Boolean {
                    isnothome = true
                    _binding?.tvTotalProd?.visibility = View.VISIBLE
                    if(item?.title?.equals("Price(Low to High)")!!){
                        prodList.clear()
                        homeProductAdapter?.notifyDataSetChanged()
                        _binding?.tvTotalProd?.text = prodList.size.toString() + " Results"

                       // selectedCatID = 0
                        pageNumber = 1
                        orderBy = "price"
                        ordered = "ASC"
                        searchString = ""
                        isnothome = true
                        //minvalue = 0
                        //maxvalue = 4999
                        isLoader = false
                        priceFilter("ASC")
                        popup.dismiss()
                        return true

                    }else if (item?.title?.equals("Price(High to Low)")!!){
                        prodList.clear()
                        homeProductAdapter?.notifyDataSetChanged()
                        _binding?.tvTotalProd?.text = prodList.size.toString() + " Results"

                        //selectedCatID = 0
                        isLoader = false
                        pageNumber = 1
                        orderBy = "price"
                        ordered = "DESC"
                        searchString = ""
                        isnothome = true
                       // minvalue = 0
                        //maxvalue = 4999
                        priceFilter("DESC")
                        popup.dismiss()
                        return true
                    }else if(item?.title?.equals("A-Z")!!){
                        prodList.clear()
                        homeProductAdapter?.notifyDataSetChanged()
                        _binding?.tvTotalProd?.text = prodList.size.toString() + " Results"

                       // selectedCatID = 0
                        pageNumber = 1
                        orderBy = "title"
                        ordered = "ASC"
                        searchString = ""
                        isnothome = true
                        //minvalue = 0
                        //maxvalue = 4999
                        isLoader = false
                        titleFilter("ASC")
                        popup.dismiss()
                        return true
                    }else if (item?.title?.equals("Z-A")!!) {
                        prodList.clear()
                        homeProductAdapter?.notifyDataSetChanged()
                        _binding?.tvTotalProd?.text = prodList.size.toString() + " Results"

                       // selectedCatID = 0
                        pageNumber = 1
                        orderBy = "title"
                        ordered = "DESC"
                        searchString = ""
                       // minvalue = 0
                        //maxvalue = 4999
                        isnothome = true
                        isLoader = false
                        titleFilter("DESC")
                        popup.dismiss()
                        return true
                    }
                    return true
                }
            })
            popup.show();
        }
        val filterFullScreenDialog = FilterFullScreenDialog(mainActivity,this)
        _binding!!.btnFliter.setOnClickListener {
         //   val filterFullScreenDialog = FilterFullScreenDialog(mainActivity,this)
            // if(filterFullScreenDialog.isHidden)
            try {
                if ( !filterFullScreenDialog.isVisible)
                 filterFullScreenDialog.show(mainActivity.supportFragmentManager, "filterfragment")
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
        settypeface()

        _binding?.etSearch?.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                prodList.clear()
                homeProductAdapter?.notifyDataSetChanged()
                _binding?.tvTotalProd?.text = prodList.size.toString() + " Dresses"

                selectedCatID = 0
                pageNumber = 1
                orderBy = "title"
                ordered = "ASC"
                searchString = binding?.etSearch?.text.toString()
                callApiforSearch(_binding?.etSearch?.text.toString())
                true
            } else false
        })

        return root
    }

    private fun callApiforSearch(str: String) {
        _binding?.tvTotalProd?.visibility = View.VISIBLE
        isloading = true
        selectedCatID = 0
        ViewUtils.showDialog(activity!!)
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        activity?.lifecycleScope?.launchWhenCreated {
            val response = apiInterface.callApiforFilter(selectedCatID, pageNumber,ordered, searchString, Perpage,orderBy, minvalue, maxvalue)
           // prodList.clear()

            if(response.isSuccessful){
                try {
                    val jsonobj : JSONObject = JSONObject(response.body()?.string())
                    val jsonArraylisting = jsonobj.getJSONArray("listings")
                    totalItem = jsonobj.optInt("total_listing")
                    totalPage = jsonobj.optInt("total_pages")
                    for (i in 0 until jsonArraylisting.length()) {
                        val objlist = jsonArraylisting.getJSONObject(i)

                        val ID =  objlist.optInt("ID")
                        val title =  objlist.optString("title")
                        val content = objlist.optString("content")
                        val status = objlist.optString("status")
                        val categories = objlist.optString("categories")
                        val displayPrice = objlist.optString("price")
                        val imgarrary: Any = objlist.get("images")
                        var image = JSONArray()
                        if (imgarrary is JSONArray)
                            image = objlist.optJSONArray("images")
                        val location = objlist.optString("location")
                        val phone_number = objlist.optString("phone_number")
                        val website_link = objlist.optString("website_link")
                        val categori = objlist.optString("categories")
                        val imagedata: ArrayList<String> = ArrayList()

                        if(image.length()>0) {
                            for ( i in 0 until  image.length()){
                                imagedata.add(image.getString(i))
                            }
                            val prodItem = ProductData(
                                ID, title, content, location,
                                imagedata, categories, displayPrice.substring(1),phone_number,website_link,categori, status, taglist)
                            prodList.add(prodItem)
                        }else{
                            val prodItem = ProductData(
                                ID, title, content, location,
                                imagedata, categories, displayPrice.substring(1),phone_number,website_link,categori, status, taglist)
                            prodList.add(prodItem)
                        }

                    }
                    _binding?.tvTotalProd?.text = "showing "+prodList.size.toString() + " results of "+ searchString
                    homeProductAdapter?.notifyDataSetChanged()
                    ViewUtils.dismissDialog()

                    if(totalPage> pageNumber)
                        pageNumber++
                    isloading = false

                    //  }
                }catch (e :Exception){
                    e.printStackTrace()
                }
            }

        }
    }

    private fun callApiwhenpageload() {
       // isloading = true
        ViewUtils.showDialog(activity!!)
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        activity?.lifecycleScope?.launchWhenCreated {
            val response = apiInterface.callApiforFilter(selectedCatID, pageNumber,ordered, searchString, Perpage,orderBy, minvalue, maxvalue)
            //prodList.clear()

            if(response.isSuccessful){
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
                        var image = JSONArray()
                        if (imgarrary is JSONArray)
                            image = objlist.optJSONArray("images")
                        val location = objlist.optString("location")
                        val phone_number = objlist.optString("phone_number")
                        val website_link = objlist.optString("website_link")
                        val categori = objlist.optString("categories")

                        val imagedata: ArrayList<String> = ArrayList()
                        if(image.length()>0) {
                            for (i in 0 until image.length()){
                                imagedata.add((image.getString(i)))
                            }
                            val prodItem = ProductData(
                                ID, title, content, location,
                                imagedata, categories, displayPrice.substring(1),phone_number,website_link,categori, status, taglist)
                            prodList.add(prodItem)
                        }else{
                            val prodItem = ProductData(
                                ID, title, content, location,
                                imagedata, categories, displayPrice.substring(1),phone_number,website_link, categori, status, taglist)
                            prodList.add(prodItem)
                        }
                        //ViewUtils.dismissDialog()

                    }
                    //isLoader = false
                    homeProductAdapter?.notifyDataSetChanged()
                   // _binding?.tvTotalProd?.text = totalItem.toString() + " Dresses"
                    if(totalPage > pageNumber)
                        pageNumber++
                    isloading = false
                    ViewUtils.dismissDialog()
                    //  }
                }catch (e :Exception){
                    e.printStackTrace()
                    ViewUtils.dismissDialog()
                }
            }

        }
    }

    private fun titleFilter(filterorder: String) {
        isloading = true
       // homeProductAdapter?.notifyDataSetChanged()
        if(isnothome)
        ViewUtils.showDialog(activity!!)
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        activity?.lifecycleScope?.launchWhenCreated {
            val response = apiInterface.callApiforFilterPricelocation(selectedCatID, pageNumber,ordered, searchString, Perpage,orderBy, minvalue, maxvalue, location)
            //prodList.clear()
         //   ViewUtils.dismissDialog()
            if(response.isSuccessful){
                try {
                    val jsonobj : JSONObject = JSONObject(response.body()?.string())
                    val jsonArraylisting = jsonobj.getJSONArray("listings")
                    totalItem = jsonobj.optInt("total_listing")
                    totalPage = jsonobj.optInt("total_pages")
                    isLoader = false
                    if (prodList.size >0) {
                        val position = prodList.size - 1
                        val item = prodList.get(position)
                        if (item != null) {
                            prodList?.removeAt(position)
                            homeProductAdapter?.notifyItemRemoved(position)
                        }
                    }
                    for (i in 0 until jsonArraylisting.length()) {
                        val objlist = jsonArraylisting.getJSONObject(i)

                        val ID =  objlist.optInt("ID")
                        val title =  objlist.optString("title")
                        val content = objlist.optString("post_content")
                        val status = objlist.optString("status")
                        val categories = objlist.optString("categories")
                        val displayPrice = objlist.optString("price")
                        val imgarrary: Any = objlist.get("images")
                        var image = JSONArray()
                        if (imgarrary is JSONArray)
                            image = objlist.optJSONArray("images")
                        val location = objlist.optString("location")
                        val phone_number = objlist.optString("phone_number")
                        val website_link = objlist.optString("website_link")
                        val categori = objlist.optString("categories")

                        val imagedata: ArrayList<String> = ArrayList()
                        if(image.length()>0) {
                            for (i in 0 until image.length()){
                                imagedata.add((image.getString(i)))
                            }
                            val prodItem = ProductData(
                                ID, title, content, location,
                                imagedata, categories, displayPrice.substring(1),phone_number,website_link,categori, status, taglist)
                            prodList.add(prodItem)
                        }else{
                            val prodItem = ProductData(
                                ID, title, content, location,
                                imagedata, categories, displayPrice.substring(1),phone_number,website_link, categori, status, taglist)
                            prodList.add(prodItem)
                        }

                    }


                    homeProductAdapter?.notifyDataSetChanged()
                  //  if(progressbar?.visibility == View.VISIBLE)
                       // progressbar?.visibility = View.GONE

                    _binding?.tvTotalProd?.text = "Showing "+prodList.size.toString() + " results of "+ totalItem
                    if(totalPage > pageNumber)
                        pageNumber++
                    isloading = false
                    isnothome = false
                    ViewUtils.dismissDialog()
                    //if(progressbar?.visibility == View.VISIBLE)
                      //  progressbar?.visibility = View.GONE
                    //  }
                }catch (e :Exception){
                    e.printStackTrace()
                }
            }

        }
    }

    private  fun callApiforProduct(){
        isloading = false
        ViewUtils.showDialog(activity!!)
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        activity?.lifecycleScope?.launchWhenCreated {
            val response = apiInterface.getProductListinHomePage(20,pageNumber)
            ViewUtils.dismissDialog()
            if(response.isSuccessful){
                try {
                    val cookie = response.headers()["Access-Control-Expose-Headers"]
                    val jsonobj : JSONObject = JSONObject(response.body()?.string())
                    val jsonArrayMessage = jsonobj.getJSONArray("message")
                    for (i in 0 until jsonArrayMessage.length()) {
                        val objlist = jsonArrayMessage.getJSONObject(i)
                        val locationJsonArray = objlist.getJSONArray("locations")
                        val logo_image =  objlist.getString("logo_image")
                        val ImageObj =  objlist.getJSONObject("images")
                        val feildObj = objlist.getJSONObject("fields")
                        val feildObjNine = feildObj.getJSONObject("9")
                        val dataObj = feildObjNine.getJSONObject("data")
                        val displayPrice = dataObj.optString("value")
                        val imageUrl = ImageObj.getJSONObject(logo_image).getString("guid")
                        val categoryArray : JSONArray = objlist.getJSONArray("categories")

                        val imagedata: ArrayList<String> = ArrayList()
                        imagedata.add(imageUrl)

                        if (categoryArray.length()>0) {
                            val catjson: JSONObject = categoryArray.getJSONObject(0)
                            val prodItem = ProductData(objlist.getInt("ID"), objlist.getString("post_name"),
                                objlist.getString("post_content"),locationJsonArray.getJSONObject(0).getString("name"),
                                imagedata, catjson.getString("name"), displayPrice,"","","", "", taglist)
                            prodList.add(prodItem)
                        }else{
                            val prodItem = ProductData(objlist.getInt("ID"), objlist.getString("post_name"),
                                objlist.getString("post_content"),locationJsonArray.getJSONObject(0).getString("name"),
                                imagedata, "", displayPrice,"","","","", taglist)
                            prodList.add(prodItem)
                        }

                    }

                        homeProductAdapter?.notifyDataSetChanged()
                        _binding?.tvTotalProd?.text = prodList.size.toString() + " results"

                    if(totalPage> pageNumber)
                        pageNumber++
                    isloading = false
                    isnothome = false
                    ViewUtils.dismissDialog()
                  //  }
                }catch (e :Exception){
                    e.printStackTrace()
                }

            }

        }
    }
    public  fun showMenuFilter(Catid: Int, filtername: String){
        _binding?.tvTotalProd?.visibility = View.VISIBLE
        isloading = true
        val filterMenuList: ArrayList<ProductData> =  ArrayList()
        selectedCatID = Catid
        if (isnothome)
        ViewUtils.showDialog(activity!!)
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        activity?.lifecycleScope?.launchWhenCreated {
            val response = apiInterface.callApiforFilter(selectedCatID, pageNumber,ordered, searchString, Perpage,orderBy, minvalue, maxvalue)
          //  prodList.clear()
           // ViewUtils.dismissDialog()
            if(response.isSuccessful){
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
                        var image = JSONArray()
                        if (imgarrary is JSONArray)
                          image = objlist.optJSONArray("images")

                        val location = objlist.optString("location")
                        val phone_number = objlist.optString("phone_number")
                        val website_link = objlist.optString("website_link")
                        val categori = objlist.optString("categories")
                        val imagedata: ArrayList<String> = ArrayList()
                        if(image.length()>0) {
                            for( i in 0 until image.length()){
                                imagedata.add(image.getString(i))
                            }
                            val prodItem = ProductData(
                                ID, title, content, location,
                                imagedata, categories, displayPrice.substring(1),phone_number,website_link,categori,"", taglist)
                            prodList.add(prodItem)
                        }else{
                            val prodItem = ProductData(
                                ID, title, content, location,
                               imagedata, categories, displayPrice.substring(1),phone_number, website_link,categori,"", taglist)
                            prodList.add(prodItem)
                        }

                    }

                    homeProductAdapter?.notifyDataSetChanged()
                    _binding?.tvTotalProd?.text = "Showing "+prodList.size.toString() + " results of "+ totalItem.toString()

                    if(totalPage> pageNumber)
                        pageNumber++
                    isloading = false
                    isnothome = false
                    ViewUtils.dismissDialog()
                    //  }
                }catch (e :Exception){
                    e.printStackTrace()
                }
            }

        }


    }
    public  fun priceFilter( filterorder: String){
        isloading = true
        val filterMenuList: ArrayList<ProductData> =  ArrayList()
        if(isnothome)
        ViewUtils.showDialog(activity!!)
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        activity?.lifecycleScope?.launchWhenCreated {
            val response = apiInterface.callApiforFilter(selectedCatID, pageNumber,filterorder, searchString, Perpage,orderBy, minvalue, maxvalue)
           // prodList.clear()
            //ViewUtils.dismissDialog()
            if(response.isSuccessful){
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
                        var image = JSONArray()
                        if (imgarrary is JSONArray)
                            image = objlist.optJSONArray("images")
                        val location = objlist.optString("location")
                        val phone_number = objlist.optString("phone_number")
                        val website_link = objlist.optString("website_link")
                        val categori = objlist.optString("categories")
                        val imagedata: ArrayList<String> = ArrayList()

                        if(image.length()>0) {
                            for (i in 0 until image.length()){
                                imagedata.add(image.getString(i))
                            }
                            val prodItem = ProductData(
                                ID, title, content, location,
                                imagedata, categories, displayPrice.substring(1),phone_number,website_link,categori,"",taglist)
                            prodList.add(prodItem)
                        }else{
                            val prodItem = ProductData(
                                ID, title, content, location,
                                imagedata, categories, displayPrice.substring(1),phone_number, website_link,categori,"",taglist)
                            prodList.add(prodItem)
                        }

                    }

                    homeProductAdapter?.notifyDataSetChanged()
                   // _binding?.tvTotalProd?.text = prodList.size.toString() + " Dresses"
                    _binding?.tvTotalProd?.text = "showing "+prodList.size.toString() + " results of "+ totalItem.toString()

                    if(totalPage> pageNumber)
                        pageNumber++
                    isloading = false
                    isnothome = false
                    ViewUtils.dismissDialog()
                    //  }
                }catch (e :Exception){
                    e.printStackTrace()
                    ViewUtils.dismissDialog()
                }
            }

        }


    }

    public fun callApiforfilterpricelocation( minvalues: Int, maxvalues: Int,  selectedlocationArray: ArrayList<Int>) {
        location  =  arrayOf<Int>()
          location = selectedlocationArray.map { i: Int? -> i ?: 0 }.toTypedArray()
        _binding?.tvTotalProd?.visibility = View.VISIBLE
        _binding?.tvTotalProd?.text = "0 Items"
        prodList.clear()
        homeProductAdapter?.notifyDataSetChanged()
        totalItem = 0
         totalPage = 0
         Perpage = 20
         isloading = false
         ordered = "ASC"
         searchString = ""
         orderBy = ""
         isloading = true
         maxvalue = maxvalues
        minvalue = minvalues
        selectedlocationArraylist = selectedlocationArray
        ViewUtils.showDialog(activity!!)
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        activity?.lifecycleScope?.launchWhenCreated {
            val response = apiInterface.callApiforFilterPricelocation(selectedCatID, pageNumber,ordered, searchString, Perpage,orderBy, minvalues, maxvalues, location!!)


            if(response.isSuccessful){
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
                        var image = JSONArray()
                        if (imgarrary is JSONArray)
                            image = objlist.optJSONArray("images")
                        val location = objlist.optString("location")
                        val phone_number = objlist.optString("phone_number")
                        val website_link = objlist.optString("website_link")
                        val categori = objlist.optString("categories")

                        val imagedata: ArrayList<String> = ArrayList()
                        if(image.length()>0) {
                            for (i in 0 until image.length()){
                                imagedata.add((image.getString(i)))
                            }
                            val prodItem = ProductData(
                                ID, title, content, location,
                                imagedata, categories, displayPrice.substring(1),phone_number,website_link,categori, status, taglist)
                            prodList.add(prodItem)
                        }else{
                            val prodItem = ProductData(
                                ID, title, content, location,
                                imagedata, categories, displayPrice.substring(1),phone_number,website_link, categori, status, taglist)
                            prodList.add(prodItem)
                        }
                        //ViewUtils.dismissDialog()

                    }

                    homeProductAdapter?.notifyDataSetChanged()
                    // _binding?.tvTotalProd?.text = totalItem.toString() + " Dresses"
                    if(totalPage > pageNumber)
                        pageNumber++
                    isloading = false
                    _binding?.tvTotalProd?.text = "showing "+prodList.size.toString() + " results of "+ totalItem.toString()
                    ViewUtils.dismissDialog()
                    //  }
                }catch (e :Exception){
                    e.printStackTrace()
                    ViewUtils.dismissDialog()
                }
            }

        }
    }

    private fun settypeface() {
        _binding?.etSearch?.typeface =  CustomTypeface(mainActivity).ralewayRegular
        _binding?.tvDazzle?.typeface =  CustomTypeface(mainActivity).ralewayMedium
        _binding?.btnFliter?.typeface =  CustomTypeface(mainActivity).ralewayMedium
        _binding?.btnSort?.typeface =  CustomTypeface(mainActivity).ralewayMedium
        _binding?.tvTotalProd?.typeface =  CustomTypeface(mainActivity).ralewaySemiBold
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}