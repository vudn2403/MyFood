package com.vudn.myfood.view.bottom_sheet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vudn.myfood.R;


public class BottomSheetSortFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    TextView txtGanToi;
    TextView txtDanhGiaCao;
    TextView txtTheoTen;
    OnSapXepClickListener onSapXepClickListener;

    public BottomSheetSortFragment() {
        //Default constructor
    }

    public void setOnSapXepClickListener(OnSapXepClickListener onSapXepClickListener) {
        this.onSapXepClickListener = onSapXepClickListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_sheet_sort, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeComponents(view);
        registerListeners();
    }

    private void initializeComponents(View view) {
        txtGanToi = view.findViewById(R.id.txt_gan_toi);
        txtDanhGiaCao = view.findViewById(R.id.txt_danh_gia_cao);
        txtTheoTen = view.findViewById(R.id.txt_theo_ten);
    }

    private void registerListeners() {
        txtGanToi.setOnClickListener(this);
        txtDanhGiaCao.setOnClickListener(this);
        txtTheoTen.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_gan_toi:
                onSapXepClickListener.onSapXep(0);
                dismiss();
                break;

            case R.id.txt_danh_gia_cao:
                onSapXepClickListener.onSapXep(1);
                dismiss();
                break;

            case R.id.txt_theo_ten:
                onSapXepClickListener.onSapXep(2);
                dismiss();
                break;

            default:
                break;
        }
    }

    public interface OnSapXepClickListener{
        void onSapXep(int tieuchi);
    }
}
