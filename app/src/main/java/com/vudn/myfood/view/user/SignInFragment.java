package com.vudn.myfood.view.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.vudn.myfood.R;
import com.vudn.myfood.model.user.Account;
import com.vudn.myfood.presenter.user.UserPresenter;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class SignInFragment extends Fragment
        implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        FirebaseAuth.AuthStateListener {
    public static final String TAG = "SignInFragment";
    public static int REQUEST_CODE_SIGN_IN_GOOGLE = 99;

    private Button btnSignInGoogle;
    private Button btnSignInFacebook;
    private Button btnSignIn;
    private TextView txtDangKyMoi, txtQuenMatKhau;
    private EditText edtEmail, edtPassword;
    private ProgressDialog progressDialog;

    private LoginManager loginManager;
    private CallbackManager callbackManagerFacebook;
    private List<String> facebookPermission;
    private Account account;

    private GoogleSignInOptions mSignInOptions;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount mGoogleAccount;
    //private GoogleApiClient googleApiClient;

    public SignInFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preparingForLogin();
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    private void preparingForLogin(){
        facebookPermission = Arrays.asList("email", "public_profile");
        loginManager = LoginManager.getInstance();
        callbackManagerFacebook = CallbackManager.Factory.create();
        loginManager.registerCallback(callbackManagerFacebook, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String idToken = loginResult.getAccessToken().getToken();
                Log.d(TAG, "onSuccess: Id Token Facebook" + idToken);
                signInFireBaseUseFacebook(idToken);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel: Login Facebook failure");
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getContext(), "" + error, Toast.LENGTH_SHORT).show();
            }
        });

        mSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), mSignInOptions);
        mGoogleAccount = GoogleSignIn.getLastSignedInAccount(getContext());
        if (mGoogleAccount != null){
            mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    // ...
                }
            });
            mGoogleSignInClient.revokeAccess().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    // ...
                }
            });
        }
        /*if (googleApiClient == null){
            googleApiClient = new GoogleApiClient.Builder(getContext())
                    .enableAutoManage(getActivity(), this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                    .build();
        }*/
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeComponents(view);
        registerListeners();
    }

    private void initializeComponents(View view) {
        progressDialog = new ProgressDialog(getContext());
        btnSignInGoogle = view.findViewById(R.id.btn_sign_in_google);
        btnSignInFacebook = view.findViewById(R.id.btn_sign_in_facebook);
        txtDangKyMoi = view.findViewById(R.id.txt_sign_up);
        txtQuenMatKhau = view.findViewById(R.id.txt_reset_pasword);
        btnSignIn = view.findViewById(R.id.btn_sign_in);
        edtEmail = view.findViewById(R.id.edt_email);
        edtPassword = view.findViewById(R.id.edt_password);
    }

    private void registerListeners() {
        btnSignInGoogle.setOnClickListener(this);
        btnSignInFacebook.setOnClickListener(this);
        txtDangKyMoi.setOnClickListener(this);
        txtQuenMatKhau.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            syncData();
        }
    }

    private void syncData() {
        if (getActivity() instanceof UserActivity) {
            if (((UserActivity) getActivity()).getAccount() != null) {
                account = ((UserActivity) getActivity()).getAccount();
                edtEmail.setText(account.getEmail());
                edtPassword.setText(account.getPassword());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in_google:
                signInGoogle();
                break;

            case R.id.btn_sign_in_facebook:
                signInFacebook();
                break;

            case R.id.txt_sign_up:
                signUp();
                break;

            case R.id.btn_sign_in:
                signIn();
                break;

            case R.id.txt_reset_pasword:
                resetPassword();
                break;

            default:
                break;
        }
    }

    private void resetPassword() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (getActivity() instanceof UserActivity) {
            if (!email.isEmpty()) {
                account = new Account();
                account.setEmail(email);
                account.setPassword(password);
                ((UserActivity) getActivity()).setAccount(account);
            }
            ((UserActivity) getActivity()).showResetPasswordFragment();
        }
    }

    private void signIn() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Tài khoản và mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(getContext(), "Mật khẩu vừa nhập ít hơn 6 ký tự, vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show();
            return;
        }

        showProgressDialog(true);

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    showProgressDialog(false);
                    Toast.makeText(getContext(), getString(R.string.thongbaodangnhapthatbai), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signInFacebook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn){
            loginManager.logOut();
        }
        loginManager.logInWithReadPermissions(this, facebookPermission);
        Toast.makeText(getContext(), "Đăng nhập Facebook", Toast.LENGTH_SHORT).show();
    }

    /*private GraphRequest.GraphJSONArrayCallback mGraphRequest = new GraphRequest.GraphJSONArrayCallback() {
        @Override
        public void onCompleted(JSONArray objects, GraphResponse response) {
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/

    private void signInFireBaseUseFacebook(String idToken) {
        showProgressDialog(true);
        AuthCredential credential = FacebookAuthProvider.getCredential(idToken);
        FirebaseAuth.getInstance().signInWithCredential(credential);
    }

    private void signInGoogle() {
        Intent iSignInGoogle = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(iSignInGoogle, REQUEST_CODE_SIGN_IN_GOOGLE);
    }

    private void signUp() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (getActivity() instanceof UserActivity) {
            if (!email.isEmpty()) {
                account = new Account();
                account.setEmail(email);
                account.setPassword(password);
                ((UserActivity) getActivity()).setAccount(account);
            }
            ((UserActivity) getActivity()).showSignUpFragment();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManagerFacebook.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SIGN_IN_GOOGLE) {
            if (resultCode == RESULT_OK) {
                /*GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                GoogleSignInAccount account = signInResult.getSignInAccount();*/
                /*String idToken = account.getIdToken();
                Log.d(TAG, "onActivityResult: " + idToken);
                SignInFireBaseUseGoogle(idToken);*/

                Task<GoogleSignInAccount> googleSignInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(googleSignInAccountTask);
            }
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            mGoogleAccount = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            String idToken = mGoogleAccount.getIdToken();
            SignInFireBaseUseGoogle(idToken);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void SignInFireBaseUseGoogle(String idToken) {
        showProgressDialog(true);
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth.getInstance().signInWithCredential(credential);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getContext(), getString(R.string.loiketnoi), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            showProgressDialog(false);
            Toast.makeText(getContext(), getString(R.string.thongbaodangnhapthanhcong), Toast.LENGTH_SHORT).show();
            /*Toast.makeText(getContext(), "" + user.getUid(), Toast.LENGTH_SHORT).show();
            Toast.makeText(getContext(), "" + user.getDisplayName(), Toast.LENGTH_SHORT).show();*/
            if (getActivity() instanceof UserActivity){
                ((UserActivity) getActivity()).getUserPresenter().signIn(user);
            }
        }
    }

    private void showProgressDialog(boolean isShow){
        if (isShow){
            progressDialog.setMessage(getString(R.string.dangxuly));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }else {
            progressDialog.dismiss();
        }

    }
    @Override
    public void onDestroy() {
        FirebaseAuth.getInstance().removeAuthStateListener(this);
        super.onDestroy();
    }
}
