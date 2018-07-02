package com.vudn.myfood.adapter.restaurant;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vudn.myfood.R;
import com.vudn.myfood.model.restaurant.WifiQuanAnModel;

import java.util.List;


public class AdapterDanhSachWifi extends RecyclerView.Adapter<AdapterDanhSachWifi.ViewHolderWifi> {
    Context context;
    int resource;
    List<WifiQuanAnModel> wifiQuanAnModelList;

    public AdapterDanhSachWifi(Context context, int resource, List<WifiQuanAnModel> wifiQuanAnModelList) {
        this.context = context;
        this.resource = resource;
        this.wifiQuanAnModelList = wifiQuanAnModelList;
    }

    public class ViewHolderWifi extends RecyclerView.ViewHolder {

        TextView txtTenWifi, txtMatKhauWifi, txtNgayDang;

        public ViewHolderWifi(View itemView) {
            super(itemView);

            txtTenWifi = (TextView) itemView.findViewById(R.id.txtTenWifi);
            txtMatKhauWifi = (TextView) itemView.findViewById(R.id.txtMatKhauWifi);
            txtNgayDang = (TextView) itemView.findViewById(R.id.txtNgayDangWifi);
        }
    }

    @Override
    public AdapterDanhSachWifi.ViewHolderWifi onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(resource, parent, false);
        ViewHolderWifi viewHolderWifi = new ViewHolderWifi(view);

        return viewHolderWifi;
    }

    @Override
    public void onBindViewHolder(AdapterDanhSachWifi.ViewHolderWifi holder, int position) {
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
