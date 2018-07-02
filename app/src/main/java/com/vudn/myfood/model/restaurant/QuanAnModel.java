package com.vudn.myfood.model.restaurant;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vudn.myfood.model.address.DiaChiModel;
import com.vudn.myfood.model.comment.BinhLuanModel;
import com.vudn.myfood.model.menu.ThucDonModel;
import com.vudn.myfood.model.user.ThanhVienModel;
import com.vudn.myfood.presenter.main.HomePresenter;
import com.vudn.myfood.presenter.search.SearchPresenter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class QuanAnModel implements Parcelable {
    public static final String TAG = "QuanAnModel";
    boolean giaohang;
    private String giodongcua,
            giomocua,
            tenquanan,
            videogioithieu,
            maquanan,
            theloai,
            khuvuc,
            gioithieu,
            mathanhvien,
            sodienthoai,
            chiduong;

    private DiaChiModel diachi;
    private List<String> tienich;
    private List<String> hinhanhquanan;
    private List<BinhLuanModel> binhLuanModelList;
    private List<ThucDonModel> thucDons;

    private long giatoida;
    private long giatoithieu;
    private long luotluu;
    private float khoangcach;
    private int sohinhbinhluan;
    private double diemdanhgia;

    private HomePresenter homePresenter;
    private SearchPresenter searchPresenter;
    private DatabaseReference nodeRoot;
    private DataSnapshot dataRoot;

    public QuanAnModel(SearchPresenter searchPresenter) {
        this.searchPresenter = searchPresenter;
        nodeRoot = FirebaseDatabase.getInstance().getReference();
    }

    public QuanAnModel(HomePresenter homePresenter) {
        this.homePresenter = homePresenter;
        nodeRoot = FirebaseDatabase.getInstance().getReference();
    }

    public QuanAnModel() {
        nodeRoot = FirebaseDatabase.getInstance().getReference();
    }


    protected QuanAnModel(Parcel in) {
        giaohang = in.readByte() != 0;
        giodongcua = in.readString();
        giomocua = in.readString();
        tenquanan = in.readString();
        videogioithieu = in.readString();
        maquanan = in.readString();
        theloai = in.readString();
        khuvuc = in.readString();
        gioithieu = in.readString();
        mathanhvien = in.readString();
        sodienthoai = in.readString();
        chiduong = in.readString();
        diachi = in.readParcelable(DiaChiModel.class.getClassLoader());
        tienich = in.createStringArrayList();
        hinhanhquanan = in.createStringArrayList();
        binhLuanModelList = in.createTypedArrayList(BinhLuanModel.CREATOR);
        thucDons = in.createTypedArrayList(ThucDonModel.CREATOR);
        giatoida = in.readLong();
        giatoithieu = in.readLong();
        luotluu = in.readLong();
        khoangcach = in.readFloat();
        sohinhbinhluan = in.readInt();
        diemdanhgia = in.readDouble();
    }

    public static final Creator<QuanAnModel> CREATOR = new Creator<QuanAnModel>() {
        @Override
        public QuanAnModel createFromParcel(Parcel in) {
            return new QuanAnModel(in);
        }

        @Override
        public QuanAnModel[] newArray(int size) {
            return new QuanAnModel[size];
        }
    };

    public void getQuanAn(final Location myLocation, final int currentItem, final int nextItem) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataRoot = dataSnapshot;
                query(dataRoot, myLocation, currentItem, nextItem);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                homePresenter.getRestaurantFailure(databaseError.getMessage());
            }
        };

        if (dataRoot != null) {
            query(dataRoot, myLocation, currentItem, nextItem);
        } else {
            nodeRoot.addListenerForSingleValueEvent(valueEventListener);
        }
    }

    private void query(DataSnapshot dataSnapshot, Location myLocation, int currentItem, int nextItem) {
        final DataSnapshot snapQuanAn = dataSnapshot.child("quanans");
        int i = 0;
        for (DataSnapshot valueQuanAn : snapQuanAn.getChildren()) {
            Log.d(TAG, "QuanAn query: " + i);
            if (i == nextItem) {
                return;
            }
            if (i < currentItem) {
                i++;
                continue;
            }
            final QuanAnModel quanAnModel = valueQuanAn.getValue(QuanAnModel.class);
            quanAnModel.setMaquanan(valueQuanAn.getKey());
            Log.d(TAG, "QuanAn query: Tên quán ăn: " + quanAnModel.getTenquanan());

            //lấy danh sách hình ảnh theo mã quán ăn
            DataSnapshot snapshotHinhAnh = dataRoot.child("hinhanhquanans").child(quanAnModel.getMaquanan());
            List<String> imagePathList = new ArrayList<>();
            for (DataSnapshot valueHinhAnh : snapshotHinhAnh.getChildren()) {
                imagePathList.add(valueHinhAnh.getValue(String.class));
            }
            quanAnModel.setHinhanhquanan(imagePathList);
            Log.d(TAG, "QuanAn query: Số lượng hình ảnh = " + imagePathList.size());

            //lấy danh sách bình luận của quán
            //node bình luận
            DataSnapshot snapshotBinhLuan = dataRoot.child("binhluans").child(quanAnModel.getMaquanan());
            List<BinhLuanModel> binhLuanModelList = new ArrayList<>();
            int sohinhbl = 0;
            double tongdiem = 0;
            for (DataSnapshot valueBinhLuan : snapshotBinhLuan.getChildren()) {
                BinhLuanModel binhLuanModel = valueBinhLuan.getValue(BinhLuanModel.class);
                binhLuanModel.setMabinhluan(valueBinhLuan.getKey());
                ThanhVienModel thanhVienModel = dataRoot.child("thanhviens").child(binhLuanModel.getMauser()).getValue(ThanhVienModel.class);
                binhLuanModel.setThanhVienModel(thanhVienModel);

                List<String> hinhanhBinhLuanList = new ArrayList<>();
                DataSnapshot snapshotNodeHinhAnhBL = dataRoot.child("hinhanhbinhluans").child(binhLuanModel.getMabinhluan());
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
            if (binhLuanModelList.size() != 0) {
                quanAnModel.setDiemdanhgia(tongdiem / binhLuanModelList.size());
            }
            Log.d(TAG, "QuanAn query: Số lượng bình luận = " + binhLuanModelList.size());
            quanAnModel.setBinhLuanModelList(binhLuanModelList);
            Location location = new Location("");
            location.setLatitude(quanAnModel.getDiachi().getLatitude());
            location.setLongitude(quanAnModel.getDiachi().getLongitude());
            float khoangcach = myLocation.distanceTo(location) / 1000;
            quanAnModel.setKhoangcach(khoangcach);
            homePresenter.getRestaurantSuccess(quanAnModel);
            i++;
        }
    }

    public void search(final QuanAnModel quanAnModel, final Location myLocation) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataRoot = dataSnapshot;
                searchV2(quanAnModel,myLocation);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        if (dataRoot != null) {
            searchV2(quanAnModel,myLocation);
        } else {
            nodeRoot.addListenerForSingleValueEvent(valueEventListener);
        }

        /*key = name;
        list = new ArrayList<>();
        Query query = FirebaseDatabase.getInstance().getReference().child("quanans").startAt(key).endAt(key + "\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot value : dataSnapshot.getChildren()){
                    QuanAnModel quanAnModel = value.getValue(QuanAnModel.class);
                    quanAnModel.setMaquanan(value.getKey());
                    list.add(quanAnModel);
                }
                if (list.isEmpty()){
                    searchPresenter.onSearchSuccess(list);
                }else {
                    searchV2();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }
    List<QuanAnModel> list;
    private void searchV2(QuanAnModel quanAnModel, final Location myLocation) {
        list = new ArrayList<>();
        final String name = quanAnModel.getTenquanan().toLowerCase();
        final String theloai = quanAnModel.getTheloai();
        final String khuvuc = quanAnModel.getKhuvuc();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("quanans");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot value : dataSnapshot.getChildren()) {
                    QuanAnModel quanAnModel = value.getValue(QuanAnModel.class);
                    quanAnModel.setMaquanan(value.getKey());
                    if (quanAnModel.getTenquanan().toLowerCase().contains(name) || quanAnModel.getDiachi().getAddress().toLowerCase().contains(name)) {
                        if (quanAnModel.getKhuvuc().contains(khuvuc) && quanAnModel.getTheloai().contains(theloai)){
                            Log.d(TAG, TAG + "QuanAn query: Tên quán ăn: " + quanAnModel.getTenquanan());
                            Log.d(TAG, TAG + "onDataChange: ");
                            //lấy danh sách hình ảnh theo mã quán ăn
                            DataSnapshot snapshotHinhAnh = dataRoot.child("hinhanhquanans").child(quanAnModel.getMaquanan());
                            List<String> imagePathList = new ArrayList<>();
                            for (DataSnapshot valueHinhAnh : snapshotHinhAnh.getChildren()) {
                                imagePathList.add(valueHinhAnh.getValue(String.class));
                            }
                            quanAnModel.setHinhanhquanan(imagePathList);
                            Log.d(TAG, "QuanAn query: Số lượng hình ảnh = " + imagePathList.size());

                            //lấy danh sách bình luận của quán
                            //node bình luận
                            DataSnapshot snapshotBinhLuan = dataRoot.child("binhluans").child(quanAnModel.getMaquanan());
                            List<BinhLuanModel> binhLuanModelList = new ArrayList<>();
                            int sohinhbl = 0;
                            double tongdiem = 0;
                            for (DataSnapshot valueBinhLuan : snapshotBinhLuan.getChildren()) {
                                BinhLuanModel binhLuanModel = valueBinhLuan.getValue(BinhLuanModel.class);
                                binhLuanModel.setMabinhluan(valueBinhLuan.getKey());
                                ThanhVienModel thanhVienModel = dataRoot.child("thanhviens").child(binhLuanModel.getMauser()).getValue(ThanhVienModel.class);
                                binhLuanModel.setThanhVienModel(thanhVienModel);

                                List<String> hinhanhBinhLuanList = new ArrayList<>();
                                DataSnapshot snapshotNodeHinhAnhBL = dataRoot.child("hinhanhbinhluans").child(binhLuanModel.getMabinhluan());
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
                            if (binhLuanModelList.size() != 0) {
                                quanAnModel.setDiemdanhgia(tongdiem / binhLuanModelList.size());
                            }
                            Log.d(TAG, "QuanAn query: Số lượng bình luận = " + binhLuanModelList.size());
                            quanAnModel.setBinhLuanModelList(binhLuanModelList);
                            Location location = new Location("");
                            location.setLongitude(quanAnModel.getDiachi().getLongitude());
                            location.setLatitude(quanAnModel.getDiachi().getLatitude());
                            float khoangcach = myLocation.distanceTo(location) / 1000;
                            quanAnModel.setKhoangcach(khoangcach);
                            list.add(quanAnModel);
                        }
                    }
                }
                if (!list.isEmpty()) {
                    searchPresenter.onSearchSuccess(list);
                } else {
                    searchPresenter.onSearchFailure("");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public boolean isGiaohang() {
        return giaohang;
    }

    public void setGiaohang(boolean giaohang) {
        this.giaohang = giaohang;
    }

    public String getGiodongcua() {
        return giodongcua;
    }

    public void setGiodongcua(String giodongcua) {
        this.giodongcua = giodongcua;
    }

    public String getGiomocua() {
        return giomocua;
    }

    public void setGiomocua(String giomocua) {
        this.giomocua = giomocua;
    }

    public String getTenquanan() {
        return tenquanan;
    }

    public void setTenquanan(String tenquanan) {
        this.tenquanan = tenquanan;
    }

    public String getVideogioithieu() {
        return videogioithieu;
    }

    public void setVideogioithieu(String videogioithieu) {
        this.videogioithieu = videogioithieu;
    }

    public String getMaquanan() {
        return maquanan;
    }

    public void setMaquanan(String maquanan) {
        this.maquanan = maquanan;
    }

    public String getMaloaiquanan() {
        return theloai;
    }

    public void setMaloaiquanan(String theloai) {
        this.theloai = theloai;
    }

    public String getMakhuvuc() {
        return khuvuc;
    }

    public void setMakhuvuc(String khuvuc) {
        this.khuvuc = khuvuc;
    }

    public DiaChiModel getDiachi() {
        return diachi;
    }

    public void setDiachi(DiaChiModel diachi) {
        this.diachi = diachi;
    }

    public String getGioithieu() {
        return gioithieu;
    }

    public void setGioithieu(String gioithieu) {
        this.gioithieu = gioithieu;
    }

    public String getMathanhvien() {
        return mathanhvien;
    }

    public void setMathanhvien(String mathanhvien) {
        this.mathanhvien = mathanhvien;
    }

    public List<String> getTienich() {
        return tienich;
    }

    public void setTienich(List<String> tienich) {
        this.tienich = tienich;
    }

    public List<String> getHinhanhquanan() {
        return hinhanhquanan;
    }

    public void setHinhanhquanan(List<String> hinhanhquanan) {
        this.hinhanhquanan = hinhanhquanan;
    }

    public List<BinhLuanModel> getBinhLuanModelList() {
        return binhLuanModelList;
    }

    public void setBinhLuanModelList(List<BinhLuanModel> binhLuanModelList) {
        this.binhLuanModelList = binhLuanModelList;
    }

    public List<ThucDonModel> getThucDons() {
        return thucDons;
    }

    public void setThucDons(List<ThucDonModel> thucDons) {
        this.thucDons = thucDons;
    }

    public long getGiatoida() {
        return giatoida;
    }

    public void setGiatoida(long giatoida) {
        this.giatoida = giatoida;
    }

    public long getGiatoithieu() {
        return giatoithieu;
    }

    public void setGiatoithieu(long giatoithieu) {
        this.giatoithieu = giatoithieu;
    }

    public long getLuotluu() {
        return luotluu;
    }

    public void setLuotluu(long luotluu) {
        this.luotluu = luotluu;
    }

    public String getTheloai() {
        return theloai;
    }

    public void setTheloai(String theloai) {
        this.theloai = theloai;
    }

    public String getKhuvuc() {
        return khuvuc;
    }

    public void setKhuvuc(String khuvuc) {
        this.khuvuc = khuvuc;
    }

    public float getKhoangcach() {
        return khoangcach;
    }

    public void setKhoangcach(float khoangcach) {
        this.khoangcach = khoangcach;
    }

    public int getSohinhbinhluan() {
        return sohinhbinhluan;
    }

    public void setSohinhbinhluan(int sohinhbinhluan) {
        this.sohinhbinhluan = sohinhbinhluan;
    }

    public double getDiemdanhgia() {
        return diemdanhgia;
    }

    public void setDiemdanhgia(double diemdanhgia) {
        this.diemdanhgia = diemdanhgia;
    }

    public String getSodienthoai() {
        return sodienthoai;
    }

    public void setSodienthoai(String sodienthoai) {
        this.sodienthoai = sodienthoai;
    }

    public String getChiduong() {
        return chiduong;
    }

    public void setChiduong(String chiduong) {
        this.chiduong = chiduong;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (giaohang ? 1 : 0));
        dest.writeString(giodongcua);
        dest.writeString(giomocua);
        dest.writeString(tenquanan);
        dest.writeString(videogioithieu);
        dest.writeString(maquanan);
        dest.writeString(theloai);
        dest.writeString(khuvuc);
        dest.writeString(gioithieu);
        dest.writeString(mathanhvien);
        dest.writeString(sodienthoai);
        dest.writeString(chiduong);
        dest.writeParcelable(diachi, flags);
        dest.writeStringList(tienich);
        dest.writeStringList(hinhanhquanan);
        dest.writeTypedList(binhLuanModelList);
        dest.writeTypedList(thucDons);
        dest.writeLong(giatoida);
        dest.writeLong(giatoithieu);
        dest.writeLong(luotluu);
        dest.writeFloat(khoangcach);
        dest.writeInt(sohinhbinhluan);
        dest.writeDouble(diemdanhgia);
    }

    public static Comparator<QuanAnModel> comparator = new Comparator<QuanAnModel>() {
        @Override
        public int compare(QuanAnModel o1, QuanAnModel o2) {
            return o1.getTenquanan().compareTo(o2.getTenquanan());
        }
    };
}
