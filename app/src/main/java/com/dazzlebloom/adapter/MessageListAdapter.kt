package com.dazzlebloom.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.dazzlebloom.R
import com.dazzlebloom.apiresponse.MessageListApiResponse
import com.dazzlebloom.ui.MessageDetailsActivity
import com.dazzlebloom.utiles.GlideCircleTransformation
import com.dazzlebloom.utiles.customtypeface.CustomTypeface
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MessageListAdapter(val context: Context, val messagelist : ArrayList<MessageListApiResponse.Messages>): RecyclerView.Adapter<MessageListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageListAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_messaelist, parent, false))
    }


    override fun getItemCount(): Int {
        return messagelist.size
    }
    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val imgmessage : AppCompatImageView = view!!.findViewById(R.id.img_message)
        val imgmauthor : AppCompatImageView = view!!.findViewById(R.id.img_author)
        val posttitle : AppCompatTextView = view!!.findViewById(R.id.tv_post_title)
        val author : AppCompatTextView  = view!!.findViewById(R.id.tv_authorname)
        val date : AppCompatTextView  = view!!.findViewById(R.id.tv_date)
        val bid : AppCompatTextView = view!!.findViewById(R.id.tv_bid)
        val unread : AppCompatTextView = view!!.findViewById(R.id.tv_unread)
        val conscard : ConstraintLayout = view!!.findViewById(R.id.cons_message)


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(this.context)
            .load(messagelist.get(position).post_image)
            .apply(RequestOptions.bitmapTransform( RoundedCorners(14)))
            .into(holder.imgmessage);


        Glide.with(this.context)
            .load(messagelist.get(position).author_image)
            // .bitmaptr( GlideCircleTransformation(context))
            .into(holder.imgmauthor);

        holder.posttitle.text= messagelist.get(position).post_title
        holder.author.text= messagelist.get(position).author

        var spf = SimpleDateFormat("yyyy-mm-dd HH:mm:ss")
        val newDate: Date = spf.parse(messagelist.get(position).post_date)
        spf = SimpleDateFormat("MMM dd, yyyy hh:mm:ss aaa")
        val date = spf.format(newDate)

        holder.date.text= date
        if(messagelist.get(position).bid_wrap.equals(""))
            holder.bid.visibility = View.GONE

        if(messagelist.get(position).read_status.equals(""))
            holder.unread.visibility = View.GONE

        holder.posttitle.typeface = CustomTypeface(context).ralewaySemiBold
        holder.author.typeface = CustomTypeface(context).ralewayRegular
        holder.date.typeface = CustomTypeface(context).ralewayRegular
        holder.conscard.setOnClickListener {
            val messageDetailsIntent = Intent(context, MessageDetailsActivity::class.java)
            messageDetailsIntent.putExtra("ID",messagelist.get(position).ID.toString())
            messageDetailsIntent.putExtra("Messagetitle",messagelist.get(position).post_title)
            messageDetailsIntent.putExtra("bidwrapper",messagelist.get(position).bid_wrap)
            messageDetailsIntent.putExtra("participent",messagelist.get(position).author)
            //messageDetailsIntent.putExtra("bidamount",messagelist.get(position).b)
            context.startActivity(messageDetailsIntent)

        }
    }
}