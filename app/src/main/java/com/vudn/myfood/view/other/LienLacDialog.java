package com.vudn.myfood.view.other;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vudn.myfood.R;

public class LienLacDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private TextView txtSoDienThoai;
    private TextView btnGoiDien;
    private TextView btnNhanTin;
    private String soDT;
    private String tenQuanAn;

    public LienLacDialog(@NonNull Context context, String soDT, String tenQuanAn) {
        super(context);
        this.context = context;
        setContentView(R.layout.dialog_lienlac);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.soDT = soDT;
        this.tenQuanAn = tenQuanAn;
        initializeComponents();
        registerListeners();
    }

    private void initializeComponents() {
        txtSoDienThoai = findViewById(R.id.txt_so_dt);
        btnGoiDien = findViewById(R.id.btn_goi_dien);
        btnNhanTin = findViewById(R.id.btn_nhan_tin);
        txtSoDienThoai.setText(soDT + "");
    }

    private void registerListeners() {
        btnGoiDien.setOnClickListener(this);
        btnNhanTin.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_goi_dien:
                goiDien();
                break;

            case R.id.btn_nhan_tin:
                nhanTin();
                break;

            default:
                break;
        }
    }

    private void goiDien() {
        String dial = "tel:" + soDT;
        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
    }

    private void nhanTin() {
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + soDT));
        smsIntent.putExtra("sms_body", tenQuanAn);
        context.startActivity(smsIntent);
    }
}
