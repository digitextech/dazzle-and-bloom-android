package com.dazzlebloom.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.dazzlebloom.OnitemCLickIterface
import com.dazzlebloom.R
import com.dazzlebloom.apiresponse.ProductData
import com.dazzlebloom.ui.ProductListDetailsActivity
import com.dazzlebloom.utiles.customtypeface.CustomTypeface


class ProductAdapter(
    val context: Context,
    val prodList: ArrayList<ProductData>,
    val OnitemCLickIterface: OnitemCLickIterface?
): BaseAdapter() {

    private var layoutInflater: LayoutInflater? = null
    private lateinit var prodName: TextView
    private lateinit var prodprice: TextView
    private lateinit var prodlocation: TextView
    private lateinit var prod_image: ImageView

    override fun getCount(): Int {
        return prodList.size
    }

    // below function is use to return the item of grid view.
    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        // on blow line we are checking if layout inflater
        // is null, if it is null we are initializing it.
        if (layoutInflater == null) {
            layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        // on the below line we are checking if convert view is null.
        // If it is null we are initializing it.
        if (convertView == null) {
            // on below line we are passing the layout file
            // which we have to inflate for each item of grid view.
            convertView = layoutInflater!!.inflate(R.layout.item_card, null)
        }
        // on below line we are initializing our course image view
        // and course text view with their ids.
        prod_image = convertView!!.findViewById(R.id.img_dress)
        prodName = convertView!!.findViewById(R.id.tv_prodname)
        prodprice = convertView!!.findViewById(R.id.tv_rupee)
        prodlocation = convertView!!.findViewById(R.id.tv_location)

        prodName.typeface = CustomTypeface(context).ralewaySemiBold
        prodprice.typeface = CustomTypeface(context).ralewayRegular
        prodlocation.typeface = CustomTypeface(context).ralewayRegular
        // on below line we are setting image for our course image view.

        // at last we are returning our convert view.

        prodName.text = prodList.get(position).post_name
        prodlocation.text = prodList.get(position).locatioin
        Glide.with(this.context)

            .load(prodList.get(position).post_image)
            .placeholder(R.drawable.placeholder)
            .centerInside()
            .apply(RequestOptions.bitmapTransform( RoundedCorners(14)))
            .into(prod_image);
         convertView.setOnClickListener {
             OnitemCLickIterface?.OnitemClick(prodList.get(position))
          //   val intent = Intent(context, ProductListDetailsActivity::class.java)
           //  context.startActivity(intent)
         }
        return convertView
    }
}