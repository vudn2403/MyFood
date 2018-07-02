package com.vudn.myfood.view.order;

import com.vudn.myfood.model.menu.ThucDonModel;

import java.util.List;

public interface OrderView {
    void onSuccess(List<ThucDonModel> thucDonModelList);
}
