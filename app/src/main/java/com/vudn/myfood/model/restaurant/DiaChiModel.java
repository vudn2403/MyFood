package com.vudn.myfood.model.restaurant;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class DiaChiModel implements Parcelable{
    private String address;
    private double latitude;
    private double longitude;

    public DiaChiModel() {
    }


    protected DiaChiModel(Parcel in) {
        address = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<DiaChiModel> CREATOR = new Creator<DiaChiModel>() {
        @Override
        public DiaChiModel createFromParcel(Parcel in) {
            return new DiaChiModel(in);
        }

        @Override
        public DiaChiModel[] newArray(int size) {
            return new DiaChiModel[size];
        }
    };

    /*public Location getLocationQuanAn(){
        Location location = new Location("");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }*/

    /*public LatLng getLagLngQuanAn(){
        return new LatLng(latitude, longitude);
    }*/

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }
}
