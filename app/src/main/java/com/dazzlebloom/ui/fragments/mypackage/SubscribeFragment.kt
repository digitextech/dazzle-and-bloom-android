package com.dazzlebloom.ui.fragments.mypackage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import com.dazzlebloom.R
import com.dazzlebloom.databinding.FragmentHomeBinding
import com.dazzlebloom.ui.MainActivity
import com.dazzlebloom.ui.MyPackageActivity
import com.dazzlebloom.utiles.customtypeface.CustomTypeface

class SubscribeFragment(val mainActivity: MyPackageActivity) : Fragment() {

    
    private lateinit var viewModel: SubscribeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(mainActivity).inflate(R.layout.subscribe_fragment, null)
        var packagename = view.findViewById<AppCompatTextView>(R.id.tv_package_name)
        var packagedesc =  view.findViewById<AppCompatTextView>(R.id.tv_package_desc)
        var packageprice = view.findViewById<AppCompatTextView>(R.id.tv_price)

        packagename.typeface = CustomTypeface(mainActivity).ralewaySemiBold
        packagedesc.typeface = CustomTypeface(mainActivity).ralewaySemiBold
        packageprice.typeface = CustomTypeface(mainActivity).ralewaySemiBold

        return view.rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SubscribeViewModel::class.java)

    }

}