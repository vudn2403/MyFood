package com.vudn.myfood.view.map;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vudn.myfood.R;
import com.vudn.myfood.base.BaseActivity;
import com.vudn.myfood.base.Key;
import com.vudn.myfood.model.restaurant.QuanAnModel;
import com.vudn.myfood.presenter.other.DanDuongToiQuanAnController;


public class DanDuongToiQuanAnActivity extends BaseActivity
        implements OnMapReadyCallback{

    GoogleMap googleMap;
    MapFragment mapFragment;
    SharedPreferences sharedPreferences;
    LatLng vitrihientai;
    LatLng vitriquanan;
    QuanAnModel quanAnModel;
    //MapManager mapManager;

    DanDuongToiQuanAnController danDuongToiQuanAnController;
    String duongdan = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.layout_danduong;
    }

    @Override
    protected void initializeComponents() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //danDuongToiQuanAnController = new DanDuongToiQuanAnController();
        sharedPreferences = getSharedPreferences("toado", Context.MODE_PRIVATE);
        Double lat = Double.parseDouble(sharedPreferences.getString("latitude","0"));
        Double log = Double.parseDouble(sharedPreferences.getString("longitude","0"));
        vitrihientai = new LatLng(lat, log);

        vitriquanan = getIntent().getParcelableExtra(Key.INTENT_TOA_DO);
        quanAnModel = getIntent().getParcelableExtra(Key.INTENT_CHI_TIET_QUAN_AN);
        //duongdan = "https://maps.googleapis.com/maps/api/directions/json?origin=" + vitrihientai.getLatitude() + "," + vitrihientai.getLongitude() + "&destination=" +latitude+"," + longitude + "&language=vi&key=AIzaSyCSNQCX6UYnoiq-BSoaHRdQvmPovWRQeSY";
        getSupportActionBar().setTitle(quanAnModel.getTenquanan());
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null){
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    protected void registerListeners() {

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.clear();
        //mapManager = new MapManager(this, googleMap);

        if (quanAnModel == null){
            Toast.makeText(this, "Lỗi hiển thị vị trí nhà hàng!", Toast.LENGTH_SHORT).show();
            return;
        }
        //mapManager.showRestaurant(quanAnModel);

        MarkerOptions mMarker = new MarkerOptions();
        mMarker.position(vitrihientai);
        mMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_youarehere_marker));
        googleMap.addMarker(mMarker);

        MarkerOptions markervitriquanan = new MarkerOptions();
        markervitriquanan.position(vitriquanan);
        markervitriquanan.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_restaurant));
        markervitriquanan.title(quanAnModel.getTenquanan());
        markervitriquanan.snippet(quanAnModel.getDiachi().getAddress());
        googleMap.addMarker(markervitriquanan).showInfoWindow();

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(vitriquanan,16);
        googleMap.moveCamera(cameraUpdate);

        //danDuongToiQuanAnController.HienThiDanDuongToiQuanAn(googleMap,duongdan);
    }

    @Override
    protected void onDestroy() {
        //mapManager.unregisterLocationUpdate();
        super.onDestroy();
    }
}
