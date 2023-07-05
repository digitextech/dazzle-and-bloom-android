package com.dazzlebloom.ui

import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dazzlebloom.R
import com.dazzlebloom.adapter.MessageListAdapter
import com.dazzlebloom.apiresponse.MessageListApiResponse
import com.dazzlebloom.databinding.ActivityMessageBinding
import com.dazzlebloom.retrofit.ApiInterface
import com.dazzlebloom.retrofit.RetrofitHelper
import com.dazzlebloom.utiles.Constants
import com.dazzlebloom.utiles.ViewUtils
import com.dazzlebloom.utiles.customtypeface.CustomTypeface
import com.dazzlebloom.utiles.sheardpreference.AppSheardPreference
import androidx.core.content.res.ResourcesCompat

import android.graphics.drawable.BitmapDrawable

import android.graphics.Bitmap




class MessageActivity : AppCompatActivity() {
    var messageBinding : ActivityMessageBinding? =null
    var  messageListAdapter : MessageListAdapter? = null

    var messageList :ArrayList<MessageListApiResponse.Messages> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        messageBinding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(messageBinding!!.root)
        ViewUtils.changeStatusBarcolor(this!!,resources!!.getColor(R.color.status_bar_color))
        messageBinding?.tvMymessage?.typeface = CustomTypeface(this).ralewayBold
        messageBinding!!.backArrow.setOnClickListener {
            finish()
        }

        if(AppSheardPreference.fetchIntFromAppPreference(Constants.USERID)!=0)
          callApiforMessageList()
        else
            ViewUtils.showdialog(this@MessageActivity,"No message List")

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                    return false
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                val icon: Bitmap
                 val p = Paint()
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    val itemView = viewHolder.itemView
                    val height = itemView.bottom.toFloat() - itemView.top.toFloat()
                    val width = height / 3

                    if (dX > 0) {
                        p?.color = Color.parseColor("#fc0703")
                        val background =
                            RectF(itemView.left.toFloat(), itemView.top.toFloat(), dX, itemView.bottom.toFloat())
                        c.drawRect(background, p!!)
                        icon = (ResourcesCompat.getDrawable(resources, R.drawable.delete, null) as BitmapDrawable?)!!.bitmap

                        // icon = BitmapFactory.decodeResource(resources, R.drawable.ic_delete)
                        val icon_dest = RectF(
                            itemView.left.toFloat() + width,
                            itemView.top.toFloat() + width,
                            itemView.left.toFloat() + 2 * width,
                            itemView.bottom.toFloat() - width)
                        c.drawBitmap(icon, null, icon_dest, p)
                    } else {
                        p?.color = Color.parseColor("#fc0703")
                        val background = RectF(
                            itemView.right.toFloat() + dX,
                            itemView.top.toFloat(),
                            itemView.right.toFloat(),
                            itemView.bottom.toFloat()
                        )
                        c.drawRect(background, p)
                        icon = (ResourcesCompat.getDrawable(resources, R.drawable.delete, null) as BitmapDrawable?)!!.bitmap
                       // icon = BitmapFactory.decodeResource(resources , R.drawable.ic_delete)
                        val icon_dest = RectF(
                            itemView.right.toFloat() - 2 * width,
                            itemView.top.toFloat() + width,
                            itemView.right.toFloat() - width,
                            itemView.bottom.toFloat() - width
                        )
                        c.drawBitmap(icon, null, icon_dest, p)
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val deletedCourse: MessageListApiResponse.Messages = messageList.get(viewHolder.adapterPosition)

                // below line is to get the position
                // of the item at that position.
                val position = viewHolder.adapterPosition



                callApiforMessageDelete(messageList.get(position).ID, position)
                messageList.removeAt(position)
                messageListAdapter?.notifyDataSetChanged()
               // messageList.removeAt(viewHolder.adapterPosition)
            }
            // at last we are adding this
            // to our recycler view.

        }).attachToRecyclerView(messageBinding?.recMessagelist)
    }

    private fun callApiforMessageDelete(id: Int, position: Int) {
        val meaasgearray  : ArrayList<Int> = ArrayList()
          meaasgearray.add(id)
        ViewUtils.showDialog(this)
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        lifecycleScope?.launchWhenCreated {
            val data: MutableMap<String, Array<Int>> = HashMap()
            val messagearray: Array<Int> = meaasgearray.toTypedArray()
            data.put("message_id", messagearray)
            val response = apiInterface.callApiforMessageDelete(data)
            ViewUtils.dismissDialog()
            if (response.isSuccessful){
                ViewUtils.showdialog(this@MessageActivity,"Deleted Successfully")

            }
           // else{
             //   ViewUtils.showdialog(this@MessageActivity,"Something wrong, try later")
           // }
        }


    }

    private fun callApiforMessageList() {
        ViewUtils.showDialog(this)
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        lifecycleScope?.launchWhenCreated {
            //val response = apiInterface.callApiforMessageList(1)
            val response = apiInterface.callApiforMessageList(AppSheardPreference.fetchIntFromAppPreference(Constants.USERID))
            ViewUtils.dismissDialog()
            if(response.isSuccessful){
             if(response.body()?.message?.messages?.size!! > 0){
                 messageList = response.body()?.message?.messages!!
                 messageBinding?.recMessagelist?.layoutManager = LinearLayoutManager(this@MessageActivity)
                  messageListAdapter = MessageListAdapter(this@MessageActivity,messageList)
                 messageBinding?.recMessagelist?.adapter = messageListAdapter
             }else
                 ViewUtils.showdialog(this@MessageActivity,"No message List")

         }else
                ViewUtils.showdialog(this@MessageActivity,"Something wrong. Try again later")



        }

    }
}