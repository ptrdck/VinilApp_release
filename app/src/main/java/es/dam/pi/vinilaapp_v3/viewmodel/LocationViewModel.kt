package es.dam.pi.vinilaapp_v3.viewmodel

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val _location = MutableStateFlow<LatLng?>(null)
    val location: StateFlow<LatLng?> = _location

    private val _address = MutableStateFlow("")
    val address: StateFlow<String> = _address

    private val defaultLocation = LatLng(36.8381, -2.4597) // Default to Almería, España

    fun geocodeAddress(address: String) {
        if (address.isBlank()) {
            // Handle empty or blank address
            _location.value = defaultLocation
            _address.value = ""
            return
        }
        viewModelScope.launch {
            try {
                val geocoder = Geocoder(getApplication<Application>().applicationContext, Locale.getDefault())
                val addressList = geocoder.getFromLocationName(address, 1)
                if (addressList != null && addressList.isNotEmpty()) {
                    val location = addressList[0]
                    _location.value = LatLng(location.latitude, location.longitude)
                    _address.value = address
                } else {
                    // Handle case where no address is found
                    _location.value = defaultLocation
                    _address.value = ""
                }
            } catch (e: Exception) {
                // Handle geocoder failure
                _location.value = defaultLocation
                _address.value = ""
            }
        }
    }

    fun getCurrentLocation(context: Context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            viewModelScope.launch {
                try {
                    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                    val location = fusedLocationClient.lastLocation.await()
                    location?.let {
                        _location.value = LatLng(it.latitude, it.longitude)
                    } ?: run {
                        _location.value = defaultLocation
                    }
                } catch (e: Exception) {
                    _location.value = defaultLocation
                }
            }
        } else {
            _location.value = defaultLocation
        }
    }
}
