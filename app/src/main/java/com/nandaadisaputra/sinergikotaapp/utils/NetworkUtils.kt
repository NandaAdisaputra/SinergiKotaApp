package com.nandaadisaputra.sinergikotaapp.utils

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

//Objek ini berfungsi sebagai penyedia fungsi
// bantuan (utility) untuk menangani koneksi jaringan
// secara manual tanpa menggunakan library pihak ketiga.
object NetworkUtils {
    fun get_connection(url_target: String, method: String): HttpURLConnection {
        // Mengubah string alamat URL menjadi objek URL
        val url = URL(url_target)

        // Membuka koneksi ke server melalui protokol HTTP
        val conn = url.openConnection() as HttpURLConnection

        // Menentukan metode HTTP (contoh: GET, POST, PUT, atau DELETE)
        conn.requestMethod = method

        // Mengatur header agar server tahu format data yang dikirim adalah form-urlencoded
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

        // Mengatur header agar aplikasi menerima respon dalam format JSON
        conn.setRequestProperty("Accept", "application/json")

        // Batas waktu maksimal untuk mencoba terhubung ke server (10 detik)
        conn.connectTimeout = 10000

        // Batas waktu maksimal untuk menunggu data dari server (10 detik)
        conn.readTimeout = 10000

        // Jika metodenya POST, izinkan aplikasi untuk mengirimkan data (output) ke server
        if (method == "POST") conn.doOutput = true

        return conn
    }
    fun read_stream(stream: InputStream): String {
        // Menyiapkan pembaca (reader) untuk mengambil data dari aliran input
        val reader = BufferedReader(InputStreamReader(stream))

        // Tempat penampungan sementara untuk menyusun teks yang dibaca
        val sb = StringBuilder()
        var line: String?

        try {
            // Membaca data baris demi baris sampai habis
            while (reader.readLine().also { line = it } != null) {
                sb.append(line)
            }
        } finally {
            // Memastikan aliran data ditutup setelah selesai untuk menghemat memori
            stream.close()
        }

        // Mengembalikan hasil akhir dalam bentuk String utuh
        return sb.toString()
    }
}