package com.vudn.myfood.view.beginscreen;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.vudn.myfood.R;
import com.vudn.myfood.base.BaseActivity;
import com.vudn.myfood.view.main.MainActivity;
import com.vudn.myfood.view.map.MapActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class SlashScreenActivity extends BaseActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    public static final String TAG = "SlashScreenActivity";
    public static final int REQUEST_PERMISSION_LOCATION = 1;

    private ImageView imgSlashScreen;
    private TextView txtVersion;
    private TextView txtAddress;
    private TextView txtLoading;
    
    GoogleApiClient googleApiClient;
    
    public SharedPreferences sharedPreferences;
    private LocationManager locationManage;
    private Geocoder geocoder;
    private List<Address> mAddressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initImage();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_splash_screen;
    }

    @Override
    protected void initializeComponents() {
        imgSlashScreen = findViewById(R.id.img_slash_screen);
        txtVersion = findViewById(R.id.txt_version);
        txtAddress = findViewById(R.id.txt_address);
        txtLoading = findViewById(R.id.txt_loading);

        sharedPreferences = getSharedPreferences("toado", MODE_PRIVATE);

        locationManage = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        geocoder = new Geocoder(this, Locale.getDefault());

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        int checkPermissionCoarseLocaltion = ContextCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int checkPermissionFineLocation = ContextCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (checkPermissionCoarseLocaltion != PackageManager.PERMISSION_GRANTED
                && checkPermissionCoarseLocaltion != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions
                    (this, new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                            },
                            REQUEST_PERMISSION_LOCATION);
        } else {
            googleApiClient.connect();
        }
    }

    @Override
    protected void registerListeners() {

    }

    private void initImage() {
        imgSlashScreen.setImageResource(R.drawable.img_splashscreen);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    googleApiClient.connect();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//        Criteria criteria = new Criteria();
//        criteria.setPowerRequirement(Criteria.POWER_HIGH);
//        Location vitriHienTai = locationManage.getLastKnownLocation(
//                locationManage.getBestProvider(criteria, true)
//        );
        Location vitriHienTai = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (vitriHienTai != null) {
            try {
                Log.d(TAG, "onConnected: Lat: " + vitriHienTai.getLatitude() );
                Log.d(TAG, "onConnected: Lng: " + vitriHienTai.getLongitude());
                for (Address address : mAddressList = geocoder.getFromLocation(vitriHienTai.getLatitude(), vitriHienTai.getLongitude(), 1)) {
                    Log.d(TAG, "onConnected: Address: " + address.getAddressLine(0));
                    String mAddress = address.getAddressLine(0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("latitude", String.valueOf(vitriHienTai.getLatitude()));
                    editor.putString("longitude", String.valueOf(vitriHienTai.getLongitude()));
                    editor.putString("address", mAddress);
                    editor.commit();

                    txtAddress.setText(mAddress.substring(0, mAddress.lastIndexOf(",")));
                }
            } catch (IOException e) {
                Log.d(TAG, "onConnected: " + e.toString());
            }
            try {
                PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                txtVersion.setText(getString(R.string.phienban) + " " + packageInfo.versionName);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent iSignIn = new Intent(SlashScreenActivity.this, MainActivity.class);
                        startActivity(iSignIn);
                        finish();
                    }
                }, 1000);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            /*Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, MapActivity.REQUEST_CODE_GPS);*/
            txtAddress.setText(sharedPreferences.getString("address", ""));
            try {
                PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                txtVersion.setText(getString(R.string.phienban) + " " + packageInfo.versionName);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent iSignIn = new Intent(SlashScreenActivity.this, MainActivity.class);
                        startActivity(iSignIn);
                        finish();
                    }
                }, 5000);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
