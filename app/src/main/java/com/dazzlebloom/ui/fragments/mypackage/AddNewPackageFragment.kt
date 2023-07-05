package com.dazzlebloom.ui.fragments.mypackage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dazzlebloom.R

class AddNewPackageFragment : Fragment() {

    companion object {
        fun newInstance() = AddNewPackageFragment()
    }

    private lateinit var viewModel: AddNewPackageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_new_package_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddNewPackageViewModel::class.java)
        // TODO: Use the ViewModel
    }

}