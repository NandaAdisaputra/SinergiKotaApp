package com.nandaadisaputra.sinergikotaapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.nandaadisaputra.sinergikotaapp.R
import com.nandaadisaputra.sinergikotaapp.viewmodel.LoginViewModel

//Bagian ini adalah satu-satunya bagian
// yang berinteraksi langsung dengan
// pengguna (menangkap ketikan dan klik
// tombol) serta menampilkan data yang
// diproses oleh ViewModel.
class LoginActivity : AppCompatActivity() {
    // Mendeklarasikan variabel ViewModel yang akan digunakan di seluruh kelas
    private lateinit var view_model: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Menghubungkan file Kotlin ini dengan layout XML activity_login
        setContentView(R.layout.activity_login)

        // Menginisialisasi ViewModel agar datanya tetap terjaga meski ada rotasi layar
        view_model = ViewModelProvider(this)[LoginViewModel::class.java]

        // Menghubungkan komponen UI (Widget) berdasarkan ID yang ada di XML
        val et_username = findViewById<EditText>(R.id.et_username)
        val et_password = findViewById<EditText>(R.id.et_password)
        val btn_login = findViewById<Button>(R.id.btn_login)
        val pb_loading = findViewById<ProgressBar>(R.id.pb_loading)
        val tv_status = findViewById<TextView>(R.id.tv_status)

        // Event saat tombol login diklik
        btn_login.setOnClickListener {
            // Memanggil fungsi login di ViewModel dengan mengambil teks dari inputan
            view_model.login_user(et_username.text.toString(), et_password.text.toString())
        }

        // Mengamati (Observe) perubahan status loading dari ViewModel
        view_model.is_loading.observe(this) { loading ->
            // Jika loading true, tampilkan ProgressBar. Jika false, sembunyikan.
            pb_loading.visibility = if (loading) View.VISIBLE else View.GONE
            // Nonaktifkan tombol login saat proses sedang berjalan agar tidak diklik berkali-kali
            btn_login.isEnabled = !loading
        }

        // Mengamati pesan status (seperti pesan error atau sukses)
        view_model.status_message.observe(this) { pesan ->
            // Menampilkan pesan ke dalam TextView
            tv_status.text = pesan
            tv_status.visibility = View.VISIBLE
        }

        // Mengamati data user (hanya terpanggil jika login berhasil)
        view_model.user_data.observe(this) { user ->
            // Menampilkan pesan singkat (Toast) sebagai tanda berhasil
            Toast.makeText(this, "Login Berhasil: ${user.fullName}", Toast.LENGTH_SHORT).show()

            // Menyiapkan perpindahan halaman (Intent) ke DetailActivity
            val intent = Intent(this, HomeActivity::class.java)
            // Menjalankan perpindahan halaman
            startActivity(intent)

            // Mengakhiri LoginActivity agar user tidak bisa kembali ke sini saat menekan tombol 'Back'
            finish()
        }
    }
}