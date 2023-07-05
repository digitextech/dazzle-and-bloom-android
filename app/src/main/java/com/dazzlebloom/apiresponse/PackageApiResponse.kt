package com.dazzlebloom.apiresponse

data class PackageApiResponse(val code: Int, val message : String, val packages : Package){

}
data class Package(val packages_array: String){

}
