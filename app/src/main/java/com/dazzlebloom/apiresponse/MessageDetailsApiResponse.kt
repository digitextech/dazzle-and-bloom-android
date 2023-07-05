package com.dazzlebloom.apiresponse

data class MessageDetailsApiResponse(val code : Int, val message : Message){
    data class Message(val messages_details : ArrayList<Messages>)
    data class Messages(val ID : Int, val post_content : String, val post_author : String, val post_date : String,
    val author: String, val author_image : String, val post_image : String, val read_status : String, val bid_wrap : String)

}
