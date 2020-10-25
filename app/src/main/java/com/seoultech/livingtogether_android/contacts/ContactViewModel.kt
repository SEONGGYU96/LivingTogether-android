package com.seoultech.livingtogether_android.contacts

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.provider.ContactsContract
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.loader.content.CursorLoader
import com.seoultech.livingtogether_android.nextofkin.data.NextOfKin
import com.seoultech.livingtogether_android.util.StringUtil
import java.lang.StringBuilder

class ContactViewModel(val application: Application) : ViewModel() {

    private val _contact = MutableLiveData<List<NextOfKin>>()
    val contact: LiveData<List<NextOfKin>>
        get() = _contact

    private val _isPermissionGranted = MutableLiveData<Boolean>()
    val isPermissionGranted: LiveData<Boolean>
        get() = _isPermissionGranted

    val contactCount: LiveData<String> = Transformations.map(contact) {
        val count = StringBuilder()
        if (it != null) {
            count.append(it.size)
        } else {
            count.append(0)
        }
        count.append("개 검색 완료").toString()
    }

    fun start() {
        _isPermissionGranted.value = checkPermission()
    }

    fun initContact() {
        _contact.value = getContactsAll()
    }

    fun initContactResult(name: String) {
        if (name.isEmpty()) {
            _contact.value = null
        } else {
            _contact.value = getContactsAll(name)
        }
    }

    private fun checkPermission() : Boolean {
        return ContextCompat.checkSelfPermission(application, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermissionResponse(grantResults: IntArray) : Boolean {
        if (grantResults.isEmpty()) {
            return false
        }

        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    private fun getContactsAll(name: String): List<NextOfKin> {
        val selectedContacts = mutableListOf<NextOfKin>()
        val contacts = getContactsAll()

        for (contact in contacts) {
            if (contact.name.contains(name)) {
                selectedContacts.add(contact)
            }
        }

        return selectedContacts
    }

    private fun getContactsAll(): List<NextOfKin> {
        val contacts = mutableListOf<NextOfKin>()


        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )
        val cursorLoader = CursorLoader(
            application, ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null,
            null, "UPPER(" + ContactsContract.Contacts.DISPLAY_NAME + ")ASC"
        )

        val c = cursorLoader.loadInBackground()

        c?.let {
            if (it.moveToFirst()) {
                val number = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val name = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                do {
                    contacts.add(NextOfKin(it.getString(name), StringUtil.removeDash(it.getString(number))))
                } while (it.moveToNext())
                it.close()
            }
        }
        return contacts
    }
}