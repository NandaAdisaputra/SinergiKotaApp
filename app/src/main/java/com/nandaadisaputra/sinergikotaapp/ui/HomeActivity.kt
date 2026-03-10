package com.nandaadisaputra.sinergikotaapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
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

        // 1. Inisialisasi komponen UI berdasarkan ID lowercase_underscore
        rv_reports = findViewById(R.id.rv_reports)
        pb_loading = findViewById(R.id.pb_home_loading)
        val fab_add_report = findViewById<FloatingActionButton>(R.id.fab_add_report)

        // 2. Mengatur LayoutManager (Daftar Vertikal)
        rv_reports.layoutManager = LinearLayoutManager(this)

        // 3. Inisialisasi ViewModel
        view_model = ViewModelProvider(this)[ReportViewModel::class.java]

        // 4. Aksi klik pada FAB untuk pindah ke halaman Upload
        fab_add_report.setOnClickListener {
            val intent_ke_upload = Intent(this, UploadActivity::class.java)
            startActivity(intent_ke_upload)
        }

        /**
         * Observasi perubahan data:
         * Memperbarui RecyclerView setiap kali ada data laporan baru dari server.
         */
        view_model.reports.observe(this) { list ->
            if (list != null) {
                rv_reports.adapter = ReportAdapter(list)
            }
        }

        /**
         * Observasi status loading:
         * Menampilkan/menyembunyikan ProgressBar saat proses pengambilan data.
         */
        view_model.is_loading.observe(this) { loading ->
            pb_loading.visibility = if (loading) View.VISIBLE else View.GONE
        }

        /**
         * Observasi pesan kesalahan:
         * Memberikan notifikasi jika koneksi atau parsing data gagal.
         */
        view_model.error_message.observe(this) { msg ->
            if (msg != null) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
        }

        // Memanggil data untuk pertama kali
        view_model.fetch_reports()
    }

    /**
     * Fungsi onResume:
     * Sangat penting untuk LKS! Fungsi ini akan berjalan otomatis saat user kembali
     * dari UploadActivity ke HomeActivity, sehingga daftar laporan langsung ter-update.
     */
    override fun onResume() {
        super.onResume()
        view_model.fetch_reports()
    }
}