package com.seoultech.livingtogether_android.contacts

import android.app.Application
import android.provider.ContactsContract
import androidx.lifecycle.MutableLiveData
import androidx.loader.content.CursorLoader
import com.seoultech.livingtogether_android.base.BaseViewModel
import com.seoultech.livingtogether_android.nok.model.NOKEntity
import com.seoultech.livingtogether_android.util.StringUtil
import java.lang.StringBuilder

class ContactViewModel(application: Application) : BaseViewModel(application) {

    var contact = getContactsAllObservable()

    fun initContactResult(name: String) {
        if (name.isEmpty()) {
            contact.value = null
        } else {
            contact.value = getContactsAll(name)
        }
    }

    private fun getContactsAll(name: String) : List<NOKEntity> {
        val selectedContacts = mutableListOf<NOKEntity>()
        val contacts = getContactsAll()

        for (contact in contacts) {
            if (contact.name.contains(name)) {
                selectedContacts.add(contact)
            }
        }

        return selectedContacts
    }

    private fun getContactsAllObservable() : MutableLiveData<List<NOKEntity>> {
        val contactLiveData = MutableLiveData<List<NOKEntity>>()
        contactLiveData.value = getContactsAll()

        return contactLiveData
    }

    private fun getContactsAll() : List<NOKEntity> {
        val contacts = mutableListOf<NOKEntity>()


        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )
        val cursorLoader = CursorLoader(
            getApplication(), ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection,
            null, null, "UPPER(" + ContactsContract.Contacts.DISPLAY_NAME + ")ASC"
        )

        val c = cursorLoader.loadInBackground()

        c?.let {
            if (it.moveToFirst()) {
                val number = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val name = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                do {
                    contacts.add(NOKEntity(it.getString(name), StringUtil.removeDash(it.getColumnName(number))))
                } while (it.moveToNext())
                it.close()
            }
        }
        return contacts
    }
}