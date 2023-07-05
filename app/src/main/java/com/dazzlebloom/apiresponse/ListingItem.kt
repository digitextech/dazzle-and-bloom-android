package com.dazzlebloom.apiresponse

import java.io.Serializable

data class ListingItem(val id : Int, var itemName : String, val desc : String , val location : String, val imageurl: String,
                     val status : String) : Serializable
