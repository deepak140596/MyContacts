package com.deepak.mycontacts

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_contact_details.*
import kotlinx.android.synthetic.main.content_contact_details.*
import kotlinx.android.synthetic.main.row_email.view.*
import kotlinx.android.synthetic.main.row_phone_number.view.*
import java.lang.Exception

class ContactDetailsActivity : AppCompatActivity() {

    val TAG = "CONTACT_DETAILS"

    lateinit var contactItem: ContactItem
    lateinit var contactsViewModel : ContactsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_details)
        setSupportActionBar(toolbar)

        contactsViewModel = ViewModelProviders.of(this).get(ContactsViewModel::class.java)
        contactItem = intent.getSerializableExtra(getString(R.string.selected_contact_item)) as ContactItem

        inflateViews()

    }

    private fun inflateViews(){

        toolbar_layout.title = contactItem.displayName
        setBackgroundPicture()

        if(!contactItem.phoneNumbers.isEmpty()){
            contactItem.phoneNumbers = Utils.removeDuplicatePhoneItems(contactItem.phoneNumbers)
            inflatePhoneNumbers()
        }
        if(!contactItem.emails.isEmpty()){
            contactItem.emails = Utils.removeDuplicateEmailItems(contactItem.emails)
            inflateEmails()
        }
        Log.d(TAG,"CONTACT: ${contactItem.toString()}")
    }

    private fun setBackgroundPicture(){
        if(contactItem.imgUri.isEmpty()) {
            toolbar_layout.background = ColorDrawable(Utils.colorGenerator())
        }
        else if(!contactItem.imgUri.isEmpty()) {
            var uri = Uri.parse(contactItem.imgUri)
            val inputStream =  contentResolver.openInputStream(uri)
            val drawable = Drawable.createFromStream(inputStream, uri.toString())

            toolbar_layout.background = drawable
        }
    }

    private fun inflatePhoneNumbers(){
        content_contact_details_phone_label_tv.visibility = View.VISIBLE
        var phoneLL = content_contact_details_phone_ll

        for(phoneItem in contactItem.phoneNumbers) {
            var child = layoutInflater.inflate(R.layout.row_phone_number, null)

            child.row_phone_number_tv.text = phoneItem.phoneNumber
            child.row_phone_label_tv.text = phoneItem.phoneLabel
            child.row_phone_call_btn.setOnClickListener {
                Utils.callIntent(this@ContactDetailsActivity,phoneItem.phoneNumber)
            }

            child.row_phone_msg_btn.setOnClickListener {
               Utils.msgIntent(this@ContactDetailsActivity,phoneItem.phoneNumber)
            }

            phoneLL.addView(child)
        }
        phoneLL.visibility = View.VISIBLE

    }

    private fun inflateEmails(){
        content_contact_details_email_label_tv.visibility = View.VISIBLE
        var emailLL = content_contact_details_email_ll

        for(email in contactItem.emails) {
            var child = layoutInflater.inflate(R.layout.row_email, null)
            child.row_email_tv.text = email.emailId
            child.row_email_label_tv.text = email.emailLabel

            child.row_email_contact_btn.setOnClickListener {
                Utils.emailIntent(this@ContactDetailsActivity,email.emailId)
            }

            emailLL.addView(child)
        }
        emailLL.visibility = View.VISIBLE
    }

    private fun clearViews(){
        var phoneLL = content_contact_details_phone_ll
        phoneLL.removeAllViews()
        var emailLL = content_contact_details_email_ll
        emailLL.removeAllViews()
    }

    override fun onResume() {
        super.onResume()
        if (contactItem != null) {
            contactsViewModel.getContactByID(contactItem.id.toString()).observe(this, Observer {
                try {
                    if (contactItem.lastUpdatedTimeStamp != it.lastUpdatedTimeStamp) {
                        contactItem = it
                        clearViews()
                        inflateViews()
                        title = contactItem.displayName
                    }
                }catch (e: Exception){
                    Toast.makeText(this,"Contact doesn't exist.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            })
        }

    }
}
