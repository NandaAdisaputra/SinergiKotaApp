package com.nandaadisaputra.sinergikotaapp.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.nandaadisaputra.sinergikotaapp.R

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Inisialisasi TextView dari layout
        val tv_welcome = findViewById<TextView>(R.id.tv_welcome)

        // Mengambil data yang dikirim dari LoginActivity
        val full_name = intent.getStringExtra("user_name")

        // Menampilkan data ke TextView
        if (full_name != null) {
            tv_welcome.text = "Selamat Datang,\n$full_name"
        } else {
            tv_welcome.text = "Selamat Datang, User"
        }
    }
}