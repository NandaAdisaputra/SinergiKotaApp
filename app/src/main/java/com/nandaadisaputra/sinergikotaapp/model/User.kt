package com.nandaadisaputra.sinergikotaapp.model

/**
 * Kelas Model untuk menyimpan data pengguna (User).
 * Menggunakan 'data class' agar Kotlin secara otomatis menangani penyimpanan
 * dan pemrosesan data objek ini.
 */
data class User(
    // ID unik pengguna dari database (bertipe angka bulat)
    val id: Int,

    // Nama akun atau ID login pengguna (bertipe teks)
    val username: String,

    // Nama lengkap asli pengguna yang akan ditampilkan di profil (bertipe teks)
    val fullName: String
)