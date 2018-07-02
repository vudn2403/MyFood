package com.vudn.myfood.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.vudn.myfood.R;

public class SoLuongDatMonAnDialog extends Dialog implements View.OnClickListener, NumberPicker.OnValueChangeListener {
    private Context context;
    private ImageView imgHinhAnhMonAn;
    private TextView txtTenMonAn;
    private TextView txtGia;
    private NumberPicker nbpSoLuong;
    private Button btnThemVaoGioHang;

    public SoLuongDatMonAnDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        setContentView(R.layout.dialog_so_luong_dat_mon_an);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        initializeComponents();
        registerListeners();
    }

    private void initializeComponents() {
        imgHinhAnhMonAn = findViewById(R.id.img_hinh_mon_an);
        txtTenMonAn = findViewById(R.id.txt_ten_mon_an);
        txtGia = findViewById(R.id.txt_gia);
        nbpSoLuong = findViewById(R.id.nbp_so_luong);
        btnThemVaoGioHang = findViewById(R.id.btn_them_vao_gio_hang);
    }

    private void registerListeners() {
        btnThemVaoGioHang.setOnClickListener(this);
        nbpSoLuong.setOnValueChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_them_vao_gio_hang:

                break;

            default:
                break;
        }
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }
}
