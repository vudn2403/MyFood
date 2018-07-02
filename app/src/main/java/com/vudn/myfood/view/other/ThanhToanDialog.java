package com.vudn.myfood.view.other;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.vudn.myfood.R;
import com.vudn.myfood.model.order.DonHangModel;
import com.vudn.myfood.view.order.GioHangActivity;

public class ThanhToanDialog extends Dialog implements View.OnClickListener {
    public static final String TAG = "ThanhToanDialog";
    private Button btnDatGiaoHang;
    private EditText edtTenKhachHang;
    private EditText edtDiaChi;
    private EditText edtSoDienThoai;
    private EditText edtGhiChu;
    private DonHangModel donHangModel;
    private RadioButton rdbThanhToanKhiGiaoHang;
    private RadioButton rdbChuyenKhoan;
    private CheckBox ckbCamKet;
    private Context context;
    private OnCreateDonHangListener onCreateDonHangListener;
    SharedPreferences sharedPreferencesDangNhap;
    SharedPreferences sharedPreferencesToaDo;

    public ThanhToanDialog(@NonNull Context context, DonHangModel donHangModel, OnCreateDonHangListener onCreateDonHangListener) {
        super(context);
        this.context = context;
        setContentView(R.layout.dialog_thanhtoan);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        sharedPreferencesDangNhap = context.getSharedPreferences("luudangnhap", Context.MODE_PRIVATE);
        sharedPreferencesDangNhap = context.getSharedPreferences("toado", Context.MODE_PRIVATE);
        setOnCreateDonHangListener(onCreateDonHangListener);
        this.donHangModel = donHangModel;
        initializeComponents();
        registerListeners();
    }

    public void setOnCreateDonHangListener(OnCreateDonHangListener onCreateDonHangListener) {
        this.onCreateDonHangListener = onCreateDonHangListener;
    }

    private void initializeComponents() {
        btnDatGiaoHang = findViewById(R.id.btn_dat_giao_hang);
        edtTenKhachHang = findViewById(R.id.edt_ten_khach_hang);
        edtTenKhachHang.setText(donHangModel.getTenkhachhang());
        edtDiaChi = findViewById(R.id.edt_dia_chi);
        edtDiaChi.setText(donHangModel.getDiachi());
        edtSoDienThoai = findViewById(R.id.edt_so_dien_thoai);
        edtSoDienThoai.setText(donHangModel.getSodienthoai());
        edtGhiChu = findViewById(R.id.edt_ghi_chu);
        edtGhiChu.setText(donHangModel.getGhichu());
        rdbThanhToanKhiGiaoHang = findViewById(R.id.rdb_thanh_toan_khi_nhan_hang);
        rdbChuyenKhoan = findViewById(R.id.rdb_chuyen_khoan);
        ckbCamKet = findViewById(R.id.ckb_cam_ket);
        if (donHangModel.getHinhthuc() != null){
            if (donHangModel.getHinhthuc().equals("Thanh toán khi nhận hàng")){
                rdbThanhToanKhiGiaoHang.setChecked(true);
            }else if (donHangModel.getHinhthuc().equals("Chuyển khoản")){
                rdbChuyenKhoan.setChecked(true);
            }
        }

        if (sharedPreferencesDangNhap == null){
            return;
        }

        String name = sharedPreferencesDangNhap.getString("hoten", "");
        String address  = sharedPreferencesToaDo.getString("address", "");
        String sodt  = sharedPreferencesDangNhap.getString("sodienthoai", "");

        if (name != null && !name.isEmpty()){
            edtTenKhachHang.setText(name);
        }

        if (address != null && !address.isEmpty()){
            edtDiaChi.setText(address);
        }

        if (sodt != null && !sodt.isEmpty()){
            edtSoDienThoai.setText(sodt);
        }

        /*if (getOwnerActivity() instanceof GioHangActivity) {
            donHangModel = ((GioHangActivity) getOwnerActivity()).getDonHangModel();
            Log.d(TAG, TAG + " initializeComponents: " + donHangModel.getChitietdonhang().size());
        }else {
            Log.d(TAG, TAG + " initializeComponents: không lấy được đơn hàng");
        }*/
    }

    private void registerListeners() {
        btnDatGiaoHang.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dat_giao_hang:
                createDonHang();
                break;

            default:
                break;
        }
    }

    private void createDonHang() {
        String tenKhachHang = edtTenKhachHang.getText().toString().trim();
        String diaChi = edtDiaChi.getText().toString().trim();
        String soDienThoai = edtSoDienThoai.getText().toString().trim();
        String ghiChu = edtGhiChu.getText().toString().trim();
        String hinhThucTT = "";
        if (rdbThanhToanKhiGiaoHang.isChecked()){
             hinhThucTT = "Thanh toán khi nhận hàng";
        } else if (rdbChuyenKhoan.isChecked()){
            hinhThucTT = "Chuyển khoản";
        }

        if (!ckbCamKet.isChecked()){
            Toast.makeText(context, "Vui lòng xác nhận rằng thông tin nhập vào là hoàn toàn chính xác", Toast.LENGTH_SHORT).show();
            return;
        }

        if (tenKhachHang.isEmpty()){
            Toast.makeText(context, "Vui lòng nhập tên đầy đủ của bạn", Toast.LENGTH_SHORT).show();
            return;
        }

        if (diaChi.isEmpty()){
            Toast.makeText(context, "Vui lòng nhập địa chỉ giao hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        if (soDienThoai.isEmpty()){
            Toast.makeText(context, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
            return;
        }

        donHangModel.setTenkhachhang(tenKhachHang);
        donHangModel.setDiachi(diaChi);
        donHangModel.setSodienthoai(soDienThoai);
        donHangModel.setGhichu(ghiChu);
        donHangModel.setHinhthuc(hinhThucTT);
        donHangModel.setTrangthai("Chờ xử lý");

        /*if (getOwnerActivity() instanceof GioHangActivity){
            ((GioHangActivity) getOwnerActivity()).setDonHangModel(donHangModel);
            Log.d(TAG, TAG + " createDonHang: Tên khách hàng... " + donHangModel.getTenkhachhang());
        }*/
        onCreateDonHangListener.onCreateDonHang(donHangModel);
        this.dismiss();
    }

    public interface OnCreateDonHangListener{
        void onCreateDonHang(DonHangModel donHangModel);
    }
}
