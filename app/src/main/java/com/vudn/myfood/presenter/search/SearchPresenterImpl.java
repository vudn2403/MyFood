package com.vudn.myfood.presenter.search;

import android.location.Location;

import com.vudn.myfood.model.restaurant.QuanAnModel;
import com.vudn.myfood.view.search.MySearchView;

import java.util.List;

public class SearchPresenterImpl implements SearchPresenter {
    private MySearchView mySearchView;
    private QuanAnModel quanAnModel;
    public SearchPresenterImpl(MySearchView mySearchView) {
        this.mySearchView = mySearchView;
        quanAnModel = new QuanAnModel(this);
    }


    @Override
    public void search(QuanAnModel quanAnModel, Location myLocation) {
        this.quanAnModel.search(quanAnModel, myLocation);
    }

    @Override
    public void onSearchSuccess(List<QuanAnModel> quanAnModelList) {
        mySearchView.onSearchSuccess(quanAnModelList);
    }

    @Override
    public void onSearchFailure(String error) {
        mySearchView.onSearchFailure(error);
    }
}
