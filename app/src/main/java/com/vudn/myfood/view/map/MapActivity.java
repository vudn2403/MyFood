package com.vudn.myfood.view.map;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vudn.myfood.R;
import com.vudn.myfood.base.BaseActivity;
import com.vudn.myfood.base.Key;
import com.vudn.myfood.model.map.MapManager;
import com.vudn.myfood.model.comment.BinhLuanModel;
import com.vudn.myfood.model.restaurant.QuanAnModel;
import com.vudn.myfood.model.user.ThanhVienModel;
import com.vudn.myfood.view.main.MainActivity;
import com.vudn.myfood.view.restaurant.ChiTietQuanAnActivity;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener{
    public static final int REQUEST_CODE_GPS = 100;
    public static final String TAG = "MapActivity";
    private MapFragment mapFragment;
    private MapManager mapManager;
    private boolean isMapReady;
    private List<QuanAnModel> quanAnModelList;
    private Toolbar toolbar;
    private SearchView searchView;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_map;
    }

    @Override
    protected void initializeComponents() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        quanAnModelList = getIntent().getParcelableArrayListExtra(Key.INTENT_DANH_SACH_QUAN_AN);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.frm_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        loadMarker(quanAnModelList);
                        break;

                    case 1:
                        mapManager.showRestaurant((QuanAnModel) msg.obj);
                    default:
                        break;
                }
            }
        };

        /*Intent intent = getIntent();
        if (intent != null){
            quanAnModelList = (List<QuanAnModel>) intent.getSerializableExtra(HomeFragment.KEY_LIST_QUAN_AN);
        }

        frmMap = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frm_map);
        if (frmMap == null) {
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction();
            frmMap = SupportMapFragment.newInstance();
            fragmentTransaction
                    .replace(R.id.frm_map, frmMap)
                    .commit();
            Toast.makeText(this, "frmMap null", Toast.LENGTH_SHORT).show();
        }
        if (frmMap != null) {
            frmMap.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    isMapReady = true;
                    if (googleMap != null) {
                        mapManager = new MapManager(MapActivity.this, googleMap);
                        mapManager.checkGpsSetting();
                    } else {
                        Toast.makeText(MapActivity.this, "Không có google Map", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }*/
    }

    @Override
    protected void registerListeners() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_GPS) {
            if (resultCode == RESULT_OK) {
                mapManager.getMyLocation();
            } else {
                Toast.makeText(MapActivity.this, "Vui lòng bật GPS!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        isMapReady = true;
        if (googleMap == null) {
            return;
        }
        googleMap.clear();
        googleMap.setOnInfoWindowClickListener(this);
        mapManager = new MapManager(this, googleMap);
        if (quanAnModelList == null){
            return;
        }
        if(quanAnModelList == null ){
            quanAnModelList = new ArrayList<>();
        }

        if (quanAnModelList.isEmpty()){
            getQuanAn("");
            return;
        }
        mapManager.showRestaurantMaker(quanAnModelList);
        //handler.sendEmptyMessageDelayed(0, 3000);
    }

    private void getQuanAn(final String key) {
        quanAnModelList = new ArrayList<>();
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot dataSnapshotQuanAn = dataSnapshot.child("quanans");
                for(DataSnapshot valueQuanAn : dataSnapshotQuanAn.getChildren()){
                    QuanAnModel quanAnModel = valueQuanAn.getValue(QuanAnModel.class);
                    quanAnModel.setMaquanan(valueQuanAn.getKey());
                    if (quanAnModel.getTenquanan().toLowerCase().contains(key) || quanAnModel.getDiachi().getAddress().toLowerCase().contains(key)){
                        //lấy danh sách hình ảnh theo mã quán ăn
                        DataSnapshot snapshotHinhAnh = dataSnapshot.child("hinhanhquanans").child(quanAnModel.getMaquanan());
                        List<String> imagePathList = new ArrayList<>();
                        for (DataSnapshot valueHinhAnh : snapshotHinhAnh.getChildren()) {
                            imagePathList.add(valueHinhAnh.getValue(String.class));
                        }
                        quanAnModel.setHinhanhquanan(imagePathList);
                        Log.d(TAG, "QuanAn query: Số lượng hình ảnh = " + imagePathList.size());

                        //lấy danh sách bình luận của quán
                        //node bình luận
                        DataSnapshot snapshotBinhLuan = dataSnapshot.child("binhluans").child(quanAnModel.getMaquanan());
                        List<BinhLuanModel> binhLuanModelList = new ArrayList<>();
                        int sohinhbl = 0;
                        double tongdiem = 0;
                        for (DataSnapshot valueBinhLuan : snapshotBinhLuan.getChildren()) {
                            BinhLuanModel binhLuanModel = valueBinhLuan.getValue(BinhLuanModel.class);
                            binhLuanModel.setMabinhluan(valueBinhLuan.getKey());
                            ThanhVienModel thanhVienModel = dataSnapshot.child("thanhviens").child(binhLuanModel.getMauser()).getValue(ThanhVienModel.class);
                            binhLuanModel.setThanhVienModel(thanhVienModel);

                            List<String> hinhanhBinhLuanList = new ArrayList<>();
                            DataSnapshot snapshotNodeHinhAnhBL = dataSnapshot.child("hinhanhbinhluans").child(binhLuanModel.getMabinhluan());
                            for (DataSnapshot valueHinhBinhLuan : snapshotNodeHinhAnhBL.getChildren()) {
                                hinhanhBinhLuanList.add(valueHinhBinhLuan.getValue(String.class));
                            }
                            binhLuanModel.setHinhanhBinhLuanList(hinhanhBinhLuanList);
                            sohinhbl += hinhanhBinhLuanList.size();
                            Log.d(TAG, "QuanAn query: Số hình ảnh bình luận = " + hinhanhBinhLuanList.size());
                            binhLuanModelList.add(binhLuanModel);
                            tongdiem += binhLuanModel.getChamdiem();
                        }
                        quanAnModel.setSohinhbinhluan(sohinhbl);
                        if (binhLuanModelList.size() != 0) {
                            quanAnModel.setDiemdanhgia(tongdiem / binhLuanModelList.size());
                        }
                        Log.d(TAG, "QuanAn query: Số lượng bình luận = " + binhLuanModelList.size());
                        quanAnModel.setBinhLuanModelList(binhLuanModelList);
                        Location location = new Location("");
                        location.setLatitude(quanAnModel.getDiachi().getLatitude());
                        location.setLongitude(quanAnModel.getDiachi().getLongitude());
                        float khoangcach = mapManager.getLocation().distanceTo(location) / 1000;
                        quanAnModel.setKhoangcach(khoangcach);
                        quanAnModelList.add(quanAnModel);
                    }
                }
                if (quanAnModelList.isEmpty()){
                    Toast.makeText(MapActivity.this, "Không tìm thấy địa điểm phù hợp", Toast.LENGTH_SHORT).show();
                }else {
                    mapManager.showRestaurantMaker(quanAnModelList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadMarker(List<QuanAnModel> quanAnModelList){
        LoadMarkerTask loadMarkerTask = new LoadMarkerTask();
        loadMarkerTask.execute(quanAnModelList);
    }

    private class LoadMarkerTask extends AsyncTask<List<QuanAnModel>, QuanAnModel, Void>{

        @Override
        protected Void doInBackground(List<QuanAnModel>... lists) {
            for (QuanAnModel quanAnModel : lists[0]){
                onProgressUpdate(quanAnModel);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(QuanAnModel... values) {
            super.onProgressUpdate(values);
            Message msg = new Message();
            msg.what = 1;
            msg.obj = values[0];
            handler.sendMessageDelayed(msg, 200);
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        QuanAnModel quanAnModel = (QuanAnModel) marker.getTag();
        if (quanAnModel != null) {
            //Toast.makeText(this, "Xem chi tiết " + quanAnModel.getTenquanan(), Toast.LENGTH_SHORT).show();
            Intent iChiTiet = new Intent(MapActivity.this, ChiTietQuanAnActivity.class);
            iChiTiet.putExtra(Key.INTENT_CHI_TIET_QUAN_AN, quanAnModel);
            startActivity(iChiTiet);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) menuItem.getActionView();
        setUpSearchView();
        return true;
    }

    private void setUpSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        ComponentName componentName = new ComponentName(getBaseContext(), MainActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
        searchView.setIconifiedByDefault(false);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    searchView.clearFocus();
                    getQuanAn(query.toLowerCase());
                    /*Log.d(TAG, "onQueryTextSubmit: Search key " + query);
                    searchPresenter.search(query);
                    rcvSearch.setVisibility(View.GONE);
                    lnlEmpty.setVisibility(View.GONE);
                    lnlLoading.setVisibility(View.VISIBLE);*/
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onDestroy() {
        mapManager.unregisterLocationUpdate();
        super.onDestroy();
    }
}
