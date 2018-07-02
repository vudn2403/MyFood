package com.vudn.myfood.view.user;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.vudn.myfood.R;
import com.vudn.myfood.model.user.Account;

public class ResetPasswordFragment extends Fragment implements View.OnClickListener {
    private TextView txtSignUp;
    private Button btnSendEmail;
    private EditText edtEmail;
    private Account account;

    ProgressDialog progressDialog;

    public ResetPasswordFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reset_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtSignUp = view.findViewById(R.id.txt_sign_up);
        btnSendEmail = view.findViewById(R.id.btn_send_email);
        edtEmail = view.findViewById(R.id.edt_email);
        btnSendEmail.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);
        syncData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_email:
                sendEmail();
                break;

            case R.id.txt_sign_up:
                signUp();
                break;

            default:
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            syncData();
        }
    }

    private void syncData(){
        if (getActivity() instanceof UserActivity){
            if (((UserActivity) getActivity()).getAccount() != null){
                account = ((UserActivity) getActivity()).getAccount();
                edtEmail.setText(account.getEmail());
            }
        }
    }

    private void sendEmail() {
        String email = edtEmail.getText().toString().trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getContext(), getString(R.string.thongbaoemailkhonghople), Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setMessage(getString(R.string.dangxuly));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), getString(R.string.thongbaoguimailthanhcong), Toast.LENGTH_SHORT).show();
                        signIn();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Gửi email không thành công, vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void signUp() {
        String email = edtEmail.getText().toString().trim();

        if (!email.isEmpty()) {
            account = new Account();
            account.setEmail(email);
            account.setPassword("");
        }
        if (getActivity() instanceof UserActivity) {
            ((UserActivity) getActivity()).setAccount(account);
            ((UserActivity) getActivity()).showSignUpFragment();
        }
    }

    private void signIn() {
        if (getActivity() instanceof UserActivity) {
            ((UserActivity) getActivity()).showSignInFragment();
        }
    }
}
