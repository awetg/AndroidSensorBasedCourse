package com.example.heartRate

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.*
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import android.Manifest
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Intent
import com.example.handler.BleWrapper
import com.example.handler.BleWrapper.BleCallback
import com.jjoe64.graphview.series.DataPoint
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

const val EXTRA_ARRAY_KEY = "INTENT EXTRA KEY"

class MainActivity : AppCompatActivity(), BleCallback {

    private var mBluetoothAdapter: BluetoothAdapter? = null
    private  var mScanCallback: BtleScanCallback? = null
    private var mBluetoothLeScanner: BluetoothLeScanner? = null
    private var mScanning: Boolean = false
    private var mHandler: Handler? = null
    private var mBleWrapper: BleWrapper? = null

    private var heartRateDatapoints: ArrayList<Int> = arrayListOf()

    val devices: HashMap<String, ScanResult> = hashMapOf<String, ScanResult>()

    val myListAdapter = MyListAdapter(this@MainActivity, arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothAdapter = bluetoothManager.adapter

        hasPermissions()

        btnScan.setOnClickListener {
            Log.d("DBg", "Calling scan")
            startScan()
        }

        list_item.adapter = myListAdapter
        list_item.setOnItemClickListener{ _, _, position, _ ->
            Log.d("DBG", " you clicked ${myListAdapter.deviceList[position]}")
            val scanResult = myListAdapter.deviceList[position]
            mBleWrapper = BleWrapper(this, scanResult.device.address)
            mBleWrapper!!.addListener(this)
            mBleWrapper!!.connect(false)
        }

        heartRateText.setOnClickListener {
            if(it == heartRateText) {
                val intent = Intent(this, GraphActivity::class.java)
                startActivity(intent)
            }
        }


    }

    private var mScanResults: HashMap<String, ScanResult>? = null
    companion object {
        const val SCAN_PERIOD: Long = 3000
    }

    private fun hasPermissions(): Boolean {
        if (mBluetoothAdapter == null || !mBluetoothAdapter!!.isEnabled) {
            Log.d("DBG", "No Bluetooth LE capability")
            return false
        } else if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            Log.d("DBG", "No fine location access")
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1);
            return true // assuming that the user grants permission
        }
        return true
    }

    private fun startScan() {
        Log.d("DBG", "Scan start")
        mScanResults = HashMap()
        mScanCallback = BtleScanCallback()
        mBluetoothLeScanner = mBluetoothAdapter!!.bluetoothLeScanner
        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
            .build()
        val filter: List<ScanFilter>? = null
        // Stops scanning after a pre-defined scan period.
        mHandler = Handler()
        mHandler!!.postDelayed({ stopScan() }, SCAN_PERIOD)
        mScanning = true
        mBluetoothLeScanner!!.startScan(filter, settings, mScanCallback)
    }

    private fun stopScan() {
        Log.d("DBG", "Ble scan stopped")
        mBluetoothLeScanner?.stopScan(mScanCallback)
    }

    private inner class BtleScanCallback : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            addScanResult(result)
        }
        override fun onBatchScanResults(results: List<ScanResult>) {
            for (result in results) {
                addScanResult(result)
            }
        }
        override fun onScanFailed(errorCode: Int) {
            Log.d("DBG", "BLE Scan Failed with code $errorCode")
        }
        private fun addScanResult(result: ScanResult) {
            val device = result.device
            val deviceAddress = device.address
            mScanResults!![deviceAddress] = result

            if (!devices.containsKey(result.device.address)) {
                devices.put(result.device.address, result)
                myListAdapter.add(result)
            }


        }
    }


    override fun onDeviceReady(gatt: BluetoothGatt) {
        Log.d("DBG", "Device ready ${gatt.device}")
        mBleWrapper!!.getNotifications(gatt, mBleWrapper!!.HEART_RATE_SERVICE_UUID, mBleWrapper!!.HEART_RATE_MEASUREMENT_CHAR_UUID)
        deviceName.text = gatt.device.name ?: "No name"
    }

    override fun onDeviceDisconnected() {
        Log.d("DBG", "Device disconnected")
    }

    override fun onNotify(characteristic: BluetoothGattCharacteristic) {
        Log.d("DBG", "Notify received ${characteristic.value[1]}")
        heartRateText.text = characteristic.value[1].toString()
        Log.d("DBG", " array size ${heartRateDatapoints.size}")
        val data = characteristic.value[1].toInt()
        heartRateDatapoints.add(data)
        HearRate.heartRateList.appendData(DataPoint(heartRateDatapoints.size.toDouble(), data.toDouble()), false, 1000)
    }
}
