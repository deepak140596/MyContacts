package com.deepak.mycontacts

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_contact.view.*
import org.jetbrains.anko.image
import org.jetbrains.anko.imageURI
import java.lang.NullPointerException

class ContactRowAdapter(var context: Context, var contactsList: List<ContactItem>)
    : RecyclerView.Adapter<ContactRowAdapter.ViewHolder>(){

    var TAG = "CONTACT_ROW"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_contact, parent, false)

        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contactItem = contactsList[position]
        holder.bindItems(context,contactItem)

        holder.itemView.setOnClickListener {
            var intent = Intent(context,ContactDetailsActivity::class.java)
            intent.putExtra(context.getString(R.string.selected_contact_item),contactItem)
            try {
                context.startActivity(intent)
            }catch (e: NullPointerException){
                Toast.makeText(context,"Contact doesn't exist.",Toast.LENGTH_SHORT).show()
            }

        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var TAG = "CONTACT_ROW"
        lateinit var context: Context

        fun bindItems(context: Context, contactItem: ContactItem){
            this.context = context

            itemView.row_contact_name_tv.text = contactItem.displayName
            if(contactItem.phoneNumbers.size > 0) {
                itemView.row_contact_call_btn.setOnClickListener {
                    Utils.callIntent(context,contactItem.phoneNumbers[0].phoneNumber)
                }
                itemView.row_contact_msg_btn.setOnClickListener {
                    Utils.msgIntent(context,contactItem.phoneNumbers[0].phoneNumber)
                }
                itemView.row_contact_call_btn.visibility = View.VISIBLE
                itemView.row_contact_msg_btn.visibility = View.VISIBLE
            }else if(contactItem.phoneNumbers.size == 0){
                itemView.row_contact_call_btn.visibility = View.INVISIBLE
                itemView.row_contact_msg_btn.visibility = View.INVISIBLE
            }

            if(contactItem.imgUri.isEmpty()) {
                itemView.row_contact_iv.imageURI = null
                itemView.row_contact_iv.background = Utils.getTextDrawable(contactItem.displayName[0].toString())
            }
            else if(!contactItem.imgUri.isEmpty()){
                var uri = Uri.parse(contactItem.imgUri)
                itemView.row_contact_iv.imageURI = null
                itemView.row_contact_iv.imageURI = uri
            }

        }


    }
}