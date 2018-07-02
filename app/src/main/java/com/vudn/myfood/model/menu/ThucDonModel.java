package com.vudn.myfood.model.menu;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vudn.myfood.presenter.order.DatGiaoHangPresenter;
import com.vudn.myfood.presenter.other.ThucDonInterface;
import com.vudn.myfood.presenter.restaurant.ChiTietQuanAnPresenter;

import java.util.ArrayList;
import java.util.List;

public class ThucDonModel implements Parcelable{
    String mathucdon;
    String tenthucdon;
    List<MonAnModel> monAnModelList;
    ChiTietQuanAnPresenter chiTietQuanAnPresenter;
    DatGiaoHangPresenter datGiaoHangPresenter;

    public ThucDonModel(ChiTietQuanAnPresenter chiTietQuanAnPresenter){
        this.chiTietQuanAnPresenter = chiTietQuanAnPresenter;
    }

    public ThucDonModel(DatGiaoHangPresenter datGiaoHangPresenter){
        this.datGiaoHangPresenter = datGiaoHangPresenter;
    }

    public ThucDonModel() {
    }

    protected ThucDonModel(Parcel in) {
        mathucdon = in.readString();
        tenthucdon = in.readString();
        monAnModelList = in.createTypedArrayList(MonAnModel.CREATOR);
    }

    public static final Creator<ThucDonModel> CREATOR = new Creator<ThucDonModel>() {
        @Override
        public ThucDonModel createFromParcel(Parcel in) {
            return new ThucDonModel(in);
        }

        @Override
        public ThucDonModel[] newArray(int size) {
            return new ThucDonModel[size];
        }
    };

    public List<MonAnModel> getMonAnModelList() {
        return monAnModelList;
    }

    public void setMonAnModelList(List<MonAnModel> monAnModelList) {
        this.monAnModelList = monAnModelList;
    }

    public String getMathucdon() {
        return mathucdon;
    }

    public void setMathucdon(String mathucdon) {
        this.mathucdon = mathucdon;
    }

    public String getTenthucdon() {
        return tenthucdon;
    }

    public void setTenthucdon(String tenthucdon) {
        this.tenthucdon = tenthucdon;
    }

    public void getDanhSachThucDonQuanAnTheoMa(final String maquanan, final ThucDonInterface thucDonInterface){
        DatabaseReference nodeThucDonQuanAn = FirebaseDatabase.getInstance().getReference().child("thucdonquanans").child(maquanan);
        nodeThucDonQuanAn.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                final List<ThucDonModel> thucDonModels = new ArrayList<>();

                for (final DataSnapshot valueThucDon : dataSnapshot.getChildren()){
                    final ThucDonModel thucDonModel = new ThucDonModel();

                    DatabaseReference nodeThucDon = FirebaseDatabase.getInstance().getReference().child("thucdons").child(valueThucDon.getKey());
                    nodeThucDon.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshotThucDon) {
                            String mathucdon = dataSnapshotThucDon.getKey();
                            thucDonModel.setMathucdon(mathucdon);
                            thucDonModel.setTenthucdon(dataSnapshotThucDon.getValue(String.class));
                            List<MonAnModel> monAnModels = new ArrayList<>();

                            for (DataSnapshot valueMonAn : dataSnapshot.child(mathucdon).getChildren()){
                                MonAnModel monAnModel = valueMonAn.getValue(MonAnModel.class);
                                monAnModel.setMamon(valueMonAn.getKey());
                                monAnModels.add(monAnModel);
                            }

                            thucDonModel.setMonAnModelList(monAnModels);
                            thucDonModels.add(thucDonModel);
                            thucDonInterface.getThucDonThanhCong(thucDonModels);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    List<ThucDonModel> thucDonModelList;
    public void getThucDon(final String maquanan){
        thucDonModelList = new ArrayList<>();
        final DatabaseReference nodeRoot = FirebaseDatabase.getInstance().getReference();
        nodeRoot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot nodeThucDon = dataSnapshot.child("thucdonquanans").child(maquanan);
                for (DataSnapshot valueThucdon : nodeThucDon.getChildren()){
                    ThucDonModel thucDonModel = valueThucdon.getValue(ThucDonModel.class);
                    thucDonModel.setMathucdon(valueThucdon.getKey());
                    String tenThucDon = dataSnapshot.child("thucdons").child(valueThucdon.getKey()).getValue(String.class);
                    thucDonModel.setTenthucdon(tenThucDon);
                    DataSnapshot nodeMonAn = nodeThucDon.child(valueThucdon.getKey());
                    List<MonAnModel> monAnModelList = new ArrayList<>();
                    for (DataSnapshot valueMonAn : nodeMonAn.getChildren()){
                        MonAnModel monAnModel = valueMonAn.getValue(MonAnModel.class);
                        monAnModel.setMamon(valueMonAn.getKey());
                        monAnModelList.add(monAnModel);
                    }
                    thucDonModel.setMonAnModelList(monAnModelList);
                    thucDonModelList.add(thucDonModel);
                }
                if (datGiaoHangPresenter != null){
                    datGiaoHangPresenter.getThucDonSuccess(thucDonModelList);
                }
                if (chiTietQuanAnPresenter != null){
                    chiTietQuanAnPresenter.getThucDonSuccess(thucDonModelList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (datGiaoHangPresenter != null){
                    datGiaoHangPresenter.getThucDonFailure(databaseError.getMessage());
                }
                if (chiTietQuanAnPresenter != null){
                    chiTietQuanAnPresenter.getThucDonFailure(databaseError.getMessage());
                }
            }
        });
/*        DatabaseReference nodeThucDonQuanAn = FirebaseDatabase.getInstance().getReference().child("thucdonquanans").child(maquanan);
        nodeThucDonQuanAn.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for (final DataSnapshot valueThucDon : dataSnapshot.getChildren()){
                    final ThucDonModel thucDonModel = new ThucDonModel();

                    DatabaseReference nodeThucDon = FirebaseDatabase.getInstance().getReference().child("thucdons").child(valueThucDon.getKey());
                    nodeThucDon.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshotThucDon) {
                            String mathucdon = dataSnapshotThucDon.getKey();
                            thucDonModel.setMathucdon(mathucdon);
                            thucDonModel.setTenthucdon(dataSnapshotThucDon.getValue(String.class));
                            List<MonAnModel> monAnModels = new ArrayList<>();

                            for (DataSnapshot valueMonAn : dataSnapshot.child(mathucdon).getChildren()){
                                MonAnModel monAnModel = valueMonAn.getValue(MonAnModel.class);
                                monAnModel.setMamon(valueMonAn.getKey());
                                monAnModels.add(monAnModel);
                            }
                            thucDonModel.setMonAnModelList(monAnModels);
                            thucDonModelList.add(thucDonModel);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mathucdon);
        dest.writeString(tenthucdon);
        dest.writeTypedList(monAnModelList);
    }
}
