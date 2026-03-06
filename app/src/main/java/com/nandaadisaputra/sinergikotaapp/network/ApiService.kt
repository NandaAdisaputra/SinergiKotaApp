package com.nandaadisaputra.sinergikotaapp.network


// Menggunakan 'object' agar ApiService bersifat Singleton (hanya ada satu instansi)
// dan bisa dipanggil langsung tanpa perlu inisialisasi (seperti static di Java)
object ApiService {

    /**
     * Alamat dasar server API.
     * Alamat "10.0.2.2" adalah IP khusus yang digunakan Emulator Android
     * untuk mengakses 'localhost' pada komputer pengembang.
     * Port 5000 biasanya merupakan port default untuk aplikasi Flask atau ASP.NET Core.
     */
    private const val BASE_URL = "http://10.0.2.2:5000/api"

    /**
     * Titik akhir (Endpoint) khusus untuk proses login.
     * Hasilnya akan menjadi: http://10.0.2.2:5000/api/auth/login
     * Variabel ini bersifat 'public' agar bisa diakses oleh AuthRepository.
     */
    const val END_POINT_LOGIN = "$BASE_URL/auth/login"
    const val ENDPOINT_REPORTS = "$BASE_URL/reports"
}