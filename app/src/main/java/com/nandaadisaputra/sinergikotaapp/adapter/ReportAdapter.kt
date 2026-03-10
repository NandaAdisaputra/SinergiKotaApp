package com.nandaadisaputra.sinergikotaapp.adapter

import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nandaadisaputra.sinergikotaapp.R
import com.nandaadisaputra.sinergikotaapp.model.Report
import com.nandaadisaputra.sinergikotaapp.network.ApiService
import java.net.URL
import java.util.concurrent.Executors

class ReportAdapter(private val list_report: List<Report>) :
    RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    // Gunakan Thread Pool terbatas agar tidak lag saat scroll cepat
    private val executor_service = Executors.newFixedThreadPool(4)
    private val main_handler = Handler(Looper.getMainLooper())

    class ReportViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_title: TextView = view.findViewById(R.id.tv_item_title)
        val tv_desc: TextView = view.findViewById(R.id.tv_item_desc)
        val tv_date: TextView = view.findViewById(R.id.tv_item_date)
        val iv_report: ImageView = view.findViewById(R.id.iv_item_report)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_report, parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val report = list_report[position]

        // Binding data teks
        holder.tv_title.text = report.title
        holder.tv_desc.text = report.description
        holder.tv_date.text = report.createdDate

        /**
         * Menggabungkan Base URL Image dengan image_path dari database.
         * URL: http://10.0.2.2:5000/uploads/ (lewat ApiService)
         */
        val full_image_url = ApiService.BASE_URL_IMAGE + report.imagePath

        // Placeholder agar tidak salah menampilkan gambar saat recycling view
        holder.iv_report.setImageResource(android.R.drawable.ic_menu_gallery)

        // Muat gambar secara asinkron
        display_image_from_url(full_image_url, holder.iv_report)
    }

    private fun display_image_from_url(url_string: String, image_view: ImageView) {
        executor_service.execute {
            try {
                // Operasi jaringan wajib di background thread
                val input_stream = URL(url_string).openStream()
                val bitmap = BitmapFactory.decodeStream(input_stream)

                // Update UI wajib di main thread
                main_handler.post {
                    image_view.setImageBitmap(bitmap)
                }
            } catch (e: Exception) {
                main_handler.post {
                    // Fallback jika file tidak ditemukan di server
                    image_view.setImageResource(android.R.drawable.ic_menu_report_image)
                }
            }
        }
    }

    override fun getItemCount(): Int = list_report.size
}