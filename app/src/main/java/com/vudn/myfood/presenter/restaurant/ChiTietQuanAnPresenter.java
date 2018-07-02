package com.vudn.myfood.presenter.restaurant;

import com.vudn.myfood.model.restaurant.BinhLuanModel;
import com.vudn.myfood.model.restaurant.ThucDonModel;

import java.util.List;

public interface ChiTietQuanAnPresenter {
    void getBinhLuan(String maquanan);
    void getBinhLuanSuccess(List<BinhLuanModel> binhLuanModelList);
    void getThucDon(String maquanan);
    void getThucDonSuccess(List<ThucDonModel> thucDonModelList);
    void getThucDonFailure(String error);
}
