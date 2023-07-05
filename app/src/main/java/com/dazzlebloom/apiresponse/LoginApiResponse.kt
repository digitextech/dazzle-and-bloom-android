package com.dazzlebloom.apiresponse

data class LoginApiResponse(val code : String ,val message : String, val data : Data, val ID : Int, val caps : Caps, val cap_key : String, var roles  : ArrayList<String>,
                            val allcaps : AllCaps ) {

}

data class Data(val ID: Int, val user_login : String, val user_pass : String, val user_nicename: String,
      val user_email : String, val user_url : String, val user_registered : String, val user_activation_key: String,
 val user_status: String, val display_name : String)

data class Caps(val subscriber : Boolean)

data class AllCaps( val read : Boolean, val level_0 : Boolean, val upload_files : Boolean , val subscriber : Boolean)


