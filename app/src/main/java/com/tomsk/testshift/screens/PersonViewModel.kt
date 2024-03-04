package com.tomsk.testshift.screens


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomsk.testshift.data.MainRepo
import com.tomsk.testshift.network.RandomuserMeApi
import com.tomsk.testshift.network.Results
import kotlinx.coroutines.launch
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


const val TAG = "PersonViewModel"

sealed interface PersoneUiState {
    object  Success: PersoneUiState
    object Error : PersoneUiState
    object Loading : PersoneUiState
}

class PersonViewModel() : ViewModel() {

    var personUiState: PersoneUiState by mutableStateOf(PersoneUiState.Loading)

    private val repo = MainRepo.get()

    private val appPath = repo.dir.toString()

    val persons = MutableLiveData<List<Results>>()

    private var personsFromDataBase = listOf<Results>()

    fun loadingPersonUiState (){
        personUiState = PersoneUiState.Loading
    }

    init {

        personsFromDataBase = repo.repoDataBasePersonsList.value!!
        if(personsFromDataBase.isEmpty()){
            getPersonsInfoFromNetwork()
        }else{
            persons.value = personsFromDataBase
            personUiState = PersoneUiState.Success
        }
    }

    fun getPersonsInfoFromNetwork() {
        Log.d(com.tomsk.testshift.data.TAG, "getPersonInfo START")
        viewModelScope.launch {
            try {
                val responce = RandomuserMeApi.retrofitService.getPersonsList()


                val responceIsSuccessful = responce.isSuccessful

                if (responceIsSuccessful) {
                    val list = responce.body()!!.results.toMutableList()
                    var i = 0
                    list.forEach {
                        it.idd = i
                        i++

                        //LoadPic
                        //https://randomuser.me/api/portraits/women/50.jpg
                        //https://randomuser.me/api/portraits/thumb/women/5.jpg
                        val url = it.picture.thumbnail
                        Log.d(TAG, "href pic = $url ")
                        val fileName = url.substring(url.lastIndexOf("/") + 1)
                        Log.d(TAG, "file name = $fileName ")

                        val pathToFile = "$appPath/$fileName"

                        Log.d(TAG, "appPath = $appPath  pathToFile = $pathToFile ")

                        var input: InputStream? = null
                        try {
                            val responseBody =
                                RandomuserMeApi.retrofitService.downloadFile(url).body()
                            if (responseBody != null) {

                                input = responseBody.byteStream()

                                val fos = FileOutputStream(pathToFile)
                                fos.use { output ->
                                    val buffer = ByteArray(4 * 1024)
                                    var read: Int
                                    while (input.read(buffer).also { read = it } != -1) {
                                        output.write(buffer, 0, read)
                                    }
                                    output.flush()
                                }
                            }

                            it.picture.thumbnail = pathToFile

                        } catch (e: IOException) {
                            personUiState = PersoneUiState.Error
                        } finally {
                            input?.close()
                        }
                    }
                    persons.value = list
                    repo.saveNetworListToRepoList(list)

                    repo.insertAllPersonsToDb(list)

                    Log.d(TAG, " >>>>>>>>>>>  ${persons.value}")
                }
                personUiState = PersoneUiState.Success
            } catch (e: IOException) {
                personUiState = PersoneUiState.Error
            }
        }
    }

    fun clearDataBase() {
        viewModelScope.launch {
            deleteJpg()
            repo.deleteAllFromDbPersons()
        }
    }

    fun deleteJpg() {
        val dir = repo.dir
        if (dir.isDirectory())
            for (child in dir.listFiles()!!) {
                if (child.name.contains(".jpg")) {
                    Log.d(TAG, " file name = ${child.name}")
                    child.deleteRecursively()
                }
            }

    }

}
