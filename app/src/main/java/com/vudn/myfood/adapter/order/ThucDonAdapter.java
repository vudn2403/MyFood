package com.vudn.myfood.adapter.order;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.vudn.myfood.R;
import com.vudn.myfood.model.menu.ThucDonModel;

import java.util.List;


public class ThucDonAdapter extends RecyclerView.Adapter<ThucDonAdapter.HolderThucDon> {

    private Context context;
    private List<ThucDonModel> thucDonModels;
    private MonAnAdapter.OnSoLuongMonAnThayDoiListener onSoLuongMonAnThayDoiListener;
    private LayoutInflater inflater;


    public ThucDonAdapter(Context context, List<ThucDonModel> thucDonModels, MonAnAdapter.OnSoLuongMonAnThayDoiListener onSoLuongMonAnThayDoiListener) {
        this.context = context;
        this.thucDonModels = thucDonModels;
        inflater = LayoutInflater.from(context);
        this.onSoLuongMonAnThayDoiListener = onSoLuongMonAnThayDoiListener;
    }

    @Override
    public ThucDonAdapter.HolderThucDon onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_dat_hang_thuc_don, parent, false);
        return new HolderThucDon(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThucDonAdapter.HolderThucDon holder, int position) {
        ThucDonModel thucDonModel = thucDonModels.get(position);
        holder.txtThucDon.setText(thucDonModel.getTenthucdon());
        holder.recyclerViewMonAn.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        MonAnAdapter adapterMonAn = new MonAnAdapter(context, thucDonModel.getMonAnModelList(), onSoLuongMonAnThayDoiListener);
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
            txtThucDon = itemView.findViewById(R.id.txtTenThucDon);
            recyclerViewMonAn = itemView.findViewById(R.id.recyclerMonAn);
        }
    }
}
