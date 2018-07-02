package com.vudn.myfood.view.order;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.vudn.myfood.R;
import com.vudn.myfood.adapter.order.GioHangAdapter;
import com.vudn.myfood.base.BaseActivity;
import com.vudn.myfood.base.Key;
import com.vudn.myfood.model.order.ChiTietDonHangModel;
import com.vudn.myfood.model.order.DonHangModel;
import com.vudn.myfood.presenter.order.GioHangPresenter;
import com.vudn.myfood.presenter.order.GioHangPresenterImpl;
import com.vudn.myfood.view.other.ThanhToanDialog;

import java.util.List;

public class GioHangActivity extends BaseActivity implements View.OnClickListener, GioHangView, ThanhToanDialog.OnCreateDonHangListener, GioHangAdapter.OnCapNhatGioHangListener {
    private Button btnDatHang;
    //private List<ChiTietDonHangModel> chiTietDonHangModelList;
    private RecyclerView rcvGioHang;
    private TextView txtTongTien;
    private GioHangAdapter gioHangAdapter;
    private Toolbar toolbar;
    private DonHangModel donHangModel;
    private GioHangPresenter gioHangPresenter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_giohang;
    }

    @Override
    protected void initializeComponents() {
        toolbar = findViewById(R.id.toolbar_giohang);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Giỏ hàng");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnDatHang = findViewById(R.id.btn_thanhtoan);
        rcvGioHang = findViewById(R.id.rcv_giohang);
        rcvGioHang.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcvGioHang.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL);
        rcvGioHang.addItemDecoration(dividerItemDecoration);
        txtTongTien = findViewById(R.id.txt_tongtienthanhtoan);
        donHangModel = getIntent().getParcelableExtra(Key.INTENT_DON_HANG);
        setUpGioHangAdapter(donHangModel.getChitietdonhang());
        /*gioHangAdapter = new GioHangAdapter(donHangModel.getChitietdonhang(), this, this);
        rcvGioHang.setAdapter(gioHangAdapter);
        gioHangAdapter.notifyDataSetChanged();*/
        gioHangPresenter = new GioHangPresenterImpl(this);
        progressDialog = new ProgressDialog(this);
    }

    @Override
    protected void registerListeners() {
        btnDatHang.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_thanhtoan:
                showThanhToanDialog();
                break;

            default:
                break;
        }
    }

    private void showThanhToanDialog() {
        ThanhToanDialog thanhToanDialog = new ThanhToanDialog(GioHangActivity.this, donHangModel, this);
        thanhToanDialog.setCancelable(true);
        thanhToanDialog.show();
    }

    public DonHangModel getDonHangModel() {
        return donHangModel;
    }

    public void setDonHangModel(DonHangModel donHangModel) {
        this.donHangModel = donHangModel;

    }

    @Override
    public void onDatHangSuccess() {
        progressDialog.dismiss();
        Toast.makeText(this, "Đặt giao hàng thành công!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onDatHangFailure(String error) {
        progressDialog.dismiss();
        Toast.makeText(this, "Đặt hàng không thành công..." + error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateDonHang(DonHangModel donHangModel) {
        progressDialog.setMessage(getString(R.string.dangxuly));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
        this.donHangModel = donHangModel;
        gioHangPresenter.themDonHang(donHangModel);
    }

    @Override
    public void onCapNhat() {
        if (gioHangAdapter.getItemCount() == 0){
            finish();
        }else {
            donHangModel.setChitietdonhang(gioHangAdapter.getChiTietDonHangList());
            long thanhtoan = donHangModel.tinhTienThanhToan();
            txtTongTien.setText(String.format("%,d", thanhtoan).replace(",", ".") + " VNĐ");
        }
    }

    private void setUpGioHangAdapter(List<ChiTietDonHangModel> chiTietDonHangModelList) {
        gioHangAdapter = new GioHangAdapter(chiTietDonHangModelList, this, this);
        rcvGioHang.setAdapter(gioHangAdapter);
        gioHangAdapter.notifyDataSetChanged();
        long thanhtoan = donHangModel.tinhTienThanhToan();
        txtTongTien.setText(String.format("%,d", thanhtoan).replace(",", ".") + " VNĐ");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
