package com.dazzlebloom.utiles.customview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dazzlebloom.R
import com.dazzlebloom.adapter.FilterLocationAdapter
import com.dazzlebloom.retrofit.ApiInterface
import com.dazzlebloom.retrofit.RetrofitHelper
import com.dazzlebloom.ui.MainActivity
import com.dazzlebloom.ui.fragments.home.HomeFragment
import com.dazzlebloom.utiles.customtypeface.CustomTypeface
import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar

import org.json.JSONObject
import com.dazzlebloom.utiles.ViewUtils


class FilterFullScreenDialog(val mainActivity: MainActivity, homeFragment: HomeFragment) : DialogFragment() {

    var selectedlocationArray : ArrayList<Int> = ArrayList()
    var reclocation : RecyclerView ?= null
    var maxvalues = 10000
    var minvalues = 0
    val homeFragment = homeFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
         super.onCreateView(inflater, container, savedInstanceState)

        val view: View = inflater.inflate(R.layout.filter_popup_layout, container, false)
        reclocation = view.findViewById(R.id.reclocation)
        val filter = view.findViewById<AppCompatTextView>(R.id.tvfilter)
        val tvupdate = view.findViewById<AppCompatTextView>(R.id.tv_update)
        val tvprice = view.findViewById<AppCompatTextView>(R.id.tvprice)
        val location = view.findViewById<AppCompatTextView>(R.id.tvlocation)
        val imgcross = view.findViewById<AppCompatImageView>(R.id.img_cross)
        val btn_update : AppCompatButton= view.findViewById(R.id.btn_update)
        val tvmin : TextView = view.findViewById(R.id.textMin1)
        val tvMax : TextView = view.findViewById(R.id.textMax1)
        tvmin.typeface = CustomTypeface(mainActivity).ralewayMedium
        tvMax.typeface = CustomTypeface(mainActivity).ralewayMedium
        if(homeFragment.locationList.size == 0)
          callApiForLocation()
        else{
            selectedlocationArray.clear()
            val layoutManager = LinearLayoutManager(mainActivity)
            reclocation?.layoutManager = layoutManager
            val filterLocationAdapter = FilterLocationAdapter(mainActivity,homeFragment.locationList, this@FilterFullScreenDialog,homeFragment)
            reclocation?.adapter = filterLocationAdapter
        }
         val rangeSeekbar : RangeSeekBar<Int> = view.findViewById(R.id.rangeseekbar)

        // rangeSeekbar.setRangeValues(homeFragment.minvalue, homeFragment.maxvalue);

          tvmin.text = homeFragment.minvalue.toString()
          tvMax.text = homeFragment.maxvalue.toString()
         rangeSeekbar.isNotifyWhileDragging = true
          rangeSeekbar.setRangeValues(0, 10000);
        // rangeSeekbar.setRangeValues(minvalues, maxvalues);
        // rangeSeekbar.setRangeValues(200,500)
        // rangeSeekbar.isNotifyWhileDragging = true

       /* rangeseekbar1.minValue = 100f
        rangeseekbar1.maxValue = 2000f*/
        rangeSeekbar.setOnRangeSeekBarChangeListener { bar, minValue, maxValue ->
           // Toast.makeText(activity, minValue.toString()  + "-" + maxValue.toString(), Toast.LENGTH_LONG).show();
            maxvalues= maxValue
            minvalues = minValue
            tvmin.text = minValue.toString()
            tvMax.text = maxValue.toString()
        }

