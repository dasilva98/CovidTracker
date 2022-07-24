package pt.ulusofona.deisi.a2020.cm.g15.ui.activities

import android.Manifest
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.a2020.cm.g15.R
import pt.ulusofona.deisi.a2020.cm.g15.data.sensors.battery.Battery
import pt.ulusofona.deisi.a2020.cm.g15.data.sensors.location.LocationService
import pt.ulusofona.deisi.a2020.cm.g15.ui.listeners.ReceiveDistrictObserver
import pt.ulusofona.deisi.a2020.cm.g15.ui.viewmodels.InDangerViewModel

private const val REQUEST_CODE = 100

class MainActivity : AppCompatActivity(),ReceiveDistrictObserver {
    private lateinit var navController:NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mService: LocationService
    private var mBound: Boolean = false

    private val TAG : String = MainActivity::class.java.simpleName
    private lateinit var viewModel: InDangerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(InDangerViewModel::class.java)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.last24hFragment,
                R.id.vaccinesFragment,
                R.id.countiesFragment,
                R.id.testsFragment,
                R.id.contactsFragment,
                R.id.settingsFragment
            ),
            drawer_layout
        )

        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)
        bottom_nav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.last24hFragment -> showBottomNav()
                R.id.vaccinesFragment -> showBottomNav()
                R.id.countiesFragment -> showBottomNav()
                else -> hideBottomNav()
            }
        }

        //Localização e Estou Em Perigo?
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getFusedLocation()

        //val view = layoutInflater.inflate(R.layout.activity_main)
        InDangerViewModel.registerDistrictObserver(this)
        viewModel.getInDanger(internetConnection())

        //Bateria
        Battery.start(this)
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as LocationService.LocationBinder
            mService = binder.getService()
            mBound = true
        }
        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    override fun onDistrictChanged(dangerLevel: String?) {
        dangerLevel.let { danger_level.text = it }
        if(danger_level.text == "Baixo a Moderado" || danger_level.text == "Moderado") {
            danger_level.setBackgroundColor(getColor(R.color.green))
            danger_level.setTextColor(getColor(R.color.white))
        }
        if(danger_level.text == "Elevado") {
            danger_level.setBackgroundColor(getColor(R.color.yellow))
            danger_level.setTextColor(getColor(R.color.black))
        }
        if(danger_level.text == "Muito Elevado" || danger_level.text == "Extremamente Elevado") {
            danger_level.setBackgroundColor(getColor(R.color.red))
            danger_level.setTextColor(getColor(R.color.white))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        InDangerViewModel.unregisterObserver()
    }

    private fun showBottomNav() {
        bottom_nav.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        bottom_nav.visibility = View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun getFusedLocation() {
        //Check permission
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)

        } else {
            startLocationService()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if(REQUEST_CODE == requestCode) {
            if((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                startLocationService()
            } else {
                Toast.makeText(this, "Acess Denied", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun startLocationService() {
        if(!isLocationServiceRunning()) {
            val serviceIntent = Intent(this, LocationService::class.java)
                .also { intent.action = "startLocationService"  }
                .also { intent -> bindService(intent,connection,Context.BIND_AUTO_CREATE)
            }
            startService(serviceIntent)
        }
    }

    private fun isLocationServiceRunning(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if(activityManager != null) {
            for (service in activityManager.getRunningServices(Int.MAX_VALUE)) {
                if(service.service.className == LocationService::class.java.name) {
                    if(service.foreground) {
                        return true
                    }
                }
            }
            return false
        }
        return false
    }

    fun internetConnection(): Boolean {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

}