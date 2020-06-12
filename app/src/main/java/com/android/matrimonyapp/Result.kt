package com.android.matrimonyapp

import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("results")
    var members: ArrayList<Member> = ArrayList()
)