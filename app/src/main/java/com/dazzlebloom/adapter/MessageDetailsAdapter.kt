package com.dazzlebloom.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.dazzlebloom.R
import com.dazzlebloom.apiresponse.MessageDetailsApiResponse
import com.dazzlebloom.retrofit.ApiInterface
import com.dazzlebloom.retrofit.RetrofitHelper

import com.dazzlebloom.ui.MessageDetailsActivity
import com.dazzlebloom.utiles.Constants
import com.dazzlebloom.utiles.ViewUtils
import com.dazzlebloom.utiles.customtypeface.CustomTypeface
import com.dazzlebloom.utiles.sheardpreference.AppSheardPreference
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.widget.Toast


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.core.content.ContextCompat

import androidx.core.content.ContextCompat.getSystemService





class MessageDetailsAdapter(val context: MessageDetailsActivity, val messagelist : ArrayList<MessageDetailsApiResponse.Messages>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object {
        const val VIEW_TYPE_Left = 1
        const val VIEW_TYPE_Right = 2
    }
   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder{
       if(viewType == VIEW_TYPE_Left){
        return ViewHolderLeft(LayoutInflater.from(context).inflate(R.layout.left_message_view, parent, false))

       }else{
           return ViewHolderRight(LayoutInflater.from(context).inflate(R.layout.right_message_view, parent, false))
       }

    }

    override fun getItemViewType(position: Int): Int {
        if(messagelist.get(position).author.equals(AppSheardPreference.fetchStringFromAppPreference(Constants.USERNAME)))
            return VIEW_TYPE_Left
        else
            return  VIEW_TYPE_Right

    }


    override fun getItemCount(): Int {
        return messagelist.size
    }
    class ViewHolderLeft (view: View) : RecyclerView.ViewHolder(view) {
        val imgauthor : AppCompatImageView = view!!.findViewById(R.id.img_left_side)
        val authorname : AppCompatTextView = view!!.findViewById(R.id.tv_left_author)
        val date : AppCompatTextView = view!!.findViewById(R.id.tv_left_date)
        val message : AppCompatTextView  = view!!.findViewById(R.id.tv_message_left)
        val consleft : ConstraintLayout = view!!.findViewById(R.id.cons_left)


    }

    class ViewHolderRight (view: View) : RecyclerView.ViewHolder(view) {
        val imgauthor : AppCompatImageView = view!!.findViewById(R.id.img_right_side)
        val authorname : AppCompatTextView = view!!.findViewById(R.id.tv_right_author)
        val date : AppCompatTextView = view!!.findViewById(R.id.tv_right_date)
        val message : AppCompatTextView  = view!!.findViewById(R.id.tv_message_right)
        val consright : ConstraintLayout = view!!.findViewById(R.id.cons_right)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder , position: Int) {
       // if(messagelist.get(position).post_author.equals(AppSheardPreference.fetchIntFromAppPreference(Constants.USERID).toString())) {
            if(messagelist.get(position).author.equals(AppSheardPreference.fetchStringFromAppPreference(Constants.USERNAME))) {
                (holder as ViewHolderLeft).authorname.typeface = CustomTypeface(context).ralewayMedium
                (holder as ViewHolderLeft).date.typeface = CustomTypeface(context).ralewayMedium
                (holder as ViewHolderLeft).message.typeface = CustomTypeface(context).ralewaySemiBold

                val imageView : AppCompatImageView = (holder as ViewHolderLeft).imgauthor
                Glide.with(this.context)
                .load(messagelist.get(position).author_image)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(14)))
                .into(imageView);


            var spf = SimpleDateFormat("yyyy-mm-dd HH:mm:ss")
            val newDate: Date = spf.parse(messagelist.get(position).post_date)
            spf = SimpleDateFormat("MMM dd, yyyy hh:mm:ss aaa")
            val date = spf.format(newDate)
                (holder as ViewHolderLeft).date.text = date
                (holder as ViewHolderLeft).authorname.text = messagelist.get(position).author
                (holder as ViewHolderLeft).message.text = messagelist.get(position).post_content

                (holder as ViewHolderLeft).consleft.setOnLongClickListener {
                    showPopupmenu(messagelist.get(position).ID, position, messagelist.get(position).post_content)
                }



            }else{
                (holder as ViewHolderRight).authorname.typeface = CustomTypeface(context).ralewayMedium
                (holder as ViewHolderRight).date.typeface = CustomTypeface(context).ralewayMedium
                (holder as ViewHolderRight).message.typeface = CustomTypeface(context).ralewaySemiBold

                val imageView : AppCompatImageView = (holder as ViewHolderRight).imgauthor
            Glide.with(this.context)
                .load(messagelist.get(position).author_image)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(14)))
                .into(imageView);


            var spf = SimpleDateFormat("yyyy-mm-dd HH:mm:ss")
            val newDate: Date = spf.parse(messagelist.get(position).post_date)
            spf = SimpleDateFormat("MMM dd, yyyy hh:mm:ss aaa")
            val date = spf.format(newDate)
            (holder as ViewHolderRight).date.text = date
                (holder as ViewHolderRight).message.text = messagelist.get(position).post_content
                (holder as ViewHolderRight).authorname.text = messagelist.get(position).author
                (holder as ViewHolderRight).consright.setOnLongClickListener {
                    showPopupmenu(messagelist.get(position).ID,position, messagelist.get(position).post_content)
                }
        }


    }

    private fun showPopupmenu(id: Int, position: Int, content : String): Boolean {
        val bottomSheetDialog : BottomSheetDialog  =  BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.item_delete_message);
        val tv_copy = bottomSheetDialog.findViewById<AppCompatTextView>(R.id.tv_copy)
        val tv_delete = bottomSheetDialog.findViewById<AppCompatTextView>(R.id.tv_delete)
        val tv_cancel = bottomSheetDialog.findViewById<AppCompatTextView>(R.id.tv_cancel)
        tv_cancel?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        tv_delete?.setOnClickListener {
            callApifordeletemessage(id,position)
            bottomSheetDialog.dismiss()
        }
        tv_copy?.setOnClickListener {

            var clipboardManager : ClipboardManager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("key", content)
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show()

            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show();
        return true
    }

    private fun callApifordeletemessage(id: Int, position: Int) {
        val meaasgearray  : ArrayList<Int> = ArrayList()
        meaasgearray.add(id)
        ViewUtils.showDialog(context)
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        context?.lifecycleScope?.launchWhenCreated {
            val data: MutableMap<String, Array<Int>> = HashMap()
            val messagearray: Array<Int> = meaasgearray.toTypedArray()
            data.put("message_id", messagearray)
            val response = apiInterface?.callApiforMessageDelete(data)
            ViewUtils.dismissDialog()
            if (response?.isSuccessful!!){
                ViewUtils.showdialog(context!!,"Deleted Successfully")
                messagelist.removeAt(position)
                notifyDataSetChanged()
            }
        }
    }
}