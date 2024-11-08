package com.submission.aplikasidicodingevent.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.submission.aplikasidicodingevent.response.DetailResponse
import com.submission.aplikasidicodingevent.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {

    private val _events = MutableLiveData<DetailResponse?>() // Menggunakan DetailResponse
    val events: LiveData<DetailResponse?> get() = _events

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    companion object {
        private const val TAG = "DetailViewModel"
    }

    // Fungsi untuk memanggil API dan mendapatkan data event berdasarkan ID
    fun fetchEvent(eventID: Int) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailEvent(eventID)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(call: Call<DetailResponse>, response: Response<DetailResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Log.d(TAG, "Event Data: ${responseBody.event}")
                        _events.value = responseBody // Update LiveData dengan DetailResponse
                    }
                    else {
                        Log.e(TAG, "Response body is null")
                        _errorMessage.value = "Response body is null"
                    }
                }
                else {
                    Log.e(TAG, "API Error: ${response.message()}")
                    _errorMessage.value = "API Error: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "API Call Failed: ${t.message}")
                _errorMessage.value = "API Call Failed: ${t.message}"
            }
        })
    }
}
