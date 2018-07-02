package com.vudn.myfood.view.restaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.vudn.myfood.R;
import com.vudn.myfood.adapter.restaurant.AdapterHienThiHinhBinhLuanDuocChon;
import com.vudn.myfood.base.BaseActivity;
import com.vudn.myfood.model.restaurant.BinhLuanModel;
import com.vudn.myfood.presenter.other.BinhLuanController;
import com.vudn.myfood.view.other.DanhGiaDialog;

import java.util.ArrayList;
import java.util.List;

public class BinhLuanActivity extends BaseActivity implements View.OnClickListener, DanhGiaDialog.OnDanhGiaListener {

    TextView txtTenQuanAn, txtDiaChiQuanAn, txtDangBinhLuan;
    Toolbar toolbar;
    EditText edTieuDeBinhLuan, edNoiDungBinhLuan;
    ImageButton btnChonHinh;
    RecyclerView recyclerViewChonHinhBinhLuan;
    AdapterHienThiHinhBinhLuanDuocChon adapterHienThiHinhBinhLuanDuocChon;
    ImageButton btnRate;

    final int REQUEST_CHONHINHBINHLUAN = 11;
    String maquanan;
    int diemDanhGia;
    SharedPreferences sharedPreferences;

    BinhLuanController binhLuanController;
    List<String> listHinhDuocChon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.layout_binhluan;
    }

    @Override
    protected void initializeComponents() {
        maquanan = getIntent().getStringExtra("maquanan");
        String tenquan = getIntent().getStringExtra("tenquan");
        String diachi = getIntent().getStringExtra("diachi");

        sharedPreferences = getSharedPreferences("luudangnhap", MODE_PRIVATE);

        txtDiaChiQuanAn = (TextView) findViewById(R.id.txtDiaChiQuanAn);
        txtTenQuanAn = (TextView) findViewById(R.id.txtTenQuanAn);
        txtDangBinhLuan = (TextView) findViewById(R.id.txtDangBinhLuan);
        edTieuDeBinhLuan = (EditText) findViewById(R.id.edTieuDeBinhLuan);
        edNoiDungBinhLuan = (EditText) findViewById(R.id.edNoiDungBinhLuan);
        btnRate = findViewById(R.id.btn_rate);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnChonHinh = (ImageButton) findViewById(R.id.btnChonHinh);
        recyclerViewChonHinhBinhLuan = (RecyclerView) findViewById(R.id.recyclerChonHinhBinhLuan);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewChonHinhBinhLuan.setLayoutManager(layoutManager);

        binhLuanController = new BinhLuanController();
        listHinhDuocChon = new ArrayList<>();

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtDiaChiQuanAn.setText(diachi);
        txtTenQuanAn.setText(tenquan);

        btnChonHinh.setOnClickListener(this);
        txtDangBinhLuan.setOnClickListener(this);
        btnRate.setOnClickListener(this);

    }

    @Override
    protected void registerListeners() {

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnChonHinh:
                Intent iChonHinhBinhLuan = new Intent(this, ChonHinhBinhLuanActivity.class);
                startActivityForResult(iChonHinhBinhLuan, REQUEST_CHONHINHBINHLUAN);

                break;

            case R.id.txtDangBinhLuan:
                BinhLuanModel binhLuanModel = new BinhLuanModel();
                String tieude = edTieuDeBinhLuan.getText().toString();
                String noidung = edNoiDungBinhLuan.getText().toString();
                if (noidung.isEmpty() || noidung.length() < 15) {
                    Toast.makeText(this, "Vui lòng nhập nội dung", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (diemDanhGia == 0){
                    danhGia();
                    return;
                }
                String mauser = sharedPreferences.getString("mauser", "");
                binhLuanModel.setTieude(tieude);
                binhLuanModel.setNoidung(noidung);
                binhLuanModel.setChamdiem(diemDanhGia);
                binhLuanModel.setLuotthich(0);
                binhLuanModel.setMauser(mauser);

                binhLuanController.ThemBinhLuan(maquanan, binhLuanModel, listHinhDuocChon);
                break;

            case R.id.btn_rate:
                danhGia();
                break;

            default:

                break;
        }
    }

    void danhGia(){
        DanhGiaDialog danhGiaDialog = new DanhGiaDialog(BinhLuanActivity.this, this);
        danhGiaDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHONHINHBINHLUAN) {
            if (resultCode == RESULT_OK) {

                listHinhDuocChon = data.getStringArrayListExtra("listHinhDuocChon");
                adapterHienThiHinhBinhLuanDuocChon = new AdapterHienThiHinhBinhLuanDuocChon(this, R.layout.custom_layout_hienthibinhluanduocchon, listHinhDuocChon);
                recyclerViewChonHinhBinhLuan.setAdapter(adapterHienThiHinhBinhLuanDuocChon);
                adapterHienThiHinhBinhLuanDuocChon.notifyDataSetChanged();

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onDanhGia(int diem) {
        diemDanhGia = diem;
    }
}
