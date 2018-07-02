package com.vudn.myfood.view.order;

import com.vudn.myfood.model.restaurant.ThucDonModel;

import java.util.List;

public interface DatGiaoHangView {
    void onGetThucDonSuccess(List<ThucDonModel> thucDonModelList);
    void onGetThucDOnFailure(String error);
}
