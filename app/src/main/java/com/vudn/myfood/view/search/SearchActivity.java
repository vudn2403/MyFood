package com.vudn.myfood.view.search;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.vudn.myfood.R;
import com.vudn.myfood.adapter.main.DsQuanAnAdapter;
import com.vudn.myfood.adapter.search.SearchAdapter;
import com.vudn.myfood.base.BaseActivity;
import com.vudn.myfood.base.Key;
import com.vudn.myfood.model.restaurant.QuanAnModel;
import com.vudn.myfood.presenter.search.SearchPresenter;
import com.vudn.myfood.presenter.search.SearchPresenterImpl;
import com.vudn.myfood.view.order.DatGiaoHangActivity;
import com.vudn.myfood.view.other.BottomSheetFilterFragment;
import com.vudn.myfood.view.other.BottomSheetSortFragment;
import com.vudn.myfood.view.main.MainActivity;
import com.vudn.myfood.view.map.MapActivity;
import com.vudn.myfood.view.restaurant.ChiTietQuanAnActivity;

import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends BaseActivity
        implements BottomSheetFilterFragment.OnFilterListener,
        View.OnClickListener,
        MySearchView,
        SearchAdapter.OnQuanAnTimKiemClickListener,
        BottomSheetSortFragment.OnSapXepClickListener {
    public static final String TAG = "SearchActivity";
    private RecyclerView rcvSearch;
    private LinearLayout lnlEmpty;
    private LinearLayout lnlLoading;
    private Button btnShowMap;
    private Button btnSort;
    private Button btnFilter;
    private Toolbar toolbar;
    private SearchView searchView;
    private SearchAdapter adapterRecyclerOdau;
    private List<QuanAnModel> quanAnModelList;
    private SearchPresenter searchPresenter;
    private Location myLocation;
    private SharedPreferences sharedPreferencesToaDo;
    QuanAnModel quanAnModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        quanAnModelList = new ArrayList<>();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_search;
    }

    @Override
    protected void initializeComponents() {
        rcvSearch = findViewById(R.id.rcv_search);
        lnlEmpty = findViewById(R.id.lnl_empty);
        lnlEmpty.setVisibility(View.GONE);
        lnlLoading = findViewById(R.id.lnl_loading);
        btnShowMap = findViewById(R.id.btn_show_map);
        btnSort = findViewById(R.id.btn_sort);
        btnFilter = findViewById(R.id.btn_filter);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        searchPresenter = new SearchPresenterImpl(this);
        rcvSearch.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcvSearch.setHasFixedSize(true);
        sharedPreferencesToaDo = this.getSharedPreferences("toado", MODE_PRIVATE);
        myLocation = new Location("");
        myLocation.setLatitude(Double.parseDouble(sharedPreferencesToaDo.getString("latitude", "0")));
        myLocation.setLongitude(Double.parseDouble(sharedPreferencesToaDo.getString("longitude", "0")));
        quanAnModel = new QuanAnModel();
        quanAnModel.setTenquanan("");
        quanAnModel.setKhuvuc("");
        quanAnModel.setTheloai("");
        quanAnModel.setGiaohang(false);
    }

    @Override
    protected void registerListeners() {
        btnShowMap.setOnClickListener(this);
        btnFilter.setOnClickListener(this);
        btnSort.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setClickable(true);
        searchView.setFocusable(true);
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
                    Log.d(TAG, "onQueryTextSubmit: Search key " + query);
                    quanAnModel.setTenquanan(query);
                    searchPresenter.search(quanAnModel, myLocation);
                    rcvSearch.setVisibility(View.GONE);
                    lnlEmpty.setVisibility(View.GONE);
                    lnlLoading.setVisibility(View.VISIBLE);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_show_map:
                showMapActivity();
                break;

            case R.id.btn_sort:
                showSortFragment();
                break;

            case R.id.btn_filter:
                showFilterFragment();
                break;

            default:
                break;
        }
    }

    private void showMapActivity() {
        Intent iShowMap = new Intent(this, MapActivity.class);
        iShowMap.putParcelableArrayListExtra(Key.INTENT_DANH_SACH_QUAN_AN, (ArrayList<? extends Parcelable>) quanAnModelList);
        startActivity(iShowMap);
    }

    private void showSortFragment() {
        BottomSheetSortFragment sortFragment = new BottomSheetSortFragment();
        sortFragment.setOnSapXepClickListener(this);
        sortFragment.show(getSupportFragmentManager(), sortFragment.getTag());
    }

    private void showFilterFragment() {
        BottomSheetFilterFragment filterFragment = new BottomSheetFilterFragment();
        filterFragment.setOnFilterListener(this);
        filterFragment.show(getSupportFragmentManager(), filterFragment.getTag());
    }

    @Override
    public void onBackPressed() {
        if (searchView.isFocusable()) {
            searchView.setFocusable(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSearchSuccess(List<QuanAnModel> quanAnModelList) {
        Toast.makeText(this, "" + quanAnModelList.size(), Toast.LENGTH_SHORT).show();
        this.quanAnModelList = quanAnModelList;
        lnlLoading.setVisibility(View.GONE);
        rcvSearch.setVisibility(View.VISIBLE);
        adapterRecyclerOdau = new SearchAdapter(this, this.quanAnModelList, R.layout.custom_layout_recyclerview_odau, this);
        rcvSearch.setAdapter(adapterRecyclerOdau);
        adapterRecyclerOdau.notifyDataSetChanged();
        lnlEmpty.setVisibility(View.GONE);
    }

    @Override
    public void onSearchFailure(String error) {
        lnlLoading.setVisibility(View.GONE);
        rcvSearch.setVisibility(View.GONE);
        lnlEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void onQuanAnModelClick(int position) {
        QuanAnModel quanAnModel = adapterRecyclerOdau.getQuanAnModel(position);
        Log.d(TAG, "onQuanAnModelClick: " + position);
        Intent iChiTietQuanAn = new Intent(this, ChiTietQuanAnActivity.class);
        iChiTietQuanAn.putExtra(Key.INTENT_CHI_TIET_QUAN_AN, quanAnModel);
        startActivity(iChiTietQuanAn);
    }

    @Override
    public void onDatGiaoHangClick(int position) {
        QuanAnModel quanAnModel = adapterRecyclerOdau.getQuanAnModel(position);
        Log.d(TAG, "onDatGiaoHangClick: " + position);
        Intent iDatGiaoHang = new Intent(this, DatGiaoHangActivity.class);
        iDatGiaoHang.putExtra(Key.INTENT_DAT_GIAO_HANG, quanAnModel);
        startActivity(iDatGiaoHang);
    }

    @Override
    public void onSapXep(int tieuchi) {
        if(adapterRecyclerOdau == null){
            return;
        }
        switch (tieuchi) {
            case 0:
                adapterRecyclerOdau.sortByDistant();
                break;

            case 1:
                adapterRecyclerOdau.sortByPoint();
                break;

            case 2:
                adapterRecyclerOdau.sortbyName();
                break;

            default:
                break;
        }
    }

    @Override
    public void onFilter(String theloai, String khuvuc) {
        quanAnModel.setTheloai(theloai);
        quanAnModel.setKhuvuc(khuvuc);
        searchPresenter.search(quanAnModel, myLocation);
        rcvSearch.setVisibility(View.GONE);
        lnlEmpty.setVisibility(View.GONE);
        lnlLoading.setVisibility(View.VISIBLE);
    }
}
