package com.dazzlebloom.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dazzlebloom.R
import com.dazzlebloom.adapter.MessageDetailsAdapter
import com.dazzlebloom.adapter.MessageListAdapter
import com.dazzlebloom.apiresponse.MessageDetailsApiResponse
import com.dazzlebloom.apiresponse.MessageListApiResponse
import com.dazzlebloom.databinding.ActivityMessageDetailsBinding
import com.dazzlebloom.retrofit.ApiInterface
import com.dazzlebloom.retrofit.RetrofitHelper
import com.dazzlebloom.utiles.Constants
import com.dazzlebloom.utiles.ViewUtils
import com.dazzlebloom.utiles.customtypeface.CustomTypeface
import com.dazzlebloom.utiles.sheardpreference.AppSheardPreference

class MessageDetailsActivity : AppCompatActivity() {
    var activityMessageDetailsBinding : ActivityMessageDetailsBinding ?= null
    var messageList :ArrayList<MessageDetailsApiResponse.Messages> = ArrayList()
    var MessageID = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMessageDetailsBinding = ActivityMessageDetailsBinding.inflate(layoutInflater)
        setContentView(activityMessageDetailsBinding?.root)
         MessageID = intent.getStringExtra("ID")!!.toString()
        val messagetitle = intent.getStringExtra("Messagetitle")
        val bidwrapper = intent.getStringExtra("bidwrapper")
        val participent = intent.getStringExtra("participent")
        activityMessageDetailsBinding?.tvReplyMessage?.typeface = CustomTypeface(this).ralewaySemiBold
        activityMessageDetailsBinding?.etReplymessage?.typeface = CustomTypeface(this).ralewayMedium
        activityMessageDetailsBinding?.btnReply?.typeface = CustomTypeface(this).ralewaySemiBold
        activityMessageDetailsBinding?.btnReply?.setOnClickListener {
            if (!activityMessageDetailsBinding?.etReplymessage?.text?.equals("")!!){
                callApiforreplyMessage()
            }
        }
        activityMessageDetailsBinding?.backArrow?.setOnClickListener {
            finish()
        }

        activityMessageDetailsBinding?.tvmessagetitle?.text = messagetitle
        activityMessageDetailsBinding?.tvparticipent?.text = "Participants: "+ participent
        activityMessageDetailsBinding?.tvmessagetitle?.typeface = CustomTypeface(this).ralewaySemiBold
         activityMessageDetailsBinding?.tvBid?.typeface = CustomTypeface(this).ralewayMedium
        activityMessageDetailsBinding?.tvLongpressdelete?.typeface = CustomTypeface(this).ralewayMediumItalic
        activityMessageDetailsBinding?.tvparticipent?.typeface = CustomTypeface(this).ralewaySemiBold
        if(!bidwrapper.equals(""))
            activityMessageDetailsBinding?.tvBid?.visibility = View.VISIBLE


        callApiforMessageDetails(MessageID)
    }

    public fun callApiforreplyMessage() {
        ViewUtils.showDialog(this)
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        lifecycleScope?.launchWhenCreated {

            val data: MutableMap<String, String> = HashMap()
              data.put("uid",AppSheardPreference.fetchIntFromAppPreference(Constants.USERID).toString())
              data.put("message_id",MessageID)
              data.put("listing_id","")
              data.put("message",activityMessageDetailsBinding?.etReplymessage?.text.toString())
              //data.put("bid","")

             val response = apiInterface.callApiforReply(data)
           // val response = apiInterface.callApiforReply(AppSheardPreference.fetchIntFromAppPreference(Constants.USERID),MessageID.toInt(),"",activityMessageDetailsBinding?.etReplymessage?.text.toString(),"")
           // val response = apiInterface.callApiforReply(1,MessageID,activityMessageDetailsBinding?.etReplymessage?.text.toString(),"")

            if(response?.isSuccessful == true){
               // callApiforMessageDetails(MessageID)

                   activityMessageDetailsBinding?.etReplymessage?.setText("")
                //callApiforMessageDetails(MessageID)
                if(response.body()?.message?.messages_details!= null) {
                    if (response.body()?.message?.messages_details?.size!! > 0) {
                        messageList = response.body()?.message?.messages_details!!

                        activityMessageDetailsBinding?.recMessagedetails?.layoutManager = LinearLayoutManager(this@MessageDetailsActivity)
                        val messageListAdapter = MessageDetailsAdapter(this@MessageDetailsActivity, messageList)
                        activityMessageDetailsBinding?.recMessagedetails?.adapter = messageListAdapter
                        activityMessageDetailsBinding?.recMessagedetails?.smoothScrollToPosition(messageList.size - 1)
                    }
                }
                ViewUtils.dismissDialog()
            }else
                ViewUtils.dismissDialog()
        }
    }

    private fun callApiforMessageDetails(messageID: String?) {
        ViewUtils.showDialog(this)
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        lifecycleScope?.launchWhenCreated {
            val response = messageID?.toInt()?.let { apiInterface.callApiforMessageDetails(AppSheardPreference.fetchIntFromAppPreference(Constants.USERID), it) }
           // val response = messageID?.toInt()?.let { apiInterface.callApiforMessageDetails(1, it) }
            ViewUtils.dismissDialog()
            if(response?.isSuccessful == true){
                messageList.clear()
                if(response.body()?.message?.messages_details?.size!! > 0){
                    messageList = response.body()?.message?.messages_details!!
                }
            }

            activityMessageDetailsBinding?.recMessagedetails?.layoutManager = LinearLayoutManager(this@MessageDetailsActivity)
            val messageListAdapter = MessageDetailsAdapter(this@MessageDetailsActivity,messageList)
            activityMessageDetailsBinding?.recMessagedetails?.adapter = messageListAdapter
        }


    }
}