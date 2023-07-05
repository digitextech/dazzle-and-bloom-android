package com.dazzlebloom.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.*
import androidx.constraintlayout.widget.ConstraintLayout

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.dazzlebloom.R
import com.dazzlebloom.apiresponse.MyListingProductData
import com.dazzlebloom.apiresponse.ProductData
import com.dazzlebloom.ui.CreateListingActivity
import com.dazzlebloom.ui.MyListingActivity
import com.dazzlebloom.utiles.ViewUtils
import com.dazzlebloom.utiles.customtypeface.CustomTypeface
import java.io.Serializable


class ListingItemAdapter(val context: MyListingActivity, val listArray: ArrayList<MyListingProductData>) : RecyclerView.Adapter<ListingItemAdapter.ViewHolder>() {
    class  ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var tvBlueby : AppCompatTextView = itemView.findViewById(R.id.tv_blueby)
        var tvExpiry : AppCompatTextView = itemView.findViewById(R.id.tv_expiry)
        val tvExprirydate : AppCompatTextView = itemView.findViewById(R.id.tv_exprirydate)
        val tvListingid : AppCompatTextView = itemView.findViewById(R.id.tv_listingid)
        val tvListingidNumber : AppCompatTextView = itemView.findViewById(R.id.tv_listingid_number)
        val tvStatus : AppCompatTextView = itemView.findViewById(R.id.tv_status)
        val img_listing : AppCompatImageView = itemView.findViewById(R.id.img_listing)

        val tvedit : AppCompatTextView = itemView.findViewById(R.id.tveditlisting)
        val img_editlisting : AppCompatImageView = itemView.findViewById(R.id.img_edit)
        val tvpreview : AppCompatTextView = itemView.findViewById(R.id.tvpreview)
        val imgPreview : AppCompatImageView = itemView.findViewById(R.id.img_preview)
        val tvpublish : AppCompatTextView = itemView.findViewById(R.id.tvpublic)
        val imgPublic : AppCompatImageView = itemView.findViewById(R.id.img_public)
        val tvnotetoadmin : AppCompatTextView = itemView.findViewById(R.id.tvnodeadmin)
        val imgnotrtoadmin : AppCompatImageView = itemView.findViewById(R.id.img_nodeadmin)
        val tvdelete : AppCompatTextView = itemView.findViewById(R.id.tvdelete)
        val imgdelete : AppCompatImageView = itemView.findViewById(R.id.img_delete)
        val consedit : ConstraintLayout = itemView.findViewById(R.id.consedit)
        val cons_note_edit : ConstraintLayout = itemView.findViewById(R.id.cons_note_edit)
        val cons_public : ConstraintLayout = itemView.findViewById(R.id.cons_public)
        val cons_noteadmin : ConstraintLayout = itemView.findViewById(R.id.cons_notetoadmin)
        val cons_delete : ConstraintLayout = itemView.findViewById(R.id.cons_delete)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ListingItemAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_listing, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.tvBlueby?.typeface = CustomTypeface(context).ralewaySemiBold
        holder.tvExpiry?.typeface = CustomTypeface(context).ralewaySemiBold
         holder.tvExprirydate?.typeface = CustomTypeface(context).ralewayMedium
         holder.tvListingid?.typeface = CustomTypeface(context).ralewaySemiBold
         holder.tvListingidNumber?.typeface = CustomTypeface(context).ralewayMedium
         holder.tvStatus?.typeface = CustomTypeface(context).ralewaySemiBold

