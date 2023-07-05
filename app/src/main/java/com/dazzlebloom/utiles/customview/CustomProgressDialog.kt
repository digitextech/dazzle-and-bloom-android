package com.dazzlebloom.utiles.customview

import android.app.Activity
import android.app.Dialog
import android.view.Window
import com.dazzlebloom.R
import com.wang.avi.AVLoadingIndicatorView

class CustomProgressDialog(){
    var customProgress: CustomProgressDialog? = null
    private var dialog: Dialog?= null
    var avi : AVLoadingIndicatorView? = null

    public fun getInstance(): CustomProgressDialog {
        if (customProgress == null) {
            customProgress = CustomProgressDialog()
        }
        return customProgress as CustomProgressDialog
    }


    fun showProgress(context: Activity) {
        dialog = Dialog(context, R.style.NewDialog) as Dialog
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.progress_dialog_view)
        //  mProgressBar.getIndeterminateDrawable().setColorFilter(context.getResources()
        // .getColor(R.color.material_blue_gray_500), PorterDuff.Mode.SRC_IN);
        val activityIndicatorView = dialog?.findViewById(R.id.avi) as AVLoadingIndicatorView
        activityIndicatorView.show()
        activityIndicatorView.setIndicator("ballZigZag")
       // activityIndicatorView.startAnimation(activityIndicatorView!!.)
        // mProgressBar.setIndeterminate(true)
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.show()
    }

    fun hideProgress() {
        if (dialog != null) {
            dialog?.dismiss()
            dialog = null
        }
    }


    /*init {
        val inflater = context.layoutInflater
        val view = inflater.inflate(R.layout.progress_dialog_view, null)
        avi = view.findViewById(R.id.avi)
        dialog = CustomDialog(context)
        dialog?.setContentView(view)
       // setContentView(view)
    }

    class CustomDialog(context: Context) : Dialog(context, R.style.CustomDialogTheme) {
        init {
            // Set Semi-Transparent Color for Dialog Background
            window?.decorView?.rootView?.setBackgroundResource(R.color.filter)
            window?.decorView?.setOnApplyWindowInsetsListener { _, insets ->
                insets.consumeSystemWindowInsets()
            }
        }
    }*/


        fun showdialo(){
            avi?.show()
        }

        fun hidedialo(){
         avi?.hide()
        }

}