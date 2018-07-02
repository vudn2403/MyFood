package com.vudn.myfood.model.restaurant;

import android.os.Parcel;
import android.os.Parcelable;

import com.vudn.myfood.model.menu.MonAnModel;

public class ThemThucDonModel implements Parcelable{
    private String mathucdon;
    private MonAnModel monAnModel;

    public ThemThucDonModel() {
    }

    protected ThemThucDonModel(Parcel in) {
        mathucdon = in.readString();
        monAnModel = in.readParcelable(MonAnModel.class.getClassLoader());
    }

    public static final Creator<ThemThucDonModel> CREATOR = new Creator<ThemThucDonModel>() {
        @Override
        public ThemThucDonModel createFromParcel(Parcel in) {
            return new ThemThucDonModel(in);
        }

        @Override
        public ThemThucDonModel[] newArray(int size) {
            return new ThemThucDonModel[size];
        }
    };

    public MonAnModel getMonAnModel() {
        return monAnModel;
    }

    public void setMonAnModel(MonAnModel monAnModel) {
        this.monAnModel = monAnModel;
    }

    public String getMathucdon() {
        return mathucdon;
    }

    public void setMathucdon(String mathucdon) {
        this.mathucdon = mathucdon;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mathucdon);
        dest.writeParcelable(monAnModel, flags);
    }
}
