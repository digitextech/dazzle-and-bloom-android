package com.dazzlebloom.ui.fragments.myaccount

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.dazzlebloom.R
import com.dazzlebloom.ui.MainActivity
import com.dazzlebloom.databinding.FragmentMyAccountBinding
import com.dazzlebloom.utiles.ViewUtils



class MyAccountFragment(val mainActivity: MainActivity) : Fragment() {
    private var _binding: FragmentMyAccountBinding? = null
    private val binding get() = _binding!!
    lateinit var myAccountFragmentViewBind : MyAccountFragmentViewBind
    lateinit var myAccountFragmentOnClick : MyAccountFragmentOnClick

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewUtils.changeStatusBarcolor(activity!!,activity?.resources!!.getColor(R.color.status_bar_color))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMyAccountBinding.inflate(inflater, container , false)
         val root: View = binding.root
        myAccountFragmentViewBind = MyAccountFragmentViewBind(mainActivity,root)
        myAccountFragmentOnClick = MyAccountFragmentOnClick(mainActivity!!,myAccountFragmentViewBind!!)
        val imag_menu = root.findViewById<ImageView>(R.id.img_menu)
        imag_menu.setOnClickListener {
            mainActivity.drawerLayout.openDrawer(Gravity.START);
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}