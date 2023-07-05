package com.dazzlebloom.adapter

import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dazzlebloom.R
import com.dazzlebloom.apiresponse.MenuItem
import com.dazzlebloom.ui.AboutUsActivity
import com.dazzlebloom.ui.ContactUsActivity
import com.dazzlebloom.ui.MainActivity
import com.dazzlebloom.ui.fragments.home.HomeFragment
import com.dazzlebloom.utiles.Constants
import com.dazzlebloom.utiles.customtypeface.CustomTypeface
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.ContextCompat.startActivity







class SideMenuAdapter(val activity: MainActivity, val menuList: ArrayList<MenuItem>) :
    RecyclerView.Adapter<SideMenuAdapter.ViewHolder>() {

    class  ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val textView: AppCompatTextView = itemView.findViewById(R.id.tv_menu_item)
        val outerlink : AppCompatImageView = itemView.findViewById(R.id.img_outerlink)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.side_menu_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.typeface = CustomTypeface(activity).ralewaySemiBold
        val menuItem = menuList.get(position)
        holder.textView.text  = menuItem.title
        if(menuItem.title.equals("About Us") || menuItem.title.equals("Contact"))
            holder.outerlink.visibility = View.VISIBLE

        holder.textView.setOnClickListener {

         if(menuItem.title.equals("About Us"))  {
        // val intentaboutus = Intent(activity, AboutUsActivity::class.java)
             //activity.startActivity(intentaboutus)
             val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://dazzleandbloom.co.uk/about-us/"))
             activity.startActivity(browserIntent)
         }
            else if(menuItem.title.equals("Contact"))  {
             val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://dazzleandbloom.co.uk/contact/"))
             activity.startActivity(browserIntent)
            // val intenContact = Intent(activity, ContactUsActivity::class.java)
            // activity.startActivity(intenContact)
         }
         else if(menuItem.title.equals("Men"))  {
             val homeFragment: HomeFragment = activity!!.supportFragmentManager.findFragmentByTag(Constants.HOME_SHOP_FRAGMENT) as HomeFragment
             homeFragment.prodList.clear()
             homeFragment.homeProductAdapter?.notifyDataSetChanged()
             homeFragment?._binding?.tvTotalProd?.text =   "0 Results"

             homeFragment.selectedCatID = 603
             homeFragment.orderBy = "title"
             homeFragment.ordered = "ASC"
             homeFragment.searchString = ""
             homeFragment.pageNumber = 1
             homeFragment.isnothome = true
             homeFragment.minvalue = 0
             homeFragment.maxvalue = 10000
             homeFragment.location = arrayOf<Int>()
             homeFragment.locationlistPosition.clear()
             homeFragment.showMenuFilter(603, menuList.get(position).title )
             activity.drawerLayout.closeDrawer(Gravity.LEFT)
         }
         else if(menuItem.title.equals("Women"))  {
             val homeFragment: HomeFragment = activity!!.supportFragmentManager.findFragmentByTag(Constants.HOME_SHOP_FRAGMENT) as HomeFragment
             homeFragment.prodList.clear()
             homeFragment?.homeProductAdapter?.notifyDataSetChanged()
             homeFragment?._binding?.tvTotalProd?.text =   "0 Results"


             homeFragment.selectedCatID = 604
             homeFragment.orderBy = "title"
             homeFragment.ordered = "ASC"
             homeFragment.searchString = ""
             homeFragment.pageNumber = 1
             homeFragment.isnothome = true
             homeFragment.minvalue = 0
             homeFragment.maxvalue = 10000
             homeFragment.locationlistPosition.clear()
             homeFragment.location = arrayOf<Int>()
             homeFragment.showMenuFilter(604, menuList.get(position).title )
             activity.drawerLayout.closeDrawer(Gravity.LEFT)
         }
         else if(menuItem.title.equals("Accessories") )  {
             val homeFragment: HomeFragment = activity!!.supportFragmentManager.findFragmentByTag(Constants.HOME_SHOP_FRAGMENT) as HomeFragment
             homeFragment.prodList.clear()
             homeFragment.homeProductAdapter?.notifyDataSetChanged()
             homeFragment?._binding?.tvTotalProd?.text =   "0 Results"

             homeFragment.selectedCatID = 606
             homeFragment.orderBy = "title"
             homeFragment.ordered = "ASC"
             homeFragment.searchString = ""
             homeFragment.pageNumber = 1
             homeFragment.isnothome = true
             homeFragment.minvalue = 0
             homeFragment.maxvalue = 10000
             homeFragment.locationlistPosition.clear()
             homeFragment.location = arrayOf<Int>()
             homeFragment.showMenuFilter(606, menuList.get(position).title )
             activity.drawerLayout.closeDrawer(Gravity.LEFT)
         }
         else if(menuItem.title.equals("Kids"))  {
             val homeFragment: HomeFragment = activity!!.supportFragmentManager.findFragmentByTag(Constants.HOME_SHOP_FRAGMENT) as HomeFragment
             homeFragment.prodList.clear()
             homeFragment.homeProductAdapter?.notifyDataSetChanged()
             homeFragment?._binding?.tvTotalProd?.text =   "0 Results"

             homeFragment.selectedCatID = 607
             homeFragment.orderBy = "title"
             homeFragment.ordered = "ASC"
             homeFragment.searchString = ""
             homeFragment.pageNumber = 1
             homeFragment.minvalue = 0
             homeFragment.maxvalue = 10000
             homeFragment.locationlistPosition.clear()
             homeFragment.isnothome = true
             homeFragment.location = arrayOf<Int>()
             homeFragment.showMenuFilter(607, menuList.get(position).title )
             activity.drawerLayout.closeDrawer(Gravity.LEFT)
         }
            else{
               // val homeFragment: HomeFragment = activity!!.supportFragmentManager.findFragmentByTag(Constants.HOME_SHOP_FRAGMENT) as HomeFragment
               // homeFragment.showMenuFilter(menuList.get(position).ID, menuList.get(position).title )
                activity.drawerLayout.closeDrawer(Gravity.LEFT)
                activity.startFragment()
            }
        }
    }

    override fun getItemCount(): Int {
        return  menuList.size
    }
}