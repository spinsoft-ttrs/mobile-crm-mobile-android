package com.ezatpanah.hilt_retrofit_paging_youtube.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezatpanah.hilt_retrofit_paging_youtube.RetrofitApi.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ContactVIewModel @Inject constructor(
    private val repository: ApiRepository,
) : ViewModel() {

    var liveData: MutableLiveData<String?> = MutableLiveData()

    private val _state = MutableStateFlow<StateController>(StateController.Empty)
    val state: StateFlow<StateController> = _state

    sealed class StateController {
        object Loading : StateController()
        object Empty : StateController()
        data class Error(val message: String) : StateController()
        data class Success(val data: String) : StateController()

    }

    lateinit var apiModelAdapterProvince: APIModelAdapterSearchProvince
    fun observerLiveData(): MutableLiveData<String?> {
        return liveData
    }


    fun getDataFromApiProvince(search: String) = viewModelScope.launch {
        _state.value = StateController.Loading
        val response = repository.contactProvince(search)
        if (response.isSuccessful) {
            if (response.body()!!.status == "OK") {
                _state.value = StateController.Success("OK")
                liveData.postValue("OK")
                showResult(response.body()!!)
            } else {
                _state.value = StateController.Empty
                Log.i("empty province", "onResponse: not have data")
                liveData.postValue("FAIL")
            }


//        ApiService.endpointprovince.contactProvince(
//            pagnition,
//            search,
//            "1",
//            "200"
//        ).enqueue(object : Callback<ProvinceModel> {
//            override fun onResponse(call: Call<ProvinceModel>, response: Response<ProvinceModel>) {
//                if (response.isSuccessful){
//                    _state.value = StateController.Success("OK")
//                    liveData.postValue("OK")
//                    showResult(response.body()!!)
//                }else{
//                    _state.value = StateController.Empty
//                    Log.i("empty province", "onResponse: not have data")
//                    liveData.postValue("FAIL")
//
//
//                }
//            }
//            override fun onFailure(call: Call<ProvinceModel>, t: Throwable) {
//                _state.value = StateController.Error("FAIL")
//                liveData.postValue("FAIL")
//
//
//            }
//
//        })
        }
    }

    private fun showResult(results: ProvinceModel) {
        apiModelAdapterProvince.setData(results.data)
    }
}