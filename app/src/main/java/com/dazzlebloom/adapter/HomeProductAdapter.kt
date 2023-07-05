package com.dazzlebloom.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.dazzlebloom.OnitemCLickIterface
import com.dazzlebloom.R
import com.dazzlebloom.apiresponse.ProductData
import com.dazzlebloom.ui.fragments.home.HomeFragment
import com.dazzlebloom.utiles.customtypeface.CustomTypeface
import android.graphics.Bitmap
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy

import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition


class HomeProductAdapter(val homeFragment: HomeFragment, val context : Context, val prodList: ArrayList<ProductData>,
                         val OnitemCLickIterface: OnitemCLickIterface? )  : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private val VIEW_TYPE_LOADING = 0
    private val VIEW_TYPE_NORMAL = 1
    private val isLoaderVisible = false

    class ViewHolder(val itemView : View) : RecyclerView.ViewHolder(itemView){
        var prodName: TextView = itemView.findViewById(R.id.tv_prodname)
        var prodprice: TextView = itemView!!.findViewById(R.id.tv_rupee)
        var prodlocation: TextView = itemView!!.findViewById(R.id.tv_location)
        var prod_image: ImageView =itemView!!.findViewById(R.id.img_dress)
        val ll_card :LinearLayout = itemView!!.findViewById(R.id.llcard)
        var tv_category: TextView = itemView!!.findViewById(R.id.tv_category)

       /* prodName.typeface = CustomTypeface(context).ralewaySemiBold
        prodprice.typeface = CustomTypeface(context).ralewayRegular
        prodlocation.typeface = CustomTypeface(context).ralewayRegular
        tv_category.typeface = CustomTypeface(context).ralewaySemiBold*/


    }

    class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val progressBar : ProgressBar = itemView!!.findViewById(R.id.progressBar)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_LOADING)
                return HomeProductAdapter.ProgressViewHolder(LayoutInflater.from(context).inflate(R.layout.progress_layou, parent, false))
         else
               return HomeProductAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_card, parent, false))

    }

    override fun getItemViewType(position: Int): Int {
        if(homeFragment.isLoader)
            return if (position === prodList.size - 1) VIEW_TYPE_LOADING else VIEW_TYPE_NORMAL
         else
           return VIEW_TYPE_NORMAL
    }

    /*override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.prodName.typeface = CustomTypeface(context).ralewaySemiBold
        holder.prodprice.typeface = CustomTypeface(context).ralewayRegular
        holder.prodlocation.typeface = CustomTypeface(context).ralewayRegular
        holder.tv_category.typeface = CustomTypeface(context).ralewayMedium

        holder.prodName.text = prodList.get(position).post_name
        holder.prodlocation.text = prodList.get(position).locatioin
        holder.prodprice.text = "£ " +prodList.get(position).displayPrice
        holder.tv_category.text = prodList.get(position).category

        if(prodList.get(position).post_image.size!= 0) {
            Glide.with(this.context)
                .load(prodList.get(position).post_image.get(0))
                .placeholder(R.drawable.placeholder)
                .centerInside()
                .apply(RequestOptions.bitmapTransform(RoundedCorners(14)))
                .into(holder.prod_image);
        }
        holder.ll_card.setOnClickListener {
            OnitemCLickIterface?.OnitemClick(prodList.get(position))
            //   val intent = Intent(context, ProductListDetailsActivity::class.java)
            //  context.startActivity(intent)

        }
    }*/

    override fun getItemCount(): Int {
       return prodList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(getItemViewType(position)){
            VIEW_TYPE_NORMAL -> {

                (holder as ViewHolder).prodName.typeface = homeFragment.typefaSemibold
                (holder as ViewHolder).prodprice.typeface = homeFragment.typefaceregular
                (holder as ViewHolder).prodlocation.typeface = homeFragment.typefaceregular
                (holder as ViewHolder).tv_category.typeface = homeFragment.typefaSemibold

                (holder as ViewHolder).prodName.text = prodList.get(position).post_name
                (holder as ViewHolder).prodlocation.text = prodList.get(position).locatioin
                (holder as ViewHolder).prodprice.text = "£ " +prodList.get(position).displayPrice
                (holder as ViewHolder).tv_category.text = prodList.get(position).category

               if(prodList.get(position).post_image.size!= 0) {
                    Glide.with(this.context)
                        .load(prodList.get(position).post_image.get(0))
                        .placeholder(R.drawable.placeholder)
                        .centerInside()
                        .diskCacheStrategy( DiskCacheStrategy.ALL )
                        .apply( RequestOptions()
                            .override(Target.SIZE_ORIGINAL)
                            .format(DecodeFormat.PREFER_ARGB_8888))
                        .into(((holder as ViewHolder) as ViewHolder).prod_image);
                }

                (holder as ViewHolder).ll_card.setOnClickListener {
                    OnitemCLickIterface?.OnitemClick(prodList.get(position))
                    //   val intent = Intent(context, ProductListDetailsActivity::class.java)
                    //  context.startActivity(intent)

                }
            }
            VIEW_TYPE_LOADING->{
                (holder as ProgressViewHolder).progressBar.visibility = View.INVISIBLE
            }
        }
/*
        if(!homeFragment.isLoader){
            (holder as ViewHolder).prodName.typeface = CustomTypeface(context).ralewaySemiBold
            (holder as ViewHolder).prodprice.typeface = CustomTypeface(context).ralewayRegular
            (holder as ViewHolder).prodlocation.typeface = CustomTypeface(context).ralewayRegular
            (holder as ViewHolder).tv_category.typeface = CustomTypeface(context).ralewaySemiBold

            (holder as ViewHolder).prodName.text = prodList.get(position).post_name
            (holder as ViewHolder).prodlocation.text = prodList.get(position).locatioin
            (holder as ViewHolder).prodprice.text = "£ " +prodList.get(position).displayPrice
            (holder as ViewHolder).tv_category.text = prodList.get(position).category

            if(prodList.get(position).post_image.size!= 0) {
                Glide.with(this.context)
                    .load(prodList.get(position).post_image.get(0))
                    .placeholder(R.drawable.placeholder)
                    .centerInside()
                  .apply( RequestOptions()
                        .override(Target.SIZE_ORIGINAL)
                        .format(DecodeFormat.PREFER_ARGB_8888))
                    .into(((holder as ViewHolder) as ViewHolder).prod_image);



            }
            (holder as ViewHolder).ll_card.setOnClickListener {
                OnitemCLickIterface?.OnitemClick(prodList.get(position))
                //   val intent = Intent(context, ProductListDetailsActivity::class.java)
                //  context.startActivity(intent)

            }
        }else{
            (holder as ProgressViewHolder).progressBar.visibility = View.INVISIBLE

        }
*/
    }
}