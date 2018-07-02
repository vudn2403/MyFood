package com.vudn.myfood.view.search;

import com.vudn.myfood.model.restaurant.QuanAnModel;

import java.util.List;

public interface MySearchView {
    void onSearchSuccess(List<QuanAnModel> quanAnModelList);
    void onSearchFailure(String error);
}
