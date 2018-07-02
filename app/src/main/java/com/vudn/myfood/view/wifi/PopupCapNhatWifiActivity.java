package com.vudn.myfood.view.wifi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vudn.myfood.R;
import com.vudn.myfood.model.wifi.WifiQuanAnModel;
import com.vudn.myfood.presenter.other.CapNhatWifiController;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PopupCapNhatWifiActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edTenWifi, edMatKhauWifi;
    Button btnDongY;

    CapNhatWifiController capNhatWifiController;
    String maquanan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_popup_catnhatwifi);

        maquanan = getIntent().getStringExtra("maquanan");

        edMatKhauWifi = (EditText) findViewById(R.id.edNhatKhauWifi);
        edTenWifi = (EditText) findViewById(R.id.edTenWifi);
        btnDongY = (Button) findViewById(R.id.btnDongYCatNhatWifi);

        btnDongY.setOnClickListener(this);

        capNhatWifiController = new CapNhatWifiController(this);
    }

    @Override
    public void onClick(View v) {
        String tenwifi = edTenWifi.getText().toString();
        String matkhauwifi = edMatKhauWifi.getText().toString();

        if(tenwifi.trim().length() > 0 && matkhauwifi.trim().length() > 0){

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String ngaydang = simpleDateFormat.format(calendar.getTime());

            WifiQuanAnModel wifiQuanAnModel = new WifiQuanAnModel();
            wifiQuanAnModel.setTen(tenwifi);
            wifiQuanAnModel.setMatkhau(matkhauwifi);
            wifiQuanAnModel.setNgaydang(ngaydang);

            capNhatWifiController.ThemWifi(this,wifiQuanAnModel,maquanan);
        }else{
            Toast.makeText(this,getResources().getString(R.string.loithemwifi), Toast.LENGTH_SHORT).show();
        }

    }
}
