package com.android.matrimonyapp

import com.android.matrimonyapp.Networking
import com.android.matrimonyapp.Result
import io.reactivex.Single

class Repository {

    private val networkService = Networking.create()

    fun getMembers(): Single<Result> {
        return networkService.getMembers()
    }

}