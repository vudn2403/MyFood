package com.vudn.myfood.model.util;

import android.os.Parcel;
import android.os.Parcelable;

public class TienIchModel implements Parcelable{
    private String hinhtienich,tentienich;
    private String maTienIch;

    protected TienIchModel(Parcel in) {
        hinhtienich = in.readString();
        tentienich = in.readString();
        maTienIch = in.readString();
    }

    public static final Creator<TienIchModel> CREATOR = new Creator<TienIchModel>() {
        @Override
        public TienIchModel createFromParcel(Parcel in) {
            return new TienIchModel(in);
        }

        @Override
        public TienIchModel[] newArray(int size) {
            return new TienIchModel[size];
        }
    };

    public String getMaTienIch() {
        return maTienIch;
    }

    public void setMaTienIch(String maTienIch) {
        this.maTienIch = maTienIch;
    }


    public TienIchModel(){

    }

    public String getHinhtienich() {
        return hinhtienich;
    }

    public void setHinhtienich(String hinhtienich) {
        this.hinhtienich = hinhtienich;
    }

    public String getTentienich() {
        return tentienich;
    }

    public void setTentienich(String tentienich) {
        this.tentienich = tentienich;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hinhtienich);
        dest.writeString(tentienich);
        dest.writeString(maTienIch);
    }
}
