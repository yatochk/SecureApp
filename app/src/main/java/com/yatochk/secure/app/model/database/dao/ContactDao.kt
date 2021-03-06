package com.yatochk.secure.app.model.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yatochk.secure.app.model.contact.Contact

@Dao
interface ContactDao {

    @Query("SELECT * FROM Contact")
    fun getContacts(): LiveData<List<Contact>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addContact(contact: Contact)

    @Update
    suspend fun updateContact(contact: Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)

}