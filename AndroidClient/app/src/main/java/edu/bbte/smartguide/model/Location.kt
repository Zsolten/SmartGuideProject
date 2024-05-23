package edu.bbte.smartguide.model

class Location(
    val id: Long,
    val name: String,
    val pictureUrl: String,
    val city: String,
    val category: String,
    val description: String,
    val openHours: String,
    val prices: String,
    val googleMapsLink: String,
    val distance: Long
)