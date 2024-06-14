package es.dam.pi.vinilaapp_v3

import android.app.Application
import com.google.android.gms.maps.MapsInitializer
import com.google.android.libraries.places.api.Places

class VinylApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Places
        Places.initialize(applicationContext, getString(R.string.g_maps_id))
        MapsInitializer.initialize(this)
    }
}
