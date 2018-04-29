package kailashsoni.com.MyBucketList.Activity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import kailashsoni.com.MyBucketList.R;

/**
 * Created by kailashsoni on 4/4/18
 */

// This is to show the google maps in this app for the update field
public class showGoogleMap extends FragmentActivity implements OnMapReadyCallback {

    // define variables to be used
    private GoogleMap mMap;
    private TextView markerAddress;
    public static double latitude,longitude;
    private String TAG = "show googlemap";
    private Button locatioPickBtn;
    private String String_markerAddress = null, locality = null,countryName = null;
    private String titleString = null,dateString = null, descriptionString = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.googlemap_screen);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }else {
            titleString = bundle.getString("titleKEY");
            dateString = bundle.getString("dueDateKEY");
            descriptionString = bundle.getString ("descriptionKEY");
        }

        markerAddress = findViewById(R.id.pinAddressTV);
        locatioPickBtn = findViewById(R.id.getLocationButton);
        // google add marker
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //click event on location button
        locatioPickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String_markerAddress = markerAddress.getText().toString();
                if(String_markerAddress != null && !String_markerAddress.equalsIgnoreCase("")){
                    Intent intent = new Intent(showGoogleMap.this,AddBucketItem.class);
                    intent.putExtra("markLateKEY",latitude);
                    intent.putExtra("markLongKEY",longitude);
                    intent.putExtra("markAddressKEY",locality+" "+countryName);
                    intent.putExtra("buttonKEY","showGoogle");
                    intent.putExtra("intTitleKEY",titleString);
                    intent.putExtra("intDescriptionKEY", descriptionString);
                    intent.putExtra("intDateKEY",dateString);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(showGoogleMap.this, "You didn't select location !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // gets the map ready to open at a specific location
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng latLng = mMap.getCameraPosition().target;
                Geocoder geocoder = new Geocoder(showGoogleMap.this);
                Log.e(TAG,"--- latlong "+latLng);
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                /*AddBucketItem.lonTxt.setText(String.valueOf(longitude));
                AddBucketItem.latTxt.setText(String.valueOf(latitude));*/
                try {
                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                    if(addressList != null && addressList.size()>0){
                         locality = addressList.get(0).getAddressLine(0);
                         countryName = addressList.get(0).getCountryName();
                        Log.e(TAG,"country is "+countryName+"\tlocality\n"+locality);
                        if (!locality.isEmpty() && !countryName.isEmpty()) {
                          /*  AddBucketItem.addressET.setText(locality + "  " + countryName);*/
                            markerAddress.setText(locality + "  " + countryName);
                        }
                    }else {

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(25.276987,55.296249),14.0f));
    }
}
