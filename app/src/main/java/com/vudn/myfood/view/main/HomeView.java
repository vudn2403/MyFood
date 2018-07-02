package com.vudn.myfood.view.main;

import com.vudn.myfood.model.restaurant.QuanAnModel;

public interface HomeView {
    void onGetRestaurantSuccess(QuanAnModel quanAnModel);
    void onGetRestaurantFailure(String error);
}
