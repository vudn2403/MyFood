package com.vudn.myfood.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vudn.myfood.R;

public class ThayDoiMatKhauDialog extends Dialog implements View.OnClickListener {
    Context context;
    private TextView txtTaiKhoan;
    private EditText edtMatKhauCu;
    private EditText edtMatKhauMoi;
    private Button btnCapNhat;
    private FirebaseUser user;
    private String email;
    private String oldPass;
    private String newPass;

    public ThayDoiMatKhauDialog(@NonNull Context context, String email) {
        super(context);
        this.context = context;
        this.email = email;
        setContentView(R.layout.dialog_thay_doi_mat_khau);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        initializeComponents();
        registerListeners();
    }

    private void initializeComponents() {
        txtTaiKhoan = findViewById(R.id.txt_tai_khoan);
        edtMatKhauCu = findViewById(R.id.edt_mat_khau_cu);
        edtMatKhauMoi = findViewById(R.id.edt_mat_khau_moi);
        btnCapNhat = findViewById(R.id.btn_cap_nhat_mat_khau);
        txtTaiKhoan.setText(email);
    }

    private void registerListeners() {
        btnCapNhat.setOnClickListener(this);
    }


    void updatePassword() {
        oldPass = edtMatKhauCu.getText().toString().trim();
        newPass = edtMatKhauMoi.getText().toString().trim();
        if (oldPass.isEmpty() || newPass.isEmpty()){
            Toast.makeText(context, "Thông tin không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }

        if (oldPass.equals(newPass)){
            Toast.makeText(context, "Mật khẩu trùng nhau", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthCredential authCredential = EmailAuthProvider.getCredential(email, oldPass);
        user.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(context, "Cập nhật mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                                dismiss();
                            }else {
                                Toast.makeText(context, "Đã xảy ra sự cố. Vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                                dismiss();
                            }
                        }
                    });
                }else {
                    Toast.makeText(context, "Lỗi xác thực tài khoản", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cap_nhat_mat_khau:
                updatePassword();
                break;

            default:
                break;
        }
    }
}
