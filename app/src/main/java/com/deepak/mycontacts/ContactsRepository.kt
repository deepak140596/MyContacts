package com.deepak.mycontacts

import android.app.Application
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import java.lang.Exception

class ContactsRepository(var application: Application){

    var TAG = "REPOSITORY"

    fun getContacts(): List<ContactItem> {
        //var contactsLiveList:  MutableLiveData<List<ContactItem>> = MutableLiveData()
        var contactsList : MutableList<ContactItem> = mutableListOf()
        var cursorContacts : Cursor?= null

        try{
            cursorContacts = application.contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,null,null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC")
        }catch (e : Exception){
            Log.e(TAG,"Error accessing Contacts! : $e")
        }

        if(cursorContacts!!.count > 0){
            //Log.d(TAG,"SIZE: ${cursorContacts!!.count}")
            //Log.d(TAG,"COL SIZE: ${cursorContacts!!.columnCount} \n COL NAMES: ${cursorContacts!!.columnNames}")

            while (cursorContacts.moveToNext()) {


                contactsList.add(generateSingleContact(cursorContacts))
            }

            //contactsLiveList.value = contactsList
            return contactsList
        }

        //contactsLiveList.value = null
        return emptyList()
    }

    fun generateSingleContact(cursorContacts:Cursor): ContactItem {
        var contactItem = ContactItem()

        var contactId = cursorContacts.getString(cursorContacts.getColumnIndex(ContactsContract.Contacts._ID))
        var displayName = cursorContacts.getString(cursorContacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)) + ""
        var lastUpdatedTimestamp = cursorContacts.getString(cursorContacts.getColumnIndex(ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP))

        //var fName = cursorContacts.getString(cursorContacts.getColumnIndex(ContactsContract.Contacts.Nam))
        var hasPhoneNumber = cursorContacts.getString(cursorContacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)).toInt()
        var imgUri = cursorContacts.getString(cursorContacts.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI))

        //Log.d(TAG,"Name: $displayName ..  ID: $contactId .. HP: $hasPhoneNumber ")
        if(imgUri != null) {
            //Log.d(TAG, "IMG URI: $imgUri")
            contactItem.imgUri = imgUri
        }

        contactItem.id = contactId.toInt()
        contactItem.displayName = displayName
        contactItem.lastUpdatedTimeStamp = lastUpdatedTimestamp+""

        // get phone numbers
        if(hasPhoneNumber > 0){
            var phoneCursor = application.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                , null
                , ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?"
                , arrayOf(contactId)
                , null
            )

            while (phoneCursor.moveToNext()){
                var phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                var phoneNumberLabel = phoneCursor.getInt(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))
                //Log.d(TAG,"PH : $phoneNumber")
                var phoneItem = PhoneItem(phoneNumber,getPhoneType(phoneNumberLabel))
                contactItem.phoneNumbers.add(phoneItem)
                //contactItem.phoneNumbersLabel.add(getPhoneType(phoneNumberLabel))
            }
            phoneCursor.close()
        }

        // get email
        var emailCursor = application.contentResolver.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
            arrayOf(contactId), null)

        while (emailCursor.moveToNext()){
            var email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
            var emailLabel = emailCursor.getInt(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE))
            if(email!=null) {
                //Log.d(TAG, "Email Lable: ${getEmailType(emailLabel)}")
                var emailItem = EmailItem(email,getEmailType(emailLabel))
                contactItem.emails.add(emailItem)
            }
        }

        return contactItem
    }

    fun getContactByID(contactId : String): ContactItem? {

        var contactCursor : Cursor ?= null
        try {
            contactCursor = application.contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                "_ID = $contactId",
                null,
                null
            )
        }catch (e : Exception){
            Log.e(TAG,"Error accessing Contact by ID! : $e")
        }

        if(contactCursor!!.count > 0){
            if(contactCursor!!.moveToFirst()){

                var contact = generateSingleContact(contactCursor)
                return contact
            }

        }
        return null
    }

    fun getPhoneType(type:Int): String {
        when(type){
            ContactsContract.CommonDataKinds.Phone.TYPE_HOME ->
                return "Home"
            ContactsContract.CommonDataKinds.Phone.TYPE_WORK ->
                return "Work"
            ContactsContract.CommonDataKinds.Phone.TYPE_OTHER ->
                return "Other"
            ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE ->
                return "Work Mobile"
        }
        return "Other"
    }

    fun getEmailType(type: Int): String{
        when(type){
            ContactsContract.CommonDataKinds.Email.TYPE_HOME ->
                return "Home"
            ContactsContract.CommonDataKinds.Email.TYPE_WORK ->
                return "Work"
            ContactsContract.CommonDataKinds.Email.TYPE_OTHER ->
                return "Other"
        }
        return "Other"
    }

}