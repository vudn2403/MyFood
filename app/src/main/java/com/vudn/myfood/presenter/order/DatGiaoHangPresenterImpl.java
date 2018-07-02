package com.vudn.myfood.presenter.order;

import com.vudn.myfood.model.restaurant.ThucDonModel;
import com.vudn.myfood.view.order.DatGiaoHangView;

import java.util.List;

public class DatGiaoHangPresenterImpl implements DatGiaoHangPresenter {
    private DatGiaoHangView datGiaoHangView;
    private ThucDonModel thucDonModel;

    public DatGiaoHangPresenterImpl(DatGiaoHangView datGiaoHangView) {
        this.datGiaoHangView = datGiaoHangView;
        thucDonModel = new ThucDonModel(this);
    }

    @Override
    public void getThucDon(String maquanan) {
        thucDonModel.getThucDon(maquanan);
    }

    @Override
    public void getThucDonSuccess(List<ThucDonModel> thucDonModelList) {
        datGiaoHangView.onGetThucDonSuccess(thucDonModelList);
    }

    @Override
    public void getThucDonFailure(String error) {
        datGiaoHangView.onGetThucDOnFailure(error);
    }
}
