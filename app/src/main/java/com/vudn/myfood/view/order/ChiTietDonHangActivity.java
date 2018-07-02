package com.vudn.myfood.view.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vudn.myfood.R;
import com.vudn.myfood.adapter.order.ChiTietDonHangAdapter;
import com.vudn.myfood.base.BaseActivity;
import com.vudn.myfood.base.Key;
import com.vudn.myfood.model.order.ChiTietDonHangModel;
import com.vudn.myfood.model.order.DonHangModel;

import java.util.ArrayList;
import java.util.List;

public class ChiTietDonHangActivity extends BaseActivity implements View.OnClickListener {
    TextView txtTongTienThanhToan, txtMaDH, txtTenKH, txtDiaChi, txtThoiGian, txtHinhThuc, txtTrangThai;
    RecyclerView rcvChiTietDonHang;
    ChiTietDonHangAdapter chiTietDonHangAdapter;
    DonHangModel donHangModel;
    List<ChiTietDonHangModel> chiTietDonHangModelList;
    NestedScrollView nestedScrollView;
    Button btnCapNhatDonHang;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_don_hang;
    }

    @Override
    protected void initializeComponents() {
        txtTongTienThanhToan = findViewById(R.id.txt_tongtienthanhtoan);
        txtMaDH = findViewById(R.id.txt_ma_dh);
        txtTenKH = findViewById(R.id.txt_ten_kh);
        txtDiaChi = findViewById(R.id.txt_dia_chi);
        txtThoiGian = findViewById(R.id.txt_thoi_gian);
        txtHinhThuc = findViewById(R.id.txt_hinh_thuc);
        txtTrangThai = findViewById(R.id.txt_trang_thai);
        nestedScrollView = findViewById(R.id.nsv_chi_tiet_don_hang);
        btnCapNhatDonHang = findViewById(R.id.btn_cap_nhat_don_hang);

        donHangModel = getIntent().getParcelableExtra(Key.INTENT_CHI_TIET_DON_HANG);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        chiTietDonHangModelList = new ArrayList<>();
        chiTietDonHangModelList = donHangModel.getChitietdonhang();
        rcvChiTietDonHang = findViewById(R.id.rcv_chi_tiet_don_hang);
        rcvChiTietDonHang.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcvChiTietDonHang.setHasFixedSize(true);
        chiTietDonHangAdapter = new ChiTietDonHangAdapter(this, chiTietDonHangModelList);
        rcvChiTietDonHang.setAdapter(chiTietDonHangAdapter);
        chiTietDonHangAdapter.notifyDataSetChanged();

        hienThiDuLieu();
        nestedScrollView.smoothScrollTo(0, 0);
    }

    private void hienThiDuLieu() {
        if (donHangModel == null) {
            return;
        }
        getSupportActionBar().setTitle(donHangModel.getTenquanan());
        txtMaDH.setText(donHangModel.getMadonhang());
        txtTenKH.setText(donHangModel.getTenkhachhang());
        txtDiaChi.setText(donHangModel.getDiachi());
        txtThoiGian.setText(donHangModel.getNgaydathang());
        txtHinhThuc.setText(donHangModel.getHinhthuc());
        txtTrangThai.setText(donHangModel.getTrangthai());
        long tongThanhToan = donHangModel.tinhTienThanhToan();
        txtTongTienThanhToan.setText(String.format("%,d", tongThanhToan).replace(",", ".") + " VNĐ");
    }

    @Override
    protected void registerListeners() {
        btnCapNhatDonHang.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cap_nhat_don_hang:
                capNhatDonHang();
                break;

            default:
                break;
        }
    }

    void capNhatDonHang() {
        Intent iGioHang = new Intent(ChiTietDonHangActivity.this, GioHangActivity.class);
        iGioHang.putExtra(Key.INTENT_DON_HANG, donHangModel);
        startActivity(iGioHang);
    }
}
