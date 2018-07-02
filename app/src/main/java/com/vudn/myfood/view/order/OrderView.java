package com.vudn.myfood.view.order;

import com.vudn.myfood.model.restaurant.ThucDonModel;

import java.util.List;

public interface OrderView {
    void onSuccess(List<ThucDonModel> thucDonModelList);
}
