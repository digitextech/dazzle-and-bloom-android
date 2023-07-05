package com.dazzlebloom.apiresponse

data class AddressApiResponse(val code : Int, val message : DataAddress){
    data class DataAddress(val address : Address)

    data class Address(val billing : Billing, val shipping : Shpping )

    data class Billing(val First_name : String, val Last_name: String,val Company_name : String, val CountryRegion : String, val Streetaddress : String,
   val Apartment_suite_unit_etc : String, val Town_City : String,val County: String, val Postcode: String, val Phone : String, val Emailaddress  : String)

    data class Shpping(val First_name : String, val Last_name: String,val Company_name : String, val CountryRegion : String, val Streetaddress : String,
                       val Apartment_suite_unit_etc : String, val Town_City : String,val County: String, val Postcode: String, val Phone : String, val Emailaddress  : String)
}
