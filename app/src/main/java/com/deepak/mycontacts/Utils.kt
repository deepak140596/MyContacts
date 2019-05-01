package com.deepak.mycontacts

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator

class Utils{
    companion object {

        fun getTextDrawable(text:String): TextDrawable? {

            var builder = TextDrawable.builder().beginConfig()
                .withBorder(4).endConfig().round()

            var textDrawable = builder.build(text, colorGenerator())
            return textDrawable
        }

        fun removeDuplicateString(originalList: MutableList<String>): MutableList<String> {
            var filteredList : MutableList<String> = mutableListOf()

            for(s in originalList){
                if(!filteredList.contains(s)){
                    filteredList.add(s)
                }
            }

            return filteredList
        }

        fun removeDuplicatePhoneItems(originalList : MutableList<PhoneItem>) : MutableList<PhoneItem>{
            var filteredList : MutableList<PhoneItem> = mutableListOf()

            for(phone in originalList){
                var isPhonePresent = false
                for(filteredPhones in filteredList){
                    if(phone.phoneNumber == filteredPhones.phoneNumber){
                        isPhonePresent = true
                    }
                }
                if(!isPhonePresent){
                    filteredList.add(phone)
                }
            }

            return filteredList
        }

        fun removeDuplicateEmailItems(originalList : MutableList<EmailItem>) : MutableList<EmailItem>{
            var filteredList : MutableList<EmailItem> = mutableListOf()

            for(email in originalList){
                var isEmailPresent = false
                for(filteredEmails in filteredList){
                    if(email.emailId == filteredEmails.emailId){
                        isEmailPresent = true
                    }
                }
                if(!isEmailPresent){
                    filteredList.add(email)
                }
            }

            return filteredList
        }

        fun colorGenerator(): Int {
            var colorGenerator = ColorGenerator.MATERIAL
            var color = colorGenerator.randomColor

            return color
        }

        fun callIntent(context: Context,phoneNumber:String){
            var callUri = "tel:$phoneNumber"
            var intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse(callUri)
            context.startActivity(intent)
        }

        fun emailIntent(context: Context,email:String){
            var intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_EMAIL,email)
            context.startActivity(Intent.createChooser(intent,"Send Email"))
        }

        fun msgIntent(context: Context,phoneNumber:String){
            var msgUri = "smsto:$phoneNumber"
            var intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse(msgUri)
            context.startActivity(intent)
        }

        fun areTwoContactListEqual(listA: List<ContactItem>,listB: List<ContactItem>): Boolean{
            if(listA.size != listB.size)
                return false
            for( i in 0.. (listA.size-1)){
                var itemA = listA[i]
                var itemB = listB[i]
                if(itemA.lastUpdatedTimeStamp != itemB.lastUpdatedTimeStamp)
                    return false
            }
            return true
        }
    }
}