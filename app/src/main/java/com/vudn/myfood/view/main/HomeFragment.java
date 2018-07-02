package com.vudn.myfood.view.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.vudn.myfood.R;
import com.vudn.myfood.adapter.main.DsQuanAnAdapter;
import com.vudn.myfood.base.Key;
import com.vudn.myfood.model.restaurant.QuanAnModel;
import com.vudn.myfood.presenter.main.HomePresenter;
import com.vudn.myfood.presenter.main.HomePresenterImpl;
import com.vudn.myfood.view.order.DatGiaoHangActivity;
import com.vudn.myfood.view.other.BottomSheetFilterFragment;
import com.vudn.myfood.view.other.BottomSheetSortFragment;
import com.vudn.myfood.view.map.MapActivity;
import com.vudn.myfood.view.restaurant.ChiTietQuanAnActivity;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

public class HomeFragment extends Fragment implements
        HomeView,
        View.OnClickListener,
        DsQuanAnAdapter.OnQuanAnModelClickListener,
        BottomSheetSortFragment.OnSapXepClickListener,
        BottomSheetFilterFragment.OnFilterListener{
    public static final String TAG = "HomeFragment";
    public static final String KEY_LIST_QUAN_AN = "list_quan_an";
    private RecyclerView rcvRestaurant;
    private LinearLayout lnlLoading;
    private Button btnShowMap;
    private Button btnSort;
    private Button btnFilter;
    private boolean isLoading = true;

    private SharedPreferences sharedPreferences;

    private HomePresenter restaurantPresenter;
    private DsQuanAnAdapter dsQuanAnAdapter;

    private List<QuanAnModel> quanAnModelList;
    private Location vitrihientai;
    int i = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        quanAnModelList = new ArrayList<>();
        Log.d(TAG, "onCreate: i ++ " + String.valueOf(i++));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Log.d(TAG, "onCreateView: i ++" + String.valueOf(i++));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeComponent(view);
        registerListener();
        Log.d(TAG, "onViewCreated: i++ " + String.valueOf(i++));
    }

    private void initializeComponent(View view) {
        btnShowMap = view.findViewById(R.id.btn_show_map);
        btnSort = view.findViewById(R.id.btn_sort);
        btnFilter = view.findViewById(R.id.btn_filter);

        rcvRestaurant = view.findViewById(R.id.rcv_restaurant);
        rcvRestaurant.setLayoutManager(new LinearLayoutManager(getContext(), VERTICAL, false));
        rcvRestaurant.setHasFixedSize(true);
        dsQuanAnAdapter = new DsQuanAnAdapter(getContext(), quanAnModelList, R.layout.custom_layout_recyclerview_odau, this);
        rcvRestaurant.setAdapter(dsQuanAnAdapter);
        lnlLoading = view.findViewById(R.id.lnl_loading);

        sharedPreferences = getContext().getSharedPreferences("toado", Context.MODE_PRIVATE);
        vitrihientai = new Location("");
        vitrihientai.setLatitude(Double.parseDouble(sharedPreferences.getString("latitude", "0")));
        vitrihientai.setLongitude(Double.parseDouble(sharedPreferences.getString("longitude", "0")));
        restaurantPresenter = new HomePresenterImpl(this);
        restaurantPresenter.getRestaurant(vitrihientai, 0, 10);
    }

    private void registerListener() {
        btnShowMap.setOnClickListener(this);
        btnSort.setOnClickListener(this);
        btnFilter.setOnClickListener(this);
        rcvRestaurant.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == recyclerView.getAdapter().getItemCount() - 3) {
                    loadMore();
                }
            }
        });
    }

    int countItem = 0;

    @Override
    public void onGetRestaurantSuccess(QuanAnModel quanAnModel) {
        countItem++;
        if (countItem == 10) {
            isLoading = false;
            countItem = 0;
            lnlLoading.setVisibility(View.GONE);
        }
        //Log.d(TAG, "onGetRestaurantSuccess: " + isLoading);
        quanAnModelList.add(quanAnModel);
        dsQuanAnAdapter.notifyDataSetChanged();
        //Toast.makeText(getContext(), "" + quanAnModelList.size(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetRestaurantFailure(String error) {
        Toast.makeText(getContext(), "" + error, Toast.LENGTH_SHORT).show();
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
        Intent iShowMap = new Intent(getContext(), MapActivity.class);
        iShowMap.putParcelableArrayListExtra(Key.INTENT_DANH_SACH_QUAN_AN, (ArrayList<? extends Parcelable>) quanAnModelList);
        startActivity(iShowMap);
    }

    private void showSortFragment() {
        BottomSheetSortFragment sortFragment = new BottomSheetSortFragment();
        sortFragment.setOnSapXepClickListener(this);
        sortFragment.show(getFragmentManager(), sortFragment.getTag());
    }

    private void showFilterFragment() {
        BottomSheetFilterFragment filterFragment = new BottomSheetFilterFragment();
        filterFragment.setOnFilterListener(this);
        filterFragment.show(getFragmentManager(), filterFragment.getTag());
    }

    private void loadMore() {
        if (isLoading == false) {
            Log.d(TAG, "onLoadMoreListener: " + countItem);
            restaurantPresenter.getRestaurant(vitrihientai, dsQuanAnAdapter.getItemCount(), dsQuanAnAdapter.getItemCount() + 10);
            isLoading = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onQuanAnModelClick(int position) {
        QuanAnModel quanAnModel = dsQuanAnAdapter.getQuanAnModel(position);
        Intent iChiTietQuanAn = new Intent(getContext(), ChiTietQuanAnActivity.class);
        iChiTietQuanAn.putExtra(Key.INTENT_CHI_TIET_QUAN_AN, quanAnModel);
        startActivity(iChiTietQuanAn);
    }

    @Override
    public void onDatGiaoHangClick(int position) {
        Log.d(TAG, TAG + "onDatGiaoHangClick: " + position);
        QuanAnModel quanAnModel = dsQuanAnAdapter.getQuanAnModel(position);
        Intent iDatGiaoHang = new Intent(getContext(), DatGiaoHangActivity.class);
        iDatGiaoHang.putExtra(Key.INTENT_DAT_GIAO_HANG, quanAnModel);
        Log.d(TAG, TAG + "onDatGiaoHangClick: tên quán " + quanAnModel.getTenquanan());
        startActivity(iDatGiaoHang);
    }

    @Override
    public void onSapXep(int tieuchi) {
        if (dsQuanAnAdapter == null) {
            return;
        }
        switch (tieuchi) {
            case 0:
                dsQuanAnAdapter.sortByDistant();
                break;

            case 1:
                dsQuanAnAdapter.sortByPoint();
                break;

            case 2:
                dsQuanAnAdapter.sortbyName();
                break;

            default:
                break;
        }
    }

    @Override
    public void onFilter(String theloai, String khuvuc) {
        dsQuanAnAdapter.filter(theloai, khuvuc);
    }
}
