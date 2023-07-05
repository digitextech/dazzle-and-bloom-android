package com.dazzlebloom.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.dazzlebloom.OnitemCLickIterface
import com.dazzlebloom.R
import com.dazzlebloom.apiresponse.ProductData
import com.dazzlebloom.apiresponse.ReleatedProductData
import com.dazzlebloom.ui.ProductListDetailsActivity
import com.dazzlebloom.utiles.customtypeface.CustomTypeface
import org.w3c.dom.Text


class ReleatedProductAdapter(val context : Context, val prodList: ArrayList<ProductData>,
    val OnitemCLickIterface: OnitemCLickIterface?)  : RecyclerView.Adapter<ReleatedProductAdapter.ViewHolder>(){
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var prodName: TextView = itemView.findViewById(R.id.tv_prodname)
        var prodprice: TextView = itemView!!.findViewById(R.id.tv_rupee)
        var prodcate : TextView = itemView!!.findViewById(R.id.tv_category)
        var prodlocation: TextView = itemView!!.findViewById(R.id.tv_location)
        var prod_image: ImageView =itemView!!.findViewById(R.id.img_dress)
        val ll_card : LinearLayout = itemView!!.findViewById(R.id.llcard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ReleatedProductAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_releated_product, parent, false))

    }


    override fun getItemCount(): Int {
        return prodList.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.prodName.typeface = CustomTypeface(context).ralewaySemiBold
        holder.prodprice.typeface = CustomTypeface(context).ralewayRegular
        holder.prodlocation.typeface = CustomTypeface(context).ralewayRegular
        holder.prodcate.typeface = CustomTypeface(context).ralewayRegular

        holder.prodName.text = prodList.get(position).post_name
        holder.prodlocation.text = prodList.get(position).locatioin
        holder.prodcate.text = prodList.get(position).prod_cat
        holder.prodprice.text = "£ " +prodList.get(position).displayPrice
        Glide.with(this.context)
            .load(prodList.get(position).post_image.get(0))
            .placeholder(R.drawable.placeholder)
            .centerInside()
            .apply( RequestOptions()
                .override(Target.SIZE_ORIGINAL)
                .format(DecodeFormat.PREFER_ARGB_8888))
            .into(holder.prod_image);
        holder.ll_card.setOnClickListener {
             OnitemCLickIterface?.OnitemClick(prodList.get(position))
           //  val intent = Intent(context, ProductListDetailsActivity::class.java)
           //  context.startActivity(intent)

        }
    }

}