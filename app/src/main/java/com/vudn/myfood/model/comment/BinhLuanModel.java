package com.vudn.myfood.model.comment;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vudn.myfood.model.user.ThanhVienModel;
import com.vudn.myfood.presenter.restaurant.ChiTietQuanAnPresenter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BinhLuanModel implements Parcelable {
    public static final String TAG = "BinhLuanModel";
    double chamdiem;
    long luotthich;
    ThanhVienModel thanhVienModel;
    String noidung;
    String tieude;
    List<String> hinhanhBinhLuanList;
    ChiTietQuanAnPresenter chiTietQuanAnPresenter;
    DatabaseReference nodeRoot;

    public BinhLuanModel(ChiTietQuanAnPresenter chiTietQuanAnPresenter) {
        this.chiTietQuanAnPresenter = chiTietQuanAnPresenter;
        nodeRoot = FirebaseDatabase.getInstance().getReference();
    }

    protected BinhLuanModel(Parcel in) {
        chamdiem = in.readDouble();
        luotthich = in.readLong();
        noidung = in.readString();
        tieude = in.readString();
        mabinhluan = in.readString();
        hinhanhBinhLuanList = in.createStringArrayList();
        mathanhvien = in.readString();
        thanhVienModel = in.readParcelable(ThanhVienModel.class.getClassLoader());
    }

    public void getBinhLuan(final String maquanan){
        nodeRoot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot snapshotBinhLuan = dataSnapshot.child("binhluans").child(maquanan);
                List<BinhLuanModel> binhLuanModelList = new ArrayList<>();

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
                    binhLuanModelList.add(binhLuanModel);
                }
                chiTietQuanAnPresenter.getBinhLuanSuccess(binhLuanModelList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static final Creator<BinhLuanModel> CREATOR = new Creator<BinhLuanModel>() {
        @Override
        public BinhLuanModel createFromParcel(Parcel in) {
            return new BinhLuanModel(in);
        }

        @Override
        public BinhLuanModel[] newArray(int size) {
            return new BinhLuanModel[size];
        }
    };

    public String getMabinhluan() {
        return mabinhluan;
    }

    public void setMabinhluan(String mabinhluan) {
        this.mabinhluan = mabinhluan;
    }

    String mabinhluan;

    public List<String> getHinhanhBinhLuanList() {
        return hinhanhBinhLuanList;
    }

    public void setHinhanhBinhLuanList(List<String> hinhanhList) {
        this.hinhanhBinhLuanList = hinhanhList;
    }

    public String getMauser() {
        return mathanhvien;
    }

    public void setMauser(String mathanhvien) {
        this.mathanhvien = mathanhvien;
    }

    String mathanhvien;


    public BinhLuanModel() {

    }

    public double getChamdiem() {
        return chamdiem;
    }

    public void setChamdiem(double chamdiem) {
        this.chamdiem = chamdiem;
    }

    public long getLuotthich() {
        return luotthich;
    }

    public void setLuotthich(long luotthich) {
        this.luotthich = luotthich;
    }

    public ThanhVienModel getThanhVienModel() {
        return thanhVienModel;
    }

    public void setThanhVienModel(ThanhVienModel thanhVienModel) {
        this.thanhVienModel = thanhVienModel;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public String getTieude() {
        return tieude;
    }

    public void setTieude(String tieude) {
        this.tieude = tieude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(chamdiem);
        dest.writeLong(luotthich);
        dest.writeString(noidung);
        dest.writeString(tieude);
        dest.writeString(mabinhluan);
        dest.writeStringList(hinhanhBinhLuanList);
        dest.writeString(mathanhvien);
        dest.writeParcelable(thanhVienModel, flags);
    }

    StorageReference root = FirebaseStorage.getInstance().getReference();

    public void ThemBinhLuan(String maQuanAn, BinhLuanModel binhLuanModel, final List<String> listHinh) {
        DatabaseReference nodeBinhLuan = FirebaseDatabase.getInstance().getReference().child("binhluans");
        final String mabinhluan = nodeBinhLuan.child(maQuanAn).push().getKey();
        /*nodeBinhLuan.child(maQuanAn).child(mabinhluan).setValue(binhLuanModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (listHinh != null && listHinh.size() > 0) {
                        for (String value : listHinh) {
                            Uri uri = Uri.fromFile(new File(value));
                            final StorageReference reference = root.child("hinhbinhluan/" + uri.getLastPathSegment());
                            UploadTask uploadTask = reference.putFile(uri);
                            Task<Uri> url = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }
                                    return reference.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri downloadUri = task.getResult();
                                        Log.d(TAG, "onComplete: Tải ảnh thành công, đường dẫn" + downloadUri);
                                        FirebaseDatabase.getInstance().getReference().child("hinhanhbinhluan").child(mabinhluan).push().setValue(downloadUri);
                                    } else {
                                        Log.d(TAG, "onComplete: Tải ảnh thất bại");
                                    }

                                }
                            });

                        }
                    }
                }
            }
        });*/
        nodeBinhLuan.child(maQuanAn).child(mabinhluan).setValue(binhLuanModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    if(listHinh.size() > 0){
                        for(String valueHinh : listHinh){
                            Uri uri = Uri.fromFile(new File(valueHinh));
                            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("hinhanh/"+uri.getLastPathSegment());
                            storageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                }
                            });
                        }
                    }

                }
            }
        });


        if(listHinh.size() > 0){
            for(String valueHinh : listHinh){
                Uri uri = Uri.fromFile(new File(valueHinh));
                FirebaseDatabase.getInstance().getReference().child("hinhanhbinhluans").child(mabinhluan).push().setValue(uri.getLastPathSegment());
            }
        }
    }
}
