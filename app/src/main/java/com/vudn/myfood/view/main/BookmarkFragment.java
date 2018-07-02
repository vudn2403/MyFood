package com.vudn.myfood.view.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vudn.myfood.R;
import com.vudn.myfood.adapter.restaurant.DanhDauAdapter;
import com.vudn.myfood.base.Key;
import com.vudn.myfood.model.comment.BinhLuanModel;
import com.vudn.myfood.model.restaurant.QuanAnModel;
import com.vudn.myfood.model.user.ThanhVienModel;
import com.vudn.myfood.view.restaurant.ChiTietQuanAnActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BookmarkFragment extends Fragment implements View.OnClickListener, DanhDauAdapter.OnClickItemListener {
    private LinearLayout btnSignIn;
    private LinearLayout lnlLoading;
    private RecyclerView rcvBookmark;
    private DanhDauAdapter mBookmarkAdapter;
    private List<QuanAnModel> quanAnModelList;
    SharedPreferences sharedPreferencesDangNhap;
    DatabaseReference root;
    List<String> dsQuanAnDaLuu;
    Random random;
    public static final String TAG = "BookmarkFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = FirebaseDatabase.getInstance().getReference();
        quanAnModelList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        btnSignIn = view.findViewById(R.id.btn_sign_in);
        lnlLoading = view.findViewById(R.id.lnl_loading);
        lnlLoading.setVisibility(View.INVISIBLE);
        rcvBookmark = view.findViewById(R.id.rcv_bookmark);
        rcvBookmark.setHasFixedSize(true);
        rcvBookmark.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rcvBookmark.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mBookmarkAdapter = new DanhDauAdapter(getContext(), quanAnModelList, this);
        rcvBookmark.setAdapter(mBookmarkAdapter);
        random = new Random();
        btnSignIn.setOnClickListener(this);

        sharedPreferencesDangNhap = getContext().getSharedPreferences("luudangnhap", Context.MODE_PRIVATE);
    }

    @Override
    public void onClickItem(int position) {
        QuanAnModel quanAnModel = mBookmarkAdapter.getItem(position);
        Intent iChitietquanan = new Intent(getActivity(), ChiTietQuanAnActivity.class);
        iChitietquanan.putExtra(Key.INTENT_CHI_TIET_QUAN_AN, quanAnModel);
        startActivity(iChitietquanan);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                signIn();
                break;

            default:
                break;
        }
    }

    private void signIn() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).signIn();
        }
    }

    private void getDSQuanAnDaLuu(final String maUser) {
        dsQuanAnDaLuu = new ArrayList<>();
        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getDSQuanAn(dataSnapshot, maUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getDSQuanAn(DataSnapshot dataSnapshot, String maUser) {
        quanAnModelList = new ArrayList<>();
        DataSnapshot quanlydanhdau = dataSnapshot.child("quanlydanhdau").child(maUser);
        for (DataSnapshot value : quanlydanhdau.getChildren()) {
            String maQuanAn = value.getKey();
            DataSnapshot dataQuanAn = dataSnapshot.child("quanans").child(maQuanAn);
            QuanAnModel quanAnModel = dataQuanAn.getValue(QuanAnModel.class);
            quanAnModel.setMaquanan(dataQuanAn.getKey());
            Log.d(TAG, "QuanAn query: Tên quán ăn: " + quanAnModel.getTenquanan());

            //lấy danh sách hình ảnh theo mã quán ăn
            DataSnapshot snapshotHinhAnh = dataSnapshot.child("hinhanhquanans").child(quanAnModel.getMaquanan());
            List<String> imagePathList = new ArrayList<>();
            for (DataSnapshot valueHinhAnh : snapshotHinhAnh.getChildren()) {
                imagePathList.add(valueHinhAnh.getValue(String.class));
            }
            quanAnModel.setHinhanhquanan(imagePathList);
            Log.d(TAG, "QuanAn query: Số lượng hình ảnh = " + imagePathList.size());

            //lấy danh sách bình luận của quán
            //node bình luận
            DataSnapshot snapshotBinhLuan = dataSnapshot.child("binhluans").child(quanAnModel.getMaquanan());
            List<BinhLuanModel> binhLuanModelList = new ArrayList<>();
            int sohinhbl = 0;
            double tongdiem = 0;
            for (DataSnapshot valueBinhLuan : snapshotBinhLuan.getChildren()) {
                BinhLuanModel binhLuanModel = valueBinhLuan.getValue(BinhLuanModel.class);
                binhLuanModel.setMabinhluan(valueBinhLuan.getKey());
                ThanhVienModel thanhVienModel = dataSnapshot.child("thanhviens").child(binhLuanModel.getMauser()).getValue(ThanhVienModel.class);
                binhLuanModel.setThanhVienModel(thanhVienModel);

                List<String> hinhanhBinhLuanList = new ArrayList<>();
                DataSnapshot snapshotNodeHinhAnhBL = dataSnapshot.child("hinhanhbinhluans").child(binhLuanModel.getMabinhluan());
                for (DataSnapshot valueHinhBinhLuan : snapshotNodeHinhAnhBL.getChildren()) {
                    hinhanhBinhLuanList.add(valueHinhBinhLuan.getValue(String.class));
                }
                binhLuanModel.setHinhanhBinhLuanList(hinhanhBinhLuanList);
                sohinhbl += hinhanhBinhLuanList.size();
                Log.d(TAG, "QuanAn query: Số hình ảnh bình luận = " + hinhanhBinhLuanList.size());
                binhLuanModelList.add(binhLuanModel);
                tongdiem += binhLuanModel.getChamdiem();
            }
            quanAnModel.setSohinhbinhluan(sohinhbl);
            if(binhLuanModelList.size() != 0){
                quanAnModel.setDiemdanhgia(tongdiem/binhLuanModelList.size());
            }
            Log.d(TAG, "QuanAn query: Số lượng bình luận = " + binhLuanModelList.size());
            quanAnModel.setBinhLuanModelList(binhLuanModelList);
            quanAnModel.setKhoangcach(random.nextFloat());
            quanAnModelList.add(quanAnModel);
        }
        if (quanAnModelList.isEmpty() || quanAnModelList == null){
            lnlLoading.setVisibility(View.VISIBLE);
        }else {
            lnlLoading.setVisibility(View.GONE);
            mBookmarkAdapter = new DanhDauAdapter(getContext(), quanAnModelList, this);
            rcvBookmark.setAdapter(mBookmarkAdapter);
            rcvBookmark.setVisibility(View.VISIBLE);
            mBookmarkAdapter.notifyDataSetChanged();
        }
    }

    public void updateUI(){
        if (sharedPreferencesDangNhap == null){
            return;
        }
        if (sharedPreferencesDangNhap.getBoolean("islogin", false)){
            btnSignIn.setVisibility(View.GONE);
            lnlLoading.setVisibility(View.GONE);
            rcvBookmark.setVisibility(View.VISIBLE);
            getDSQuanAnDaLuu(sharedPreferencesDangNhap.getString("mauser", ""));
        }else {
            btnSignIn.setVisibility(View.VISIBLE);
            rcvBookmark.setVisibility(View.GONE);
            lnlLoading.setVisibility(View.GONE);
        }
    }
}
