package com.nandaadisaputra.sinergikotaapp.model

data class Report(
    val id: Int,
    val title: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val imagePath: String,
    val createdDate: String
)