package com.vudn.myfood.adapter.comment;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;


import com.vudn.myfood.R;
import com.vudn.myfood.model.comment.ChonHinhBinhLuanModel;

import java.util.List;


public class ChonHinhBinhLuanAdapter extends RecyclerView.Adapter<ChonHinhBinhLuanAdapter.ViewHolderChonHinh> {

    private Context context;
    private int resource;
    private List<ChonHinhBinhLuanModel> listDuongDan;
    private LayoutInflater inflater;

    public ChonHinhBinhLuanAdapter(Context context, int resource, List<ChonHinhBinhLuanModel> listDuongDan){
        this.context = context;
        this.resource = resource;
        this.listDuongDan = listDuongDan;
        try{
            inflater = LayoutInflater.from(context);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public ViewHolderChonHinh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(resource,parent,false);
        return new ViewHolderChonHinh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderChonHinh holder, int position) {
        final ChonHinhBinhLuanModel chonHinhBinhLuanModel = listDuongDan.get(position);
        Uri uri = Uri.parse(chonHinhBinhLuanModel.getDuongdan());
        holder.imageView.setImageURI(uri);
        holder.checkBox.setChecked(chonHinhBinhLuanModel.isCheck());
       holder.checkBox.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               CheckBox checkBox = (CheckBox) v;
               listDuongDan.get(holder.getAdapterPosition()).setCheck(checkBox.isChecked());
           }
       });
    }

    @Override
    public int getItemCount() {
        return listDuongDan.size();
    }

    public class ViewHolderChonHinh extends RecyclerView.ViewHolder {

        ImageView imageView;
        CheckBox checkBox;

        public ViewHolderChonHinh(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgChonHinhBinhLuan);
            checkBox = itemView.findViewById(R.id.checkBoxChonHinhBinhLuan);

        }
    }
}
