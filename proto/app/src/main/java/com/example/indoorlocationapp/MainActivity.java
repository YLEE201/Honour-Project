package com.example.indoorlocationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;

import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.sip.SipSession;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.Manifest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    private WifiManager mainWifiObj;
    private ListView listView;
    private Button buttonScan;
    private Button buttonsend;
    private List<ScanResult> results;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter adapter;
    private ArrayList<String> bssid = new ArrayList<>();
    private ArrayList<String> SignalStrength = new ArrayList<>();
    private final int REQUEST_LOCATION_PERMISSION = 1;
    private LocationListener Listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //permission check
        requestLocationPermission();

        buttonScan = findViewById(R.id.scanBtn);
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanWifi();

            }
        });

        buttonsend = findViewById(R.id.sendBtn);
        buttonsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openjsonpage();

            }
        });


        //instantiates variables
        listView = findViewById(R.id.wifiList);
        mainWifiObj = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        //makes sure Wifi is enabled
        if (!mainWifiObj.isWifiEnabled()) {
            Toast.makeText(this,  "WiFi is disabled ... we need to enable it", Toast.LENGTH_LONG).show();
            mainWifiObj.setWifiEnabled(true);
        }

        //store the array list into a list view
        adapter = new ArrayAdapter<>( this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);

        //starts the class scanWifi()
        scanWifi();
    }
        //initialises array list, check if scan is available, starts scan and check if its successful
        private void scanWifi(){
            arrayList.clear();
            bssid.clear();
            registerReceiver(wifiReceiver, new IntentFilter(mainWifiObj.SCAN_RESULTS_AVAILABLE_ACTION));
            boolean success = mainWifiObj.startScan();
            Toast.makeText( this, "Scanning WiFi ...", Toast.LENGTH_SHORT).show();
            if (success) {
                //scanSuccess();
            }
            else{
                // scan failure handling
                //scanFailure();
            }
        }

        //sends broadcasts to see if scan is available
        BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context context, Intent intent)  {
            //get scan results
            results = mainWifiObj.getScanResults();
            //unregisters receiver
            unregisterReceiver(this);

            //stores results in array list then sends to adapter
            for (ScanResult scanResult : results) {
                //wifi name
                arrayList.add("SSID: " + scanResult.SSID);

                //wifi identifier
                arrayList.add("BSSID: " + scanResult.BSSID);
                bssid.add(scanResult.BSSID);

                //Signal strength
                String dbm = String.valueOf(scanResult.level);
                arrayList.add("dbm: " + dbm);
                SignalStrength.add(dbm);

                //WifiInfo wifiInfo = mainWifiObj.getConnectionInfo();
                //int frequency = wifiInfo.getFrequency();
                //arrayList.add(String.valueOf(frequency));

                //frequency
                arrayList.add("frequency: " + scanResult.frequency +"MHZ");

                //Distance
                double distance = calculateDistance(scanResult.level, scanResult.frequency);
                String Sdistance = String.valueOf(distance);
                arrayList.add("Router is " + Sdistance + "m");
                adapter.notifyDataSetChanged();


            }
        }
    };
    //Distance formula
    public double calculateDistance(double signalLevelInDb, double freqInMHz) {
        double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(signalLevelInDb)) / 20.0;
        return Math.pow(10.0, exp);
    }

    //Permissions code - uses the EasyPermissions from google - dependency in gradle.app
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if(EasyPermissions.hasPermissions(this, perms)) {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
        else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }
    }
    public void openjsonpage(){
        Intent intent = new Intent(this, jsonpage.class);
        intent.putExtra("bssid_id", bssid);
        intent.putExtra("ss_id", SignalStrength);
        startActivity(intent);
    }
 /*
    private void scanSuccess() {
        WifiManager mainWifiObj;
        mainWifiObj = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);


// Level of a Scan Result
        List<ScanResult> wifiList = mainWifiObj.getScanResults();
        for (ScanResult scanResult : wifiList) {
            int level = WifiManager.calculateSignalLevel(scanResult.level, 5);
            System.out.println("Level is " + level + " out of 5");
        }

// Level of current connection
        int rssi = mainWifiObj.getConnectionInfo().getRssi();
        int level = WifiManager.calculateSignalLevel(rssi, 5);
        System.out.println("Level is " + level + " out of 5");

        WifiInfo wifiInfo = mainWifiObj.getConnectionInfo();
        int frequency = wifiInfo.getFrequency();
    }
    private void scanFailure() {
        // handle failure: new scan did NOT succeed
        // consider using old scan results: these are the OLD results!
        WifiManager mainWifiObj;
        mainWifiObj = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        List<ScanResult> results = mainWifiObj.getScanResults();
    }*/
}
