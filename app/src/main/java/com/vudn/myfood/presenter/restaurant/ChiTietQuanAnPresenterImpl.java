package com.vudn.myfood.presenter.restaurant;

import com.vudn.myfood.model.restaurant.BinhLuanModel;
import com.vudn.myfood.model.restaurant.ThucDonModel;
import com.vudn.myfood.view.restaurant.ChiTietQuanAnView;

import java.util.List;

public class ChiTietQuanAnPresenterImpl implements ChiTietQuanAnPresenter {
    ChiTietQuanAnView chiTietQuanAnView;
    ThucDonModel thucDonModel;
    BinhLuanModel binhLuanModel;

    public ChiTietQuanAnPresenterImpl(ChiTietQuanAnView chiTietQuanAnView) {
        this.chiTietQuanAnView = chiTietQuanAnView;
        thucDonModel = new ThucDonModel(this);
        binhLuanModel = new BinhLuanModel(this);
    }

    @Override
    public void getBinhLuan(String maquanan) {
        binhLuanModel.getBinhLuan(maquanan);
    }

    @Override
    public void getBinhLuanSuccess(List<BinhLuanModel> binhLuanModelList) {
        chiTietQuanAnView.onGetBinhLuanSuccess(binhLuanModelList);
    }

    @Override
    public void getThucDon(String maquanan) {
        thucDonModel.getThucDon(maquanan);
    }

    @Override
    public void getThucDonSuccess(List<ThucDonModel> thucDonModelList) {
        chiTietQuanAnView.onGetThucDonSuccess(thucDonModelList);
}

    @Override
    public void getThucDonFailure(String error) {
        chiTietQuanAnView.onGetThucDonFailure(error);
    }
}
