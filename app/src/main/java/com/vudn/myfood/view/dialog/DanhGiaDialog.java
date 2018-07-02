package com.vudn.myfood.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import com.vudn.myfood.R;


public class DanhGiaDialog extends Dialog implements NumberPicker.OnValueChangeListener, View.OnClickListener {
    private NumberPicker numberPicker;
    private OnDanhGiaListener onDanhGiaListener;
    private Button btnDongY;

    public DanhGiaDialog(@NonNull Context context, OnDanhGiaListener onDanhGiaListener) {
        super(context);
        setContentView(R.layout.dialog_danh_gia);
        try{
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        init();
        setOnDanhGiaListener(onDanhGiaListener);
    }

    public void setOnDanhGiaListener(OnDanhGiaListener onDanhGiaListener) {
        this.onDanhGiaListener = onDanhGiaListener;
    }

    private void init() {
        numberPicker = findViewById(R.id.nbp_danh_gia);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setOnValueChangedListener(this);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);
        btnDongY = findViewById(R.id.btn_dong_y);
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dong_y:
            onDanhGiaListener.onDanhGia(numberPicker.getValue());
            break;

            default:
                break;
        }
    }

    public interface OnDanhGiaListener {
        void onDanhGia(int diem);
    }
}
