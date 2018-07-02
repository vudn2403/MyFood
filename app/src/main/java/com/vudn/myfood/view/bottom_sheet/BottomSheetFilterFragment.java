package com.vudn.myfood.view.bottom_sheet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vudn.myfood.R;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetFilterFragment extends BottomSheetDialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Spinner spnType;
    private Spinner spnArea;
    private ArrayAdapter<String> adapterType;
    private ArrayAdapter<String> adapterArea;
    private List<String> listType;
    private List<String> listArea;
    private String theloai;
    private String khuvuc;
    private boolean isGiaoHang;
    private CheckBox ckbGiaoHang;
    private Button btnApDung;
    private Button btnDatLai;
    OnFilterListener onFilterListener;

    public BottomSheetFilterFragment() {
        //Default constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setOnFilterListener(OnFilterListener onFilterListener) {
        this.onFilterListener = onFilterListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_sheet_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeComponents(view);
        registerListeners();
    }

    private void initializeComponents(View view) {
        spnType = view.findViewById(R.id.spn_theloai);
        spnArea = view.findViewById(R.id.spn_khuvuc);
        //ckbGiaoHang = view.findViewById(R.id.ckb_giao_hang);
        btnApDung = view.findViewById(R.id.btn_ap_dung);
        btnDatLai = view.findViewById(R.id.btn_dat_lai);

        listType = new ArrayList<>();
        listType.add("");
        listArea = new ArrayList<>();
        listArea.add("");

        LayDanhSAchTheLoai();
        LayDanhSachKhuVuc();

        initializeSpinner();
    }

    private void initializeSpinner() {
        adapterType = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, listType);
        spnType.setAdapter(adapterType);
        adapterType.notifyDataSetChanged();

        adapterArea = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, listArea);
        spnArea.setAdapter(adapterArea);
        adapterArea.notifyDataSetChanged();
    }

    private void registerListeners() {
        spnType.setOnItemSelectedListener(this);
        spnArea.setOnItemSelectedListener(this);
        btnApDung.setOnClickListener(this);
        btnDatLai.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spn_theloai:
                theloai = listType.get(position);
                break;

            case R.id.spn_khuvuc:
                khuvuc = listArea.get(position);
                break;

            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void LayDanhSAchTheLoai() {
        FirebaseDatabase.getInstance().getReference().child("theloai").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot value : dataSnapshot.getChildren()) {
                    String type = value.getValue(String.class);
                    listType.add(type);
                }
                adapterType.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void LayDanhSachKhuVuc() {
        FirebaseDatabase.getInstance().getReference().child("khuvucs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String tenKhuVuc = snapshot.getValue(String.class);
                    listArea.add(tenKhuVuc);
                }
                adapterArea.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ap_dung:
                /*if (ckbGiaoHang.isChecked()){
                    isGiaoHang = true;
                }else {
                    isGiaoHang = false;
                }*/
                onFilterListener.onFilter(theloai, khuvuc);
                dismiss();
                break;

            case R.id.btn_dat_lai:
                theloai = "";
                khuvuc = "";
                //isGiaoHang = false;
                //ckbGiaoHang.setChecked(false);
                spnArea.setSelection(0);
                spnType.setSelection(0);
                break;

            default:
                break;
        }
    }

    public interface OnFilterListener{
        void onFilter(String theloai, String khuvuc);
    }
}
