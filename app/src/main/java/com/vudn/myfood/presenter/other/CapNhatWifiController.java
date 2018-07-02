package com.vudn.myfood.presenter.other;

import android.content.Context;
import android.support.v7.widget.RecyclerView;


import com.vudn.myfood.R;
import com.vudn.myfood.adapter.wifi.DanhSachWifiAdapter;
import com.vudn.myfood.model.wifi.WifiQuanAnModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Binh on 5/16/17.
 */

public class CapNhatWifiController {
    private WifiQuanAnModel wifiQuanAnModel;
    private Context context;
    private List<WifiQuanAnModel> wifiQuanAnModelList;

    public CapNhatWifiController(Context context){
        wifiQuanAnModel = new WifiQuanAnModel();
        this.context = context;
    }

    public void HienThiDanhSachWifi(String maquanan, final RecyclerView recyclerView){

        wifiQuanAnModelList = new ArrayList<>();
        ChiTietQuanAnInterface chiTietQuanAnInterface = new ChiTietQuanAnInterface() {
            @Override
            public void HienThiDanhSachWifi(WifiQuanAnModel wifiQuanAnModel) {

                wifiQuanAnModelList.add(wifiQuanAnModel);
                DanhSachWifiAdapter adapterDanhSachWifi = new DanhSachWifiAdapter(context, R.layout.layout_wifi_chitietquanan,wifiQuanAnModelList);
                recyclerView.setAdapter(adapterDanhSachWifi);
                adapterDanhSachWifi.notifyDataSetChanged();
            }
        };

        wifiQuanAnModel.LayDanhSachWifiQuanAn(maquanan,chiTietQuanAnInterface);
    }

    public void ThemWifi(Context context, WifiQuanAnModel wifiQuanAnModel, String maquanan){
        wifiQuanAnModel.ThemWifiQuanAn(context,wifiQuanAnModel,maquanan);
    }
}
