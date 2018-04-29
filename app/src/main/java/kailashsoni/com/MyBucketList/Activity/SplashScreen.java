package kailashsoni.com.MyBucketList.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import kailashsoni.com.MyBucketList.R;
import kailashsoni.com.MyBucketList.SessionManger.SessionManager;

// When the app opens up the following will be displayed
public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    public static final int MY_PERMISSIONS_REQUEST = 123;
    private ConstraintLayout constraintLayout;
    private Context context ;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_);
        //all views
        constraintLayout = findViewById(R.id.constraintlayoutSplash);
        //------------------------------------- 
        context = SplashScreen.this;
        sessionManager = new SessionManager( this);
        boolean result = checkPermissionIS();

        if (result == true) {
            if (sessionManager.isLoggedIn()) {
                startActivity(new Intent(SplashScreen.this, NavigationActivity.class));
                finish();
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent mainIntent = new Intent(SplashScreen.this, Login.class);
                        SplashScreen.this.startActivity(mainIntent);
                    }
                }, SPLASH_TIME_OUT);
            }
        }
    }

// checks if  the permissions are accepted by the user or not
    private boolean checkPermissionIS() {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("Permission necessary");
                alertBuilder.setMessage("permission is necessary!!!");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST);
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
            } else {
                ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST);
            }
            return false;
        } else {
            return true;
        }
    }
    // Will perform the following class of the request permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, 	@NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (sessionManager.isLoggedIn()){
                        startActivity(new Intent(SplashScreen.this, NavigationActivity.class));
                        finish();
                    }else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent mainIntent = new Intent(SplashScreen.this,Login.class);
                                SplashScreen.this.startActivity(mainIntent);
                            }
                        }, SPLASH_TIME_OUT);
                     }
                        } else {
                    //code for deny
                    Toast.makeText(context, "Please allow permission", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }

    }
}
