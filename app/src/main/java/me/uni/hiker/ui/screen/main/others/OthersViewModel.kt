package me.uni.hiker.ui.screen.main.others

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.uni.hiker.api.service.CommonService
import me.uni.hiker.service.ConnectionService
import javax.inject.Inject

@HiltViewModel
class OthersViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val commonService: CommonService,
): ViewModel() {
    private val _isServerAvailable = MutableStateFlow(true)
    val isServerAvailable = _isServerAvailable.asStateFlow()

    init {
        Log.d("TEST", "Begining of init")

        if (ConnectionService.hasConnection(context)) {
            Log.d("TEST", "Has internet connection")
            viewModelScope.launch {
                val isAvailable = try {
                    val healthcheckResponse = commonService.healthcheck()
                    Log.d("TEST", "After getting response")


                    Log.d("TEST", "Status code is ${healthcheckResponse.code()}, while status is ${healthcheckResponse.body()?.status}")

                    healthcheckResponse.code() == 200 && healthcheckResponse.body()?.status == "Ok"
                } catch (err: Exception) {
                    Log.w("Error in 'OVM'", err.message ?: "Unknown error")
                    false
                }

                _isServerAvailable.update { isAvailable }

            }
        } else {
            Log.d("TEST", "Doesn't have internet connection")
            _isServerAvailable.update { false }
        }

    }
}