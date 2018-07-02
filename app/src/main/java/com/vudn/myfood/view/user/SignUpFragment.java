package com.vudn.myfood.view.user;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vudn.myfood.R;
import com.vudn.myfood.model.user.Account;

import static android.content.Context.MODE_PRIVATE;

public class SignUpFragment extends Fragment implements View.OnClickListener {
    private Button btnSignUp;
    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtCfmPassword;
    private TextView btnSignIn;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    private Account account;
    public SignUpFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeComponents(view);
        registerListeners();
    }

    private void initializeComponents(View view) {
        sharedPreferences = getActivity().getSharedPreferences("luudangnhap", MODE_PRIVATE);
        progressDialog = new ProgressDialog(getContext());
        btnSignUp = view.findViewById(R.id.btn_sign_up);
        edtEmail = view.findViewById(R.id.edt_email);
        edtCfmPassword = view.findViewById(R.id.edt_confirm_pasword);
        edtPassword = view.findViewById(R.id.edt_password);
        btnSignIn = view.findViewById(R.id.txt_sign_in);
        syncData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            syncData();
        }
    }

    private void syncData(){
        if (getActivity() instanceof UserActivity){
            if (((UserActivity) getActivity()).getAccount() != null){
                account = ((UserActivity) getActivity()).getAccount();
                edtEmail.setText(account.getEmail());
                edtPassword.setText(account.getPassword());
            }
        }
    }

    private void registerListeners() {
        btnSignUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_up:
                signUp();
                break;

            case R.id.txt_sign_in:
                signIn();
                break;

            default:
                break;
        }
    }

    private void signUp() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String cfmPassword = edtCfmPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || cfmPassword.isEmpty()){
            Toast.makeText(getContext(), getResources().getString(R.string.thongbaoloidangky), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(getContext(),getString(R.string.thongbaoemailkhonghople), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(cfmPassword)){
            Toast.makeText(getContext(), getResources().getString(R.string.thongbaonhaplaimatkhau), Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6){
            Toast.makeText(getContext(), "Mật khẩu quá ngắn, vui lòng chọn mật khẩu có tối thiểu 6 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }
        account = new Account();
        account.setEmail(email);
        account.setPassword(password);
        ((UserActivity) getActivity()).setAccount(account);
        progressDialog.setMessage(getString(R.string.dangxuly));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), getString(R.string.thongbaodangkythanhcong), Toast.LENGTH_SHORT).show();
                    if (getActivity() instanceof UserActivity){
                        FirebaseUser user = task.getResult().getUser();
                        ((UserActivity) getActivity()).getUserPresenter().signUp(user);
                        //((UserActivity) getActivity()).showSignInFragment();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Tài khoản đã tồn tại!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signIn() {
        if (getActivity() instanceof UserActivity){
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            if(!email.isEmpty()){
                account = new Account();
                account.setEmail(email);
                account.setPassword(password);
                ((UserActivity) getActivity()).setAccount(account);
            }
            ((UserActivity) getActivity()).showSignInFragment();
        }
    }

}
