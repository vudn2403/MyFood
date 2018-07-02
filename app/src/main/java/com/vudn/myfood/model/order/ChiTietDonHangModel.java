package com.vudn.myfood.model.order;

import android.os.Parcel;
import android.os.Parcelable;

import com.vudn.myfood.model.menu.MonAnModel;

public class ChiTietDonHangModel implements Parcelable {
    private MonAnModel monan;
    private int soluong;

    public ChiTietDonHangModel() {
    }

    public ChiTietDonHangModel(MonAnModel monAnModel, int soluong) {
        this.monan = monAnModel;
        this.soluong = soluong;
    }

    protected ChiTietDonHangModel(Parcel in) {
        monan = in.readParcelable(MonAnModel.class.getClassLoader());
        soluong = in.readInt();
    }

    public static final Creator<ChiTietDonHangModel> CREATOR = new Creator<ChiTietDonHangModel>() {
        @Override
        public ChiTietDonHangModel createFromParcel(Parcel in) {
            return new ChiTietDonHangModel(in);
        }

        @Override
        public ChiTietDonHangModel[] newArray(int size) {
            return new ChiTietDonHangModel[size];
        }
    };

    public MonAnModel getMonan() {
        return monan;
    }

    public void setMonan(MonAnModel monan) {
        this.monan = monan;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(monan, flags);
        dest.writeInt(soluong);
    }
}
