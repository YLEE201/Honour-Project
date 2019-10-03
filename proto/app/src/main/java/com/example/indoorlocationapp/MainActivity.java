package com.example.indoorlocationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
//import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private  WifiManager mainWifiObj;
    private ListView listView;
    private Button buttonScan;
    private int size = 0;
    private List<ScanResult> results;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonScan = findViewById(R.id.scanBtn);
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanWifi();

            }
        });

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
        @Override
        public void onReceive(Context context, Intent intent)  {
            //get scan results
            results = mainWifiObj.getScanResults();
            //unregisters receiver
            unregisterReceiver(this);

            //stores results in array list then sends to adapter
            for (ScanResult scanResult : results) {
                arrayList.add(scanResult.SSID);
                arrayList.add(String.valueOf(scanResult.level));
                adapter.notifyDataSetChanged();
            }
        }
    };

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
