package pt.ulusofona.deisi.a2020.cm.g15.data.sensors.location

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import pt.ulusofona.deisi.a2020.cm.g15.ui.viewmodels.InDangerViewModel
import java.lang.UnsupportedOperationException
import java.util.*

class LocationService: Service() {

    private val TAG = LocationService::class.java.simpleName
    private val TIME_BETWEEN_UPDATES = 10 * 1000L
    private var state = ""
    private var iBinder = LocationBinder()
    private var viewModel = InDangerViewModel()

    private val locationCallback: LocationCallback = object : LocationCallback() {

        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            if(locationResult != null && locationResult.lastLocation != null && internetConnection()) {
                val latitude: Double = locationResult.lastLocation.latitude
                val longitude: Double = locationResult.lastLocation.longitude
                val geocoder = Geocoder(this@LocationService, Locale.getDefault())

                val adresses: List<Address> =
                    geocoder.getFromLocation(latitude, longitude, 1)

                //Distrito
                val stateTemp = adresses[0].adminArea

                state = stateTemp
            }
            viewModel.districtChanged(state,internetConnection())
        }
    }

    fun getDistrict(): String{
        return state
    }

    inner class LocationBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): LocationService = this@LocationService
    }

    override fun onBind(intent: Intent?): IBinder {
        return iBinder
    }

    fun internetConnection(): Boolean {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent != null) {
            startLocationService()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startLocationService() {
        val channelId = "location_notification_channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val resultIntent = Intent()
        val pendingIntent = PendingIntent.getActivity(applicationContext, 500, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(applicationContext, channelId)
        builder.setContentTitle("Location Service")
        builder.setDefaults(NotificationCompat.DEFAULT_ALL)
        builder.setContentTitle("Running")
        builder.setContentIntent(pendingIntent)
        builder.setAutoCancel(false)
        builder.priority = NotificationCompat.PRIORITY_MAX

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(notificationManager != null && notificationManager.getNotificationChannel(channelId) == null) {
                val notificationChannel = NotificationChannel(channelId, "Location Service", NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.description = "Used by location Service"
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }

        val locationRequest = LocationRequest()
        locationRequest.interval = TIME_BETWEEN_UPDATES
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        LocationServices.getFusedLocationProviderClient(this)
            .requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper())



        startForeground(150, builder.build())
    }
}