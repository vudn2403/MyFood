package com.vudn.myfood.view.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.vudn.myfood.R;

public class KhoiPhucMatKhauActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtDangKyKP;
    Button btnGuiEmail;
    EditText edEmailKP;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        firebaseAuth = FirebaseAuth.getInstance();

        txtDangKyKP = (TextView) findViewById(R.id.txtDangKyKP);
        btnGuiEmail = (Button) findViewById(R.id.btnGuiEmailKP);
        edEmailKP = (EditText) findViewById(R.id.edEmailKP);

        btnGuiEmail.setOnClickListener(this);
        txtDangKyKP.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btnGuiEmailKP:
                progressDialog.setMessage(getString(R.string.dangxuly));
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                String email = edEmailKP.getText().toString();
                boolean kiemtraemail = KiemTraEmail(email);
                if(kiemtraemail){
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(KhoiPhucMatKhauActivity.this,getString(R.string.thongbaoguimailthanhcong), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(KhoiPhucMatKhauActivity.this,getString(R.string.thongbaoemailkhonghople), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.txtDangKyKP:
                Intent iDangKy = new Intent(KhoiPhucMatKhauActivity.this,DangKyActivity.class);
                startActivity(iDangKy);
                break;
        }
    }

    private boolean KiemTraEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
