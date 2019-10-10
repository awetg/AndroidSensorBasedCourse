package com.example.retrofit_adapter

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

import retrofit2.Call
import retrofit2.http.Url

object DemoApi {
    const val URL = "https://en.wikipedia.org/w/"
    object Model {
        data class Results(@SerializedName("query")val query:MYQuery)
        data class MYQuery(@SerializedName("searchinfo")val searchInfo:SearchInfo?, @SerializedName("pages")val pages: HashMap<Int, Page>?)
        data class SearchInfo(@SerializedName("totalhits")val totalhits: Int)
        data class Page(@SerializedName("thumbnail")val thumbnail: Thumbnail)
        data class Thumbnail(@SerializedName("source")val source: String)
    }

/*    interface Service {
        @GET("api.php")
        fun presidentName(
            @Query("name") Name: String
        )
                : Call<Model.SearchInfo>
    }*/

    interface Service {

        @GET("api.php")
        fun presidentName(
            @Query("action") action: String,
            @Query("format") format: String,
            @Query("list") list: String,
            @Query("srsearch") srsearch: String)
                : Call<Model.Results>

        @GET("api.php")
        fun presidenImage(
            @Query("action") action: String,
            @Query("format") format: String,
            @Query("prop") prop: String,
            @Query("pithumbsize") pithumbsize: Int,
            @Query("titles") title: String)
                : Call<Model.Results>

        @Keep
        @GET
        fun loadImage(@Url url: String?): Call<ResponseBody>
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(Service::class.java)
}