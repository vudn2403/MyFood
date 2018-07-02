package com.vudn.myfood.model.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vudn.myfood.presenter.user.UserPresenter;

public class ThanhVienModel implements Parcelable {
    String hoten;
    String hinhanh;
    String mathanhvien;
    String sodienthoai;
    String diachi;
    String ngaysinh;
    String email;

    private DatabaseReference nodeThanhVien;
    private UserPresenter userPresenter;

    public ThanhVienModel(UserPresenter userPresenter){
        nodeThanhVien = FirebaseDatabase.getInstance().getReference().child("thanhviens");
        this.userPresenter = userPresenter;
    }

    public ThanhVienModel() {
        nodeThanhVien = FirebaseDatabase.getInstance().getReference().child("thanhviens");
    }


    protected ThanhVienModel(Parcel in) {
        hoten = in.readString();
        hinhanh = in.readString();
        mathanhvien = in.readString();
        sodienthoai = in.readString();
        diachi = in.readString();
        ngaysinh = in.readString();
        email = in.readString();
    }

    public static final Creator<ThanhVienModel> CREATOR = new Creator<ThanhVienModel>() {
        @Override
        public ThanhVienModel createFromParcel(Parcel in) {
            return new ThanhVienModel(in);
        }

        @Override
        public ThanhVienModel[] newArray(int size) {
            return new ThanhVienModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hoten);
        dest.writeString(hinhanh);
        dest.writeString(mathanhvien);
        dest.writeString(sodienthoai);
        dest.writeString(diachi);
        dest.writeString(ngaysinh);
        dest.writeString(email);
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public String getMathanhvien() {
        return mathanhvien;
    }

    public void setMathanhvien(String mathanhvien) {
        this.mathanhvien = mathanhvien;
    }

    public String getSodienthoai() {
        return sodienthoai;
    }

    public void setSodienthoai(String sodienthoai) {
        this.sodienthoai = sodienthoai;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getNgaysinh() {
        return ngaysinh;
    }

    public void setNgaysinh(String ngaysinh) {
        this.ngaysinh = ngaysinh;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static Creator<ThanhVienModel> getCREATOR() {
        return CREATOR;
    }

    public void signUp(FirebaseUser user){
        ThanhVienModel thanhVienModel = new ThanhVienModel();
        thanhVienModel.setHoten("Thành viên mới");
        thanhVienModel.setHinhanh("https://firebasestorage.googleapis.com/v0/b/myfood-9339d.appspot.com/o/thanhvien%2Fimage_user.jpg?alt=media&token=3f88a16a-df9e-487d-9738-2456bbf562d1");
        thanhVienModel.setSodienthoai("");
        thanhVienModel.setDiachi("");
        thanhVienModel.setEmail(user.getEmail());
        nodeThanhVien.child(user.getUid()).setValue(thanhVienModel);
        nodeThanhVien.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ThanhVienModel value = dataSnapshot.getValue(ThanhVienModel.class);
                value.setMathanhvien(dataSnapshot.getKey());
                userPresenter.onSignUpSuccess(value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                userPresenter.onSignUpFailure(databaseError.getMessage());
            }
        });
    }

    public void signIn(FirebaseUser user){
        final String name = user.getDisplayName();
        final String hinhanh = user.getPhotoUrl().toString();
        final String sdt = user.getPhoneNumber();
        final String diachi = "";
        final String email = user.getEmail();
        nodeThanhVien.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ThanhVienModel thanhVienModel = new ThanhVienModel();
                if (dataSnapshot.getValue(ThanhVienModel.class) == null){
                    thanhVienModel.setHoten(name);
                    thanhVienModel.setHinhanh(hinhanh);
                    thanhVienModel.setSodienthoai(sdt);
                    thanhVienModel.setDiachi(diachi);
                    thanhVienModel.setEmail(email);
                    nodeThanhVien.child(dataSnapshot.getKey()).setValue(thanhVienModel);
                    nodeThanhVien.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ThanhVienModel thanhVienModel = dataSnapshot.getValue(ThanhVienModel.class);
                            thanhVienModel.setMathanhvien(dataSnapshot.getKey());
                            userPresenter.onSignInSuccess(thanhVienModel);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else {
                    thanhVienModel = dataSnapshot.getValue(ThanhVienModel.class);
                    thanhVienModel.setMathanhvien(dataSnapshot.getKey());
                    userPresenter.onSignInSuccess(thanhVienModel);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                userPresenter.onSignInFailure(databaseError.getMessage());
            }
        });
    }

    public void Update(ThanhVienModel thanhVienModel){
        nodeThanhVien.child(thanhVienModel.getMathanhvien()).setValue(thanhVienModel);
    }
}
