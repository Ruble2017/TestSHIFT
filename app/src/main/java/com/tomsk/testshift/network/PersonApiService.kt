package com.tomsk.testshift.network


import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url


private const val BASE_URL = "https://randomuser.me/"

private val retrofit = Retrofit.Builder()
    //.addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()



interface PersonApiService {

    @GET("api/?results=10")
    suspend fun getPersonsList(): Response<PersonFromJsonData>

    @Streaming
    @GET
    suspend fun downloadFile(@Url fileUrl:String): Response<ResponseBody>


}

object RandomuserMeApi {
    val retrofitService : PersonApiService by lazy {
        retrofit.create(PersonApiService::class.java)
    }
}

