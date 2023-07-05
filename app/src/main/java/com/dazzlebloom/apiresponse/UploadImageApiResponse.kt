package com.dazzlebloom.apiresponse

data class UploadImageApiResponse(val code : Int, val message : UploadedFile){
    data class UploadedFile(val uploaded_file: String,  val attachment_id : Int)
}
