package com.vudn.myfood.model.address;

public class MyAddress {
    private String mAddress;
    private String mCity;
    private String mState;
    private String mCountry;
    private String mPostalCode;
    private String mKnownName;

    public MyAddress(){

    }

    public MyAddress(String mAddress, String mCity, String mState, String mCountry, String mPostalCode, String mKnownName) {
        this.mAddress = mAddress;
        this.mCity = mCity;
        this.mState = mState;
        this.mCountry = mCountry;
        this.mPostalCode = mPostalCode;
        this.mKnownName = mKnownName;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public String getmState() {
        return mState;
    }

    public void setmState(String mState) {
        this.mState = mState;
    }

    public String getmCountry() {
        return mCountry;
    }

    public void setmCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    public String getmPostalCode() {
        return mPostalCode;
    }

    public void setmPostalCode(String mPostalCode) {
        this.mPostalCode = mPostalCode;
    }

    public String getmKnownName() {
        return mKnownName;
    }

    public void setmKnownName(String mKnownName) {
        this.mKnownName = mKnownName;
    }

    @Override
    public String toString() {
        return mAddress +" and "+ mCity +" and "+ mCountry
                +" and "+ mKnownName +" and "+ mPostalCode +" and "+ mState;
    }
}
