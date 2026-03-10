package com.nandaadisaputra.sinergikotaapp.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.nandaadisaputra.sinergikotaapp.R
import com.nandaadisaputra.sinergikotaapp.viewmodel.UploadViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

/**
 * UploadActivity: Mengelola antarmuka untuk menginput laporan baru,
 * termasuk pemilihan gambar dari galeri dan pengiriman data ke ViewModel.
 */
class UploadActivity : AppCompatActivity() {

    private lateinit var view_model: UploadViewModel
    private var selected_file: File? = null // Menyimpan referensi file gambar yang dipilih
    private lateinit var iv_preview: ImageView

    /**
     * Activity Result API: Cara modern untuk mendapatkan hasil dari Activity lain (Galeri).
     * GetContent() akan membuka pemilih file sistem.
     */
    private val pick_image_launcher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        // Jika user memilih gambar (uri tidak null)
        if (uri != null) {
            iv_preview.setImageURI(uri) // Menampilkan preview gambar ke layar
            selected_file = get_file_from_uri(uri) // Mengonversi URI menjadi File fisik
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        // Inisialisasi ViewModel
        view_model = ViewModelProvider(this)[UploadViewModel::class.java]

        // Inisialisasi Komponen UI
        iv_preview = findViewById(R.id.iv_preview)
        val btn_pilih_gambar = findViewById<Button>(R.id.btn_pilih_gambar)
        val et_title = findViewById<EditText>(R.id.et_title)
        val et_desc = findViewById<EditText>(R.id.et_desc)
        val et_lat = findViewById<EditText>(R.id.et_lat)
        val et_lon = findViewById<EditText>(R.id.et_lon)
        val btn_kirim = findViewById<Button>(R.id.btn_kirim)
        val pb_upload = findViewById<ProgressBar>(R.id.pb_upload)

        // Klik tombol pilih gambar: Memicu pembukaan galeri (mime type image/*)
        btn_pilih_gambar.setOnClickListener {
            pick_image_launcher.launch("image/*")
        }

        // Klik tombol kirim: Mengambil input teks dan mengirimnya ke ViewModel
        btn_kirim.setOnClickListener {
            val title = et_title.text.toString()
            val desc = et_desc.text.toString()
            val lat = et_lat.text.toString()
            val lon = et_lon.text.toString()

            view_model.submit_report(title, desc, lat, lon, selected_file)
        }

        // --- OBSERVASI LIVEDATA ---

        // Mengatur tampilan saat proses unggah sedang berlangsung (Loading)
        view_model.is_loading.observe(this) { loading ->
            pb_upload.visibility = if (loading) View.VISIBLE else View.GONE
            btn_kirim.isEnabled = !loading // Menonaktifkan tombol agar tidak klik ganda
        }

        // Menampilkan pesan dari server atau pesan validasi
        view_model.status_message.observe(this) { pesan ->
            Toast.makeText(this, pesan, Toast.LENGTH_SHORT).show()
        }

        // Jika upload berhasil, tutup activity dan kembali ke Home
        view_model.upload_success.observe(this) { success ->
            if (success) {
                finish()
            }
        }
    }

    /**
     * Fungsi Helper: Mengonversi URI galeri menjadi File di cache internal.
     * Alasan: HttpURLConnection memerluile fisik/Path untuk dikirim
     * sebagai multipart stream, sementara URI galeri seringkali diproteksi/dienkripsi.
     */
    private fun get_file_from_uri(uri: Uri): File {
        // Membuka akses Fka aliran data dari URI
        val input_stream: InputStream? = contentResolver.openInputStream(uri)
        // Membuat file sementara di folder cache aplikasi
        val temp_file = File(cacheDir, "temp_upload_image.jpg")
        val output_stream = FileOutputStream(temp_file)

        // Menyalin data dari galeri ke file cache aplikasi
        input_stream?.copyTo(output_stream)

        input_stream?.close()
        output_stream.close()
        return temp_file
    }
}