package com.android.matrimonyapp


import com.android.matrimonyapp.Result
import io.reactivex.Single
import retrofit2.http.GET


interface NetworkService {

    @GET("/api/?results=10")
    fun getMembers(): Single<Result>


}