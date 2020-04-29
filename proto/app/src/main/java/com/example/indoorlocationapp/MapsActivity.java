package com.example.indoorlocationapp;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<String> lon = new ArrayList<>();
    private ArrayList<String> lat = new ArrayList<>();
    private ArrayList<LatLng> cord = new ArrayList<LatLng>();
    private String LatUser;
    private String LongUser;

    private double latvalue1;
    private double lonvalue1;

    private double latvalue2;
    private double lonvalue2;

    private double latvalue3;
    private double lonvalue3;

    private double userLat;
    private double userLong;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        lat = getIntent().getStringArrayListExtra("lat");
        lon = getIntent().getStringArrayListExtra("lon");
        LatUser = getIntent().getStringExtra("xUser");
        LongUser = getIntent().getStringExtra("yUser");

        latvalue1 = new Double(lat.get(0)).doubleValue();
        lonvalue1 = new Double(lon.get(0)).doubleValue();

        latvalue2 = new Double(lat.get(1)).doubleValue();
        lonvalue2 = new Double(lon.get(1)).doubleValue();

        latvalue3 = new Double(lat.get(2)).doubleValue();
        lonvalue3 = new Double(lon.get(2)).doubleValue();

        userLat = new Double(LatUser).doubleValue();
        userLong = new Double(LongUser).doubleValue();

        LatLng AP1 = new LatLng(latvalue1, lonvalue1);
        LatLng AP2 = new LatLng(latvalue2, lonvalue2);
        LatLng AP3 = new LatLng(latvalue3, lonvalue3);
        LatLng User = new LatLng(userLat, userLong);

        cord.add(AP1);
        cord.add(AP2);
        cord.add(AP3);
        cord.add(User);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //places markers for user and access points
        for (int i = 0; i<cord.size();i++){
            if(i == 3)
            {
                mMap.addMarker(new MarkerOptions().position(cord.get(i)).title("User"));
            }
            else {
                mMap.addMarker(new MarkerOptions().position(cord.get(i)).title("Access Point " + (i + 1)));
            }
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cord.get(i), 19));

            //draws line between access points
            PolylineOptions line=
                    new PolylineOptions().add(new LatLng(latvalue1,
                                    lonvalue1),
                            new LatLng(latvalue2,
                                    lonvalue2),
                            new LatLng(latvalue3,
                                    lonvalue3),
                            new LatLng(latvalue1,
                                    lonvalue1))
                            .width(5).color(Color.RED);

            mMap.addPolyline(line);

            //adds the floor map to google maps
            LatLng c1 = new LatLng( 55.866575, -4.250756);
            LatLng c2 = new LatLng( 55.866334, -4.249962);
            LatLng c3 = new LatLng( 55.867475, -4.249479);
            LatLng c4 = new LatLng( 55.867692, -4.250273);
            LatLngBounds latLngBounds = new LatLngBounds(c1, c3);
            GroundOverlayOptions groundOverlayOptions = new
                    GroundOverlayOptions();

            Bitmap FloorMap = BitmapFactory.decodeResource(getResources(), R.drawable.g0001);
            BitmapDescriptor bitmapDescriptor =
                    BitmapDescriptorFactory.fromBitmap(FloorMap);
            groundOverlayOptions.image(bitmapDescriptor);
            groundOverlayOptions.positionFromBounds(latLngBounds);
            mMap.addGroundOverlay(groundOverlayOptions);
        }

    }
}
