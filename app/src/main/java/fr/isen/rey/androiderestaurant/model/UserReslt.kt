package fr.isen.rey.androiderestaurant.model

import java.io.Serializable

data class UserData (val data: UserResult, val code: String = ""): Serializable

data class UserResult(
    val id : String,
    val id_shop : String,
    val code : String,
    val firstname : String,
    val lastname : String,
    val birth_date : String,
    val email : String,
    val phone : String,
    val address : String,
    val postal_code : String,
    val town : String,
    val token : String,
    val gcmtoken : String,
    val points : String,
    val create_date : String,
    val update_date : String
) : Serializable