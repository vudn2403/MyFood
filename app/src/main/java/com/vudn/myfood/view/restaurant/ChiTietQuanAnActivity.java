package com.vudn.myfood.view.restaurant;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vudn.myfood.R;
import com.vudn.myfood.adapter.comment.BinhLuanApdater;
import com.vudn.myfood.base.BaseActivity;
import com.vudn.myfood.base.Key;
import com.vudn.myfood.model.comment.BinhLuanModel;
import com.vudn.myfood.model.restaurant.QuanAnModel;
import com.vudn.myfood.model.menu.ThucDonModel;
import com.vudn.myfood.model.util.TienIchModel;
import com.vudn.myfood.presenter.restaurant.ChiTietQuanAnPresenter;
import com.vudn.myfood.presenter.restaurant.ChiTietQuanAnPresenterImpl;
import com.vudn.myfood.view.comment.BinhLuanActivity;
import com.vudn.myfood.view.dialog.LienLacDialog;
import com.vudn.myfood.view.map.DanDuongToiQuanAnActivity;
import com.vudn.myfood.view.menu.ThucDonActivity;
import com.vudn.myfood.view.order.DatGiaoHangActivity;
import com.vudn.myfood.view.dialog.TienIchDialog;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ChiTietQuanAnActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener, ChiTietQuanAnView {

    TextView btnXemThucDon, txtTenQuanAn, txtDiaChi, txtThoiGianHoatDong, txtTrangThaiHoatDong, txtTongSoHinhAnh, txtTongSoBinhLuan, txtTongSoCheckIn, txtTongSoLuuLai, txtTieuDeToolbar, txtGioiHanGia, txtTenWifi, txtMatKhauWifi, txtNgayDangWifi;
    ImageView imHinhAnhQuanAn, imgPlayTrailer;
    Button btnBinhLuan;
    LinearLayout khungWifi;
    QuanAnModel quanAnModel;
    Toolbar toolbar;
    RecyclerView recyclerViewBinhLuan;
    BinhLuanApdater adapterBinhLuan;
    GoogleMap googleMap;
    MapFragment mapFragment;
    LinearLayout khungTienIch;
    View khungTinhNang;
    VideoView videoView;
    RecyclerView recyclerThucDon;

    ImageView btnShowMap;
    private TextView txtLoaiHinh;
    private TextView txtDiemTB;
    private TextView txtKhoangCach;
    private TextView txtLienHe;
    private LatLng latLng;
    private Button btnDatMon;
    private List<ThucDonModel> thucDonModelList;
    private ChiTietQuanAnPresenter chiTietQuanAnPresenter;
    protected List<TienIchModel> tienIchModelList;

    //ChiTietQuanController chiTietQuanController;
    //ThucDonController thucDonController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.layout_main_chitietquanan;
    }

    @Override
    protected void initializeComponents() {
        quanAnModel = getIntent().getParcelableExtra(Key.INTENT_CHI_TIET_QUAN_AN);
        txtTenQuanAn = (TextView) findViewById(R.id.txtTenQuanAn);
        txtDiaChi = (TextView) findViewById(R.id.txtDiaChiQuanAn);
        txtThoiGianHoatDong = (TextView) findViewById(R.id.txtThoiGianHoatDong);
        txtTrangThaiHoatDong = (TextView) findViewById(R.id.txtTrangThaiHoatDong);
        txtTongSoBinhLuan = (TextView) findViewById(R.id.tongSoBinhLuan);
        txtTongSoCheckIn = (TextView) findViewById(R.id.tongSoCheckIn);
        txtTongSoHinhAnh = (TextView) findViewById(R.id.tongSoHinhAnh);
        txtTongSoLuuLai = (TextView) findViewById(R.id.tongSoLuuLai);
        imHinhAnhQuanAn = (ImageView) findViewById(R.id.imHinhQuanAn);
        txtTieuDeToolbar = (TextView) findViewById(R.id.txtTieuDeToolbar);
        txtGioiHanGia = (TextView) findViewById(R.id.txtGioiHanGia);
        txtLoaiHinh = findViewById(R.id.txtLoaiHinh);
        txtDiemTB = findViewById(R.id.txtDiemTrungBinhQuanAn);
        txtKhoangCach = findViewById(R.id.txtKhoangCachQuanAnODau);
        txtLienHe = findViewById(R.id.txtLienHe);
        btnXemThucDon = findViewById(R.id.btnXemThucDon);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerViewBinhLuan = (RecyclerView) findViewById(R.id.recyclerBinhLuanChiTietQuanAn);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        khungTienIch = (LinearLayout) findViewById(R.id.khungTienTich);
        txtTenWifi = (TextView) findViewById(R.id.txtTenWifi);
        txtMatKhauWifi = (TextView) findViewById(R.id.txtMatKhauWifi);
        khungWifi = (LinearLayout) findViewById(R.id.khungWifi);
        txtNgayDangWifi = (TextView) findViewById(R.id.txtNgayDangWifi);
        khungTinhNang = (View) findViewById(R.id.khungTinhNang);
        btnBinhLuan = (Button) findViewById(R.id.btnBinhLuan);
        videoView = (VideoView) findViewById(R.id.videoTrailer);
        imgPlayTrailer = (ImageView) findViewById(R.id.imgPLayTrailer);
        btnShowMap = findViewById(R.id.btn_showMap);
        /*recyclerThucDon = (RecyclerView) findViewById(R.id.recyclerThucDon);
        recyclerThucDon.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerThucDon.setHasFixedSize(true);
        recyclerThucDon.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));*/
        tienIchModelList = new ArrayList<>();

        btnDatMon = findViewById(R.id.btnDatMonOdau);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        chiTietQuanAnPresenter = new ChiTietQuanAnPresenterImpl(this);
        //chiTietQuanController = new ChiTietQuanController();
        //thucDonController = new ThucDonController();
        mapFragment.getMapAsync(this);
        HienThiChiTietQuanAn();
    }

    @Override
    protected void registerListeners() {
        txtLienHe.setOnClickListener(this);
        btnShowMap.setOnClickListener(this);
        btnBinhLuan.setOnClickListener(this);
        btnDatMon.setOnClickListener(this);
        btnXemThucDon.setOnClickListener(this);
        khungTienIch.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void HienThiChiTietQuanAn() {
        if (quanAnModel == null) {
            return;
        }

        //Load thực đơn quán ăn
        chiTietQuanAnPresenter.getThucDon(quanAnModel.getMaquanan());

        //Thời gian hoạt động
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

        //Tiêu đề toolbar
        txtTieuDeToolbar.setText(quanAnModel.getTenquanan());

        //Tên quán ăn
        txtTenQuanAn.setText(quanAnModel.getTenquanan());

        //Hiển thị địa chỉ quán ăn
        String mAddress = quanAnModel.getDiachi().getAddress();
        if (mAddress.contains(", Vietnam")) {
            txtDiaChi.setText(mAddress.replace(", Vietnam", ""));
        } else if (mAddress.contains(", Việt Nam")) {
            txtDiaChi.setText(mAddress.replace(", Việt Nam", ""));
        } else {
            txtDiaChi.setText(mAddress);
        }

        //Hiển thị thời gian hoạt động, thông tin tiện ích
        txtThoiGianHoatDong.setText(quanAnModel.getGiomocua() + " - " + quanAnModel.getGiodongcua());
        txtTongSoHinhAnh.setText(quanAnModel.getSohinhbinhluan() + "");
        txtTongSoBinhLuan.setText(quanAnModel.getBinhLuanModelList().size() + "");
        txtThoiGianHoatDong.setText(giomocua + " - " + giodongcua);
        txtLoaiHinh.setText(quanAnModel.getTheloai());
        if (quanAnModel.getDiemdanhgia() != 0) {
            txtDiemTB.setText(String.format("%.1f", quanAnModel.getDiemdanhgia()));
        } else {
            txtDiemTB.setText("_._");
        }

        txtKhoangCach.setText(String.format("%.1f", quanAnModel.getKhoangcach()) + " km từ đây");
        DownLoadHinhTienIch();

        //Hiển thị mức giá
        if (quanAnModel.getGiatoida() != 0 && quanAnModel.getGiatoithieu() >= 0) {
            NumberFormat numberFormat = new DecimalFormat("###,###");
            String giatoithieu = numberFormat.format(quanAnModel.getGiatoithieu()) + " đ";
            String giatoida = numberFormat.format(quanAnModel.getGiatoida()) + " đ";
            txtGioiHanGia.setText(giatoithieu + " - " + giatoida);
        } else {
            txtGioiHanGia.setVisibility(View.INVISIBLE);
        }

        /*StorageReference storageHinhQuanAn = FirebaseStorage.getInstance().getReference().child("hinhanh").child(quanAnModel.getHinhanhquanan().get(0));
        long ONE_MEGABYTE = 1024 * 1024;
        storageHinhQuanAn.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imHinhAnhQuanAn.setImageBitmap(bitmap);
            }
        });*/

        if (quanAnModel.getVideogioithieu() != null) {
            videoView.setVisibility(View.VISIBLE);
            imgPlayTrailer.setVisibility(View.VISIBLE);
            imHinhAnhQuanAn.setVisibility(View.GONE);
            StorageReference storageVideo = FirebaseStorage.getInstance().getReference().child("video").child(quanAnModel.getVideogioithieu());
            storageVideo.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    videoView.setVideoURI(uri);
                    videoView.seekTo(1);
                }
            });

            imgPlayTrailer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoView.start();
                    MediaController mediaController = new MediaController(ChiTietQuanAnActivity.this);
                    videoView.setMediaController(mediaController);
                    imgPlayTrailer.setVisibility(View.GONE);
                }
            });

        } else {
            videoView.setVisibility(View.GONE);
            imgPlayTrailer.setVisibility(View.GONE);
            imHinhAnhQuanAn.setVisibility(View.VISIBLE);
        }

        //Hiển thị hình ảnh quán ăn
        if (quanAnModel.getHinhanhquanan() == null || quanAnModel.getHinhanhquanan().isEmpty()){
            Glide.with(this).load(R.drawable.background).into(imHinhAnhQuanAn);
        }else {
            Glide.with(this).load(quanAnModel.getHinhanhquanan().get(0)).into(imHinhAnhQuanAn);
        }
        Glide.with(this).load(quanAnModel.getHinhanhquanan().get(0)).into(imHinhAnhQuanAn);

        //Hiển thì nút đặt giao hàng
        if (quanAnModel.isGiaohang()) {
            btnDatMon.setVisibility(View.VISIBLE);
        } else {
            btnDatMon.setVisibility(View.GONE);
        }

        txtTongSoLuuLai.setText(String.valueOf(quanAnModel.getLuotluu()));

        //Load danh sach binh luan cua quan
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewBinhLuan.setLayoutManager(layoutManager);
        if (quanAnModel.getBinhLuanModelList() != null) {
            setUpBinhLuanAdapter(quanAnModel.getBinhLuanModelList());
            txtTongSoBinhLuan.setText(String.valueOf(quanAnModel.getSohinhbinhluan()));
            txtTongSoHinhAnh.setText(String.valueOf(quanAnModel.getSohinhbinhluan()));
        } else {
            chiTietQuanAnPresenter.getBinhLuan(quanAnModel.getMaquanan());
        }

        NestedScrollView nestedScrollViewChiTiet = (NestedScrollView) findViewById(R.id.nestScrollViewChiTiet);
        nestedScrollViewChiTiet.smoothScrollTo(0, 0);

        /*chiTietQuanController.HienThiDanhSachWifiQuanAn(quanAnModel.getMaquanan(), txtTenWifi, txtMatKhauWifi, txtNgayDangWifi);
        khungWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iDanhSachWifi = new Intent(ChiTietQuanAnActivity.this, CapNhatDanhSachWifiActivity.class);
                iDanhSachWifi.putExtra("maquanan", quanAnModel.getMaquanan());
                startActivity(iDanhSachWifi);
            }
        });*/
        //thucDonController.getDanhSachThucDonQuanAnTheoMa(this, quanAnModel.getMaquanan(), recyclerThucDon);
    }

    private void setUpBinhLuanAdapter(List<BinhLuanModel> binhLuanModelList) {
        adapterBinhLuan = new BinhLuanApdater(this, R.layout.item_binhluan, binhLuanModelList);
        recyclerViewBinhLuan.setAdapter(adapterBinhLuan);
        adapterBinhLuan.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (quanAnModel == null) {
            return;
        }
        this.googleMap = googleMap;

        latLng = new LatLng(quanAnModel.getDiachi().getLatitude(), quanAnModel.getDiachi().getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_restaurant));
        markerOptions.title(quanAnModel.getTenquanan());

        googleMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
        googleMap.moveCamera(cameraUpdate);
    }

    private void DownLoadHinhTienIch() {
        if (quanAnModel.getTienich() == null) {
            return;
        }
        for (String matienich : quanAnModel.getTienich()) {
            DatabaseReference nodeTienIch = FirebaseDatabase.getInstance().getReference().child("quanlytienichs").child(matienich);
            nodeTienIch.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    TienIchModel tienIchModel = dataSnapshot.getValue(TienIchModel.class);
                    ImageView imageTienIch = new ImageView(ChiTietQuanAnActivity.this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
                    layoutParams.setMargins(20, 10, 10, 10);
                    imageTienIch.setLayoutParams(layoutParams);
                    imageTienIch.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageTienIch.setPadding(5, 5, 5, 5);
                    khungTienIch.addView(imageTienIch);
                    Glide.with(ChiTietQuanAnActivity.this).load(tienIchModel.getHinhtienich()).into(imageTienIch);
                    tienIchModelList.add(tienIchModel);
                    /*StorageReference storageHinhQuanAn = FirebaseStorage.getInstance().getReference().child("hinhtienich").child(tienIchModel.getHinhtienich());
                    long ONE_MEGABYTE = 1024 * 1024;
                    storageHinhQuanAn.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            ImageView imageTienIch = new ImageView(ChiTietQuanAnActivity.this);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(150, 150);
                            layoutParams.setMargins(10, 10, 10, 10);
                            imageTienIch.setLayoutParams(layoutParams);
                            imageTienIch.setScaleType(ImageView.ScaleType.FIT_XY);
                            imageTienIch.setPadding(5, 5, 5, 5);

                            imageTienIch.setImageBitmap(bitmap);
                            khungTienIch.addView(imageTienIch);
                        }
                    });*/
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_showMap:
                showMap();
                break;

            case R.id.btnBinhLuan:
                Intent iBinhLuan = new Intent(this, BinhLuanActivity.class);
                iBinhLuan.putExtra("maquanan", quanAnModel.getMaquanan());
                iBinhLuan.putExtra("tenquan", quanAnModel.getTenquanan());
                iBinhLuan.putExtra("diachi", quanAnModel.getDiachi());
                startActivityForResult(iBinhLuan, Key.RC_COMMENT);
                break;

            case R.id.txtLienHe:
                showContactDialog();
                break;

            case R.id.btnDatMonOdau:
                Intent iOrder = new Intent(ChiTietQuanAnActivity.this, DatGiaoHangActivity.class);
                iOrder.putExtra(Key.INTENT_DAT_GIAO_HANG, quanAnModel);
                startActivity(iOrder);
                break;

            case R.id.btnXemThucDon:
                xemThucDon();
                break;

            case R.id.khungTienTich:
                showTienIchDialog();
                break;

            default:
                break;
        }
    }

    private void showTienIchDialog() {
        TienIchDialog tienIchDialog = new TienIchDialog(ChiTietQuanAnActivity.this, tienIchModelList);
        tienIchDialog.show();
    }

    private void xemThucDon() {
        Intent iThucDon = new Intent(ChiTietQuanAnActivity.this, ThucDonActivity.class);
        iThucDon.putExtra(Key.INTENT_THUC_DON, quanAnModel);
        startActivity(iThucDon);
    }

    private void showContactDialog() {
        LienLacDialog customContactDialog = new LienLacDialog(ChiTietQuanAnActivity.this, quanAnModel.getSodienthoai(), quanAnModel.getTenquanan());
        customContactDialog.setCancelable(true);
        customContactDialog.show();
    }

    private void showMap() {
        if (latLng == null || quanAnModel == null) {
            return;
        }
        Intent iDanDuong = new Intent(this, DanDuongToiQuanAnActivity.class);
        iDanDuong.putExtra(Key.INTENT_TOA_DO, latLng);
        iDanDuong.putExtra(Key.INTENT_CHI_TIET_QUAN_AN, quanAnModel);
        startActivity(iDanDuong);
    }

    @Override
    public void onGetBinhLuanSuccess(List<BinhLuanModel> binhLuanModelList) {
        setUpBinhLuanAdapter(binhLuanModelList);
    }

    @Override
    public void onGetThucDonSuccess(List<ThucDonModel> thucDonModelList) {
        quanAnModel.setThucDons(thucDonModelList);
    }

    @Override
    public void onGetThucDonFailure(String error) {
        Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
    }
}
