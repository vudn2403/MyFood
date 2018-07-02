package com.vudn.myfood.presenter.restaurant;

import com.vudn.myfood.model.comment.BinhLuanModel;
import com.vudn.myfood.model.menu.ThucDonModel;
import com.vudn.myfood.view.restaurant.ChiTietQuanAnView;

import java.util.List;

public class ChiTietQuanAnPresenterImpl implements ChiTietQuanAnPresenter {
    private ChiTietQuanAnView chiTietQuanAnView;
    private ThucDonModel thucDonModel;
    private BinhLuanModel binhLuanModel;

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
