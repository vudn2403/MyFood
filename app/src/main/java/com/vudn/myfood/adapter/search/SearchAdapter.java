package com.vudn.myfood.adapter.search;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vudn.myfood.R;
import com.vudn.myfood.model.comment.BinhLuanModel;
import com.vudn.myfood.model.restaurant.QuanAnModel;
import com.vudn.myfood.util.SortQuanAn;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    public static final String TAG = "SearchAdapter";
    private List<QuanAnModel> quanAnModelList;
    private int resource;
    private Context context;
    private OnQuanAnTimKiemClickListener quanAnTimKiemClickListener;


    public SearchAdapter(Context context, List<QuanAnModel> quanAnModelList, int resource, OnQuanAnTimKiemClickListener onQuanAnTimKiemClickListener) {
        this.quanAnModelList = quanAnModelList;
        this.resource = resource;
        this.context = context;
        setQuanAnTimKiemClickListener(onQuanAnTimKiemClickListener);
    }

    public void setQuanAnTimKiemClickListener(OnQuanAnTimKiemClickListener quanAnTimKiemClickListener) {
        this.quanAnTimKiemClickListener = quanAnTimKiemClickListener;
    }

    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        SearchAdapter.ViewHolder viewHolder = new SearchAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final SearchAdapter.ViewHolder holder, int position) {
        QuanAnModel quanAnModel = quanAnModelList.get(position);
        holder.txtTenQuanAnOdau.setText(quanAnModel.getTenquanan());
        if (quanAnModel.isGiaohang()) {
            holder.btnDatMonOdau.setVisibility(View.VISIBLE);
            holder.btnDatMonOdau.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quanAnTimKiemClickListener.onDatGiaoHangClick(holder.getAdapterPosition());
                    Log.d(TAG, TAG + "onClick: giao hàng " + holder.getAdapterPosition());
                }
            });
        }
        if (quanAnModel.getHinhanhquanan() != null && !quanAnModel.getHinhanhquanan().isEmpty()) {
            Glide.with(context).load(quanAnModel.getHinhanhquanan().get(0)).into(holder.imageHinhQuanAnODau);
        }else {
            Glide.with(context).load(R.drawable.background).into(holder.imageHinhQuanAnODau);
        }

        //Lấy danh sách bình luận của quán ăn
        if (quanAnModel.getBinhLuanModelList().size() > 0) {
            BinhLuanModel binhLuanModel = quanAnModel.getBinhLuanModelList().get(0);
            holder.txtTieudebinhluan.setText(binhLuanModel.getTieude());
            holder.txtNodungbinhluan.setText(binhLuanModel.getNoidung());
            holder.txtChamDiemBinhLuan.setText(binhLuanModel.getChamdiem() + "");
            Glide.with(context).load(binhLuanModel.getThanhVienModel().getHinhanh()).into(holder.cicleImageUser);
            if (quanAnModel.getBinhLuanModelList().size() > 2) {
                BinhLuanModel binhLuanModel2 = quanAnModel.getBinhLuanModelList().get(1);
                holder.txtTieudebinhluan2.setText(binhLuanModel2.getTieude());
                holder.txtNodungbinhluan2.setText(binhLuanModel2.getNoidung());
                holder.txtChamDiemBinhLuan2.setText(binhLuanModel2.getChamdiem() + "");
                Glide.with(context).load(binhLuanModel2.getThanhVienModel().getHinhanh());
            }
            holder.txtTongBinhLuan.setText(quanAnModel.getBinhLuanModelList().size() + "");
            holder.txtDiemTrungBinhQuanAn.setText(String.format("%.1f", quanAnModel.getDiemdanhgia()));
            holder.txtTongHinhBinhLuan.setText(quanAnModel.getSohinhbinhluan() + "");
            /*int tongsohinhbinhluan = 0;
            double tongdiem = 0;
            //Tính tổng điểm trung bình của bình luận và đếm tổng số hình của bình luận
            for (BinhLuanModel binhLuanModel1 : quanAnModel.getBinhLuanModelList()) {
                tongsohinhbinhluan += binhLuanModel1.getHinhanhBinhLuanList().size();
                tongdiem += binhLuanModel1.getChamdiem();
            }

            double diemtrungbinh = tongdiem / quanAnModel.getBinhLuanModelList().size();
            holder.txtDiemTrungBinhQuanAn.setText(String.format("%.1f", diemtrungbinh));

            if (tongsohinhbinhluan > 0) {
                holder.txtTongHinhBinhLuan.setText(tongsohinhbinhluan + "");
            }*/
            holder.containerBinhLuan.setVisibility(View.VISIBLE);
            holder.containerBinhLuan2.setVisibility(View.VISIBLE);
        } else {
            holder.txtDiemTrungBinhQuanAn.setText("_._");
            holder.txtTongBinhLuan.setText("0");
            holder.txtTongHinhBinhLuan.setText("0");
            holder.containerBinhLuan.setVisibility(View.GONE);
            holder.containerBinhLuan2.setVisibility(View.GONE);
        }
        holder.txtKhoanCachQuanAnODau.setText(String.format("%.1f", quanAnModel.getKhoangcach()) + " km");
        holder.txtDiaChiQuanAnODau.setText(quanAnModel.getDiachi().getAddress()
                .replace(", VietNam", "")
                .replace(", Việt Nam", ""));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quanAnTimKiemClickListener.onQuanAnModelClick(holder.getAdapterPosition());
                Log.d(TAG, TAG + "onClick: QuanAnModelClick" + holder.getAdapterPosition());
            }
        });

        //Lấy chi nhánh quán ăn và hiển thị địa chỉ và km
        /*if (quanAnModel.getChiNhanhQuanAnModelList().size() > 0) {
            ChiNhanhQuanAnModel chiNhanhQuanAnModelTam = quanAnModel.getChiNhanhQuanAnModelList().get(0);
            for (ChiNhanhQuanAnModel chiNhanhQuanAnModel : quanAnModel.getChiNhanhQuanAnModelList()) {
                if (chiNhanhQuanAnModelTam.getKhoangcach() > chiNhanhQuanAnModel.getKhoangcach()) {
                    chiNhanhQuanAnModelTam = chiNhanhQuanAnModel;
                }
            }

            holder.txtDiaChiQuanAnODau.setText(chiNhanhQuanAnModelTam.getDiachi()
                    .replace(", VietNam", "")
                    .replace(", Việt Nam", ""));
            holder.txtKhoanCachQuanAnODau.setText(String.format("%.1f", chiNhanhQuanAnModelTam.getKhoangcach()) + " km");
        }*/
        /*        if (quanAnModel.getHinhanhquanan() != null && !quanAnModel.getHinhanhquanan().isEmpty()) {
            for (String link : quanAnModel.getHinhanhquanan()) {
                FirebaseStorage.getInstance().getReference().child("hinhanh").child(link).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(context)
                                .load(uri)
                                .into(holder.imageHinhQuanAnODau);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        }*/

        /*if (quanAnModel.getBitmapList() != null && !quanAnModel.getBitmapList().isEmpty()) {
            holder.imageHinhQuanAnODau.setImageBitmap(quanAnModel.getBitmapList().get(0));
        }*/
    }

    /*private void setHinhAnhBinhLuan(final CircleImageView circleImageView, String linkhinh) {
        StorageReference storageHinhUser = FirebaseStorage.getInstance().getReference().child("thanhvien").child(linkhinh);
        long ONE_MEGABYTE = 128 * 128;
        storageHinhUser.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                circleImageView.setImageBitmap(bitmap);
            }
        });
    }*/

    @Override
    public int getItemCount() {
        return quanAnModelList == null ? 0 : quanAnModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenQuanAnOdau, txtTieudebinhluan2, txtTieudebinhluan, txtNodungbinhluan2, txtNodungbinhluan, txtChamDiemBinhLuan, txtChamDiemBinhLuan2, txtTongBinhLuan, txtTongHinhBinhLuan, txtDiemTrungBinhQuanAn, txtDiaChiQuanAnODau, txtKhoanCachQuanAnODau;
        Button btnDatMonOdau;
        ImageView imageHinhQuanAnODau;
        CircleImageView cicleImageUser2, cicleImageUser;
        LinearLayout containerBinhLuan, containerBinhLuan2;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTenQuanAnOdau = (TextView) itemView.findViewById(R.id.txtTenQuanQuanOdau);
            btnDatMonOdau = (Button) itemView.findViewById(R.id.btnDatMonOdau);
            imageHinhQuanAnODau = (ImageView) itemView.findViewById(R.id.imageHinhQuanAnOdau);
            txtNodungbinhluan = (TextView) itemView.findViewById(R.id.txtNodungbinhluan);
            txtNodungbinhluan2 = (TextView) itemView.findViewById(R.id.txtNodungbinhluan2);
            txtTieudebinhluan = (TextView) itemView.findViewById(R.id.txtTieudebinhluan);
            txtTieudebinhluan2 = (TextView) itemView.findViewById(R.id.txtTieudebinhluan2);
            cicleImageUser = (CircleImageView) itemView.findViewById(R.id.cicleImageUser);
            cicleImageUser2 = (CircleImageView) itemView.findViewById(R.id.cicleImageUser2);
            containerBinhLuan = (LinearLayout) itemView.findViewById(R.id.containerBinhLuan);
            containerBinhLuan2 = (LinearLayout) itemView.findViewById(R.id.containerBinhLuan2);
            txtChamDiemBinhLuan = (TextView) itemView.findViewById(R.id.txtChamDiemBinhLuan);
            txtChamDiemBinhLuan2 = (TextView) itemView.findViewById(R.id.txtChamDiemBinhLuan2);
            txtTongBinhLuan = (TextView) itemView.findViewById(R.id.txtTongBinhLuan);
            txtTongHinhBinhLuan = (TextView) itemView.findViewById(R.id.txtTongHinhBinhLuan);
            txtDiemTrungBinhQuanAn = (TextView) itemView.findViewById(R.id.txtDiemTrungBinhQuanAn);
            txtDiaChiQuanAnODau = (TextView) itemView.findViewById(R.id.txtDiaChiQuanAnODau);
            txtKhoanCachQuanAnODau = (TextView) itemView.findViewById(R.id.txtKhoangCachQuanAnODau);
            cardView = (CardView) itemView.findViewById(R.id.cardViewOdau);
        }
    }

    public void sortbyName(){
        if (quanAnModelList == null || quanAnModelList.isEmpty()){
            return;
        }
        List<QuanAnModel> quanAnModels = SortQuanAn.sortByName(quanAnModelList);
        quanAnModelList = quanAnModels;
        notifyDataSetChanged();
        Toast.makeText(context, "Sắp xếp theo tên A - Z", Toast.LENGTH_SHORT).show();
    }

    public void sortByDistant(){
        if (quanAnModelList == null || quanAnModelList.isEmpty()){
            return;
        }
        List<QuanAnModel> quanAnModels = SortQuanAn.sortByDistant(quanAnModelList);
        quanAnModelList = quanAnModels;
        notifyDataSetChanged();
        Toast.makeText(context, "Sắp xếp theo khoảng cách từ gần tới xa", Toast.LENGTH_SHORT).show();
    }

    public void sortByPoint(){
        if (quanAnModelList == null || quanAnModelList.isEmpty()){
            return;
        }
        List<QuanAnModel> quanAnModels = SortQuanAn.sortByPoint(quanAnModelList);
        quanAnModelList = quanAnModels;
        notifyDataSetChanged();
        Toast.makeText(context, "Sắp xếp theo điểm đánh giá từ cao tới thấp", Toast.LENGTH_SHORT).show();
    }

    public QuanAnModel getQuanAnModel(int position) {
        return quanAnModelList.get(position);
    }

    public interface OnQuanAnTimKiemClickListener {
        void onQuanAnModelClick(int position);
        void onDatGiaoHangClick(int position);
    }
}
