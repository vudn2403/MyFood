package com.vudn.myfood.presenter.main;

import android.location.Location;

import com.vudn.myfood.model.restaurant.QuanAnModel;

public interface HomePresenter {
    void getRestaurant(Location myLocation, int currentItem, int nextItem);
    void getRestaurantSuccess(QuanAnModel quanAnModel);
    void getRestaurantFailure(String error);
}
