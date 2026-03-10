package com.nandaadisaputra.sinergikotaapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nandaadisaputra.sinergikotaapp.repository.ReportRepository
import com.nandaadisaputra.sinergikotaapp.repository.ReportRepository.UploadCallback
import java.io.File

/**
 * UploadViewModel: Jembatan antara UI (UploadActivity) dan Data (ReportRepository).
 * Berfungsi mengelola logika validasi dan status pengiriman laporan.
 */
class UploadViewModel : ViewModel() {
    private val repository = ReportRepository()

    // Status untuk menampilkan ProgressBar di UI
    private val _is_loading = MutableLiveData<Boolean>()
    val is_loading: LiveData<Boolean> get() = _is_loading

    // Pesan feedback untuk user (Validasi, Sukses, atau Error)
    private val _status_message = MutableLiveData<String>()
    val status_message: LiveData<String> get() = _status_message

    // Status pemicu untuk menutup Activity jika upload berhasil
    private val _upload_success = MutableLiveData<Boolean>()
    val upload_success: LiveData<Boolean> get() = _upload_success

    /**
     * Fungsi untuk mengirim laporan ke server.
     * Melakukan pengecekan input terlebih dahulu sebelum memanggil fungsi di Repository.
     */
    fun submit_report(title: String, desc: String, lat: String, lon: String, file: File?) {
        // 1. Validasi Input di sisi klien
        if (title.isEmpty() || desc.isEmpty() || lat.isEmpty() || lon.isEmpty() || file == null) {
            _status_message.value = "Semua kolom dan gambar wajib diisi"
            return
        }

        // 2. Aktifkan indikator loading
        _is_loading.value = true

        // 3. Panggil fungsi upload di Repository
        repository.upload_report(title, desc, lat, lon, file, object : UploadCallback {
            override fun onSuccess(message: String) {
                // Gunakan postValue karena ini dijalankan di dalam Thread (Background)
                _is_loading.postValue(false)
                _status_message.postValue(message)
                _upload_success.postValue(true)
            }

            override fun onError(message: String) {
                _is_loading.postValue(false)
                _status_message.postValue(message)
                _upload_success.postValue(false)
            }
        })
    }
}