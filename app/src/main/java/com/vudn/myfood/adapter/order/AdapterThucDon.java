package com.vudn.myfood.adapter.order;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.vudn.myfood.R;
import com.vudn.myfood.model.restaurant.ThucDonModel;

import java.util.List;


public class AdapterThucDon extends RecyclerView.Adapter<AdapterThucDon.HolderThucDon> {

    Context context;
    List<ThucDonModel> thucDonModels;
    AdapterMonAn.OnSoLuongMonAnThayDoiListener onSoLuongMonAnThayDoiListener;


    public AdapterThucDon(Context context, List<ThucDonModel> thucDonModels, AdapterMonAn.OnSoLuongMonAnThayDoiListener onSoLuongMonAnThayDoiListener) {
        this.context = context;
        this.thucDonModels = thucDonModels;
        this.onSoLuongMonAnThayDoiListener = onSoLuongMonAnThayDoiListener;
    }

    @Override
    public AdapterThucDon.HolderThucDon onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_layout_thucdon, parent, false);
        return new HolderThucDon(view);
    }

    @Override
    public void onBindViewHolder(AdapterThucDon.HolderThucDon holder, int position) {
        ThucDonModel thucDonModel = thucDonModels.get(position);
        holder.txtThucDon.setText(thucDonModel.getTenthucdon());
        holder.recyclerViewMonAn.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        AdapterMonAn adapterMonAn = new AdapterMonAn(context, thucDonModel.getMonAnModelList(), onSoLuongMonAnThayDoiListener);
        holder.recyclerViewMonAn.setAdapter(adapterMonAn);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context,
                LinearLayoutManager.VERTICAL);
        holder.recyclerViewMonAn.addItemDecoration(dividerItemDecoration);
        adapterMonAn.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return thucDonModels.size();
    }

    public class HolderThucDon extends RecyclerView.ViewHolder {
        TextView txtThucDon;
        RecyclerView recyclerViewMonAn;

        public HolderThucDon(View itemView) {
            super(itemView);
            txtThucDon = (TextView) itemView.findViewById(R.id.txtTenThucDon);
            recyclerViewMonAn = (RecyclerView) itemView.findViewById(R.id.recyclerMonAn);
        }
    }
}
