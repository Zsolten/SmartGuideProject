package edu.bbte.smartguide.model

data class Location(
    val id: Long,
    val name: String,
    val city: String,
    val category: String,
    val description: String,
    val openHours: String,
    val price: String,
    val googleMapsLink: String,
    val distance: Long
)