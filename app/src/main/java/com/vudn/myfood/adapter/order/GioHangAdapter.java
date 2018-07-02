package com.vudn.myfood.adapter.order;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vudn.myfood.R;
import com.vudn.myfood.model.order.ChiTietDonHangModel;
import com.vudn.myfood.model.restaurant.MonAnModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.ViewHolder> {
    List<ChiTietDonHangModel> chiTietDonHangModelList;
    Context context;
    LayoutInflater inflater;
    OnCapNhatGioHangListener onCapNhatGioHangListener;

    public GioHangAdapter(List<ChiTietDonHangModel> chiTietDonHangModelList, Context context, OnCapNhatGioHangListener onCapNhatGioHangListener) {
        this.chiTietDonHangModelList = chiTietDonHangModelList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        setOnCapNhatGioHangListener(onCapNhatGioHangListener);
    }

    public void setOnCapNhatGioHangListener(OnCapNhatGioHangListener onCapNhatGioHangListener) {
        this.onCapNhatGioHangListener = onCapNhatGioHangListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_giohang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        ChiTietDonHangModel item = chiTietDonHangModelList.get(position);
        if (item.getMonan().getHinhanh() != null && !item.getMonan().getHinhanh().isEmpty()) {
            Glide.with(context).load(item.getMonan().getHinhanh()).into(holder.imageView);
        }else {
            Glide.with(context).load(R.drawable.background).into(holder.imageView);
        }
        Glide.with(context).load(item.getMonan().getHinhanh()).into(holder.imageView);
        holder.txtName.setText(item.getMonan().getTenmon());
        holder.txtGia.setText(String.format("%,d", item.getMonan().getGiatien()).replace(",", ".") + "đ");
        holder.txtSoLuong.setText(String.valueOf(item.getSoluong()));
        holder.txtSoLuong.setTag(item.getSoluong());
        holder.imgTangSoLuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dem = Integer.parseInt(holder.txtSoLuong.getTag().toString());
                dem ++;
                chiTietDonHangModelList.get(holder.getAdapterPosition()).setSoluong(dem);
                holder.txtSoLuong.setText(dem+"");
                holder.txtSoLuong.setTag(dem);
                onCapNhatGioHangListener.onCapNhat();
            }
        });

        holder.imgGiamSoLuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dem = Integer.parseInt(holder.txtSoLuong.getTag().toString());
                if (dem > 0) {
                    dem--;
                    holder.txtSoLuong.setText(dem + "");
                    holder.txtSoLuong.setTag(dem);
                    if (dem == 0) {
                        removeItem(holder.getAdapterPosition());
                    } else {
                        chiTietDonHangModelList.get(holder.getAdapterPosition()).setSoluong(dem);
                    }
                    onCapNhatGioHangListener.onCapNhat();
                }
            }
        });

        holder.btnXoaMonAn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(holder.getAdapterPosition());
                onCapNhatGioHangListener.onCapNhat();
            }
        });
    }

    public void removeItem(int position){
        chiTietDonHangModelList.remove(position);
        notifyDataSetChanged();
    }

    public List<ChiTietDonHangModel> getChiTietDonHangList(){
        return chiTietDonHangModelList;
    }

    @Override
    public int getItemCount() {
        return chiTietDonHangModelList == null ? 0 : chiTietDonHangModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView txtName;
        TextView txtGia;
        TextView txtSoLuong;
        ImageView imgTangSoLuong;
        ImageView imgGiamSoLuong;
        ImageView btnXoaMonAn;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_monan);
            txtName = itemView.findViewById(R.id.txt_name);
            txtGia = itemView.findViewById(R.id.txt_cost);
            txtSoLuong = (TextView) itemView.findViewById(R.id.txtSoLuong);
            imgGiamSoLuong = (ImageView) itemView.findViewById(R.id.imgGiamSoLuong);
            imgTangSoLuong = (ImageView) itemView.findViewById(R.id.imgTangSoLuong);
            btnXoaMonAn = itemView.findViewById(R.id.btn_xoa_mon_an);
        }
    }

    public interface OnCapNhatGioHangListener {
        void onCapNhat();
    }
}
