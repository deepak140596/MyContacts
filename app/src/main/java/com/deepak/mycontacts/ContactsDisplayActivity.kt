package com.deepak.mycontacts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_contacts_display.*
import java.util.*

class ContactsDisplayActivity : AppCompatActivity(),
    SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    val TAG = "CONTACTS_DISPLAY"
    lateinit var contactsViewModel: ContactsViewModel
    var contactsList : List<ContactItem> = emptyList()
    lateinit var adapter : ContactRowAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts_display)

        activity_contacts_display_rv.layoutManager = LinearLayoutManager(this)
        activity_contacts_display_progress_bar.visibility = View.VISIBLE

        contactsViewModel = ViewModelProviders.of(this).get(ContactsViewModel::class.java)
        //getContacts()

        adapter = ContactRowAdapter(this,contactsList)
        activity_contacts_display_rv.adapter = adapter

    }

    private fun getContacts(){
        contactsViewModel.getContactsList().observe(this, Observer {
            // if values change
            Log.d(TAG,"IT S: ${it.size}  CL S: ${contactsList.size} Equals: ${Utils.areTwoContactListEqual(it,contactsList)}")
            if(!Utils.areTwoContactListEqual(it,contactsList)){
                Log.d(TAG, "Values Changed")
                contactsList = it
                activity_contacts_display_progress_bar.visibility = View.INVISIBLE

                adapter.contactsList = contactsList
                adapter.notifyDataSetChanged()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_contact_search,menu)
        val searchItem = menu!!.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setIconifiedByDefault(true)
        searchView.queryHint = "Search"
        searchView.setOnQueryTextListener(this)

        return super.onCreateOptionsMenu(menu)

    }


    override fun onMenuItemActionExpand(item: MenuItem): Boolean {
        return false
    }

    override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
        onQueryTextChange("")
        return false
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {

        if (newText == null || newText.trim { it <= ' ' }.isEmpty()){
            adapter.contactsList = contactsList
            adapter.notifyDataSetChanged()
            return false
        }

        var filteredValues = ArrayList<ContactItem>(contactsList)
        for (contact in contactsList) {
            val searchString = contact.displayName + " " + contact.emails + " " + contact.phoneNumbers.toString()

            if (!searchString.trim().toLowerCase().contains(newText!!.trim().toLowerCase()))
                filteredValues.remove(contact)
        }

        adapter.contactsList = filteredValues
        adapter.notifyDataSetChanged()
        return false
    }

    override fun onResume() {
        super.onResume()
        getContacts()
        //contactsViewModel.getContactsList().removeObserver(this)
    }

    override fun onPause() {
        super.onPause()
        contactsViewModel.getContactsList().removeObservers(this)
    }

    override fun onBackPressed() {
        this.moveTaskToBack(true)

    }
}
