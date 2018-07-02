package com.vudn.myfood.model.map;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vudn.myfood.R;
import com.vudn.myfood.model.restaurant.QuanAnModel;
import com.vudn.myfood.view.map.MapActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MapManager {
    private static final String TAG = "Map Manager";
    private GoogleMap googleMap;
    private Context context;
    private LocationRequest locationRequest;
    private LocationManager locationManager;
    private String provider;
    private LocationListener locationListener;
    private boolean isRegister;
    private double myLatitude;
    private double myLongitude;
    private Marker myMaker;
    private LayoutInflater inflater;
    private List<Marker> markerList;
    private Location location;

    public Location getLocation() {
        location = new Location("");
        location.setLatitude(myLatitude);
        location.setLongitude(myLongitude);
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public MapManager(Context context, GoogleMap googleMap) {
        this.context = context;
        this.googleMap = googleMap;
        inflater = LayoutInflater.from(context);
        markerList = new ArrayList<>();
        setupMaps();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        checkGpsSetting();
    }

    private void setupMaps() {
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setInfoWindowAdapter(new CustomInfoWindow());
        try {
            googleMap.setMyLocationEnabled(true);
            googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    return false;
                }
            });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void checkGpsSetting() {
        /*LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        SettingsClient client = new SettingsClient();*/

        if (locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getMyLocation();
            return;
        } else {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            ((MapActivity) context).startActivityForResult(intent, MapActivity.REQUEST_CODE_GPS);
        }
    }

    public void getMyLocation() {
        try {
            Criteria criteria = new Criteria();
            criteria.setPowerRequirement(Criteria.POWER_HIGH);
            provider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(provider);

            if (location != null) {
                if (!isRegister){
                    registerLocationUpdate();
                    isRegister = true;
                }
                myLatitude = location.getLatitude();
                myLongitude = location.getLongitude();
                //showMyMaker();
                moveCameraToPosition();
                Log.d(TAG, TAG + "getMyLocation: move Camera");
                //drawShape();
            } else {
                Log.i(TAG, "getMyLocation: null");
            }

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void registerLocationUpdate() {
        if (locationManager != null) {
            try {
                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        myLatitude = location.getLatitude();
                        myLongitude = location.getLongitude();
                        //showMyMaker();
                        moveCameraToPosition();
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                };
                locationManager.requestLocationUpdates(provider, 50000, 10, locationListener);
            } catch (SecurityException e) {
                e.printStackTrace();
            }

        }
    }

    public void unregisterLocationUpdate() {
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    private void showMyMaker() {
        if (myMaker == null) {
            MarkerOptions markerOptions = new MarkerOptions();
            //markerOptions.icon(BitmapDescriptorFactory.defaultMarker());
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_youarehere_marker));
            markerOptions.position(new LatLng(myLatitude, myLongitude));
            markerOptions.title("My location");
            myMaker = googleMap.addMarker(markerOptions);
            //myMaker.setTag("abc xyz");
        } else {
            myMaker.setPosition(new LatLng(myLatitude, myLongitude));
        }

    }

    private void moveCameraToPosition() {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(myLatitude, myLongitude))
                .zoom(16)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void showRestaurantMaker(List<QuanAnModel> quanAnModelList) {
        googleMap.clear();
        /*if (markerList != null && !markerList.isEmpty()){
            while (markerList.size() != 0){
                markerList.get(0).remove();
                markerList.remove(0);
            }
            *//*for (int i = 0 ; i < markerList.size(); i ++){
                markerList.get(i).remove();
            }*//*
            markerList.clear();
        }
        googleMap.clear();*/
        for (QuanAnModel quanAnModel : quanAnModelList) {
            LatLng latLng = new LatLng(quanAnModel.getDiachi().getLatitude(), quanAnModel.getDiachi().getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_restaurant));
            markerOptions.title(quanAnModel.getTenquanan());
            Marker marker = googleMap.addMarker(markerOptions);
            marker.setTag(quanAnModel);
            markerList.add(marker);
        }
        //AddMarkerTask addMarkerTask = new AddMarkerTask();
        //addMarkerTask.execute(quanAnModelList);
        //CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(myLatitude, myLongitude),16);
        //googleMap.moveCamera(cameraUpdate);
    }

    public void showRestaurant(QuanAnModel quanAnModel) {
        /*LatLng latLng = quanAnModel.getDiachi().getLagLngQuanAn();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_restaurant));
        markerOptions.title(quanAnModel.getTenquanan());
        googleMap.addMarker(markerOptions).setTag(quanAnModel);*/
    }

    QuanAnModel quanAnModel;

    private class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {
        View itemView = inflater.inflate(R.layout.view_info_windown, null);
        CircleImageView imgView = itemView.findViewById(R.id.img_view);
        TextView txtName = itemView.findViewById(R.id.txt_name);
        TextView txtAddress = itemView.findViewById(R.id.txt_address);
        TextView txtDistant = itemView.findViewById(R.id.txt_distant);

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            if (marker.getTag() instanceof QuanAnModel) {
                quanAnModel = (QuanAnModel) marker.getTag();
            }

            if (quanAnModel != null) {
                Glide.with(context).load(quanAnModel.getHinhanhquanan().get(0)).into(imgView);
                Glide.with(context).load(quanAnModel.getHinhanhquanan().get(0)).into(imgView);
                Log.d(TAG, TAG + "getInfoContents: glide load ảnh");
                txtName.setText(quanAnModel.getTenquanan());
                txtAddress.setText(quanAnModel.getDiachi().getAddress()
                        .replace(" VietNam", "")
                        .replace(", Việt Nam", ""));
                txtAddress.setSelected(true);
                txtDistant.setText(String.format("%.1f", quanAnModel.getKhoangcach()) + "km");
            }
            return itemView;
        }
    }

    private class AddMarkerTask extends AsyncTask<List<QuanAnModel>, Object, Void> {

        @Override
        protected Void doInBackground(List<QuanAnModel>... lists) {

            return null;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
            Message message = new Message();
            Message message1 = new Message();
            if (values[0] instanceof MarkerOptions) {
                MarkerOptions markerOptions = (MarkerOptions) values[0];

                message.what = 1;
                message.obj = markerOptions;
            }
            if (values[1] instanceof QuanAnModel) {
                QuanAnModel quanAnModel = (QuanAnModel) values[1];
                message1.what = 2;
                message1.obj = quanAnModel;
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
