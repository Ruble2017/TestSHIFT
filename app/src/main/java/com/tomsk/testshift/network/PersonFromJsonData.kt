package com.tomsk.testshift.network

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable



@Serializable
data class PersonFromJsonData(
    var results: List<Results>,
    var info: Info
)


@Entity(tableName = "results")
@Serializable
data class Results(
    @PrimaryKey
    var idd: Int,
    var gender: String,
    var name: Name,
    var location: Location,
    var email: String,
    var login: Login,
    var dob: Dob,
    var registered: Registered,
    var phone: String,
    var cell: String,
    //@PrimaryKey
    var id: Id,
    var picture: Picture,
    var nat: String
)
@Serializable
data class Coordinates (
    var latitude : String,
    var longitude : String
)

@Serializable
data class Dob (
    var date : String,
    var age : String
)
@Serializable
data class Id (
    var name : String,
    var varue : String
)
@Serializable
data class Info (
    var seed : String,
    var results : String,
    var page : String,
    var version : String
)
@Serializable
data class Location (
    var street : Street,
    var city : String,
    var state : String,
    var country : String,
    var postcode : String,
    var coordinates : Coordinates,
    var timezone : Timezone
)
@Serializable
data class Login (
    var uuid : String,
    var username : String,
    var password : String,
    var salt : String,
    var md5 : String,
    var sha1 : String,
    var sha256 : String
)
@Serializable
data class Name (
    var title : String,
    var first : String,
    var last : String
)
@Serializable
data class Picture (
    var large : String,
    var medium : String,
    var thumbnail : String
)
@Serializable
data class Registered (
    var date : String,
    var age : String
)
@Serializable
data class Street (
    var number : String,
    var name : String
)
@Serializable
data class Timezone (
    var offset : String,
    var description : String
)