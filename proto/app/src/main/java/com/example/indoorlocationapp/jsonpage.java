package com.example.indoorlocationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class jsonpage extends AppCompatActivity {
    private Button buttonScan;
    private Button queryButton;
    private EditText macText1;
    private EditText macText2;
    private EditText macText3;
    private ProgressBar progressBar1;
    private ProgressBar progressBar2;
    private ProgressBar progressBar3;
    private TextView responseView1;
    private TextView responseView2;
    private TextView responseView3;
    private ArrayList<String> bssid_id = new ArrayList<>();
    private ArrayList<String> bssid_limit = new ArrayList<>();
    private ArrayList<String> ss_id = new ArrayList<>();
    private ArrayList<String> ss_limit = new ArrayList<>();
    private ArrayList<String> bssid_ss = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsonpage);

        buttonScan = findViewById(R.id.scanBtn);
        queryButton = findViewById(R.id.queryButton);

        progressBar1 = findViewById(R.id.progressBar1);
        progressBar2 = findViewById(R.id.progressBar2);
        progressBar3 = findViewById(R.id.progressBar3);

        macText1 = findViewById(R.id.macText1);
        macText2 = findViewById(R.id.macText2);
        macText3 = findViewById(R.id.macText3);

        bssid_id = getIntent().getStringArrayListExtra("bssid_id");
        ss_id = getIntent().getStringArrayListExtra("ss_id");

        for (int i = 0; i < 3; i++) {

            try {
                bssid_limit.add(bssid_id.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        for (int i = 0; i < 3; i++) {

            try {
                ss_limit.add(ss_id.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        for (int i = 0; i < 3; i++) { // Loop through every bssid/ss number combo
            bssid_ss.add(bssid_limit.get(i) + "," + ss_limit.get(i)); // Concat the two, and add it
        }

            StringBuilder bssid = new StringBuilder();
        for(String s : bssid_ss){
            bssid.append(s + ";");
        }

        String listString = bssid.toString();

        String mac1 = bssid_limit.get(0);
        String mac2 = bssid_limit.get(1);
        String mac3 = bssid_limit.get(2);

        macText1.setText(mac1);
        macText2.setText(mac2);
        macText3.setText(mac3);

        responseView1 = findViewById(R.id.responseView1);
        responseView2 = findViewById(R.id.responseView2);
        responseView3 = findViewById(R.id.responseView3);

        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openmainpage();

            }
        });

        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RetrieveFeedTask1().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                new RetrieveFeedTask2().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                new RetrieveFeedTask3().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            }
        });
    }
    private class RetrieveFeedTask1 extends AsyncTask<Void, Void, String> {
        private Exception exception;
        String mac1;

        protected void onPreExecute() {
            progressBar1.setVisibility(View.VISIBLE);
            responseView1.setText("");
            mac1 = macText1.getText().toString();
        }

        protected String doInBackground(Void... urls) {
            // Do some validation here
            try {
                String API_URL = "https://api.mylnikov.org/geolocation/wifi?v=1.1&bssid=";
                URL url1 = new URL(API_URL +  mac1);
                HttpURLConnection urlConnection1 = (HttpURLConnection) url1.openConnection();
                try {
                    BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(urlConnection1.getInputStream()));
                    StringBuilder stringBuilder1 = new StringBuilder();

                    String line1;
                    while ((line1 = bufferedReader1.readLine()) != null) {
                        stringBuilder1.append(line1).append("\n");
                    }
                    bufferedReader1.close();
                    return stringBuilder1.toString();
                }
                finally{
                    urlConnection1.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            progressBar1.setVisibility(View.GONE);
            Log.i("INFO", response);
            /*
            //First create JSON object from the json...
            JSONObject  json = null;
            try {
                json = new JSONObject(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject  data = null;
            try {
                data = json.getJSONObject ("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Then get lon and lat as string...
            String longitude = null;
            try {
                longitude = data.getString("lon");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String latitude = null;
            try {
                latitude = data.getString("lat");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            StringBuilder sb = new StringBuilder();
            sb.append(longitude).append(" longitude, ");
            sb.append(latitude).append(" latitude, ");
            String finalMsgText = sb.toString();

             */
            responseView1.setText(response);

        }

    }
    private class RetrieveFeedTask2 extends AsyncTask<Void, Void, String> {
        private Exception exception;
        String mac2;

        protected void onPreExecute() {
            progressBar2.setVisibility(View.VISIBLE);
            responseView2.setText("");
            mac2 = macText2.getText().toString();
        }

        protected String doInBackground(Void... urls) {
            try {
                String API_URL = "https://api.mylnikov.org/geolocation/wifi?v=1.1&bssid=";
                URL url2 = new URL(API_URL + mac2);
                HttpURLConnection urlConnection2 = (HttpURLConnection) url2.openConnection();
                try {
                    BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(urlConnection2.getInputStream()));
                    StringBuilder stringBuilder2 = new StringBuilder();
                    String line2;
                    while ((line2 = bufferedReader2.readLine()) != null) {
                        stringBuilder2.append(line2).append("\n");
                    }
                    bufferedReader2.close();
                    return stringBuilder2.toString();
                } finally {
                    urlConnection2.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            }
            progressBar2.setVisibility(View.GONE);
            Log.i("INFO", response);
            responseView2.setText(response);

        }
    }
    private class RetrieveFeedTask3 extends AsyncTask<Void, Void, String> {
        private Exception exception;
        String mac3;

        protected void onPreExecute() {
            progressBar3.setVisibility(View.VISIBLE);
            responseView3.setText("");
            mac3 = macText3.getText().toString();
        }

        protected String doInBackground(Void... urls) {
            try {
                String API_URL = "https://api.mylnikov.org/geolocation/wifi?v=1.1&bssid=";
                URL url3 = new URL(API_URL +  mac3);
                HttpURLConnection urlConnection3 = (HttpURLConnection) url3.openConnection();
                try {
                    BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(urlConnection3.getInputStream()));
                    StringBuilder stringBuilder3 = new StringBuilder();
                    String line3;
                    while ((line3 = bufferedReader3.readLine()) != null) {
                        stringBuilder3.append(line3).append("\n");
                    }
                    bufferedReader3.close();
                    return stringBuilder3.toString();
                }
                finally{
                    urlConnection3.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }
        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            }
            progressBar3.setVisibility(View.GONE);
            Log.i("INFO", response);
            responseView3.setText(response);
        }
        }
        public void openmainpage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
