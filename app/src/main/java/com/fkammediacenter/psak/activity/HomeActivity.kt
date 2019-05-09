package com.fkammediacenter.psak.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.fkammediacenter.psak.R
import com.fkammediacenter.psak.fragments.donasi.DonasiFragment
import com.fkammediacenter.psak.fragments.donatur.DonaturFragment
import com.fkammediacenter.psak.fragments.home.HomeFragment
import com.fkammediacenter.psak.fragments.kinerja.KinerjaFragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private var permissionsRequired = arrayOf(
        Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val PERMISSION_CALLBACK_CONSTANT = 100
    private val REQUEST_PERMISSION_SETTING = 101
    private var permissionStatus: SharedPreferences? = null
    private var sentToSettings = false

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content,
                        HomeFragment(), HomeFragment::class.java.simpleName).commit()


                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_donasi -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content,
                        DonasiFragment(), DonasiFragment::class.java.simpleName).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_donatur -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content,
                        DonaturFragment(), DonaturFragment::class.java.simpleName).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_kinerja -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content,
                        KinerjaFragment(), KinerjaFragment::class.java.simpleName).commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
///////////////////////////////////////////////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportFragmentManager.beginTransaction()
            .add(R.id.main_content,
                HomeFragment(), HomeFragment::class.java.simpleName).commit()

        navigation.disableShiftMode()
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        permissionStatus = getSharedPreferences("permissionStatus", Context.MODE_PRIVATE)
        requestPermission()

    }

    private fun requestPermission() {

        if (ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this,permissionsRequired[3]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[0])
                || ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[1])
                || ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[2])) {
                //Show Information about why you need the permission
                getAlertDialog()
            }else if (permissionStatus!!.getBoolean(permissionsRequired[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Need Multiple Permissions")
                builder.setMessage("This app needs permissions.")
                builder.setPositiveButton("Grant") { dialog, which ->
                    dialog.cancel()
                    sentToSettings = true
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING)
                    Toast.makeText(applicationContext, "Go to Permissions to Grant ", Toast.LENGTH_LONG).show()
                }
                builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
                builder.show()
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
            }

            //   txtPermissions.setText("Permissions Required")

            val editor = permissionStatus!!.edit()
            editor.putBoolean(permissionsRequired[0], true)
            editor.apply()
        } else {
            //You already have the permission, just go ahead.

            Toast.makeText(applicationContext, "Allowed All Permissions", Toast.LENGTH_LONG).show()
        }


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            var allgranted = false
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true
                } else {
                    allgranted = false
                    break
                }
            }

            if (allgranted) {
                Toast.makeText(applicationContext, "Allowed All Permissions", Toast.LENGTH_LONG).show()
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[0])
                || ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[1])
                || ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[2])) {

                getAlertDialog()
            } else {
                Toast.makeText(applicationContext, "Unable to get Permission", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onPostResume() {
        super.onPostResume()
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                Toast.makeText(applicationContext, "Allowed All Permissions", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Need Multiple Permissions")
        builder.setMessage("This app needs permissions.")
        builder.setPositiveButton("Grant") { dialog, which ->
            dialog.cancel()
            ActivityCompat.requestPermissions(this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        builder.show()
    }


    //disable Shifting Mode

    companion object {
        @SuppressLint("RestrictedApi")
        fun BottomNavigationView.disableShiftMode() {
            val menuView = getChildAt(0) as BottomNavigationMenuView
            try {
                val shiftingMode = menuView::class.java.getDeclaredField("mShiftingMode")
                shiftingMode.isAccessible = true
                shiftingMode.setBoolean(menuView, false)
                shiftingMode.isAccessible = false
                for (i in 0 until menuView.childCount) {
                    val item = menuView.getChildAt(i) as BottomNavigationItemView
                    item.setShifting(false)
                    // set once again checked value, so view will be updated
                    item.setChecked(item.itemData.isChecked)
                }
            } catch (e: NoSuchFieldException) {
                Log.e("TAG", "Unable to get shift mode field", e)
            } catch (e: IllegalStateException) {
                Log.e("TAG", "Unable to change value of shift mode", e)
            }
        }

    }
}
