package com.vudn.myfood.presenter.main;

import android.location.Location;

import com.vudn.myfood.model.restaurant.QuanAnModel;
import com.vudn.myfood.presenter.main.HomePresenter;
import com.vudn.myfood.view.main.HomeView;

public class HomePresenterImpl implements HomePresenter {
    private HomeView homeView;
    private QuanAnModel quanAnModel;

    public HomePresenterImpl(HomeView homeView) {
        this.homeView = homeView;
        quanAnModel = new QuanAnModel(this);
    }


    @Override
    public void getRestaurant(Location myLocation, int currentItem, int nextItem) {
        quanAnModel.getQuanAn(myLocation,currentItem,nextItem);
    }

    @Override
    public void getRestaurantSuccess(QuanAnModel quanAnModel) {
        homeView.onGetRestaurantSuccess(quanAnModel);
    }



    @Override
    public void getRestaurantFailure(String error) {
        homeView.onGetRestaurantFailure(error);
    }
}
