package com.vudn.myfood.adapter.comment;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.vudn.myfood.R;

import java.util.List;


public class HienThiHinhBinhLuanDuocChonAdapter extends RecyclerView.Adapter<HienThiHinhBinhLuanDuocChonAdapter.ViewHolderHinhBinhLuan> {

    private Context context;
    private int resource;
    private List<String> list;
    private LayoutInflater inflater;

    public HienThiHinhBinhLuanDuocChonAdapter(Context context, int resource, List<String> list){
        this.context = context;
        this.resource = resource;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public HienThiHinhBinhLuanDuocChonAdapter.ViewHolderHinhBinhLuan onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(resource,parent,false);
        return new ViewHolderHinhBinhLuan(view);
    }

    @Override
    public void onBindViewHolder(HienThiHinhBinhLuanDuocChonAdapter.ViewHolderHinhBinhLuan holder, int position) {

        Uri uri = Uri.parse(list.get(position));
        holder.imageView.setImageURI(uri);
        holder.imgXoa.setTag(position);
        holder.imgXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int vitri = (int) v.getTag();
                list.remove(vitri);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolderHinhBinhLuan extends RecyclerView.ViewHolder {
        ImageView imageView,imgXoa;

        public ViewHolderHinhBinhLuan(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imgChonHinhBinhLuan);
            imgXoa = itemView.findViewById(R.id.imgeDelete);
        }
    }
}
