package com.vudn.myfood.model.order;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vudn.myfood.presenter.order.GioHangPresenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DonHangModel implements Parcelable {
    private String madonhang;
    private String maquanan;
    private String tenkhachhang;
    private String mathanhvien;
    private String diachi;
    private String hinhthuc;
    private String sodienthoai;
    private String ngaydathang;
    private String tenquanan;
    private String trangthai;
    private String ghichu;
    private List<ChiTietDonHangModel> chitietdonhang;
    private GioHangPresenter gioHangPresenter;

    public DonHangModel() {

    }

    @Exclude
    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("tenkhachhang", tenkhachhang);
        result.put("diachi", diachi);
        result.put("sodienthoai", sodienthoai);
        result.put("mathanhvien", mathanhvien);
        result.put("ghichu", ghichu);
        result.put("ngaydathang", ngaydathang);
        result.put("hinhthuc", hinhthuc);
        result.put("trangthai", trangthai);
        result.put("chitietdonhang", chitietdonhang);
        return null;
    }

    public DonHangModel(GioHangPresenter gioHangPresenter) {
        this.gioHangPresenter = gioHangPresenter;
    }

    public long tinhTienThanhToan(){
        long thanhtoan = 0;
        for (ChiTietDonHangModel value : chitietdonhang){
            thanhtoan += value.getMonan().getGiatien() * value.getSoluong();
        }
        return thanhtoan;
    }

    protected DonHangModel(Parcel in) {
        madonhang = in.readString();
        maquanan = in.readString();
        tenkhachhang = in.readString();
        mathanhvien = in.readString();
        diachi = in.readString();
        hinhthuc = in.readString();
        sodienthoai = in.readString();
        ngaydathang = in.readString();
        tenquanan = in.readString();
        trangthai = in.readString();
        ghichu = in.readString();
        chitietdonhang = in.createTypedArrayList(ChiTietDonHangModel.CREATOR);
    }

    public static final Creator<DonHangModel> CREATOR = new Creator<DonHangModel>() {
        @Override
        public DonHangModel createFromParcel(Parcel in) {
            return new DonHangModel(in);
        }

        @Override
        public DonHangModel[] newArray(int size) {
            return new DonHangModel[size];
        }
    };

    public String getMadonhang() {
        return madonhang;
    }

    public void setMadonhang(String madonhang) {
        this.madonhang = madonhang;
    }

    public String getMaquanan() {
        return maquanan;
    }

    public void setMaquanan(String maquanan) {
        this.maquanan = maquanan;
    }

    public String getTenkhachhang() {
        return tenkhachhang;
    }

    public void setTenkhachhang(String tenkhachhang) {
        this.tenkhachhang = tenkhachhang;
    }

    public String getMathanhvien() {
        return mathanhvien;
    }

    public void setMathanhvien(String mathanhvien) {
        this.mathanhvien = mathanhvien;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getHinhthuc() {
        return hinhthuc;
    }

    public void setHinhthuc(String hinhthuc) {
        this.hinhthuc = hinhthuc;
    }

    public String getSodienthoai() {
        return sodienthoai;
    }

    public void setSodienthoai(String sodienthoai) {
        this.sodienthoai = sodienthoai;
    }

    public String getNgaydathang() {
        return ngaydathang;
    }

    public void setNgaydathang(String ngaydathang) {
        this.ngaydathang = ngaydathang;
    }

    public String getGhichu() {
        return ghichu;
    }

    public void setGhichu(String ghichu) {
        this.ghichu = ghichu;
    }

    public List<ChiTietDonHangModel> getChitietdonhang() {
        return chitietdonhang;
    }

    public void setChitietdonhang(List<ChiTietDonHangModel> chitietdonhang) {
        this.chitietdonhang = chitietdonhang;
    }

    public static Creator<DonHangModel> getCREATOR() {
        return CREATOR;
    }

    public String getTenquanan() {
        return tenquanan;
    }

    public void setTenquanan(String tenquanan) {
        this.tenquanan = tenquanan;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(madonhang);
        dest.writeString(maquanan);
        dest.writeString(tenkhachhang);
        dest.writeString(mathanhvien);
        dest.writeString(diachi);
        dest.writeString(hinhthuc);
        dest.writeString(sodienthoai);
        dest.writeString(ngaydathang);
        dest.writeString(tenquanan);
        dest.writeString(trangthai);
        dest.writeString(ghichu);
        dest.writeTypedList(chitietdonhang);
    }

    public void themDonHang(DonHangModel donHangModel) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        long updateTime = Calendar.getInstance().getTime().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd" + "' Tháng '" + "MM" + "' lúc '" + "HH:mm");
        donHangModel.setNgaydathang(sdf.format(updateTime));
        String key = "";
        if (donHangModel.getMadonhang() == null){
            key = mDatabase.child("donhang").push().getKey();
        }else {
            key = donHangModel.getMadonhang();
        }
        mDatabase.child("donhang").child(key).setValue(donHangModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        gioHangPresenter.themDonHangSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        gioHangPresenter.themDonHangFailure(e.toString());
                    }
                });
    }

    public void getDonHang(String userId) {
        final List<DonHangModel> donHangModelList = new ArrayList<>();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd" + "' Tháng '" + "MM" + "' lúc '" + "HH:mm");
        Query query = FirebaseDatabase.getInstance().getReference().child("donhang").orderByChild("mathanhvien").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot value : dataSnapshot.getChildren()){
                    DonHangModel donHangModel = value.getValue(DonHangModel.class);
                    String time = sdf.format(Long.parseLong(donHangModel.getNgaydathang()));
                    donHangModel.setNgaydathang(time);
                    donHangModel.setMadonhang(value.getKey());
                    donHangModelList.add(donHangModel);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void capNhatDonHang(DonHangModel donHangModel) {

    }
}
