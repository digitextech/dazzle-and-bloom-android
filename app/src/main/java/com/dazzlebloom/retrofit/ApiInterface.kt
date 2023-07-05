package com.dazzlebloom.retrofit

import com.dazzlebloom.apiresponse.*
import okhttp3.MultipartBody

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import java.io.File

interface ApiInterface {

     @Headers("Content-Type: application/json")
     @POST("wp-json/users/login?")
     suspend fun callLoginApi( @Body body :Map<String, String>) : Response<LoginApiResponse>

     @Headers("Content-Type: application/json")
     @POST("wp-json/users/register")
     suspend fun callSignUpApi( @Body bodyb :Map<String, String>) : Response<SignUpresponse>

     @Headers("Content-Type: application/json")
     @GET("wp-json/listings/get?")
     suspend fun getlisting(@Query("perpage")  perpage : Int, @Query("page") page :Int, @Query("uid") uid : Int, @Query("status") status : String) : Response<ResponseBody>

     @GET("wp-json/menu/get")
     suspend fun getMenu() : Response<ResponseBody>

     @Headers("Content-Type: application/json")
     @GET("wp-json/listings/get?")
     suspend fun getProductListinHomePage(@Query("perpage")  perpage : Int, @Query("page") page :Int) : Response<ResponseBody>

     @Headers("Content-Type: application/json")
     @GET("api/forget_password.php")
     suspend fun forgotPassword(@Query("login") useremail : String) : Response<ResponseBody>

     @Headers("Content-Type: application/json")
     @POST("wp-json/listing/prepare-form")
     suspend fun callApiForPrepareForm(@Body body :Map<String, Int>) : Response<PrepareFormApiResponse>

     @Headers("Content-Type: application/json")
     @POST("wp-json/listings/upload/image")
     suspend fun callApiForUploadImage(@Body body: MultipartBody) : Response<UploadImageApiResponse>

     @Headers("Content-Type: application/json")
     @GET("wp-json/listings/tags/get")
     suspend fun callApiForListingTags() : Response<ResponseBody>

     @Headers("Content-Type: application/json")
     @GET("wp-json/listings/categories/get")
     suspend fun callApiForCategories() : Response<ResponseBody>

     @GET("wp-json/listings/locations/get")
     @Headers("Content-Type: application/json")
     suspend fun callApiForLocation() : Response<ResponseBody>

     @GET("wp-json/listings/types/get")
     @Headers("Content-Type: application/json")
     suspend fun callApiForType() : Response<ResponseBody>

     @Headers("Content-Type: application/json")
     @POST("wp-json/listing/create")
     suspend fun callApiForCreateList(@Body  createListModel: CreateListModel) : Response<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("wp-json/listing/update")
    suspend fun callApiForUpdateList(@Body  createListModel: CreateListModel) : Response<ResponseBody>


     @Headers("Content-Type: application/json")
     @GET("wp-json/address/get")
     suspend fun callApiforAddressd(@Query("uid") uid : Int) : Response<AddressApiResponse>

     @Headers("Content-Type: application/json")
     @POST("wp-json/profile/update")
     suspend fun callApiForUpdateProfile(@Body bodyb :Map<String, String>) : Response<ResponseBody>

     @Headers("Content-Type: application/json")
     @GET("wp-json/message/get?difppage=1")
     suspend fun callApiforMessageList(@Query("uid") uid : Int) : Response<MessageListApiResponse>

    @Headers("Content-Type: application/json")
    @GET("wp-json/listings/count")
    suspend fun callApiforListingcount(@Query("uid") uid : Int) : Response<ResponseBody>


     @Headers("Content-Type: application/json")
     @GET("wp-json/message/get-details")
     suspend fun callApiforMessageDetails(@Query("uid") uid : Int, @Query("id") id : Int) : Response<MessageDetailsApiResponse>

     @Headers("Content-Type: application/json")
     @POST("wp-json/message/reply")
     //suspend fun callApiforReply(@Query("uid") uid : Int,@Query("message_id") message_id : Int, @Query("listing_id") listing_id : String,  @Query("message") message : String, @Query("bid") bid : String) : Response<MessageListApiResponse>
     suspend fun callApiforReply(@Body bodyb :Map<String, String>) : Response<MessageDetailsApiResponse>

