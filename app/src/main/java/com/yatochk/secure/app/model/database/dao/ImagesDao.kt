package com.yatochk.secure.app.model.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yatochk.secure.app.model.contact.Contact
import com.yatochk.secure.app.model.images.Image

@Dao
interface ImagesDao {

    @Query("SELECT * FROM Image")
    fun getImages(): LiveData<List<Image>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addImage(contact: Contact)

    @Update
    fun updateImage(contact: Contact)

    @Delete
    fun deleteImage(contact: Contact)

}