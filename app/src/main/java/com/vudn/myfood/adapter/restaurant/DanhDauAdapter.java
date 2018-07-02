package com.vudn.myfood.adapter.restaurant;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vudn.myfood.R;
import com.vudn.myfood.model.restaurant.QuanAnModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DanhDauAdapter extends RecyclerView.Adapter<DanhDauAdapter.ViewHolder>{
    private Context context;
    private LayoutInflater inflater;
    private List<QuanAnModel> quanAnModelList;
    private OnClickItemListener onClickItemListener;

    public DanhDauAdapter(Context context, List<QuanAnModel> quanAnModelList, OnClickItemListener onClickItemListener) {
        this.context = context;
        this.quanAnModelList = quanAnModelList;
        inflater = LayoutInflater.from(context);
        setOnClickItemListener(onClickItemListener);
    }

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    @NonNull
    @Override
    public DanhDauAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_bookmark, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DanhDauAdapter.ViewHolder holder, int position) {
        QuanAnModel quanAnModel = quanAnModelList.get(position);
        String name = quanAnModel.getTenquanan();
        String address = quanAnModel.getDiachi().getAddress();
        if (quanAnModel.getHinhanhquanan() == null || quanAnModel.getHinhanhquanan().isEmpty()){
            Glide.with(context).load(R.drawable.background).into(holder.imgReview);
        }else {
            Glide.with(context).load(quanAnModel.getHinhanhquanan().get(0)).into(holder.imgReview);
        }
        /*String path = quanAnModel.getHinhanhquanan().get(0);
        Glide.with(context).load(path).into(holder.imgReview);
*/
        holder.txtName.setText(name);
        holder.txtAddress.setText(address);
        final int itemPosition = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItemListener.onClickItem(itemPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return quanAnModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        TextView txtAddress;
        CircleImageView imgReview;

        public ViewHolder(final View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtAddress = itemView.findViewById(R.id.txt_address);
            imgReview = itemView.findViewById(R.id.img_review);
        }
    }

    public QuanAnModel getItem(int position){
        return quanAnModelList.get(position);
    }

    public interface OnClickItemListener{
        void onClickItem(int position);
    }
}
