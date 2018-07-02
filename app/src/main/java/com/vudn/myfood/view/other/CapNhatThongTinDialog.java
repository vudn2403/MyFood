package com.vudn.myfood.view.other;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vudn.myfood.R;
import com.vudn.myfood.model.user.ThanhVienModel;

public class CapNhatThongTinDialog extends Dialog implements View.OnClickListener {
    TextView txtTen;
    TextView txtDiaChi;
    TextView txtSoDienThoai;
    TextView txtNgaySinh;

    EditText edtTen;
    EditText edtDiaChi;
    EditText edtSoDienThoai;
    EditText edtNgaySinh;

    Button btnCapNhat;
    String mauser;

    DatabaseReference databaseReference;
    ThanhVienModel thanhVienModel;
    SharedPreferences sharedPreferencesToaDo;
    SharedPreferences sharedPreferencesDangNhap;

    public CapNhatThongTinDialog(@NonNull Context context, String mauser) {
        super(context);
        this.mauser = mauser;
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_cap_nhat_thong_tin);
        initializeComponents();
        registerListeners();
    }

    private void initializeComponents() {
        txtTen = findViewById(R.id.txt_ten_hien_thi);
        txtDiaChi = findViewById(R.id.txt_dia_chi);
        txtSoDienThoai = findViewById(R.id.txt_so_dien_thoai);
        txtNgaySinh = findViewById(R.id.txt_ngay_sinh);
        edtTen = findViewById(R.id.edt_ten_moi);
        edtDiaChi = findViewById(R.id.edt_dia_chi_moi);
        edtSoDienThoai = findViewById(R.id.edt_sdt_moi);
        edtNgaySinh = findViewById(R.id.edt_ngay_sinh);
        btnCapNhat = findViewById(R.id.btn_cap_nhat);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("thanhviens").child(mauser);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                thanhVienModel = dataSnapshot.getValue(ThanhVienModel.class);
                txtTen.setText("Tên hiển thị: " + thanhVienModel.getHoten());
                if (thanhVienModel.getDiachi() == null || thanhVienModel.getDiachi().isEmpty()){
                    txtDiaChi.setText("Địa chỉ: " + "chưa có dữ liệu");
                }else {
                    txtDiaChi.setText("Địa chỉ: " + thanhVienModel.getDiachi());
                }

                if (thanhVienModel.getSodienthoai() == null){
                    txtSoDienThoai.setText("Số điện thoại: " + "chưa có dữ liệu");
                }else {
                    txtSoDienThoai.setText("Số điện thoại: " + thanhVienModel.getSodienthoai());
                }
                if (thanhVienModel.getNgaysinh() == null || thanhVienModel.getNgaysinh().isEmpty()){
                    txtNgaySinh.setText("Ngày sinh: " + "chưa có dữ liệu");
                }else {
                    txtNgaySinh.setText("Ngày sinh: " + thanhVienModel.getNgaysinh());
                }

                sharedPreferencesToaDo = getContext().getSharedPreferences("toado", Context.MODE_PRIVATE);
                edtDiaChi.setText(sharedPreferencesToaDo.getString("address", ""));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void registerListeners() {
        btnCapNhat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cap_nhat:
                capNhatThongTin();
                break;

            default:
                break;
        }
    }

    private void capNhatThongTin() {
        String newName = edtTen.getText().toString();
        String newAdd = edtDiaChi.getText().toString();
        String newPhone = edtSoDienThoai.getText().toString();
        String newDate = edtNgaySinh.getText().toString();
        if (!newName.equals(thanhVienModel.getHoten()) && !newName.isEmpty()) {
            thanhVienModel.setHoten(newName);
        } else {
            //Toast.makeText(getContext(), "Tên không phù hợp", Toast.LENGTH_SHORT).show();
        }

        if (!newAdd.equals(thanhVienModel.getDiachi()) && !newAdd.isEmpty()) {
            thanhVienModel.setDiachi(newAdd);
        }

        if (!newPhone.isEmpty() && !newPhone.equals(thanhVienModel.getSodienthoai())) {
            thanhVienModel.setSodienthoai(newPhone);
        }

        if (!newDate.isEmpty() && !newDate.equals(thanhVienModel.getNgaysinh())) {
            thanhVienModel.setNgaysinh(newDate);
        }
        databaseReference.setValue(thanhVienModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Đã xảy ra lỗi. Vui long thử lại sau", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
