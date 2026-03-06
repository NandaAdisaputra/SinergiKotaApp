package com.nandaadisaputra.sinergikotaapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nandaadisaputra.sinergikotaapp.model.Report
import com.nandaadisaputra.sinergikotaapp.repository.ReportRepository

class ReportViewModel : ViewModel() {
    // Inisialisasi repository untuk mengambil data dari network/database
    private val repository = ReportRepository()

    // _reports (MutableLiveData) bersifat privat agar datanya tidak bisa diubah langsung dari luar (UI)
    private val _reports = MutableLiveData<List<Report>>()
    // reports (LiveData) bersifat publik dan read-only, digunakan oleh UI untuk mengobservasi perubahan data
    val reports: LiveData<List<Report>> get() = _reports

    // Menyimpan status loading (apakah sedang mengambil data atau tidak)
    private val _is_loading = MutableLiveData<Boolean>()
    val is_loading: LiveData<Boolean> get() = _is_loading

    // Menyimpan pesan kesalahan jika terjadi error pada proses pengambilan data
    private val _error_message = MutableLiveData<String>()
    val error_message: LiveData<String> get() = _error_message

    /**
     * Fungsi untuk memicu pengambilan data laporan.
     * Mengatur status loading dan memanggil fungsi di repository.
     */
    fun fetch_reports() {
        // Set status loading menjadi true saat proses dimulai
        _is_loading.value = true

        // Memanggil fungsi get_all_reports dari repository dengan callback
        repository.get_all_reports(object : ReportRepository.ReportCallback {

            // Callback jika data berhasil didapatkan
            override fun onSuccess(reports: List<Report>) {
                // Menghentikan status loading
                _is_loading.postValue(false)
                // Memperbarui LiveData reports dengan data terbaru (postValue digunakan karena di thread berbeda)
                _reports.postValue(reports)
            }

            // Callback jika terjadi kesalahan (error)
            override fun onError(message: String) {
                // Menghentikan status loading
                _is_loading.postValue(false)
                // Mengirimkan pesan error ke UI melalui LiveData
                _error_message.postValue(message)
            }
        })
    }
}