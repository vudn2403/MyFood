package com.vudn.myfood.presenter.order;

import com.vudn.myfood.model.restaurant.ThucDonModel;

import java.util.List;

public interface DatGiaoHangPresenter {
    void getThucDon(String maquanan);
    void getThucDonSuccess(List<ThucDonModel> thucDonModelList);
    void getThucDonFailure(String error);
}
