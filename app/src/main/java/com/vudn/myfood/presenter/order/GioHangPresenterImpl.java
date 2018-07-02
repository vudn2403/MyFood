package com.vudn.myfood.presenter.order;

import com.vudn.myfood.model.order.DonHangModel;
import com.vudn.myfood.view.order.GioHangView;

public class GioHangPresenterImpl implements GioHangPresenter {
    private GioHangView gioHangView;
    private DonHangModel donHangModel;

    public GioHangPresenterImpl(GioHangView gioHangView) {
        this.gioHangView = gioHangView;
        donHangModel = new DonHangModel(this);
    }

    @Override
    public void themDonHang(DonHangModel donHangModel) {
        this.donHangModel.themDonHang(donHangModel);
    }

    @Override
    public void themDonHangSuccess() {
        gioHangView.onDatHangSuccess();
    }

    @Override
    public void themDonHangFailure(String error) {
        gioHangView.onDatHangFailure(error);
    }
}
