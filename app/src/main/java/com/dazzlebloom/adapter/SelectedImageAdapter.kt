package com.dazzlebloom.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.dazzlebloom.R
import com.dazzlebloom.apiresponse.ImageModel
import com.dazzlebloom.ui.CreateListingActivity

class SelectedImageAdapter(val imagelist : ArrayList<ImageModel>, val activity : CreateListingActivity): RecyclerView.Adapter<SelectedImageAdapter.ViewHolder>() {

    class ViewHolder(child: View) : RecyclerView.ViewHolder(child){
        val imgView = child.findViewById<AppCompatImageView>(R.id.img_selected)
        val imgdelete = child.findViewById<AppCompatImageView>(R.id.img_delete_selected)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return SelectedImageAdapter.ViewHolder(LayoutInflater.from(activity).inflate(R.layout.image_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(activity)

            .load(imagelist.get(position).ImagelistingUrl)
            .error(R.drawable.placeholder)
            .centerInside()
            .apply( RequestOptions()
                .override(Target.SIZE_ORIGINAL)
                .format(DecodeFormat.PREFER_ARGB_8888))
            .into(holder.imgView)

        holder.imgdelete.setOnClickListener {

            activity.attachedImageIds.remove(imagelist.get(position).ImagelistingId)
            imagelist.removeAt(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return imagelist.size
    }
}