package com.vudn.myfood.presenter.order;

import com.vudn.myfood.model.order.DonHangModel;

public interface GioHangPresenter {
    void themDonHang(DonHangModel donHangModel);
    void themDonHangSuccess();
    void themDonHangFailure(String error);
}
