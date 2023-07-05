package com.dazzlebloom.apiresponse

data class PrepareFormApiResponse( val code : Int, val message : ListingIdData){
    data class ListingIdData(val listing_id : Int, val package_id : Int )
}
