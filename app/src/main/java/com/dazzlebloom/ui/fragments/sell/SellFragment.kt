package com.dazzlebloom.ui.fragments.sell

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dazzlebloom.ListDetailsActivity
import com.dazzlebloom.R
import com.dazzlebloom.databinding.FragmentMyAccountBinding
import com.dazzlebloom.databinding.FragmentSellBinding
import com.dazzlebloom.ui.MainActivity
import com.dazzlebloom.ui.MyListingActivity
import com.dazzlebloom.ui.MyPackageActivity
import com.dazzlebloom.utiles.BaseFragment
import com.dazzlebloom.utiles.ViewUtils
import com.dazzlebloom.utiles.customtypeface.CustomTypeface


class SellFragment (val mainActivity: MainActivity): BaseFragment() {
    private var _binding: FragmentSellBinding? = null
    private val binding get() = _binding!!

    companion object {
        public fun newInstance(bundle: Bundle): SellFragment {
            val fragment = SellFragment(MainActivity.getInstance()!!)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentSellBinding.inflate(inflater, container , false)
        val root: View = binding.root
        return  root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewUtils.changeStatusBarcolor(mainActivity!!,mainActivity?.resources!!.getColor(R.color.status_bar_color))
        binding.btnSubmitList.setOnClickListener {
              val mypackageActivity = Intent(mainActivity, ListDetailsActivity::class.java)
              mainActivity.startActivity(mypackageActivity)
        }
        binding.imgMenu.setOnClickListener {
            mainActivity.drawerLayout.openDrawer(Gravity.START);
        }
        settypeface()

    }

    private fun settypeface() {
        binding.tvDazzle.typeface = CustomTypeface(mainActivity).ralewayRegular
        binding.tvSellShip.typeface = CustomTypeface(mainActivity).ralewaySemiBold
        binding.tvSellDesc.typeface = CustomTypeface(mainActivity).ralewayRegular
        binding.tvFreelist.typeface = CustomTypeface(mainActivity).ralewaySemiBold
        binding.tvFreeDesc.typeface = CustomTypeface(mainActivity).ralewayRegular
        binding.tvPaydaytime.typeface = CustomTypeface(mainActivity).ralewaySemiBold
        binding.tvPaydaydesc.typeface = CustomTypeface(mainActivity).ralewayRegular
        binding.btnSubmitList.typeface = CustomTypeface(mainActivity).ralewaySemiBold
    }


}