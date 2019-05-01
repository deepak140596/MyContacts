package com.deepak.mycontacts

import android.content.Context
import android.preference.PreferenceManager

class SharedPreferenceDB{
    companion object {

        fun saveContactsSizeToDB(context: Context,size: Int){
            var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            var editor = sharedPreferences.edit()

            editor.putInt(context.getString(R.string.contacts_size),size)

            editor.apply()
            editor.commit()
        }

        fun getContactsSizeFromDB(context: Context): Int {
            var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            var size = sharedPreferences.getInt(context.getString(R.string.contacts_size),0)
            return size
        }
    }
}