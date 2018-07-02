package com.vudn.myfood.view.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vudn.myfood.R;
import com.vudn.myfood.adapter.order.MonAnAdapter;
import com.vudn.myfood.adapter.order.ThucDonAdapter;
import com.vudn.myfood.base.BaseActivity;
import com.vudn.myfood.base.Key;
import com.vudn.myfood.model.order.ChiTietDonHangModel;
import com.vudn.myfood.model.order.DonHangModel;
import com.vudn.myfood.model.menu.MonAnModel;
import com.vudn.myfood.model.restaurant.QuanAnModel;
import com.vudn.myfood.model.menu.ThucDonModel;
import com.vudn.myfood.presenter.order.DatGiaoHangPresenter;
import com.vudn.myfood.presenter.order.DatGiaoHangPresenterImpl;
import com.vudn.myfood.view.user.UserActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DatGiaoHangActivity extends BaseActivity implements DatGiaoHangView, View.OnClickListener, MonAnAdapter.OnSoLuongMonAnThayDoiListener {
    public static final String TAG = "DatGiaoHangActivity";
    private TextView txtTenQuanAn, txtDiaChi, txtThoiGianHoatDong, txtTrangThaiHoatDong;
    private ImageView imHinhAnhQuanAn;
    private Toolbar toolbar;
    private RecyclerView recyclerThucDon;
    private TextView txtTieuDeToolbar;
    private QuanAnModel quanAnModel;
    private List<ThucDonModel> thucDonModelList;
    private ThucDonAdapter adapterThucDon;
    private DatGiaoHangPresenter datGiaoHangPresenter;
    private LinearLayout btnGiaoHang;
    private TextView txtInfo;
    private List<ChiTietDonHangModel> chiTietDonHangModelList;
    private DonHangModel donHangModel;
    private SharedPreferences sharedPreferencesDangNhap;
    NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_dat_giao_hang;
    }

    @Override
    protected void initializeComponents() {
        quanAnModel = getIntent().getParcelableExtra(Key.INTENT_DAT_GIAO_HANG);
        Log.d(TAG, TAG + "initializeComponents: tên quán ăn " + quanAnModel.getTenquanan());
        datGiaoHangPresenter = new DatGiaoHangPresenterImpl(this);
        sharedPreferencesDangNhap = this.getSharedPreferences("luudangnhap", MODE_PRIVATE);
        txtTieuDeToolbar = findViewById(R.id.txtTieuDeToolbar);
        txtTenQuanAn = (TextView) findViewById(R.id.txtTenQuanAn);
        txtDiaChi = (TextView) findViewById(R.id.txtDiaChiQuanAn);
        txtThoiGianHoatDong = (TextView) findViewById(R.id.txtThoiGianHoatDong);
        txtTrangThaiHoatDong = (TextView) findViewById(R.id.txtTrangThaiHoatDong);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imHinhAnhQuanAn = (ImageView) findViewById(R.id.imHinhQuanAn);
        recyclerThucDon = (RecyclerView) findViewById(R.id.recyclerThucDon);
        btnGiaoHang = findViewById(R.id.btn_giaohang);
        txtInfo = findViewById(R.id.txt_info);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recyclerThucDon.setLayoutManager(new LinearLayoutManager(DatGiaoHangActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerThucDon.setHasFixedSize(true);
        /*thucDonModelList = new ArrayList<>();
        adapterThucDon = new ThucDonAdapter(this,thucDonModelList);
        recyclerThucDon.setAdapter(adapterThucDon);*/
        hienThiThucDonQuanAn();
        /*if (quanAnModel.getThucDons().isEmpty()){
            orderPresenter.getThucDon(quanAnModel.getMaquanan());
        }
        else {
            thucDonModelList = quanAnModel.getThucDons();
            adapterThucDon.notifyDataSetChanged();
        }*/
        checkLogin();
        nestedScrollView = findViewById(R.id.nestScrollViewDatHang);
        nestedScrollView.smoothScrollTo(0, 0);
    }

    private void checkLogin() {
        if (sharedPreferencesDangNhap.getBoolean("islogin", false)) {
            donHangModel.setMathanhvien(sharedPreferencesDangNhap.getString("mauser", ""));
        } else {
            final android.support.v7.app.AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new android.support.v7.app.AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_NoActionBar);
            } else {
                builder = new android.support.v7.app.AlertDialog.Builder(this);
            }
            builder.setTitle("Vui lòng đăng nhập để thực hiện chức năng này")
                    .setCancelable(false)
                    .setPositiveButton("Đồng ý".toUpperCase(), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent iDangNhap = new Intent(DatGiaoHangActivity.this, UserActivity.class);
                            startActivityForResult(iDangNhap, Key.RC_SIGN_IN);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("Hủy".toUpperCase(), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            DatGiaoHangActivity.this.onBackPressed();
                        }
                    })
                    .show();
        }
    }

    void addItem() {
        MonAnModel monAnModel = new MonAnModel("MAMON10", 45000, "https://firebasestorage.googleapis.com/v0/b/myfood-9339d.appspot.com/o/hinhanh%2F635896603750554553.jpg?alt=media&token=f8f14494-9543-4a54-9ec6-f4322a0675d3", "Chân gà muối chiên");
        ChiTietDonHangModel chiTietDonHangModel = new ChiTietDonHangModel(monAnModel, 4);
        MonAnModel monAnModel1 = new MonAnModel("MAMON7", 12000, "https://firebasestorage.googleapis.com/v0/b/myfood-9339d.appspot.com/o/hinhanh%2F635896614771349910.jpg?alt=media&token=fa00ed1e-2714-43ce-adb3-c45915e977f0", "Phô mai que");
        ChiTietDonHangModel chiTietDonHangModel1 = new ChiTietDonHangModel(monAnModel1, 5);
        chiTietDonHangModelList.add(chiTietDonHangModel);
        chiTietDonHangModelList.add(chiTietDonHangModel1);
        donHangModel.setChitietdonhang(chiTietDonHangModelList);
    }

    @Override
    protected void registerListeners() {
        btnGiaoHang.setOnClickListener(this);
    }

    private void hienThiThucDonQuanAn() {
        if (quanAnModel == null) {
            Toast.makeText(this, "Lỗi hiển thị thông tin quán ăn", Toast.LENGTH_SHORT).show();
            Log.d(TAG, TAG + "hienThiThucDonQuanAn: quăn ăn bị null");
            return;
        }

        //chiTietDonHangModelList = new ArrayList<>();
        donHangModel = new DonHangModel();
        donHangModel.setMaquanan(quanAnModel.getMaquanan());
        donHangModel.setTenquanan(quanAnModel.getTenquanan());
        donHangModel.setTenkhachhang("");
        donHangModel.setSodienthoai("");
        donHangModel.setDiachi("");
        donHangModel.setGhichu("");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

        String giohientai = dateFormat.format(calendar.getTime());
        String giomocua = quanAnModel.getGiomocua();
        String giodongcua = quanAnModel.getGiodongcua();

        try {
            Date dateHienTai = dateFormat.parse(giohientai);
            Date dateMoCua = dateFormat.parse(giomocua);
            Date dateDongCua = dateFormat.parse(giodongcua);

            if (dateHienTai.after(dateMoCua) && dateHienTai.before(dateDongCua)) {
                //gio mo cua
                txtTrangThaiHoatDong.setText(getString(R.string.dangmocua));
            } else {
                //dong cua
                txtTrangThaiHoatDong.setText(getString(R.string.dadongcua));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Glide.with(this).load(quanAnModel.getHinhanhquanan().get(0)).into(imHinhAnhQuanAn);
        txtTieuDeToolbar.setText(quanAnModel.getTenquanan());
        txtTenQuanAn.setText(quanAnModel.getTenquanan());
        String mAddress = quanAnModel.getDiachi().getAddress();
        if (mAddress.contains(", Vietnam")) {
            txtDiaChi.setText(mAddress.replace(", Vietnam", ""));
        } else if (mAddress.contains(", Việt Nam")) {
            txtDiaChi.setText(mAddress.replace(", Việt Nam", ""));
        } else {
            txtDiaChi.setText(mAddress);
        }
        txtThoiGianHoatDong.setText(quanAnModel.getGiomocua() + " - " + quanAnModel.getGiodongcua());

        if (quanAnModel.getThucDons() == null || quanAnModel.getThucDons().isEmpty()) {
            Log.d(TAG, TAG + "hienThiThucDonQuanAn: thực đơn null");
            datGiaoHangPresenter.getThucDon(quanAnModel.getMaquanan());
        } else {
            Log.d(TAG, TAG + "hienThiThucDonQuanAn: đã có thực đơn và change");
            setUpThucDonAdapter(quanAnModel.getThucDons());
        }
    }

    void setUpThucDonAdapter(List<ThucDonModel> thucDonModelList) {
        adapterThucDon = new ThucDonAdapter(this, thucDonModelList, this);
        recyclerThucDon.setAdapter(adapterThucDon);
        adapterThucDon.notifyDataSetChanged();
        Log.d(TAG, TAG + " onSuccess: " + adapterThucDon.getItemCount());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_giaohang:
                showGioHang();
                break;

            default:
                break;
        }
    }

    void showGioHang() {
        if (donHangModel == null || donHangModel.getChitietdonhang() == null || donHangModel.getChitietdonhang().isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn số lượng món ăn muốn đặt hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent iGioHang = new Intent(DatGiaoHangActivity.this, GioHangActivity.class);
        iGioHang.putExtra(Key.INTENT_DON_HANG, donHangModel);
        startActivityForResult(iGioHang, Key.RC_ORDER);
    }

    /*@Override
    public void onClick(CartItem cartItem) {
        int tongtien = 0;
        for (int i = 0; i< cartItemList.size(); i++){
            if (cartItemList.get(i).getMonan().getMamon().equalsIgnoreCase(cartItem.getMonan().getMamon())){
                cartItemList.set(i, cartItem);
            }
            tongtien += cartItemList.get(i).getMonan().getGiatien();
        }
        txtInfo.setText(cartItemList.size() + " phần - " + tongtien + " VNĐ");
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGetThucDonSuccess(List<ThucDonModel> thucDonModelList) {
        Log.d(TAG, TAG + " onSuccess: kích thước list thực đơn " + thucDonModelList.size());
        setUpThucDonAdapter(thucDonModelList);
    }

    @Override
    public void onGetThucDOnFailure(String error) {
        Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Key.RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                updateUI();
            }else {
                checkLogin();
            }
        }

        if (requestCode == Key.RC_ORDER){
            if (resultCode == RESULT_OK){
                finish();
            }
        }
    }

    private void updateUI() {
        donHangModel.setMathanhvien(sharedPreferencesDangNhap.getString("mauser", ""));
    }

    @Override
    public void onSoLuongThayDoi(List<ChiTietDonHangModel> chiTietDonHangModelList) {
        donHangModel.setChitietdonhang(chiTietDonHangModelList);
        int soluong = chiTietDonHangModelList.size();
        long thanhtoan = donHangModel.tinhTienThanhToan();
        txtInfo.setText(soluong + " phần - " + String.format("%,d", thanhtoan).replace(",", ".") + "đ");
    }
}
