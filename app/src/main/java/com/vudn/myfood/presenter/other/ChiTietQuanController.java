package com.vudn.myfood.presenter.other;

import android.widget.TextView;


import com.vudn.myfood.model.wifi.WifiQuanAnModel;

import java.util.ArrayList;
import java.util.List;


public class ChiTietQuanController {

    private WifiQuanAnModel wifiQuanAnModel;
    private List<WifiQuanAnModel> wifiQuanAnModelList;

    public ChiTietQuanController(){
        wifiQuanAnModel = new WifiQuanAnModel();
        wifiQuanAnModelList = new ArrayList<>();
    }

    public void HienThiDanhSachWifiQuanAn(String maquanan, final TextView txtTenWifi, final TextView txtMatKhauWifi, final TextView txtNgayDangWifi){

        ChiTietQuanAnInterface chiTietQuanAnInterface = new ChiTietQuanAnInterface() {
            @Override
            public void HienThiDanhSachWifi(WifiQuanAnModel wifiQuanAnModel) {
                wifiQuanAnModelList.add(wifiQuanAnModel);
                txtTenWifi.setText(wifiQuanAnModel.getTen());
                txtMatKhauWifi.setText(wifiQuanAnModel.getMatkhau());
                txtNgayDangWifi.setText(wifiQuanAnModel.getNgaydang());
            }
        };

        wifiQuanAnModel.LayDanhSachWifiQuanAn(maquanan,chiTietQuanAnInterface);
    }
}
