package com.nandaadisaputra.sinergikotaapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nandaadisaputra.sinergikotaapp.model.Report
import com.nandaadisaputra.sinergikotaapp.repository.ReportRepository

class ReportViewModel : ViewModel() {
    private val repository = ReportRepository()

    private val _reports = MutableLiveData<List<Report>>()
    val reports: LiveData<List<Report>> get() = _reports

    private val _is_loading = MutableLiveData<Boolean>()
    val is_loading: LiveData<Boolean> get() = _is_loading

    private val _error_message = MutableLiveData<String>()
    val error_message: LiveData<String> get() = _error_message

    fun fetch_reports() {
        _is_loading.value = true
        repository.get_all_reports(object : ReportRepository.ReportCallback {
            override fun onSuccess(reports: List<Report>) {
                _is_loading.postValue(false)
                _reports.postValue(reports)
            }
            override fun onError(message: String) {
                _is_loading.postValue(false)
                _error_message.postValue(message)
            }
        })
    }
}