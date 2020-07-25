package com.seoultech.livingtogether_android.contacts

import android.app.Application
import android.provider.ContactsContract
import androidx.lifecycle.MutableLiveData
import androidx.loader.content.CursorLoader
import com.seoultech.livingtogether_android.base.BaseViewModel
import com.seoultech.livingtogether_android.nok.model.NOKEntity

class ContactViewModel(application: Application) : BaseViewModel(application) {

    val contact = getContactsAll()


    private fun getContactsAll() : MutableLiveData<List<NOKEntity>> {
        val contacts = mutableListOf<NOKEntity>()
        val contactLiveData = MutableLiveData<List<NOKEntity>>()

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
                    contacts.add(NOKEntity(it.getString(name), it.getString(number)))
                } while (it.moveToNext())
                it.close()
            }
        }


        contactLiveData.value = contacts

        return contactLiveData
    }

}