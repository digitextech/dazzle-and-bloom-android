package com.dazzlebloom.ui

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.dazzlebloom.R
import com.dazzlebloom.databinding.ActivityMainBinding

import com.dazzlebloom.ui.fragments.home.HomeFragment
import com.dazzlebloom.ui.fragments.myaccount.MyAccountFragment
import com.dazzlebloom.utiles.Constants
import android.widget.TextView

import com.google.android.material.internal.BaselineLayout

import android.view.ViewGroup

import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.dazzlebloom.retrofit.ApiInterface
import com.dazzlebloom.retrofit.RetrofitHelper
import com.dazzlebloom.ui.fragments.sell.SellFragment
import com.dazzlebloom.utiles.ViewUtils
import com.dazzlebloom.utiles.customtypeface.CustomTypeface
import org.json.JSONArray

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dazzlebloom.adapter.SideMenuAdapter
import com.dazzlebloom.apiresponse.MenuItem
import com.dazzlebloom.utiles.sheardpreference.AppSheardPreference
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.tasks.Task

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    public lateinit var drawerLayout: DrawerLayout
    var menuList : ArrayList<MenuItem>? = ArrayList()
    var drawermenu : RecyclerView ?= null
    var sideMenuAdapter : SideMenuAdapter ?= null
    private var installStateUpdatedListener: InstallStateUpdatedListener? = null
    private val FLEXIBLE_APP_UPDATE_REQ_CODE = 123
    private var appUpdateManager: AppUpdateManager? = null

    companion object{
        var mainActivity: MainActivity? = null
        fun getInstance() : MainActivity? {
            return  mainActivity
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());


        mainActivity = this
        setSupportActionBar(binding!!.appBarMain.toolbar)


        drawerLayout = binding.drawerLayout
        drawermenu = binding.menuitemRecyclerView
        binding.cardCrossMenu.setOnClickListener {
            binding.drawerLayout.closeDrawer(Gravity.LEFT)
        }


        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            
            setOf(R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout
        )
        setNavigationTypeface()
        binding.appBarMain.contentMain.bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.nav_shop -> {
                    startFragment()
                }
                R.id.nav_save -> {
                    val bookMarkActivity = Intent(this,BookMarkActivity::class.java)
                    startActivity(bookMarkActivity)

                }
                R.id.nav_message -> {
                    if(AppSheardPreference.fetchIntFromAppPreference(Constants.USERID)!=0) {
                        val messageIntent = Intent(mainActivity, MessageActivity::class.java)
                        startActivity(messageIntent)
                    }else{
                        val intent = Intent(mainActivity, EmailLoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                }
                R.id.nav_sell -> {

                        val manager: FragmentManager = this.supportFragmentManager
                        val transaction: FragmentTransaction = manager.beginTransaction()
                        transaction.add(R.id.nav_host_fragment_content_main, SellFragment(this), Constants.MYACCOUNT_FRAGMENT)
                        transaction.addToBackStack(null)
                        transaction.commit()

                }
                R.id.nav_account -> {
                    if(AppSheardPreference.fetchIntFromAppPreference(Constants.USERID)!=0) {
                        val manager: FragmentManager = this.supportFragmentManager
                        val transaction: FragmentTransaction = manager.beginTransaction()
                        transaction.add(
                            R.id.nav_host_fragment_content_main,
                            MyAccountFragment(this),
                            Constants.MYACCOUNT_FRAGMENT
                        )
                        transaction.addToBackStack(null)
                        transaction.commit()
                    }else{
                        val intent = Intent(mainActivity, EmailLoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                }
            }

            return@setOnItemSelectedListener true
        }



        if(ViewUtils.isNetworkAvailable(this)) {
            startFragment()
            callApiforMenu()
        } else
           ViewUtils.showdialog(this, "You don't have internet connection")

        checkAppStoreVersion()

    }

    private fun checkAppStoreVersion() {
        installStateUpdatedListener = InstallStateUpdatedListener { state: InstallState ->
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate()
            } else if (state.installStatus() == InstallStatus.INSTALLED) {
                removeInstallStateUpdateListener()
            } else {
                Toast.makeText(
                    applicationContext,
                    "InstallStateUpdatedListener: state: " + state.installStatus(),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        appUpdateManager!!.registerListener(installStateUpdatedListener)
        checkUpdate();
    }

    fun checkUpdate(){
        val appUpdateInfoTask: Task<AppUpdateInfo> = appUpdateManager!!.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() === UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                startUpdateFlow(appUpdateInfo)
            } else if (appUpdateInfo.installStatus() === InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate()
            }
        }
    }

    private fun startUpdateFlow(appUpdateInfo: AppUpdateInfo) {
        try {
            appUpdateManager!!.startUpdateFlowForResult(appUpdateInfo,
                AppUpdateType.FLEXIBLE, this, FLEXIBLE_APP_UPDATE_REQ_CODE)
        } catch (e: IntentSender.SendIntentException) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FLEXIBLE_APP_UPDATE_REQ_CODE) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Update canceled by user! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(),"Update success! Result Code: " + resultCode, Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getApplicationContext(), "Update Failed! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
                checkUpdate();
            }

        }
    }

    private fun popupSnackBarForCompleteUpdate() {
        Snackbar.make(findViewById<View>(android.R.id.content).rootView, "New app is ready!", Snackbar.LENGTH_INDEFINITE)
            .setAction("Install") { view: View? ->
                if (appUpdateManager != null) {
                    appUpdateManager!!.completeUpdate()
                }
            }
            .setActionTextColor(resources.getColor(R.color.pink_1))
            .show()
    }

    private fun removeInstallStateUpdateListener() {
        if (appUpdateManager != null) {
            appUpdateManager!!.unregisterListener(installStateUpdatedListener)
        }
    }

    override fun onStop() {
        super.onStop()
        removeInstallStateUpdateListener()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    public fun openAccount(){
        val manager: FragmentManager = this.supportFragmentManager
        val transaction: FragmentTransaction = manager.beginTransaction()
        transaction.add(R.id.nav_host_fragment_content_main, MyAccountFragment(this), Constants.MYACCOUNT_FRAGMENT)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun callApiforMenu() {
       // ViewUtils.showDialog(this)
        var retrofit = RetrofitHelper.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)
        lifecycleScope.launchWhenCreated {
            val response = apiInterface.getMenu()
          //  ViewUtils.dismissDialog()
            if(response.isSuccessful){

                val menuItem = MenuItem(1,"Home" )
                menuList?.add(menuItem)
                val jarray: JSONArray = JSONArray(response.body()?.string())


                 for(i in 0 until jarray.length()){
                      val menuobject = jarray.getJSONObject(i)

                         val menuItem = MenuItem(menuobject.getInt("ID"), menuobject.getString("title"))
                         menuList?.add(menuItem)
                 }
                if(menuList?.size!! > 0){
                    drawermenu?.layoutManager = LinearLayoutManager(this@MainActivity)
                    sideMenuAdapter = SideMenuAdapter(this@MainActivity,menuList!!)
                    drawermenu?.adapter = sideMenuAdapter

                }
            }else{
                ViewUtils.showdialog(this@MainActivity,"Something Wrong for menu items")
            }
        }



    }

    public fun startFragment() {
        val manager: FragmentManager = this.supportFragmentManager
        val transaction: FragmentTransaction = manager.beginTransaction()
        transaction.add(R.id.nav_host_fragment_content_main, HomeFragment(this), Constants.HOME_SHOP_FRAGMENT)
        transaction.addToBackStack(null)
        transaction.commitAllowingStateLoss()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    fun setNavigationTypeface() {
        val navigationGroup = binding?.appBarMain?.contentMain?.bottomNavigationView?.getChildAt(0) as ViewGroup
        for (i in 0 until navigationGroup.childCount) {
            val navUnit = navigationGroup.getChildAt(i) as ViewGroup
            for (j in 0 until navUnit.childCount) {
                val navUnitChild: View = navUnit.getChildAt(j)
                if (navUnitChild is BaselineLayout) {
                    val baselineLayout = navUnitChild as BaselineLayout
                    for (k in 0 until baselineLayout.childCount) {
                        val baselineChild: View = baselineLayout.getChildAt(k)
                        if (baselineChild is TextView) {
                            val textView = baselineChild as TextView
                            textView.setTypeface(CustomTypeface(this).ralewaySemiBold)
                        }
                    }
                }
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() ==1) {
            finish();
        }else{
            super.onBackPressed()
            binding.appBarMain.contentMain.bottomNavigationView?.getMenu()?.findItem(R.id.nav_shop)?.setChecked(true);

        }
    }



    public fun showsubmitlisting(){
        val args = Bundle()
       // SellFragment.newInstance(args)
        val manager: FragmentManager = this.supportFragmentManager
        val transaction: FragmentTransaction = manager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment_content_main, SellFragment.newInstance(args), Constants.MYACCOUNT_FRAGMENT)
        transaction.addToBackStack(null)
        transaction.commitAllowingStateLoss()
        binding.appBarMain.contentMain.bottomNavigationView?.getMenu()?.findItem(R.id.nav_sell)?.setChecked(true);

    }

    public fun showaccount(){
        if(AppSheardPreference.fetchIntFromAppPreference(Constants.USERID)!=0) {
            val manager: FragmentManager = this.supportFragmentManager
            val transaction: FragmentTransaction = manager.beginTransaction()
            transaction.replace(
                R.id.nav_host_fragment_content_main,
                MyAccountFragment(this),
                Constants.MYACCOUNT_FRAGMENT
            )
            transaction.addToBackStack(null)
            transaction.commitAllowingStateLoss()
            binding.appBarMain.contentMain.bottomNavigationView?.getMenu()?.findItem(R.id.nav_account)?.setChecked(true);
        }else{
            val intent = Intent(MainActivity.mainActivity, EmailLoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }



}