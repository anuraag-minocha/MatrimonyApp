package com.android.matrimonyapp

import com.google.gson.annotations.SerializedName

data class Member(
    @SerializedName("name")
    var name: Name,
    @SerializedName("dob")
    var dob: DOB,
    @SerializedName("picture")
    var picture: Picture,
    @SerializedName("location")
    var location: Location
)

data class Name(

    @SerializedName("first")
    var first: String,

    @SerializedName("last")
    var last: String
)

data class Picture(

    @SerializedName("large")
    var medium: String
)

data class Location(

    @SerializedName("city")
    var city: String,

    @SerializedName("state")
    var state: String
)

data class DOB(

    @SerializedName("age")
    var age: Int
)
