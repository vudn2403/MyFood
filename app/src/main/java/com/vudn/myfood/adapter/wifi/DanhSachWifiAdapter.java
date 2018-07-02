package com.vudn.myfood.adapter.wifi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vudn.myfood.R;
import com.vudn.myfood.model.wifi.WifiQuanAnModel;

import java.util.List;


public class DanhSachWifiAdapter extends RecyclerView.Adapter<DanhSachWifiAdapter.ViewHolderWifi> {
    private Context context;
    private int resource;
    private List<WifiQuanAnModel> wifiQuanAnModelList;
    private LayoutInflater inflater;

    public DanhSachWifiAdapter(Context context, int resource, List<WifiQuanAnModel> wifiQuanAnModelList) {
        this.context = context;
        this.resource = resource;
        this.wifiQuanAnModelList = wifiQuanAnModelList;
        try {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public class ViewHolderWifi extends RecyclerView.ViewHolder {

        TextView txtTenWifi, txtMatKhauWifi, txtNgayDang;

        public ViewHolderWifi(View itemView) {
            super(itemView);

            txtTenWifi = itemView.findViewById(R.id.txtTenWifi);
            txtMatKhauWifi = itemView.findViewById(R.id.txtMatKhauWifi);
            txtNgayDang = itemView.findViewById(R.id.txtNgayDangWifi);
        }
    }

    @Override
    public DanhSachWifiAdapter.ViewHolderWifi onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(resource, parent, false);
        return new ViewHolderWifi(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DanhSachWifiAdapter.ViewHolderWifi holder, int position) {
        WifiQuanAnModel wifiQuanAnModel = wifiQuanAnModelList.get(position);

        holder.txtTenWifi.setText(wifiQuanAnModel.getTen());
        holder.txtMatKhauWifi.setText(wifiQuanAnModel.getMatkhau());
        holder.txtNgayDang.setText(wifiQuanAnModel.getNgaydang());

    }

    @Override
    public int getItemCount() {
        return wifiQuanAnModelList.size();
    }


}