       /* double_range_seekbar.setOnRangeSeekBarViewChangeListener(object : OnDoubleValueSeekBarChangeListener {
            override fun onValueChanged(seekBar: DoubleValueSeekBarView?, min: Int, max: Int, fromUser: Boolean) {
                maxvalues= max
                minvalues = min
                homeFragment.minvalue = min
                homeFragment.maxvalue = max
            }

            override fun onStartTrackingTouch(seekBar: DoubleValueSeekBarView?, min: Int, max: Int) {
            }

            override fun onStopTrackingTouch(seekBar: DoubleValueSeekBarView?, min: Int, max: Int) {
            }
        })*/
        /* val seekBar = RangeSeekBar<Int>(activity)
         seekBar.setRangeValues(0, 4900);
         seekBar.setOnRangeSeekBarChangeListener { bar, minValue, maxValue ->
             Toast.makeText(activity, minValue.toString()  + "-" + maxValue.toString(), Toast.LENGTH_LONG).show();
             maxvalues= maxValue
             minvalues = minValue
         }
         seekBar.setNotifyWhileDragging(true);
         val seeklayout = view.findViewById<LinearLayout>(R.id.seekbarLayout)
         seeklayout.addView(seekBar)*/
       /*val crystalRangeSeekbar : CrystalRangeSeekbar = view.findViewById(R.id.PriceRangeSeekbar)

        // set listener
        // set listener
        crystalRangeSeekbar.setOnRangeSeekbarChangeListener(OnRangeSeekbarChangeListener { minValue, maxValue ->
            tvmin.setText(minValue.toString())
            tvMax.setText(maxValue.toString())
            maxvalues= maxValue?.toInt()!!
            minvalues = minValue?.toInt()!!
        })*/
        imgcross.setOnClickListener {
           // selectedlocationArray.clear()
            homeFragment.minvalue = minvalues
            homeFragment.maxvalue = maxvalues
            dismiss()
        }
        btn_update.setOnClickListener {

            homeFragment.callApiforfilterpricelocation(minvalues,maxvalues,selectedlocationArray)
            homeFragment.minvalue = minvalues
            homeFragment.maxvalue = maxvalues
            dismiss()
           // dismiss()
           // dialog?.hide()
        }

        tvupdate.setOnClickListener {
            selectedlocationArray.clear()
            homeFragment.locationlistPosition.clear()
            val filterLocationAdapter = FilterLocationAdapter(mainActivity,homeFragment.locationList, this@FilterFullScreenDialog,homeFragment)
            reclocation?.adapter = filterLocationAdapter
            minvalues = 0
            maxvalues = 10000
            homeFragment.minvalue = minvalues
            homeFragment.maxvalue = maxvalues
            tvmin.text = homeFragment.minvalue.toString()
            tvMax.text = homeFragment.maxvalue.toString()

            rangeSeekbar.setRangeValues(0, 10000);
            rangeSeekbar.selectedMinValue = 0
            rangeSeekbar.selectedMaxValue = 10000
            rangeSeekbar.isNotifyWhileDragging = true

        }

        filter.typeface = CustomTypeface(mainActivity).ralewaySemiBold
        tvprice.typeface = CustomTypeface(mainActivity).ralewaySemiBold
        location.typeface = CustomTypeface(mainActivity).ralewaySemiBold
        tvupdate.typeface = CustomTypeface(mainActivity).ralewayBoldItalic
        btn_update?.typeface = CustomTypeface(mainActivity).ralewaySemiBold


        return view

    }

    private fun callApiForLocation() {
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
       // ViewUtils.showDialog(mainActivity)
      /*  mainActivity.runOnUiThread {
            ViewUtils.showDialog(mainActivity)
        }*/

        lifecycleScope.launchWhenCreated {
            try {
                val response = apiInterface.callApiForLocation()

                if(response.isSuccessful){
                    val  mainJSONObj : JSONObject = JSONObject(response.body()?.string())
                    val jsonAobjMessage = mainJSONObj.optJSONArray("message")

                    for ( i in 0 until jsonAobjMessage.length()){
                        val location : JSONObject = jsonAobjMessage.getJSONObject(i)
                        homeFragment.locationList.add(location.optString("name"))
                        homeFragment.cityKeyID.add(location.optString("id"))
                    }

                    /*val  iterator : Iterator<String> = jsonAobjMessage.keys();
                    while (iterator.hasNext()) {
                        val  key : String = iterator?.next().toString()
                        locationList.add(jsonAobjMessage.optString(key))
                        cityKeyID.add(key)
                    }*/
                   // var locationAdapter = ArrayAdapter(mainActivity, R.layout.item_dropdown, locationList)
                 //   activityCreateListingBinding?.autolistingCity?.setAdapter(locationAdapter)
                    val layoutManager = LinearLayoutManager(mainActivity)
                    reclocation?.layoutManager = layoutManager
                    val filterLocationAdapter = FilterLocationAdapter(mainActivity,homeFragment.locationList, this@FilterFullScreenDialog,homeFragment)
                    reclocation?.adapter = filterLocationAdapter
                    ViewUtils.dismissDialog()

                }else
                    ViewUtils.dismissDialog()

            }catch (e : java.lang.Exception){
                e.printStackTrace()
                ViewUtils.dismissDialog()
            }
        }
    }
}