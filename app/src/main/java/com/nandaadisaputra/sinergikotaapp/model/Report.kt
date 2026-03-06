package com.nandaadisaputra.sinergikotaapp.model

/**
 * Data class Report digunakan sebagai model representasi data laporan.
 * Kotlin secara otomatis menyediakan fungsi pendukung seperti equals(), hashCode(), dan toString().
 */
data class Report(
    // ID unik untuk setiap laporan
    val id: Int,

    // Judul laporan yang dikirimkan user
    val title: String,

    // Deskripsi detail mengenai isi laporan
    val description: String,

    // Titik koordinat lintang untuk lokasi laporan di peta
    val latitude: Double,

    // Titik koordinat bujur untuk lokasi laporan di peta
    val longitude: Double,

    // Alamat atau path lokasi file gambar (URL/lokal) yang berkaitan dengan laporan
    val imagePath: String,

    // Tanggal pembuatan laporan dalam format String (contoh: "2026-03-06")
    val createdDate: String
)