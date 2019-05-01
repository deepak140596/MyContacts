package com.deepak.mycontacts

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val CONTACTS_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        askForPermissions()
        activity_main_grant_permission_btn.setOnClickListener {
            askForPermissions()
        }
        if (!isTaskRoot)
        {
            val intent = intent
            val intentAction = intent.action
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intentAction != null && intentAction.equals(Intent.ACTION_MAIN)) {
                finish()
                return
            }
        }
    }


    private fun askForPermissions() {
        ActivityCompat.requestPermissions(this,
            arrayOf(
                Manifest.permission.READ_CONTACTS),
            CONTACTS_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            CONTACTS_REQUEST_CODE ->{

                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // permission was granted

                    //isPermissionAcquired = true
                    startActivity(Intent(this,ContactsDisplayActivity::class.java))
                    finish()
                }
                else{
                    // permission was denied
                    // isPermissionAcquired = false
                    Toast.makeText(this,"Permission Denied!",Toast.LENGTH_SHORT).show()
                    // set empty list view
                }
            }

        }
    }
}
