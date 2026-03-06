package com.nandaadisaputra.sinergikotaapp.ui

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nandaadisaputra.sinergikotaapp.R
import com.nandaadisaputra.sinergikotaapp.adapter.ReportAdapter
import com.nandaadisaputra.sinergikotaapp.viewmodel.ReportViewModel

class HomeActivity : AppCompatActivity() {

    // Deklarasi variabel untuk ViewModel dan komponen UI
    private lateinit var view_model: ReportViewModel
    private lateinit var rv_reports: RecyclerView
    private lateinit var pb_loading: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Menghubungkan Activity dengan layout XML activity_home
        setContentView(R.layout.activity_home)

        // Inisialisasi komponen UI berdasarkan ID yang ada di XML
        rv_reports = findViewById(R.id.rv_reports)
        pb_loading = findViewById(R.id.pb_home_loading)

        // Mengatur agar RecyclerView tampil dalam bentuk daftar vertikal (list)
        rv_reports.layoutManager = LinearLayoutManager(this)

        // Inisialisasi ViewModel menggunakan ViewModelProvider agar data tetap terjaga meski layar diputar (orientasi berubah)
        view_model = ViewModelProvider(this)[ReportViewModel::class.java]

        /**
         * Observasi perubahan data:
         * Setiap kali daftar laporan di ViewModel berubah, blok kode ini akan dijalankan
         * untuk memperbarui tampilan RecyclerView.
         */
        view_model.reports.observe(this) { list ->
            // Mengatur adapter RecyclerView dengan data laporan terbaru
            rv_reports.adapter = ReportAdapter(list)
        }

        /**
         * Observasi status loading:
         * Menampilkan ProgressBar jika sedang mengambil data, dan menyembunyikannya jika sudah selesai.
         */
        view_model.is_loading.observe(this) { loading ->
            // Mengubah visibilitas ProgressBar berdasarkan nilai boolean 'loading'
            pb_loading.visibility = if (loading) View.VISIBLE else View.GONE
        }

        /**
         * Observasi pesan kesalahan:
         * Menampilkan pesan error dalam bentuk Toast (pop-up singkat) jika terjadi masalah.
         */
        view_model.error_message.observe(this) { msg ->
            if (msg != null) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
        }

        // Memanggil fungsi untuk mulai mengambil data laporan dari server saat aplikasi pertama kali dibuka
        view_model.fetch_reports()
    }
}