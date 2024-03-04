package com.tomsk.testshift.data

import androidx.room.TypeConverter
import com.tomsk.testshift.network.Coordinates
import com.tomsk.testshift.network.Dob
import com.tomsk.testshift.network.Id
import com.tomsk.testshift.network.Info
import com.tomsk.testshift.network.Location
import com.tomsk.testshift.network.Login
import com.tomsk.testshift.network.Name
import com.tomsk.testshift.network.Picture
import com.tomsk.testshift.network.Registered
import com.tomsk.testshift.network.Street
import com.tomsk.testshift.network.Timezone

class PersonTypeConvertor {
    @TypeConverter
    fun fromCoordinates(coordinates: Coordinates): String {
        return "${coordinates.latitude},${coordinates.longitude}"
    }

    @TypeConverter
    fun toCoordinates(coordinatesString: String): Coordinates {
        val (latitude, longitude) = coordinatesString.split(",")
        return Coordinates(latitude, longitude)
    }

    @TypeConverter
    fun fromDob(dob: Dob): String {
        return "${dob.date},${dob.age}"
    }

    @TypeConverter
    fun toDob(dobString: String): Dob {
        val (date, age) = dobString.split(",")
        return Dob(date, age)
    }

    @TypeConverter
    fun fromId(id: Id): String {
        return "${id.name},${id.varue}"
    }

    @TypeConverter
    fun toId(idString: String): Id {
        val (name, varue) = idString.split(",")
        return Id(name, varue)
    }

    @TypeConverter
    fun fromInfo(info: Info): String {
        return "${info.seed},${info.results},${info.page},${info.version}"
    }

    @TypeConverter
    fun toInfo(infoString: String): Info {
        val (seed, result, page, version) =
            infoString.split(",")
        return Info(seed, result, page, version)
    }

    @TypeConverter
    fun fromLocation(location: Location): String {
        return "${location.street.number},${location.street.name},${location.city},${location.state}," +
                "${location.country},${location.postcode},${location.coordinates.latitude}," +
                "${location.coordinates.longitude},${location.timezone.offset},${location.timezone.description}"
    }

    @TypeConverter
    fun toLocation(locationString: String): Location {
        val arr = locationString.split(",")
        return Location(
            Street(arr[0], arr[1]), arr[2], arr[3], arr[4], arr[5],
            Coordinates(arr[6], arr[7]), Timezone(arr[8], arr[9])
        )
    }

    @TypeConverter
    fun fromLogin(login: Login): String {
        return "${login.uuid},${login.username},${login.password}," +
                "${login.salt},${login.md5},${login.sha1},${login.sha256}"
    }

    @TypeConverter
    fun toLogin(loginString: String): Login {
        val arr = loginString.split(",")
        return Login(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5], arr[6])
    }

    @TypeConverter
    fun fromName(name: Name): String {
        return "${name.title},${name.first},${name.last}"
    }

    @TypeConverter
    fun toName(nameString: String): Name {
        val (title, first, last) = nameString.split(",")
        return Name(title, first, last)
    }

    @TypeConverter
    fun fromPicture(picture: Picture): String {
        return "${picture.large},${picture.medium},${picture.thumbnail}"
    }

    @TypeConverter
    fun toPicture(pictureString: String): Picture {
        val (large, medium, thumbnail) = pictureString.split(",")
        return Picture(large, medium, thumbnail)
    }

    @TypeConverter
    fun fromRegistered(registered: Registered): String {
        return "${registered.date},${registered.age}"
    }

    @TypeConverter
    fun toRegistered(registeredString: String): Registered {
        val (date, age) = registeredString.split(",")
        return Registered(date, age)
    }

    @TypeConverter
    fun fromStreet(street: Street): String {
        return "${street.number},${street.name}"
    }

    @TypeConverter
    fun toStreet(streetString: String): Street {
        val (number, name) = streetString.split(",")
        return Street(number, name)
    }

    @TypeConverter
    fun fromTimezone(timezone: Timezone) : String{
        return "${timezone.offset},${timezone.description}"
    }

    @TypeConverter
    fun toTimezone(timezoneString:String):Timezone{
        val(offset, description) =timezoneString.split(",")
        return Timezone(offset, description)
    }

}