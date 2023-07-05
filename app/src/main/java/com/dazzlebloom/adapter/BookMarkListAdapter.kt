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
import com.dazzlebloom.apiresponse.ProductData
import com.dazzlebloom.ui.ProductListDetailsActivity
import com.dazzlebloom.utiles.customtypeface.CustomTypeface
import java.io.Serializable
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.target.Target
import com.dazzlebloom.utiles.sheardpreference.AppSheardPreference


class BookMarkListAdapter(val context: Context, val prodlist: ArrayList<ProductData>): RecyclerView.Adapter<BookMarkListAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookMarkListAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_bookmark, parent, false))
    }


    override fun getItemCount(): Int {
        return prodlist.size
    }
    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val prod_image : AppCompatImageView = view!!.findViewById(R.id.img_prod_bookmark)
        val consbookmark :ConstraintLayout = view!!.findViewById(R.id.cons_bookmark)
        val prodName : AppCompatTextView = view!!.findViewById(R.id.tv_prod_name)
        val prodprice : AppCompatTextView  = view!!.findViewById(R.id.tv_rupee)
        val imgdelete : AppCompatTextView = view!!.findViewById(R.id.img_delete)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(this.context)
            .load(prodlist.get(position).post_image.get(0))
            .centerInside()
            .apply( RequestOptions()
                .override(Target.SIZE_ORIGINAL)
                .format(DecodeFormat.PREFER_ARGB_8888))
            .into(holder.prod_image);


        holder.prodName.typeface = CustomTypeface(context).ralewaySemiBold
        holder.prodprice.typeface = CustomTypeface(context).ralewaySemiBold
        holder.imgdelete.typeface = CustomTypeface(context).ralewaySemiBold

        holder.prodName.text = prodlist.get(position).post_name
        holder.prodprice.text = "£ "+ prodlist.get(position).displayPrice
        holder.consbookmark.setOnClickListener {
            val intent = Intent(context, ProductListDetailsActivity::class.java)
            intent.putExtra("product", prodlist.get(position) as Serializable)
            context?.startActivity(intent)
        }

        holder.imgdelete.setOnClickListener {
            val dialogClickListener =
                DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            prodlist.removeAt(position)
                            notifyDataSetChanged()
                            AppSheardPreference.saveProductList(context, prodlist)
                            dialog.dismiss()

                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                            dialog.dismiss()
                        }
                    }
                }

            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show()
        }
    }
}