package com.vudn.myfood.view.other;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vudn.myfood.R;
import com.vudn.myfood.model.restaurant.TienIchModel;

import java.util.List;

public class TienIchDialog extends Dialog {
    RecyclerView rcvTienIch;
    List<TienIchModel> tienIchModelList;
    Context context;
    TienIchAdapter tienIchAdapter;

    public TienIchDialog(@NonNull Context context, List<TienIchModel> tienIchModelList) {
        super(context);
        this.context = context;
        setContentView(R.layout.dialog_tien_ich);
        this.tienIchModelList = tienIchModelList;
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeComponents();
    }

    private void initializeComponents() {
        rcvTienIch = findViewById(R.id.rcv_tien_ich);
        rcvTienIch.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rcvTienIch.setHasFixedSize(true);
        rcvTienIch.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        tienIchAdapter = new TienIchAdapter();
        rcvTienIch.setAdapter(tienIchAdapter);
    }

    private class TienIchAdapter extends RecyclerView.Adapter<TienIchAdapter.ViewHolder>{
        LayoutInflater inflater;

        public TienIchAdapter() {
            inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_chi_tiet_tien_ich, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            TienIchModel tienIchModel = tienIchModelList.get(position);
            Glide.with(context).load(tienIchModel.getHinhtienich()).into(holder.imgTienIch);
            holder.txtTenTienIch.setText(tienIchModel.getTentienich());
        }

        @Override
        public int getItemCount() {
            return tienIchModelList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgTienIch;
            TextView txtTenTienIch;
            public ViewHolder(View itemView) {
                super(itemView);
                imgTienIch = itemView.findViewById(R.id.img_tien_ich);
                txtTenTienIch = itemView.findViewById(R.id.txt_ten_tien_ich);
            }
        }
    }
}