    @Headers("Content-Type: application/json")
    @GET("wp-json/get/newlisting")
    suspend fun callApiforFilter(@Query("category_id") category_id : Int, @Query("paged") paged : Int, @Query("order") order : String , @Query("s") s : String,
    @Query("per_page") per_page : Int, @Query("order_by") order_by : String,  @Query("min_amount") min_amount : Int, @Query("max_amount") max_amount : Int) : Response<ResponseBody>


    @Headers("Content-Type: application/json")
    @GET("wp-json/get/newlisting")
    suspend fun callApiforFilterPricelocation(@Query("category_id") category_id : Int, @Query("paged") paged : Int, @Query("order") order : String , @Query("s") s : String,
                                 @Query("per_page") per_page : Int, @Query("order_by") order_by : String , @Query("min_amount") min_amount : Int, @Query("max_amount") max_amount : Int, @Query("location") location : Array<Int>) : Response<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET("wp-json/get/newlisting")
    suspend fun callApiformylisting(@Query("category_id") category_id : Int, @Query("paged") paged : Int, @Query("order") order : String , @Query("s") s : String,
                                 @Query("per_page") per_page : Int, @Query("order_by") order_by : String, @Query("uid") uid : String , @Query("status") status : String  ) : Response<ResponseBody>


     @Headers("Content-Type: application/json")
     @GET("wp-json/listing/send-message")
     suspend fun callApiforSendmessage(@Query("listing_id") listing_id : Int, @Query("uid") uid : Int,@Query("message") message : String, @Query("bid") bid : String ) : Response<ResponseBody>


    @Headers("Content-Type: application/json")
    @POST("wp-json/message/send-message")
    suspend fun callApiformessageSend(@Body bodyb :Map<String, String>) : Response<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("wp-json/delete/listing")
    suspend fun callApiforDelete(@Body bodyb :Map<String, String>) : Response<ResponseBody>

    /* @Headers("Content-Type: application/json")
     @GET("wp-json/delete/listing")
     suspend fun callApiforDelete(@Query("listing_id") listing_id : Int, @Query("reason") reason : String ) : Response<ResponseBody>
*/
     @Headers("Content-Type: application/json")
     @GET("wp-json/admin-note/send")
     suspend fun callApiforNotetoAdmin(@Query("uid") uid : Int,@Query("listing_id") listing_id : Int, @Query("reason") reason : String ) : Response<ResponseBody>


     @Headers("Content-Type: application/json")
     @GET("wp-json/admin-note/send")
     suspend fun callApifornotetoadmin(@Query("listing_id") listing_id : Int, @Query("uid") uid : Int, @Query("_notice_to_admin") _notice_to_admin : String ) : Response<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET("wp-json/get/about")
    suspend fun callApiforAboutus() : Response<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET("wp-json/profile/edit")
    suspend fun callApiforGetProfile(@Query("uid") uid : Int) : Response<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("wp-json/send/otp")
    suspend fun callApiForOtp(@Body bodyb :Map<String, String>) : Response<ResponseBody>


    @Headers("Content-Type: application/json")
    @POST("wp-json/verify/otp")
    suspend fun callApiVerifyOtp(@Body bodyb :Map<String, String>) : Response<LoginApiResponse>


    @Headers("Content-Type: application/json")
    @GET("wp-json/get/relatedlisting")
    suspend fun callApiforreleatedProduct(@Query("id") id : Int ) : Response<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("wp-json/messages/delete")
    suspend fun callApiforMessageDelete(@Body bodyb :Map<String, Array<Int>>) : Response<ResponseBody>

    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE", path = "wp-json/delete/user/", hasBody = true)
    //@DELETE("wp-json/delete/user/{uid}")
  //  suspend fun callApiforDelete(@Path("uid") uid : Int) : Response<ResponseBody>
    suspend fun callApiforDeleteuser(@Body bodyb :Map<String, Int>) : Response<ResponseBody>


    @Headers("Content-Type: application/json")
    @POST("wp-json/add/phone")
    suspend fun callApiforAddPhone(@Body bodyb :Map<String, String>) : Response<ResponseBody>


}