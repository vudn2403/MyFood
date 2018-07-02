package com.vudn.myfood.presenter.search;

import android.location.Location;

import com.vudn.myfood.model.restaurant.QuanAnModel;

import java.util.List;

public interface SearchPresenter {
    void search(QuanAnModel quanAnModel, Location myLocation);
    void onSearchSuccess(List<QuanAnModel> quanAnModelList);
    void onSearchFailure(String error);
}
