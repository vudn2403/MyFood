package com.vudn.myfood.view.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vudn.myfood.R;
import com.vudn.myfood.adapter.order.DanhSachDonHangAdapter;
import com.vudn.myfood.base.Key;
import com.vudn.myfood.model.order.ChiTietDonHangModel;
import com.vudn.myfood.model.order.DonHangModel;
import com.vudn.myfood.model.menu.MonAnModel;
import com.vudn.myfood.view.order.ChiTietDonHangActivity;

import java.util.ArrayList;
import java.util.List;

public class OrderListFragment extends Fragment implements View.OnClickListener, DanhSachDonHangAdapter.OnDonHangClickListener {
    public static final String TAG = "OrderListFragment";
    private LinearLayout btnSignIn;
    private LinearLayout lnlLoading;
    private RecyclerView rcvOrder;
    private DanhSachDonHangAdapter dsDonHangAdapter;
    private List<DonHangModel> donHangModelList;
    List<ChiTietDonHangModel> chiTietDonHangModelList;
    private SharedPreferences sharedPreferencesDangNhap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        donHangModelList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSignIn = view.findViewById(R.id.btn_sign_in);
        lnlLoading = view.findViewById(R.id.lnl_loading);
        rcvOrder = view.findViewById(R.id.rcv_order);
        rcvOrder.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rcvOrder.setHasFixedSize(true);
        dsDonHangAdapter = new DanhSachDonHangAdapter(getContext(), donHangModelList, this);
        rcvOrder.setAdapter(dsDonHangAdapter);
        rcvOrder.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        btnSignIn.setOnClickListener(this);
        sharedPreferencesDangNhap = getContext().getSharedPreferences("luudangnhap", Context.MODE_PRIVATE);
        Log.d(TAG, "onViewCreated: ");
        //hienThiDuLieu();
    }

    private void hienThiDuLieu() {
        chiTietDonHangModelList = new ArrayList<>();
        MonAnModel monAnModel = new MonAnModel("MAMON10", 45000, "https://firebasestorage.googleapis.com/v0/b/myfood-9339d.appspot.com/o/hinhanh%2F635896603750554553.jpg?alt=media&token=f8f14494-9543-4a54-9ec6-f4322a0675d3", "Chân gà muối chiên");
        ChiTietDonHangModel chiTietDonHangModel = new ChiTietDonHangModel(monAnModel, 4);
        MonAnModel monAnModel1 = new MonAnModel("MAMON7", 12000, "https://firebasestorage.googleapis.com/v0/b/myfood-9339d.appspot.com/o/hinhanh%2F635896614771349910.jpg?alt=media&token=fa00ed1e-2714-43ce-adb3-c45915e977f0", "Phô mai que");
        ChiTietDonHangModel chiTietDonHangModel1 = new ChiTietDonHangModel(monAnModel1, 5);
        chiTietDonHangModelList.add(chiTietDonHangModel);
        chiTietDonHangModelList.add(chiTietDonHangModel1);
        DonHangModel donHangModel = new DonHangModel();
        donHangModel.setChitietdonhang(chiTietDonHangModelList);
        donHangModel.setMadonhang("MDH123");
        donHangModel.setTenquanan("Olympia - Lẩu & Các Món Nhậu");
        donHangModel.setNgaydathang("7:22 ngày 10/6/2018");
        donHangModel.setTrangthai("Đợi xử lý");
        donHangModelList.add(donHangModel);
        dsDonHangAdapter.notifyDataSetChanged();
        MonAnModel monAnModel2 = new MonAnModel("MAMON7", 16000, "https://firebasestorage.googleapis.com/v0/b/myfood-9339d.appspot.com/o/hinhanh%2F635896614771349910.jpg?alt=media&token=fa00ed1e-2714-43ce-adb3-c45915e977f0", "Phô mai que");
        ChiTietDonHangModel chiTietDonHangModel2 = new ChiTietDonHangModel(monAnModel2, 5);
        List<ChiTietDonHangModel> chiTietDonHangModelList1 = new ArrayList<>();
        chiTietDonHangModelList1.add(chiTietDonHangModel);
        chiTietDonHangModelList1.add(chiTietDonHangModel1);
        chiTietDonHangModelList1.add(chiTietDonHangModel2);
        DonHangModel donHangModel2 = new DonHangModel();
        donHangModel2.setChitietdonhang(chiTietDonHangModelList1);
        donHangModel2.setMadonhang("MDH221");
        donHangModel2.setTenquanan("RENG NƯỚNG");
        donHangModel2.setNgaydathang("18:36 ngày 9/6/2018");
        donHangModel2.setTrangthai("Đã hoàn thành");
        donHangModelList.add(donHangModel2);
        dsDonHangAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        Log.d(TAG, TAG + "onStart: ");
        super.onStart();
        updateUI();
    }

    @Override
    public void onResume() {
        Log.d(TAG, TAG + "onResume: ");
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                signIn();
                break;

            default:
                break;
        }
    }

    private void signIn() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).signIn();
        }
    }

    public void updateUI() {
        if (sharedPreferencesDangNhap == null) {
            return;
        }
        if (sharedPreferencesDangNhap.getBoolean("islogin", false)) {
            btnSignIn.setVisibility(View.GONE);
            lnlLoading.setVisibility(View.GONE);
            rcvOrder.setVisibility(View.VISIBLE);
            getDonHang(sharedPreferencesDangNhap.getString("mauser", ""));
        } else {
            lnlLoading.setVisibility(View.GONE);
            btnSignIn.setVisibility(View.VISIBLE);
            rcvOrder.setVisibility(View.GONE);
        }
    }

    void getDonHang(final String mauser) {
        donHangModelList = new ArrayList<>();
        Query query = FirebaseDatabase.getInstance().getReference().child("donhang").orderByChild("mathanhvien").equalTo(mauser);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot valueDonHang : dataSnapshot.getChildren()) {
                    DonHangModel donHangModel = valueDonHang.getValue(DonHangModel.class);
                    donHangModel.setMadonhang(valueDonHang.getKey());
                    donHangModelList.add(donHangModel);
                }
                setUpAdapter();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /*DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot valueDonHang : dataSnapshot.child("donhang").getChildren()) {
                    DonHangModel donHangModel = valueDonHang.getValue(DonHangModel.class);
                    donHangModel.setMadonhang(valueDonHang.getKey());
                    donHangModelList.add(donHangModel);
                }
                if (donHangModelList.size() == 0){
                    lnlLoading.setVisibility(View.VISIBLE);
                }else {
                    lnlLoading.setVisibility(View.GONE);
                }
                dsDonHangAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

    private void setUpAdapter() {
        if (donHangModelList == null || donHangModelList.isEmpty()){
            lnlLoading.setVisibility(View.VISIBLE);
        }else {
            lnlLoading.setVisibility(View.GONE);
            dsDonHangAdapter = new DanhSachDonHangAdapter(getContext(), donHangModelList, this);
            rcvOrder.setAdapter(dsDonHangAdapter);
            dsDonHangAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onClick(int position) {
        DonHangModel donHangModel = dsDonHangAdapter.getDonHang(position);
        Intent iDonHang = new Intent(getContext(), ChiTietDonHangActivity.class);
        iDonHang.putExtra(Key.INTENT_CHI_TIET_DON_HANG, donHangModel);
        startActivity(iDonHang);
    }
}
