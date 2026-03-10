package com.nandaadisaputra.sinergikotaapp.repository

import com.nandaadisaputra.sinergikotaapp.model.Report
import com.nandaadisaputra.sinergikotaapp.network.ApiService
import com.nandaadisaputra.sinergikotaapp.utils.NetworkUtils
import org.json.JSONArray
import org.json.JSONObject
import java.io.DataOutputStream
import java.io.File
import java.io.FileInputStream
import java.net.HttpURLConnection

/**
 * ReportRepository: Kelas ini bertanggung jawab atas semua operasi data laporan,
 * baik mengambil data (GET) maupun mengirim data (POST Multipart).
 */
class ReportRepository {

    // --- INTERFACE CALLBACK ---

    // Digunakan untuk mengirim hasil daftar laporan kembali ke ViewModel
    interface ReportCallback {
        fun onSuccess(reports: List<Report>)
        fun onError(message: String)
    }

    // Digunakan untuk mengirim status proses unggah (upload) ke ViewModel
    interface UploadCallback {
        fun onSuccess(message: String)
        fun onError(message: String)
    }

    // --- FUNGSI AMBIL DATA (GET) ---

    fun  get_all_reports(callback: ReportCallback) {
        // Menjalankan proses di Thread terpisah agar aplikasi tidak "Not Responding"
        Thread {
            try {
                // Membuka koneksi dengan endpoint GET /api/reports
                val conn = NetworkUtils.get_connection(ApiService.ENDPOINT_REPORTS, "GET")

                if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                    val res = NetworkUtils.read_stream(conn.inputStream)
                    val json_array = JSONArray(res)
                    val report_list = mutableListOf<Report>()

                    // Looping untuk memindahkan data dari JSON ke List model Report
                    for (i in 0 until json_array.length()) {
                        val item = json_array.getJSONObject(i)
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
                    callback.onSuccess(report_list)
                } else {
                    callback.onError("Gagal memuat data laporan")
                }
                conn.disconnect()
            } catch (e: Exception) {
                callback.onError("Kesalahan Jaringan: ${e.message}")
            }
        }.start()
    }
    // --- FUNGSI UNGGAH DATA (POST MULTIPART) ---

    fun upload_report(
        title: String,
        description: String,
        latitude: String,
        longitude: String,
        image_file: File,
        callback: UploadCallback
    ) {
        Thread {
            try {
                // Boundary: Penanda unik untuk memisahkan antar bagian data (teks dan file)
                val boundary = "*****" + System.currentTimeMillis() + "*****"
                val line_end = "\r\n"
                val two_hyphens = "--"

                val url = java.net.URL("http://10.0.2.2:5000/api/reports")
                val conn = url.openConnection() as HttpURLConnection

                // Pengaturan Header Multipart Form Data
                conn.requestMethod = "POST"
                conn.setRequestProperty("Connection", "Keep-Alive")
                conn.setRequestProperty("Cache-Control", "no-cache")
                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")
                conn.doOutput = true
                conn.doInput = true

                val output_stream = DataOutputStream(conn.outputStream)

                /**
                 * Fungsi lokal untuk menulis bagian teks (form-data) ke output stream.
                 * Sesuai dengan parameter API: title, description, latitude, longitude.
                 */
                fun add_text_field(name: String, value: String) {
                    output_stream.writeBytes("$two_hyphens$boundary$line_end")
                    output_stream.writeBytes("Content-Disposition: form-data; name=\"$name\"$line_end$line_end")
                    output_stream.writeBytes("$value$line_end")
                }

                // Menulis data teks ke body request
                add_text_field("title", title)
                add_text_field("description", description)
                add_text_field("latitude", latitude)
                add_text_field("longitude", longitude)

                // Menulis data file gambar (image) ke body request
                output_stream.writeBytes("$two_hyphens$boundary$line_end")
                output_stream.writeBytes("Content-Disposition: form-data; name=\"image\"; filename=\"${image_file.name}\"$line_end")
                output_stream.writeBytes("Content-Type: image/jpeg$line_end$line_end")

                // Proses membaca file gambar dan menulis bit per bit ke output stream
                val file_input_stream = FileInputStream(image_file)
                val buffer = ByteArray(1024)
                var bytes_read: Int
                while (file_input_stream.read(buffer).also { bytes_read = it } != -1) {
                    output_stream.write(buffer, 0, bytes_read)
                }
                output_stream.writeBytes(line_end)
                file_input_stream.close()

                // Menandai akhir dari seluruh request multipart
                output_stream.writeBytes("$two_hyphens$boundary$two_hyphens$line_end")
                output_stream.flush()
                output_stream.close()

                // Membaca respon sukses dari server
                if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                    val res = NetworkUtils.read_stream(conn.inputStream)
                    val json_res = JSONObject(res)
                    // Ambil pesan sukses seperti di Postman: "Laporan berhasil dikirim"
                    callback.onSuccess(json_res.getString("message"))
                } else {
                    callback.onError("Gagal mengunggah data: ${conn.responseCode}")
                }
                conn.disconnect()

            } catch (e: Exception) {
                callback.onError("Kesalahan Sistem: ${e.message}")
            }
        }.start()
    }
}