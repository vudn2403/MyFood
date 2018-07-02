package com.vudn.myfood.adapter.order;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vudn.myfood.R;
import com.vudn.myfood.model.order.ChiTietDonHangModel;

import java.util.List;

public class ChiTietDonHangAdapter extends RecyclerView.Adapter<ChiTietDonHangAdapter.ViewHolder>{
    Context context;
    List<ChiTietDonHangModel> chiTietDonHangModelList;
    LayoutInflater inflater;

    public ChiTietDonHangAdapter(Context context, List<ChiTietDonHangModel> chiTietDonHangModelList) {
        this.context = context;
        this.chiTietDonHangModelList = chiTietDonHangModelList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_chi_tiet_don_hang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChiTietDonHangModel chiTietDonHangModel = chiTietDonHangModelList.get(position);
        holder.txtMonAn.setText(chiTietDonHangModel.getMonan().getTenmon());
        holder.txtSoluong.setText(String.format("%,d",chiTietDonHangModel.getSoluong()));
        long thanhtien = chiTietDonHangModel.getMonan().getGiatien() * chiTietDonHangModel.getSoluong();
        holder.txtThanhTien.setText(String.format("%,d", thanhtien).replace(",", ".") + "đ");
    }

    @Override
    public int getItemCount() {
        return chiTietDonHangModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMonAn;
        TextView txtSoluong;
        TextView txtThanhTien;

        public ViewHolder(View itemView) {
            super(itemView);
            txtMonAn = itemView.findViewById(R.id.txt_monan);
            txtSoluong = itemView.findViewById(R.id.txt_soluong);
            txtThanhTien = itemView.findViewById(R.id.txt_thanhtien);
        }
    }
}
