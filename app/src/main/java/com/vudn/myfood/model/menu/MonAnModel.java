package com.vudn.myfood.model.menu;

import android.os.Parcel;
import android.os.Parcelable;

public class MonAnModel implements Parcelable{
    private String mamon;
    private long giatien;
    private String hinhanh;
    private String tenmon;

    public MonAnModel() {
    }

    public MonAnModel(String mamon, long giatien, String hinhanh, String tenmon) {
        this.mamon = mamon;
        this.giatien = giatien;
        this.hinhanh = hinhanh;
        this.tenmon = tenmon;
    }

    protected MonAnModel(Parcel in) {
        mamon = in.readString();
        giatien = in.readLong();
        hinhanh = in.readString();
        tenmon = in.readString();
    }

    public static final Creator<MonAnModel> CREATOR = new Creator<MonAnModel>() {
        @Override
        public MonAnModel createFromParcel(Parcel in) {
            return new MonAnModel(in);
        }

        @Override
        public MonAnModel[] newArray(int size) {
            return new MonAnModel[size];
        }
    };

    public String getMamon() {
        return mamon;
    }

    public void setMamon(String mamon) {
        this.mamon = mamon;
    }

    public long getGiatien() {
        return giatien;
    }

    public void setGiatien(long giatien) {
        this.giatien = giatien;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public String getTenmon() {
        return tenmon;
    }

    public void setTenmon(String tenmon) {
        this.tenmon = tenmon;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mamon);
        dest.writeLong(giatien);
        dest.writeString(hinhanh);
        dest.writeString(tenmon);
    }
}
