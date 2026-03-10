package com.nandaadisaputra.sinergikotaapp.utils

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Utility untuk menangani koneksi HTTP secara manual.
 * Dioptimalkan untuk menangani ErrorStream (Status 401/404/500).
 */
object NetworkUtils {

    fun get_connection(url_target: String, method: String): HttpURLConnection {
        val url = URL(url_target)
        val conn = url.openConnection() as HttpURLConnection

        conn.requestMethod = method
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        conn.setRequestProperty("Accept", "application/json")

        // Timeout 10 detik untuk koneksi dan pembacaan data
        conn.connectTimeout = 10000
        conn.readTimeout = 10000

        // Konfigurasi stream untuk metode POST
        if (method == "POST") {
            conn.doOutput = true
            conn.doInput = true // Memastikan stream input siap menerima respon
        }

        return conn
    }

    /**
     * Membaca aliran data (Stream) menjadi String.
     * Ditambahkan pengecekan null untuk menangani errorStream yang kosong.
     */
    fun read_stream(stream: InputStream?): String {
        if (stream == null) return "" // Mencegah crash/freeze jika stream kosong
        val reader = BufferedReader(InputStreamReader(stream))
        val sb = StringBuilder()
        try {
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                sb.append(line)
            }
        } catch (e: Exception) {
            // Log error agar tidak silent freeze
        } finally {
            stream.close() // Wajib ditutup
        }
        return sb.toString()
    }
}