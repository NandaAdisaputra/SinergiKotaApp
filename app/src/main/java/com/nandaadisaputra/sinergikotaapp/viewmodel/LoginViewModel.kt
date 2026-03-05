package com.nandaadisaputra.sinergikotaapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nandaadisaputra.sinergikotaapp.model.User
import com.nandaadisaputra.sinergikotaapp.repository.AuthRepository

// Kelas ini bertindak sebagai perantara
// antara UI (Activity) dan data (Repository)
//Karena sudah ada AuthRepository, maka
// peran LoginViewModel di sini murni
// sebagai pengatur lalu lintas data dan
// penyimpan status (state) agar UI tetap
// sinkron.
class LoginViewModel : ViewModel() {

    // 1. Inisialisasi AuthRepository sebagai sumber data (Single Source of Truth)
    private val repository = AuthRepository()

    // 2. _is_loading: State internal untuk memberi tahu UI apakah proses sedang berjalan
    private val _is_loading = MutableLiveData<Boolean>()
    // Properti publik (read-only) agar Activity bisa mengamati status loading
    val is_loading: LiveData<Boolean> get() = _is_loading

    // 3. _status_message: Menyimpan pesan teks untuk ditampilkan ke pengguna (Toast/TextView)
    private val _status_message = MutableLiveData<String>()
    val status_message: LiveData<String> get() = _status_message

    // 4. _user_data: Menyimpan objek User hasil tangkapan dari Repository setelah login sukses
    private val _user_data = MutableLiveData<User>()
    val user_data: LiveData<User> get() = _user_data

    /**
     * Fungsi utama yang dipanggil oleh Activity saat tombol login ditekan
     */
    fun login_user(username: String, pass: String) {
        // Validasi awal di sisi ViewModel sebelum meminta data ke Repository
        if (username.isEmpty() || pass.isEmpty()) {
            _status_message.value = "Data tidak boleh kosong"
            return
        }

        // Memberitahu UI untuk menampilkan ProgressBar (Indikator Loading)
        _is_loading.value = true

        // 5. MEMANGGIL REPOSITORY: ViewModel meminta Repository untuk melakukan login ke server
        repository.login(username, pass, object : AuthRepository.LoginCallback {

            // Callback jika Repository berhasil mendapatkan data dari server
            override fun onSuccess(user: User) {
                // postValue digunakan karena Repository bekerja di Background Thread (Thread manual)
                _is_loading.postValue(false) // Instruksi ke UI untuk sembunyikan loading
                _user_data.postValue(user)   // Menyimpan data user yang didapat dari Repository
                _status_message.postValue("Selamat datang, ${user.fullName}")
            }

            // Callback jika Repository gagal (salah password, tidak ada internet, dll)
            override fun onError(message: String) {
                _is_loading.postValue(false) // Instruksi ke UI untuk sembunyikan loading
                _status_message.postValue(message) // Meneruskan pesan error dari Repository ke UI
            }
        })
    }
}