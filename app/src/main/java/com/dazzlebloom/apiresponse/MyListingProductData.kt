package com.dazzlebloom.apiresponse

import java.io.Serializable

data class MyListingProductData(var ID : Int, var post_name : String, var post_content : String, val locatioin : String, val post_image : ArrayList<String>, val prod_cat : String, val displayPrice : String,
                                val phonenumber: String, val websitelink: String, val category : String, val status : String, val taglist: ArrayList<TagData>, val address_line_1 : String, val address_line_2:String,val zip : String,
val lat : String, val long : String, val linkedin_link : String, val instagram_link : String, val youtube_link : String,
val twitter_link : String, val facebook_link :String,val whatsapp_number : String, val summery :String , val listingtype : String, val additionalinfo : String, val imageIDS : ArrayList<String>) : Serializable
