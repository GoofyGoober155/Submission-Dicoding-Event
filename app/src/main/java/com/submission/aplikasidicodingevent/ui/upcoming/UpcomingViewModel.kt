package com.submission.aplikasidicodingevent.ui.upcoming

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.submission.aplikasidicodingevent.response.EventResponse
import com.submission.aplikasidicodingevent.response.ListEventsItem
import com.submission.aplikasidicodingevent.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingViewModel : ViewModel() {

    // LiveData yang akan diobservasi oleh Fragment
    private val _events = MutableLiveData<List<ListEventsItem>>()
    val events: LiveData<List<ListEventsItem>> get() = _events

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    // Fungsi untuk memanggil API dan mendapatkan data events
    fun fetchEvents() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvents()
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Log.d(TAG, "Event Data: ${responseBody.listEvents}") // Tambahkan log ini
                        _events.value = responseBody.listEvents // Update LiveData dengan data event
                    }
                    else {
                        Log.d(TAG, "Response body is null")
                    }
                }
                else {
                    Log.e(TAG, "API Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "API Call Failed: ${t.message}")
            }
        })

    }
}