package com.nandaadisaputra.sinergikotaapp.repository

import com.nandaadisaputra.sinergikotaapp.model.User
import com.nandaadisaputra.sinergikotaapp.network.ApiService
import com.nandaadisaputra.sinergikotaapp.utils.NetworkUtils
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URLEncoder

//Kode AuthRepository ini adalah bagian
// yang menangani logika data dan komunikasi
// dengan server.
class AuthRepository {
    // Interface untuk mengirimkan hasil (sukses/gagal) kembali ke ViewModel
    interface LoginCallback {
        fun onSuccess(user: User)
        fun onError(message: String)
    }

    /**
     * Fungsi untuk melakukan request login ke server
     * Menggunakan Thread agar tidak mengganggu kinerja layar utama (UI Thread)
     */
    fun login(username: String, pass: String, callback: LoginCallback) {
        Thread {
            try {
                // Menyiapkan koneksi HTTP POST menggunakan URL dari ApiService
                val conn = NetworkUtils.get_connection(ApiService.END_POINT_LOGIN, "POST")

                // Menyusun body data dengan format x-www-form-urlencoded (seperti form pada web)
                // URLEncoder digunakan agar karakter khusus tidak merusak struktur URL
                val postData = "username=" + URLEncoder.encode(username, "UTF-8") +
                        "&password=" + URLEncoder.encode(pass, "UTF-8")

                // Mengirimkan data username & password ke dalam stream output koneksi
                conn.outputStream.use { it.write(postData.toByteArray()) }

                // Mengecek apakah respon dari server adalah 200 (OK)
                if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                    // Membaca data stream dari server dan mengubahnya menjadi String
                    val res = NetworkUtils.read_stream(conn.inputStream)

                    // Mengubah String tersebut menjadi objek JSON agar mudah diambil datanya
                    val json = JSONObject(res)

                    // Mapping (memindahkan) data dari JSON ke dalam objek model User
                    val user = User(
                        id = json.getInt("id"),
                        username = json.getString("username"),
                        fullName = json.getString("fullName")
                    )
                    // Mengembalikan data user yang berhasil login ke ViewModel
                    callback.onSuccess(user)
                } else {
                    // Jika kode respon bukan 200 (misal 401 atau 404)
                    callback.onError("Login Gagal: Username atau Password salah")
                }
                // Memutuskan koneksi dengan server setelah selesai
                conn.disconnect()
            } catch (e: Exception) {
                // Menangani jika terjadi masalah seperti tidak ada internet atau URL salah
                callback.onError("Masalah Jaringan: ${e.message}")
            }
        }.start() // Menjalankan thread
    }
}