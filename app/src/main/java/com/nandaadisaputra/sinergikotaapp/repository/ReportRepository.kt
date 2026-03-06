package com.nandaadisaputra.sinergikotaapp.repository

import com.nandaadisaputra.sinergikotaapp.model.Report
import com.nandaadisaputra.sinergikotaapp.network.ApiService
import com.nandaadisaputra.sinergikotaapp.utils.NetworkUtils
import org.json.JSONArray
import java.net.HttpURLConnection

class ReportRepository {

    // Interface untuk menangani hasil kembalian (callback) secara asinkron
    interface ReportCallback {
        fun onSuccess(reports: List<Report>) // Dipanggil saat data berhasil diambil
        fun onError(message: String)         // Dipanggil saat terjadi kesalahan
    }

    // Fungsi untuk mengambil semua data laporan dari server
    fun get_all_reports(callback: ReportCallback) {
        // Menjalankan proses jaringan di thread terpisah agar tidak memblokir UI (Main Thread)
        Thread {
            try {
                // Membuka koneksi HTTP menggunakan URL endpoint laporan dengan metode "GET"
                val conn = NetworkUtils.get_connection(ApiService.ENDPOINT_REPORTS, "GET")

                // Memeriksa apakah status respon server adalah 200 (OK)
                if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                    // Membaca data stream dari input server menjadi String
                    val res = NetworkUtils.read_stream(conn.inputStream)

                    // Mengonversi String respon menjadi array JSON
                    val json_array = JSONArray(res)
                    val report_list = mutableListOf<Report>()

                    // Melakukan iterasi untuk setiap objek di dalam array JSON
                    for (i in 0 until json_array.length()) {
                        val item = json_array.getJSONObject(i)

                        // Memasukkan data dari JSON ke dalam model Report
                        report_list.add(Report(
                            id = item.getInt("id"),
                            title = item.getString("title"),
                            description = item.getString("description"),
                            latitude = item.getDouble("latitude"),
                            longitude = item.getDouble("longitude"),
                            imagePath = item.getString("imagePath"),
                            createdDate = item.getString("createdDate")
                        ))
                    }
                    // Mengirim daftar laporan yang berhasil diproses ke callback sukses
                    callback.onSuccess(report_list)
                } else {
                    // Mengirim pesan kesalahan jika respon server bukan 200 OK
                    callback.onError("Gagal memuat data laporan")
                }
                // Memutus koneksi setelah selesai digunakan
                conn.disconnect()
            } catch (e: Exception) {
                // Menangani pengecualian seperti masalah koneksi internet atau parsing data
                callback.onError("Kesalahan Jaringan: ${e.message}")
            }
        }.start() // Memulai eksekusi thread
    }
}