package com.deepak.mycontacts

import java.io.Serializable

class ContactItem(var id: Int = -1,
                  var displayName: String= "",
                  //var phoneNumbers: MutableList<String> = mutableListOf(),
                  //var phoneNumbersLabel : MutableList<String> = mutableListOf(),
                  var phoneNumbers: MutableList<PhoneItem> = mutableListOf(),
                  var emails: MutableList<EmailItem> = mutableListOf(),
                  var imgUri: String ="",
                  var lastUpdatedTimeStamp: String = "")
    :Serializable{

    override fun toString(): String {
        return "ContactItem(id=$id, displayName='$displayName', " +
                "phoneNumbers=$phoneNumbers," +
                " email='$emails'," +
                " imgUri='$imgUri')  lastUpdatedTimeStamp=$lastUpdatedTimeStamp"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ContactItem

        if (id != other.id) return false
        if (displayName != other.displayName) return false
        if (phoneNumbers != other.phoneNumbers) return false
        if (emails != other.emails) return false
        if (imgUri != other.imgUri) return false
        if (lastUpdatedTimeStamp != other.lastUpdatedTimeStamp) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + displayName.hashCode()
        result = 31 * result + phoneNumbers.hashCode()
        result = 31 * result + emails.hashCode()
        result = 31 * result + imgUri.hashCode()
        result = 31 * result + lastUpdatedTimeStamp.hashCode()
        return result
    }


}

class PhoneItem(var phoneNumber : String ="",
                var phoneLabel : String = "")
    :Serializable{
    override fun toString(): String {
        return "PhoneItem(phoneNumber='$phoneNumber', phoneLabel='$phoneLabel')"
    }
}

class EmailItem(var emailId : String ="",
                var emailLabel : String = "")
    :Serializable{
    override fun toString(): String {
        return "EmailItem(emailId='$emailId', emailLabel='$emailLabel')"
    }
}