package com.vudn.myfood.adapter.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vudn.myfood.R;
import com.vudn.myfood.model.menu.MonAnModel;
import com.vudn.myfood.model.menu.ThucDonModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ThucDonQuanAnAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<ThucDonModel> thucDonModelList;
    private LayoutInflater inflater;

    public ThucDonQuanAnAdapter(Context context, List<ThucDonModel> thucDonModelList) {
        this.context = context;
        this.thucDonModelList = thucDonModelList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return thucDonModelList == null ? 0 : thucDonModelList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return thucDonModelList.get(groupPosition).getMonAnModelList() == null ?
                0 : thucDonModelList.get(groupPosition).getMonAnModelList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return thucDonModelList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return thucDonModelList.get(groupPosition).getMonAnModelList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_thuc_don, parent, false);
        }
        TextView name = convertView.findViewById(R.id.txt_ten_thuc_don);

        if (getGroup(groupPosition) instanceof ThucDonModel) {
            ThucDonModel thucDonModel = (ThucDonModel) getGroup(groupPosition);
            name.setText(thucDonModel.getTenthucdon());
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_mon_an, parent, false);
        }
        CircleImageView imgView = convertView.findViewById(R.id.img_mon_an);
        TextView txtTenMonAn = convertView.findViewById(R.id.txt_ten_mon_an);
        TextView txtGia = convertView.findViewById(R.id.txt_gia);

        if (getChild(groupPosition, childPosition) instanceof MonAnModel) {
            final MonAnModel monAnModel = (MonAnModel) getChild(groupPosition, childPosition);
            Glide.with(context).load(monAnModel.getHinhanh()).into(imgView);
            txtTenMonAn.setText(monAnModel.getTenmon());
            txtGia.setText(String.format("%,d", monAnModel.getGiatien()).replace(",", ".") + "đ");
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
