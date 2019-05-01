package com.deepak.mycontacts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.jetbrains.anko.doAsync

class ContactsViewModel(application: Application) : AndroidViewModel(application){
    var repository: ContactsRepository = ContactsRepository(application)
    var contactsList : MutableLiveData<List<ContactItem>> = MutableLiveData()
    var contactByID : MutableLiveData<ContactItem> = MutableLiveData()

    fun getContactsList(): LiveData<List<ContactItem>> {

        doAsync {
                contactsList.postValue(repository.getContacts())
        }
        return contactsList
    }


    fun getContactByID(contactId : String): MutableLiveData<ContactItem> {
        doAsync {
            contactByID.postValue(repository.getContactByID(contactId))
        }
        return contactByID
    }
}