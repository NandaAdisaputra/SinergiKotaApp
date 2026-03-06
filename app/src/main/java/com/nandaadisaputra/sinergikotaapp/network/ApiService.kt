package com.nandaadisaputra.sinergikotaapp.network

/**
 * Menggunakan 'object' agar ApiService bersifat Singleton (hanya ada satu instansi).
 * Ini memudahkan akses ke konstanta URL dari seluruh bagian aplikasi
 * tanpa perlu melakukan instansiasi ulang.
 */
object ApiService {

    /**
     * Alamat dasar (Base URL) server API.
     * Alamat "10.0.2.2" adalah IP khusus yang digunakan Emulator Android
     * untuk mengakses 'localhost' pada komputer host (PC).
     * Jika menggunakan perangkat fisik, ganti dengan alamat IP lokal PC Anda.
     */
    private const val BASE_URL = "http://10.0.2.2:5000/api"

    /**
     * Endpoint khusus untuk proses autentikasi login.
     * URL Lengkap: http://10.0.2.2:5000/api/auth/login
     */
    const val END_POINT_LOGIN = "$BASE_URL/auth/login"

    /**
     * Endpoint khusus untuk mengambil atau mengirim data laporan.
     * URL Lengkap: http://10.0.2.2:5000/api/reports
     */
    const val ENDPOINT_REPORTS = "$BASE_URL/reports"
}