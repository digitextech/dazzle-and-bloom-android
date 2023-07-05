package com.dazzlebloom.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.dazzlebloom.R
import com.dazzlebloom.ui.MainActivity
import com.dazzlebloom.ui.fragments.home.HomeFragment
import com.dazzlebloom.utiles.customtypeface.CustomTypeface
import com.dazzlebloom.utiles.customview.FilterFullScreenDialog

class FilterLocationAdapter(
    val activity: MainActivity,
    val locationlist: ArrayList<String>,
    val filterFullScreenDialog: FilterFullScreenDialog,
    val homeFragment: HomeFragment
) :
    RecyclerView.Adapter<FilterLocationAdapter.ViewHolder>() {

    class  ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val textView: AppCompatTextView = itemView.findViewById(R.id.tv_location)
        val chklocation: AppCompatCheckBox = itemView.findViewById(R.id.chk_location)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_filter_location, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.typeface = CustomTypeface(activity).ralewaySemiBold

        holder.textView.text  = locationlist.get(position)
        if(homeFragment.locationlistPosition.contains(position)){
            filterFullScreenDialog.selectedlocationArray.add(homeFragment.cityKeyID.get(position).toInt())
            holder.chklocation.isChecked = true
        }


        holder.chklocation.setOnCheckedChangeListener({ buttonView, isChecked ->
            if (isChecked){
               filterFullScreenDialog.selectedlocationArray.add(homeFragment.cityKeyID.get(position).toInt())
               if ( !homeFragment.locationlistPosition.contains(position))
                    homeFragment.locationlistPosition.add(position)
            }else{
                if (homeFragment.locationlistPosition.contains(position))
                homeFragment.locationlistPosition.remove(position)
                filterFullScreenDialog.selectedlocationArray.remove(homeFragment.cityKeyID.get(position).toInt())
            }
        })



    }

    override fun getItemCount(): Int {
        return  locationlist.size
    }
}