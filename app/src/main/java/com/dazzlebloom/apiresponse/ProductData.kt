package com.dazzlebloom.apiresponse

import java.io.Serializable

data class ProductData(var ID : Int, var post_name : String, var post_content : String,val locatioin : String, val post_image : ArrayList<String> , val prod_cat : String, val displayPrice : String,
val phonenumber: String, val websitelink: String, val category : String, val status : String, val taglist: ArrayList<TagData>) : Serializable
