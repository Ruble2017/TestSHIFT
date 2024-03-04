package com.tomsk.testshift.data


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tomsk.testshift.network.Results

@Dao
interface ItemDao {

    @Query("delete from results")
    suspend fun deleteAllFromPersons()

    @Query("SELECT * from results ORDER BY name ASC")
    suspend fun getResaltsItemsList(): List<Results>

//    @Query("SELECT * from results ORDER BY name ASC")
//    fun getAllItems(): Flow<List<Results>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(myDataList: List<Results>)

}