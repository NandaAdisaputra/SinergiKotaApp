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

    private lateinit var view_model: ReportViewModel
    private lateinit var rv_reports: RecyclerView
    private lateinit var pb_loading: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        rv_reports = findViewById(R.id.rv_reports)
        pb_loading = findViewById(R.id.pb_home_loading)

        rv_reports.layoutManager = LinearLayoutManager(this)
        view_model = ViewModelProvider(this)[ReportViewModel::class.java]

        // Observasi perubahan data
        view_model.reports.observe(this) { list ->
            rv_reports.adapter = ReportAdapter(list)
        }

        // Observasi status loading
        view_model.is_loading.observe(this) { loading ->
            pb_loading.visibility = if (loading) View.VISIBLE else View.GONE
        }

        // Observasi error
        view_model.error_message.observe(this) { msg ->
            if (msg != null) Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        // Ambil data pertama kali
        view_model.fetch_reports()
    }
}