package com.vudn.myfood.view.restaurant;

import com.vudn.myfood.model.restaurant.BinhLuanModel;
import com.vudn.myfood.model.restaurant.ThucDonModel;

import java.util.List;

public interface ChiTietQuanAnView {
    void onGetBinhLuanSuccess(List<BinhLuanModel> binhLuanModelList);
    void onGetThucDonSuccess(List<ThucDonModel> thucDonModelList);
    void onGetThucDonFailure(String error);
}
