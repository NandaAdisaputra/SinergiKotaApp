package com.nandaadisaputra.sinergikotaapp.network

/**
 * Menggunakan 'object' agar ApiService bersifat Singleton (hanya ada satu instansi).
 * Ini memudahkan akses ke konstanta URL dari seluruh bagian aplikasi
 * tanpa perlu melakukan instansiasi ulang.
 */
object ApiService {

    /**
     * Alamat dasar (Base URL) server API.
     * Alamat "10.0.22" adalah IP khusus yang digunakan Emulator Android
     * untuk mengakses 'localhost' pada komputer host (PC).
     */
    private const val BASE_URL = "http://10.0.2.2:5000/api"

    /**
     * Endpoint untuk proses autentikasi login.
     * Metode: POST (x-www-form-urlencoded)
     */
    const val END_POINT_LOGIN = "$BASE_URL/auth/login"

    /**
     * Endpoint laporan (Reports).
     * Digunakan untuk dua fungsi:
     * 1. GET: Mengambil daftar laporan (List)
     * 2. POST: Mengunggah laporan baru (Multipart Form Data)
     */
    const val ENDPOINT_REPORTS = "$BASE_URL/reports"
    /**
     * Gunakan /images/ sesuai folder di wwwroot C#
//     */
    const val BASE_URL_IMAGE = "http://10.0.2.2:5000/images/"
}