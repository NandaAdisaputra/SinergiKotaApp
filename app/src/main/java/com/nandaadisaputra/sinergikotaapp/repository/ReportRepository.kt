package com.nandaadisaputra.sinergikotaapp.repository

import com.nandaadisaputra.sinergikotaapp.model.Report
import com.nandaadisaputra.sinergikotaapp.network.ApiService
import com.nandaadisaputra.sinergikotaapp.utils.NetworkUtils
import org.json.JSONArray
import java.net.HttpURLConnection

class ReportRepository {
    interface ReportCallback {
        fun onSuccess(reports: List<Report>)
        fun onError(message: String)
    }

    fun get_all_reports(callback: ReportCallback) {
        Thread {
            try {
                val conn = NetworkUtils.get_connection(ApiService.ENDPOINT_REPORTS, "GET")

                if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                    val res = NetworkUtils.read_stream(conn.inputStream)
                    val json_array = JSONArray(res)
                    val report_list = mutableListOf<Report>()

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
}