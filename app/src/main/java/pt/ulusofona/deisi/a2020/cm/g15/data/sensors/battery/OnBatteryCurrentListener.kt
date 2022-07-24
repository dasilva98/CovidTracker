package pt.ulusofona.deisi.a2020.cm.g15.data.sensors.battery

interface OnBatteryCurrentListener {

    fun onCurrentChanged (current: Double)
}