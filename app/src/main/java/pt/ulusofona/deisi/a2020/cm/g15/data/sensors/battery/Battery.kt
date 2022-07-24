package pt.ulusofona.deisi.a2020.cm.g15.data.sensors.battery
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager

class Battery private constructor(private val context: Context): Runnable{

    private val TAG = Battery::class.java.simpleName

    //Intervalo de tempo em que a thread será lançada
    private val TIME_BETWEEN_UPDATES = 10 * 1000L

    companion object {

        private var instance: Battery? = null
        private val handler = Handler()

        fun start(context: Context) {
            instance = if(instance == null) Battery(context) else instance
            instance?.start()
        }
    }

    private fun start() {
        //Agenda a execução da thread pela primeira vezz
        handler.postDelayed(this, TIME_BETWEEN_UPDATES)
    }

    private fun getBatteryCurrentNow(): Float? {

        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            context.registerReceiver(null, ifilter)
        }

        val batteryPct: Float? = batteryStatus?.let { intent ->
            val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            level * 100 / scale.toFloat()
        }

        return batteryPct
    }

    private fun darkModeIfBatteryLow(current : Float?) {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        val preferenceBattery = sp.getBoolean("switch_preference", false)
        if (current != null) {
            if(current <= 20) {
                if(preferenceBattery) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun run() {
        val current = getBatteryCurrentNow()
        darkModeIfBatteryLow(getBatteryCurrentNow())
        //Log.i(TAG, current.toString())
        //Agenda uma nova thread após a execução
        handler.postDelayed(this, TIME_BETWEEN_UPDATES)
    }


}