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
import com.vudn.myfood.model.order.DonHangModel;

import java.util.List;

public class DanhSachDonHangAdapter extends RecyclerView.Adapter<DanhSachDonHangAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    List<DonHangModel> donHangModelList;
    OnDonHangClickListener onDonHangClickListener;

    public DanhSachDonHangAdapter(Context context, List<DonHangModel> donHangModelList, OnDonHangClickListener onDonHangClickListener) {
        this.context = context;
        this.donHangModelList = donHangModelList;
        inflater = LayoutInflater.from(context);
        setOnDonHangClickListener(onDonHangClickListener);
    }

    public void setOnDonHangClickListener(OnDonHangClickListener onDonHangClickListener) {
        this.onDonHangClickListener = onDonHangClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_don_hang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        DonHangModel donHangModel = donHangModelList.get(position);
        holder.txtId.setText(donHangModel.getMadonhang());
        long tongTien = 0;
        for (ChiTietDonHangModel chiTietDonHangModel : donHangModel.getChitietdonhang()) {
            tongTien += chiTietDonHangModel.getMonan().getGiatien() * chiTietDonHangModel.getSoluong();
        }
        holder.txtPaymentAmount.setText(String.format("%,d", tongTien).replace(",", ".") + " VNĐ");
        holder.txtRestaurantName.setText(donHangModel.getTenquanan());

        holder.txtOrderTime.setText(donHangModel.getNgaydathang());
        holder.txtStatus.setText(donHangModel.getTrangthai());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDonHangClickListener.onClick(holder.getAdapterPosition());
            }
        });
    }

    public DonHangModel getDonHang(int position) {
        return donHangModelList.get(position);
    }

    @Override
    public int getItemCount() {
        return donHangModelList == null ? 0 : donHangModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtId;
        TextView txtPaymentAmount;
        TextView txtRestaurantName;
        TextView txtOrderTime;
        TextView txtStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            txtId = itemView.findViewById(R.id.txt_id);
            txtPaymentAmount = itemView.findViewById(R.id.txt_payment_amount);
            txtRestaurantName = itemView.findViewById(R.id.txt_restaurant_name);
            txtOrderTime = itemView.findViewById(R.id.txt_order_time);
            txtStatus = itemView.findViewById(R.id.txt_status);
        }
    }

    public interface OnDonHangClickListener {
        void onClick(int position);
    }
}
