package com.vudn.myfood.adapter.order;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vudn.myfood.R;
import com.vudn.myfood.model.order.ChiTietDonHangModel;
import com.vudn.myfood.model.quanans.DatMon;
import com.vudn.myfood.model.restaurant.MonAnModel;

import java.util.ArrayList;
import java.util.List;


public class AdapterMonAn extends RecyclerView.Adapter<AdapterMonAn.HolderMonAn> {

    Context context;
    List<MonAnModel> monAnModelList;
    LayoutInflater inflater;
    public static List<ChiTietDonHangModel> chiTietDonHangModelList;

    public static List<DatMon> datMonList = new ArrayList<>();
    OnSoLuongMonAnThayDoiListener onSoLuongMonAnThayDoiListener;

    public AdapterMonAn(Context context, List<MonAnModel> monAnModelList, OnSoLuongMonAnThayDoiListener onSoLuongMonAnThayDoiListener) {
        this.context = context;
        this.monAnModelList = monAnModelList;
        inflater = LayoutInflater.from(context);
        chiTietDonHangModelList = new ArrayList<>();
        setOnSoLuongMonAnThayDoiListener(onSoLuongMonAnThayDoiListener);
    }

    public void setOnSoLuongMonAnThayDoiListener(OnSoLuongMonAnThayDoiListener onSoLuongMonAnThayDoiListener) {
        this.onSoLuongMonAnThayDoiListener = onSoLuongMonAnThayDoiListener;
    }

    @Override
    public HolderMonAn onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_layout_monan, parent, false);
        return new HolderMonAn(view);
    }

    @Override
    public void onBindViewHolder(final HolderMonAn holder, int position) {
        final MonAnModel monAnModel = monAnModelList.get(position);
        holder.txtTenMonAn.setText(monAnModel.getTenmon());
        String gia = String.format("%,d", monAnModel.getGiatien());
        holder.txtGia.setText(gia.replace(",", ".") + "đ");
        holder.txtSoLuong.setTag(0);
        holder.imgTangSoLuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dem = Integer.parseInt(holder.txtSoLuong.getTag().toString());
                dem++;
                holder.txtSoLuong.setText(dem + "");
                holder.txtSoLuong.setTag(dem);

                ChiTietDonHangModel tag = (ChiTietDonHangModel) holder.imgGiamSoLuong.getTag();
                if (tag != null) {
                    chiTietDonHangModelList.remove(tag);
                }

                /*DatMon datMonTag = (DatMon) holder.imgGiamSoLuong.getTag();
                if (datMonTag != null) {
                    AdapterMonAn.datMonList.remove(datMonTag);
                }*/

                ChiTietDonHangModel chiTietDonHangModel = new ChiTietDonHangModel();
                chiTietDonHangModel.setMonan(monAnModel);
                chiTietDonHangModel.setSoluong(dem);

                holder.imgGiamSoLuong.setTag(chiTietDonHangModel);
                chiTietDonHangModelList.add(chiTietDonHangModel);
                //Toast.makeText(context, "Số lượng món ăn trong giỏ hàng " + chiTietDonHangModelList.size(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(context, "Tên món ăn: " + chiTietDonHangModel.getMonan().getTenmon() +
                //        "_ số lượng: " + chiTietDonHangModel.getSoluong(), Toast.LENGTH_SHORT).show();
                /*DatMon datMon = new DatMon();
                datMon.setSoLuong(dem);
                datMon.setTenMonAn(monAnModel.getTenmon());

                holder.imgGiamSoLuong.setTag(datMon);
                AdapterMonAn.datMonList.add(datMon);*/
                onSoLuongMonAnThayDoiListener.onSoLuongThayDoi(chiTietDonHangModelList);
            }
        });
        if (monAnModel.getHinhanh() != null && !monAnModel.getHinhanh().isEmpty()) {
            Glide.with(context).load(monAnModel.getHinhanh()).into(holder.imgHinhMonAn);
        }else {
            Glide.with(context).load(R.drawable.background).into(holder.imgHinhMonAn);
        }
        holder.imgGiamSoLuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dem = Integer.parseInt(holder.txtSoLuong.getTag().toString());
                if (dem > 0) {
                    dem--;
                    holder.txtSoLuong.setText(dem + "");
                    holder.txtSoLuong.setTag(dem);
                    if (dem == 0) {
                        ChiTietDonHangModel tag = (ChiTietDonHangModel) v.getTag();
                        chiTietDonHangModelList.remove(tag);
                        /*DatMon datMon = (DatMon) v.getTag();
                        AdapterMonAn.datMonList.remove(datMon);*/
                    } else {
                        ChiTietDonHangModel tag = (ChiTietDonHangModel) v.getTag();
                        if (tag != null) {
                            chiTietDonHangModelList.remove(tag);
                        }

                /*DatMon datMonTag = (DatMon) holder.imgGiamSoLuong.getTag();
                if (datMonTag != null) {
                    AdapterMonAn.datMonList.remove(datMonTag);
                }*/

                        ChiTietDonHangModel chiTietDonHangModel = new ChiTietDonHangModel();
                        chiTietDonHangModel.setMonan(monAnModel);
                        chiTietDonHangModel.setSoluong(dem);

                        holder.imgGiamSoLuong.setTag(chiTietDonHangModel);
                        chiTietDonHangModelList.add(chiTietDonHangModel);
                        //Toast.makeText(context, "Số lượng món ăn trong giỏ hàng " + chiTietDonHangModelList.size(), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(context, "Tên món ăn: " + chiTietDonHangModel.getMonan().getTenmon() +
                        //        "_ số lượng: " + chiTietDonHangModel.getSoluong(), Toast.LENGTH_SHORT).show();
                    }
                }
                onSoLuongMonAnThayDoiListener.onSoLuongThayDoi(chiTietDonHangModelList);

            }
        });
    }

    @Override
    public int getItemCount() {
        return monAnModelList.size();
    }

    public class HolderMonAn extends RecyclerView.ViewHolder {
        TextView txtTenMonAn, txtGia, txtSoLuong;
        ImageView imgGiamSoLuong, imgTangSoLuong;
        ImageView imgHinhMonAn;

        public HolderMonAn(View itemView) {
            super(itemView);
            txtGia = itemView.findViewById(R.id.txt_gia);
            txtTenMonAn = (TextView) itemView.findViewById(R.id.txtTenMonAn);
            txtSoLuong = (TextView) itemView.findViewById(R.id.txtSoLuong);
            imgGiamSoLuong = (ImageView) itemView.findViewById(R.id.imgGiamSoLuong);
            imgTangSoLuong = (ImageView) itemView.findViewById(R.id.imgTangSoLuong);
            imgHinhMonAn = itemView.findViewById(R.id.img_monan);
        }
    }

    public interface OnSoLuongMonAnThayDoiListener {
        void onSoLuongThayDoi(List<ChiTietDonHangModel> chiTietDonHangModelList);
    }
}
