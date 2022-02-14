package fr.isen.rey.androiderestaurant.model

import java.io.Serializable

data class PanierResult (var itemPanier: List<PanierModel>): Serializable
data class PanierModel (
    val name_fr: String,
    var pictures: String,
    val prices: Double,
    var total: Int
): Serializable
