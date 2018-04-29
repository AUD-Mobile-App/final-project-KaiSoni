package kailashsoni.com.MyBucketList.Activity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import kailashsoni.com.MyBucketList.R;

/**
 * Created by kailashsoni on 4/4/18
 */

// This is to show the google maps in this app
public class GoogleMap extends FragmentActivity implements
        OnMapReadyCallback, com.google.android.gms.maps.GoogleMap.OnMapLongClickListener
        , com.google.android.gms.maps.GoogleMap.OnMarkerDragListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    // define variables to be used
    private TextView markerAddress,mapLatitude,mapLongitude;
    public static double latitude, longitude;
    private String TAG = "show googlemap";
    private Button locatioPickBtn;
    private String String_markerAddress = null, locality = null, countryName = null, stringItemID = null
            , stringAddress = null, stringLat = null, stringLong = null, stringBtnClick = null;
    private String titleString = null, dateString = null, descriptionString = null;
    private LatLng latLng;
    private GoogleApiClient googleApiClient;
    private com.google.android.gms.maps.GoogleMap mMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.googlemap_screen);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        } else {
            titleString = bundle.getString("titleKEY");
            dateString = bundle.getString("dueDateKEY");
            stringItemID = bundle.getString("itemIDKEY");
            descriptionString = bundle.getString ("descriptionKEY");
            stringAddress = bundle.getString("addressKEY");
            stringLat = bundle.getString("latKEY");
            stringLong = bundle.getString("longKEY");
            stringBtnClick = bundle.getString("btnKEY");


        }

        markerAddress = findViewById(R.id.pinAddressTV);
        locatioPickBtn = findViewById(R.id.getLocationButton);

        mapLatitude = findViewById(R.id.latTxtvalue_map);
        mapLongitude = findViewById(R.id.lonTxtvalue_map);

        //set latitude and longitude and address
        markerAddress.setText(stringAddress);
        mapLatitude.setText(stringLat);
        mapLongitude.setText(stringLong);

        //The google add marker
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        //click event on location button
        locatioPickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String_markerAddress = markerAddress.getText().toString();
                if (String_markerAddress != null && !String_markerAddress.equalsIgnoreCase("")) {
                    Intent intent = new Intent(GoogleMap.this, AddBucketItem.class);
                    intent.putExtra("markLateKEY", latitude);
                    intent.putExtra("markLongKEY", longitude);
                    intent.putExtra("markAddressKEY", locality + " " + countryName);
                    intent.putExtra("buttonKEY", "showGoogle");
                    intent.putExtra("intTitleKEY", titleString);
                    intent.putExtra("intDescriptionKEY", descriptionString);
                    intent.putExtra("intDateKEY", dateString);
                    intent.putExtra("intItemIDKEY", stringItemID);
                    intent.putExtra("intBtnKEY", stringBtnClick);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(GoogleMap.this, "You didn't select location !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // gets the map ready to open at a specific location
    @Override
    public void onMapReady(com.google.android.gms.maps.GoogleMap googleMap) {
        mMap = googleMap;
        UiSettings settings =googleMap.getUiSettings();
        settings.setZoomControlsEnabled(true);
        mMap.setOnMapLongClickListener(this);
        mMap.setPadding(0,0,0,120);
        //LatLng latLng = new LatLng(25.276987, 55.296249);
        //mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(25.276987,55.296249),14.0f));

    }

    // when an map is clicked upon
    @Override
    public void onMapLongClick(LatLng latLng) {
        //Clearing all the markers
        mMap.clear();

        //Adding a new marker to the current pressed position we are also making the draggable true
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true));

        Geocoder geocoder = new Geocoder(GoogleMap.this);
        //Log.e(TAG, "--- latlong " + latLng);
        latitude = latLng.latitude;
        longitude = latLng.longitude;
        try {
            List<Address> addressList = geocoder.getFromLocation(latitude,longitude,1);
            if(addressList != null && addressList.size()>0){
                locality = addressList.get(0).getAddressLine(0);
                countryName = addressList.get(0).getCountryName();
                AddBucketItem.addressET.setText(locality + "  " + countryName);
                markerAddress.setText(locality + "  " + countryName);
                Log.e(TAG,"address with countr.y is "+locality+"--"+countryName+"==="
                        +addressList.get(0).getAddressLine(0)+"----"+addressList.get(0).getFeatureName()+
                        "state iii"+addressList.get(0).getAdminArea());
                mapLatitude.setText(String.valueOf(latitude));
                mapLongitude.setText(String.valueOf( longitude));
            }else {
                //Log.e(TAG,"latlang null !!!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
//Getting the coordinates
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }
}
