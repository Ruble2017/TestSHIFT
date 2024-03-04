package com.tomsk.testshift.data

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.tomsk.testshift.network.Results
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking


const val TAG = "mainRepo"

class MainRepo private constructor(context: Context) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val dataBase = Room.databaseBuilder(context, PersonDataBase::class.java, "persons")
        .build()

    private val itemDao = dataBase.itemDao()

    private var repoNetworkPersonsList = MutableLiveData<List<Results>>()

    val repoDataBasePersonsList = MutableLiveData<List<Results>>()

    val dir = context.dataDir


    //fun getAllItemsStream(): Flow<List<Results>> = itemDao.getAllItems()

    fun deleteAllFromDbPersons() {
        coroutineScope.async {
            itemDao.deleteAllFromPersons()
        }
    }

    fun insertAllPersonsToDb(myDataList: List<Results>) {
        coroutineScope.async {
            itemDao.insertAll(myDataList)
        }
    }

    suspend fun getResultsItemsFromDbAsync(): Deferred<List<Results>> =

        coroutineScope.async {
            return@async itemDao.getResaltsItemsList()
        }

    fun saveNetworListToRepoList(list: MutableList<Results>) {
        repoNetworkPersonsList.value = list
    }


    fun getPersonForDetails(itemId: Int): Results? {
        return repoNetworkPersonsList.value?.find { it.idd == itemId }
    }


    companion object {
        private var INSTANCE: MainRepo? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = MainRepo(context)
            }

            runBlocking {
                INSTANCE!!.repoDataBasePersonsList.value =  INSTANCE!!.getResultsItemsFromDbAsync().await()
                INSTANCE!!.repoNetworkPersonsList.value=INSTANCE!!.repoDataBasePersonsList.value
            }

        }

        fun get(): MainRepo {
            return INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized")
        }
    }

}