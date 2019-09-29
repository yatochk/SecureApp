package com.yatochk.secure.app.model.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yatochk.secure.app.model.images.Image

@Dao
interface ImagesDao {

    @Query("SELECT * FROM Image")
    fun getImages(): LiveData<List<Image>>

    @Query("SELECT * FROM Image WHERE album = :albumName")
    fun getImages(albumName: String): LiveData<List<Image>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addImage(contact: Image)

    @Update
    fun updateImage(contact: Image)

    @Delete
    fun deleteImage(contact: Image)

}