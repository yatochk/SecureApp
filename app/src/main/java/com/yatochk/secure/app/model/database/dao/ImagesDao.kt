package com.yatochk.secure.app.model.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yatochk.secure.app.model.images.Image

@Dao
interface ImagesDao {

    @Query("SELECT * FROM Image ORDER BY album")
    fun getImages(): LiveData<List<Image>>

    @Query("SELECT * FROM Image WHERE album = :albumName")
    fun getImages(albumName: String): LiveData<List<Image>>

    @Query("SELECT DISTINCT album FROM Image WHERE album != :excepted")
    fun getAlbumsExcept(excepted: String): LiveData<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addImage(image: Image)

    @Update
    suspend fun updateImage(image: Image)

    @Delete
    suspend fun deleteImage(image: Image)

}