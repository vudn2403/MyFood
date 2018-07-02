package com.vudn.myfood.presenter.other;

import android.content.Context;
import android.location.Location;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.vudn.myfood.adapter.main.DsQuanAnAdapter;
import com.vudn.myfood.model.restaurant.QuanAnModel;


import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

public class OdauController {
    public static final String TAG = "OdauController";
    Context context;
    QuanAnModel quanAnModel;
    DsQuanAnAdapter adapterRecyclerOdau;
    int itemdaco = 10;

    public OdauController(Context context){
        this.context = context;
        quanAnModel = new QuanAnModel();
    }

    public void getDanhSachQuanAnController(Context context, NestedScrollView nestedScrollView, RecyclerView recyclerOdau, final LinearLayout linearLayout, final Location vitrihientai){

        final List<QuanAnModel> quanAnModelList = new ArrayList<>();
        recyclerOdau.setLayoutManager(new LinearLayoutManager(context, VERTICAL, false));
        //adapterRecyclerOdau = new DsQuanAnAdapter(context,quanAnModelList, R.layout.custom_layout_recyclerview_odau);
        recyclerOdau.setAdapter(adapterRecyclerOdau);
        linearLayout.setVisibility(View.VISIBLE);

        /*final OdauInterface odauInterface = new OdauInterface() {
            @Override
            public void getDanhSachQuanAnModel(final QuanAnModel quanAnModel) {
                final List<Bitmap> bitmaps = new ArrayList<>();
                for(String linkhinh : quanAnModel.getHinhanhquanan()){

                    StorageReference storageHinhAnh = FirebaseStorage.getInstance().getReference().child("hinhanh").child(linkhinh);
                    Log.d(TAG, "getDanhSachQuanAnModel: Đã lấy được danh sách hình ảnh");
                    long ONE_MEGABYTE = 1024 * 1024;
                    storageHinhAnh.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Log.d(TAG, "onSuccess: Tải anh về");
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                            bitmaps.add(bitmap);
                            quanAnModel.setBitmapList(bitmaps);

                            if(quanAnModel.getBitmapList().size() == quanAnModel.getHinhanhquanan().size()){
                                quanAnModelList.add(quanAnModel);
                                adapterRecyclerOdau.notifyDataSetChanged();
                                linearLayout.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        };*/

        /*nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(v.getChildAt(v.getChildCount() - 1) !=null){
                    {if(scrollY >= (v.getChildAt(v.getChildCount() - 1)).getMeasuredHeight() - 3*v.getMeasuredHeight())
                        itemdaco += 10;
                        quanAnModel.getDanhSachQuanAn(odauInterface,vitrihientai,itemdaco,itemdaco-10);
                    }
                }
            }
        });
        quanAnModel.getDanhSachQuanAn(odauInterface,vitrihientai,itemdaco,0);*/
    }
}
