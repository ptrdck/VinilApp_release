package es.dam.pi.vinilaapp_v3.ui.home.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@Composable
fun AddressMapPicker(
    initialPosition: LatLng = LatLng(36.8381, -2.4597), // Almería, España
    addressText: String,
    onAddressSelected: (String, Double, Double) -> Unit
) {
    var hasLocationPermission by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(initialPosition, 12f)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasLocationPermission = granted
        }
    )

    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        SideEffect {
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    } else {
        hasLocationPermission = true
    }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(addressText) {
        if (addressText.isNotBlank()) {
            try {
                val geocoder = Geocoder(context)
                val addressList = geocoder.getFromLocationName(addressText, 1)
                if (addressList != null && addressList.isNotEmpty()) {
                    val location = addressList[0]
                    val newLatLng = LatLng(location.latitude, location.longitude)
                    cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(newLatLng, 12f))
                    onAddressSelected(addressText, location.latitude, location.longitude)
                }
            } catch (e: Exception) {
                // Handle geocoding error
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (hasLocationPermission) {
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = true),
                onMapClick = { latLng ->
                    coroutineScope.launch {
                        val geocoder = Geocoder(context)
                        val addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                        val address = addressList?.get(0)?.getAddressLine(0) ?: "Unknown address"
                        onAddressSelected(address, latLng.latitude, latLng.longitude)
                    }
                }
            ) {
                Marker(
                    position = cameraPositionState.position.target,
                    title = "Selected Location"
                )
            }
        } else {
            Text(text = "Please grant location permission to use the map.")
        }
    }
}