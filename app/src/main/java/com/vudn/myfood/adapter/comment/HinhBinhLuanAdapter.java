package com.vudn.myfood.adapter.comment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.vudn.myfood.R;
import com.vudn.myfood.model.comment.BinhLuanModel;
import com.vudn.myfood.view.comment.HienThiChiTietBinhLuanActivity;

import java.util.List;


public class HinhBinhLuanAdapter extends RecyclerView.Adapter<HinhBinhLuanAdapter.ViewHolderHinhBinhLuan>{

    Context context;
    int resource;
    List<Bitmap> listHinh;
    BinhLuanModel binhLuanModel;
    boolean isChiTietBinhLuan;

    public HinhBinhLuanAdapter(Context context, int resource, List<Bitmap> listHinh, BinhLuanModel binhLuanModel, boolean isChiTietBinhLuan){
        this.context = context;
        this.resource = resource;
        this.binhLuanModel = binhLuanModel;
        this.listHinh = listHinh;
        this.isChiTietBinhLuan = isChiTietBinhLuan;

    }

    public class ViewHolderHinhBinhLuan extends RecyclerView.ViewHolder {
        ImageView imageHinhBinhLuan;
        TextView txtSoHinhBinhLuan;
        FrameLayout khungSoHinhBinhLuan;

        public ViewHolderHinhBinhLuan(View itemView) {
            super(itemView);

            imageHinhBinhLuan = (ImageView) itemView.findViewById(R.id.imageBinhLuan);
            txtSoHinhBinhLuan = (TextView) itemView.findViewById(R.id.txtSoHinhBinhLuan);
            khungSoHinhBinhLuan = (FrameLayout) itemView.findViewById(R.id.khungSoHinhBinhLuan);
        }
    }

    @Override
    public HinhBinhLuanAdapter.ViewHolderHinhBinhLuan onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        ViewHolderHinhBinhLuan viewHolderHinhBinhLuan = new ViewHolderHinhBinhLuan(view);

        return viewHolderHinhBinhLuan;
    }

    @Override
    public void onBindViewHolder(final HinhBinhLuanAdapter.ViewHolderHinhBinhLuan holder, final int position) {

        holder.imageHinhBinhLuan.setImageBitmap(listHinh.get(position));

        if(!isChiTietBinhLuan){
            if(position == 3){

                int sohinhconlai = listHinh.size()-4;
                if(sohinhconlai > 0){
                    holder.khungSoHinhBinhLuan.setVisibility(View.VISIBLE);
                    holder.txtSoHinhBinhLuan.setText("+" + sohinhconlai);
                    holder.imageHinhBinhLuan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent iChiTietBinhLuan = new Intent(context, HienThiChiTietBinhLuanActivity.class);
                            iChiTietBinhLuan.putExtra("binhluanmodel",binhLuanModel);
                            context.startActivity(iChiTietBinhLuan);
                        }
                    });

                }

            }
        }


    }

    @Override
    public int getItemCount() {
        if(!isChiTietBinhLuan){
            if(listHinh.size() < 4){
                return listHinh.size();
            }else{
                return 4;
            }

        }else{
            return listHinh.size();
        }

    }


}
