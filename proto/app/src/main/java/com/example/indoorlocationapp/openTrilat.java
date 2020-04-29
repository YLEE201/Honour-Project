package com.example.indoorlocationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class openTrilat extends AppCompatActivity {

    private ArrayList<String> sd_id = new ArrayList<>();
    private ArrayList<String> lon = new ArrayList<>();
    private ArrayList<String> lat = new ArrayList<>();
    private ArrayList<String> ssid_id = new ArrayList<>();
    private Button buttonsend;
    private Button buttonMaps;
    private Button getpos;
    private ArrayList<String> wifi = new ArrayList<>();
    private EditText wifitext1;
    private EditText wifitext2;
    private EditText wifitext3;
    private EditText user;
    private String LatUser;
    private String LongUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_trilat);

        lat = getIntent().getStringArrayListExtra("lat");
        lon = getIntent().getStringArrayListExtra("lon");
        sd_id = getIntent().getStringArrayListExtra("sd_id");
        ssid_id = getIntent().getStringArrayListExtra("ssid_id");

        for (int i = 0; i < 3; i++) { // Loop through every wifi
            wifi.add("Name :" + ssid_id.get(i) + "\n" + "Latitude :" + lat.get(i) + "\n" + "Longitude :" + lon.get(i) + "\n" +
                    "Distance :" + sd_id.get(i) ); // Concat, and add it
        }


        wifitext1 = findViewById(R.id.wifitext1);
        wifitext1.setText(wifi.get(0));

        wifitext2 = findViewById(R.id.wifitext2);
        wifitext2.setText(wifi.get(1));

        wifitext3 = findViewById(R.id.wifitext3);
        wifitext3.setText(wifi.get(2));

        buttonsend = findViewById(R.id.sendBtn);
        getpos = findViewById(R.id.getPos);
        buttonMaps = findViewById(R.id.buttonMaps);


        buttonsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openjsonpage();

            }
        });

        buttonMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMappage();

            }
        });
        getpos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Trilateration Method
                //DECLARE VARIABLES
                double[] P1 = new double[2];
                double[] P2 = new double[2];
                double[] P3 = new double[2];
                double[] ex = new double[2];
                double[] ey = new double[2];
                double[] p3p1 = new double[2];
                double distance1;
                double distance2;
                double distance3;
                double jval = 0;
                double temp = 0;
                double ival = 0;
                double p3p1i = 0;
                double triptx;
                double tripty;
                double xval;
                double yval;
                double t1;
                double t2;
                double t3;
                double t;
                double exx;
                double d;
                double eyy;

                //TRANSALTE POINTS TO VECTORS
                //POINT 1
                double latvalue1 = new Double(lat.get(0)).doubleValue();
                double lonvalue1 = new Double(lon.get(0)).doubleValue();
                P1[0] = latvalue1;
                P1[1] = lonvalue1;
                //POINT 2
                double latvalue2 = new Double(lat.get(1)).doubleValue();
                double lonvalue2 = new Double(lon.get(1)).doubleValue();
                P2[0] = latvalue2;
                P2[1] = lonvalue2;
                //POINT 3
                double latvalue3 = new Double(lat.get(2)).doubleValue();
                double lonvalue3 = new Double(lon.get(2)).doubleValue();
                P3[0] = latvalue3;
                P3[1] = lonvalue3;

                //TRANSFORM THE METERS VALUE FOR THE MAP UNIT
                //Amended distance division as my the wifi AP in my area is to far apart
                //ordinal value is 100000
                //DISTANCE BETWEEN POINT 1 AND MY LOCATION
                double dis1 = new Double(sd_id.get(0)).doubleValue();
                distance1 = (dis1 / 100000);
                //DISTANCE BETWEEN POINT 2 AND MY LOCATION
                double dis2 = new Double(sd_id.get(1)).doubleValue();
                distance2 = (dis2 / 100000);
                //DISTANCE BETWEEN POINT 3 AND MY LOCATION
                double dis3 = new Double(sd_id.get(2)).doubleValue();
                distance3 = (dis3 / 100000);

                for (int i = 0; i < P1.length; i++) {
                    t1 = P2[i];
                    t2 = P1[i];
                    t = t1 - t2;
                    temp += (t * t);
                }
                d = Math.sqrt(temp);
                for (int i = 0; i < P1.length; i++) {
                    t1 = P2[i];
                    t2 = P1[i];
                    exx = (t1 - t2) / (Math.sqrt(temp));
                    ex[i] = exx;
                }
                for (int i = 0; i < P3.length; i++) {
                    t1 = P3[i];
                    t2 = P1[i];
                    t3 = t1 - t2;
                    p3p1[i] = t3;
                }
                for (int i = 0; i < ex.length; i++) {
                    t1 = ex[i];
                    t2 = p3p1[i];
                    ival += (t1 * t2);
                }
                for (int i = 0; i < P3.length; i++) {
                    t1 = P3[i];
                    t2 = P1[i];
                    t3 = ex[i] * ival;
                    t = t1 - t2 - t3;
                    p3p1i += (t * t);
                }
                for (int i = 0; i < P3.length; i++) {
                    t1 = P3[i];
                    t2 = P1[i];
                    t3 = ex[i] * ival;
                    eyy = (t1 - t2 - t3) / Math.sqrt(p3p1i);
                    ey[i] = eyy;
                }
                for (int i = 0; i < ey.length; i++) {
                    t1 = ey[i];
                    t2 = p3p1[i];
                    jval += (t1 * t2);
                }
                xval = (Math.pow(distance1, 2) - Math.pow(distance2, 2) + Math.pow(d, 2)) / (2 * d);
                yval = ((Math.pow(distance1, 2) - Math.pow(distance3, 2) + Math.pow(ival, 2) + Math.pow(jval, 2)) / (2 * jval)) - ((ival / jval) * xval);

                t1 = new Double(lat.get(0)).doubleValue();
                t2 = ex[0] * xval;
                t3 = ey[0] * yval;
                triptx = t1 + t2 + t3;

                t1 = new Double(lon.get(0)).doubleValue();
                t2 = ex[1] * xval;
                t3 = ey[1] * yval;
                tripty = t1 + t2 + t3;

                LatUser = String.valueOf(triptx);
                LongUser = String.valueOf(tripty);

                String concat = "user Latitude: " + LatUser + "\n" + "user Longitude: " + LongUser;

                user = findViewById(R.id.user);
                user.setText(concat);
            }
        });


    }
    public void openjsonpage(){
        Intent intent = new Intent(this, jsonpage.class);
        startActivity(intent);
    }
    public void openMappage(){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("lon", lon);
        intent.putExtra("lat", lat);
        intent.putExtra("xUser", LatUser);
        intent.putExtra("yUser", LongUser);
        startActivity(intent);
    }
}
