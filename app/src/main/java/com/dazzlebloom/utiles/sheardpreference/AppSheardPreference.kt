package com.dazzlebloom.utiles.sheardpreference

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.dazzlebloom.apiresponse.ProductData
import com.dazzlebloom.utiles.Constants

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

class AppSheardPreference(val context: Activity) {

    companion object{
        var sharedPreference : SharedPreferences ?= null
        var editor : SharedPreferences.Editor ? = null
        fun storeStringAppPreference(context: Activity, key :String, value : String){
             sharedPreference =  context.getSharedPreferences("APP_PREFERENCE",Context.MODE_PRIVATE)
             editor = sharedPreference?.edit()
            editor?.putString(key,value)
            editor?.commit()
        }

        fun fetchStringFromAppPreference(key : String) : String{
            var value : String = ""
             if(sharedPreference!= null)
                 value = sharedPreference?.getString(key,"")!!

            return value
        }

        fun storeIntAppPreference(context: Activity, key :String, value : Int){
            sharedPreference =  context.getSharedPreferences("APP_PREFERENCE",Context.MODE_PRIVATE)
            editor = sharedPreference?.edit()
            editor?.clear()
            editor?.putInt(key,value)
            editor?.commit()
        }


        fun fetchIntFromAppPreference(key : String) : Int {
            var value : Int = 0
            if(sharedPreference!= null)
                value = sharedPreference?.getInt(key,0)!!

            return value
        }

        fun clearSheardpreference(context: Context){
            sharedPreference =  context.getSharedPreferences("APP_PREFERENCE",Context.MODE_PRIVATE)
            editor = sharedPreference?.edit()
            editor?.clear();
            editor?.apply();
        }

        fun getsheardPreference(context: Context){
            sharedPreference =  context.getSharedPreferences("APP_PREFERENCE",Context.MODE_PRIVATE)
            editor = sharedPreference?.edit()
        }


        @OptIn(ExperimentalStdlibApi::class)
        fun getProductList(context: Context) : ArrayList<ProductData>{
             var productlist: ArrayList<ProductData> = ArrayList()
            sharedPreference =  context.getSharedPreferences("APP_PREFERENCE",Context.MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreference?.getString(Constants.PRODUCTLIst, null)
           // val myType = typeOf<List<ProductData>>().javaType
            val type: Type = object : TypeToken<List<ProductData?>?>() {}.type
            //val type: Type = object : TypeToken<List<String?>?>() {}.getType()
            if(json != null)
              productlist = gson.fromJson<Any>(json, type) as ArrayList<ProductData>
            if(productlist == null) {
                productlist = ArrayList()
            }
            return productlist
        }

        fun saveProductList(context: Context,productlist: ArrayList<ProductData> ){
            sharedPreference =  context.getSharedPreferences("APP_PREFERENCE",Context.MODE_PRIVATE)
            val gson = Gson()

            val json: String = gson.toJson(productlist)
            editor?.putString(Constants.PRODUCTLIst, json)
            editor?.apply()

        }

    }
}