        holder.tvpreview?.typeface = CustomTypeface(context).ralewayMedium
        holder.tvpublish?.typeface = CustomTypeface(context).ralewayMedium
        holder.tvnotetoadmin?.typeface = CustomTypeface(context).ralewayMedium
        holder.tvdelete?.typeface = CustomTypeface(context).ralewayMedium
        holder.tvedit?.typeface = CustomTypeface(context).ralewayMedium
        try{
            if (listArray.get(position).post_image.size > 0) {
                Glide.with(this.context)
                    .load(listArray.get(position).post_image.get(0))
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(14)))
                    .centerInside()
                    .error(R.drawable.placeholder)
                    .apply(
                        RequestOptions()
                            .override(Target.SIZE_ORIGINAL)
                            .format(DecodeFormat.PREFER_ARGB_8888)
                    )
                    .placeholder(R.drawable.placeholder)
                    .into(holder.img_listing);
            }

            holder.tvStatus.text = listArray.get(position).status
            holder.tvListingidNumber.text = listArray.get(position).ID.toString()
            holder.tvBlueby.text = listArray.get(position).post_name

            if (listArray.get(position).status.equals("pending")) {
                holder.consedit.visibility = View.GONE
            } else
                holder.consedit.visibility = View.VISIBLE


            holder.cons_note_edit.setOnClickListener {
                val intent = Intent(context, CreateListingActivity::class.java)
                intent.putExtra("listing", listArray.get(position) as Serializable)
                intent.putExtra("isedit", "1")
                context.startActivity(intent)
            }
            holder.img_editlisting.setOnClickListener {
                val intent = Intent(context, CreateListingActivity::class.java)
                intent.putExtra("listing", listArray.get(position) as Serializable)
                intent.putExtra("isedit", "1")
                context.startActivity(intent)
            }

            holder.tvdelete.setOnClickListener {

                val popup = PopupMenu(context, holder.tvdelete)
                popup.getMenuInflater().inflate(R.menu.delete_menu, popup.getMenu());
                popup.setOnMenuItemClickListener ( object : PopupMenu.OnMenuItemClickListener{
                    override fun onMenuItemClick(item: MenuItem?): Boolean {
                        if(item?.title?.equals("I have sold my item") == true){
                            context.callApiforDelete(listArray.get(position).ID, "I have sold my item", position)
                            return true
                        }
                        else if(item?.title?.equals("I no longer wish to sell my item") == true){
                            context.callApiforDelete(listArray.get(position).ID, "I no longer wish to sell my item", position)
                            return true
                        }
                      return true
                    }


                })
                popup.show()
               // ShowAlertforDelete(listArray.get(position).ID, position)
            }
            holder.cons_noteadmin.setOnClickListener {
                ShowAlertfornotetoadmin(listArray.get(position).ID, position)
            }
            holder.imgdelete.setOnClickListener {
                ShowAlertforDelete(listArray.get(position).ID, position)

            }

            holder.tvnotetoadmin.setOnClickListener {
                ShowAlertfornotetoadmin(listArray.get(position).ID, position)
            }

            holder.imgnotrtoadmin.setOnClickListener {
                ShowAlertfornotetoadmin(listArray.get(position).ID, position)

            }
            holder.cons_delete.setOnClickListener {
                val popup = PopupMenu(context, holder.tvdelete)
                popup.getMenuInflater().inflate(R.menu.delete_menu, popup.getMenu());
                popup.setOnMenuItemClickListener ( object : PopupMenu.OnMenuItemClickListener{
                    override fun onMenuItemClick(item: MenuItem?): Boolean {
                        if(item?.title?.equals("I have sold my item") == true){
                            context.callApiforDelete(listArray.get(position).ID, "I have sold my item", position)
                            return true
                        }
                        else if(item?.title?.equals("I no longer wish to sell my item") == true){
                            context.callApiforDelete(listArray.get(position).ID, "I no longer wish to sell my item", position)
                            return true
                        }
                        return true
                    }


                })
                popup.show()
            }

        }
        catch (e : Exception){
            e.printStackTrace()
        }
    }

    private fun ShowAlertforDelete( listingID : Int, position : Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        var dialog = builder.create()
        val customLayout: View = LayoutInflater.from(context).inflate(R.layout.dialog_delete, null)
        builder.setView(customLayout)
        val tvwhydelete : AppCompatTextView= customLayout.findViewById(R.id.why_delete)
        val cancel : AppCompatTextView= customLayout.findViewById(R.id.tv_cancel)
        val etwriteHere : AppCompatEditText= customLayout.findViewById(R.id.et_typehere)
        val btndelete : AppCompatButton= customLayout.findViewById(R.id.btn_delete)
        tvwhydelete.typeface = CustomTypeface(context).ralewaySemiBold
        etwriteHere.typeface = CustomTypeface(context).ralewayMedium
        btndelete.typeface = CustomTypeface(context).ralewaySemiBold
        etwriteHere.typeface = CustomTypeface(context).ralewayMedium
        cancel.typeface = CustomTypeface(context).ralewaySemiBold



        btndelete.setOnClickListener {
            if(!etwriteHere.text.toString().equals(""))
            //context.callApiforDelete(listingID, etwriteHere.text.toString(), position, dialog)
            else
                ViewUtils.showdialog(context,"Please enter the reason")


        }
        cancel.setOnClickListener {
        dialog.dismiss()
        }
          dialog = builder.create()
        dialog.show()
    }

    private fun ShowAlertfornotetoadmin( listingID : Int, position : Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        var dialog = builder.create()
        val customLayout: View = LayoutInflater.from(context).inflate(R.layout.note_to_admin, null)
        builder.setView(customLayout)
        val tvwhydelete : AppCompatTextView= customLayout.findViewById(R.id.why_delete)
        val cancel : AppCompatTextView= customLayout.findViewById(R.id.tv_cancel)
        val etwriteHere : AppCompatEditText= customLayout.findViewById(R.id.et_typehere)
        val btndelete : AppCompatButton= customLayout.findViewById(R.id.btn_delete)
        tvwhydelete.typeface = CustomTypeface(context).ralewaySemiBold
        etwriteHere.typeface = CustomTypeface(context).ralewayMedium
        btndelete.typeface = CustomTypeface(context).ralewaySemiBold
        etwriteHere.typeface = CustomTypeface(context).ralewayMedium
        cancel.typeface = CustomTypeface(context).ralewaySemiBold

        tvwhydelete.text = "Give note to admin"
        btndelete.text = " Send"

        btndelete.setOnClickListener {
            if(!etwriteHere.text.toString().equals(""))
                context.callApinotetoAdmin(listingID, etwriteHere.text.toString(), position)
            else
                ViewUtils.showdialog(context,"Please enter the reason")


        }
        cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }

    override fun getItemCount(): Int {
        return  listArray.size
    }
}