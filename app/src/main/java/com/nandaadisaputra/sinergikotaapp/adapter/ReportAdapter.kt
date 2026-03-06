package com.nandaadisaputra.sinergikotaapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nandaadisaputra.sinergikotaapp.R
import com.nandaadisaputra.sinergikotaapp.model.Report

// Adapter untuk menghubungkan data List<Report> dengan tampilan RecyclerView
class ReportAdapter(private val list_report: List<Report>) :
    RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    // ViewHolder: Kelas pendukung untuk memegang (holding) referensi ke view di dalam setiap item list
    class ReportViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Inisialisasi komponen UI dari layout item_report.xml
        val tv_title: TextView = view.findViewById(R.id.tv_item_title)
        val tv_desc: TextView = view.findViewById(R.id.tv_item_desc)
        val tv_date: TextView = view.findViewById(R.id.tv_item_date)
    }

    /**
     * Membuat tampilan (View) untuk satu baris item.
     * Fungsi ini memanggil layout item_report.xml.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        // Mengubah file XML layout menjadi objek View
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_report, parent, false)
        return ReportViewHolder(view)
    }

    /**
     * Menghubungkan (Binding) data dari list ke komponen UI di dalam ViewHolder.
     * Fungsi ini dipanggil untuk setiap baris data yang muncul di layar.
     */
    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        // Mendapatkan objek report berdasarkan posisi item saat ini
        val report = list_report[position]

        // Memasukkan data ke masing-masing TextView
        holder.tv_title.text = report.title
        holder.tv_desc.text = report.description
        holder.tv_date.text = report.createdDate
    }

    /**
     * Menentukan jumlah total data yang akan ditampilkan di RecyclerView.
     */
    override fun getItemCount(): Int = list_report.size
}