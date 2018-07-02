package com.vudn.myfood.view.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vudn.myfood.R;
import com.vudn.myfood.base.BaseActivity;
import com.vudn.myfood.model.user.Account;
import com.vudn.myfood.model.user.ThanhVienModel;
import com.vudn.myfood.presenter.user.UserPresenter;
import com.vudn.myfood.presenter.user.UserPresenterImpl;
import com.vudn.myfood.view.main.MainActivity;

public class UserActivity extends BaseActivity implements UserView {
    private static final int INDEX_LOGIN = 0;
    private static final int INDEX_REGISTER = 1;
    private static final int INDEX_RESET_PASSWORD = 2;
    public static final String INTENT_USER = "intent_user";

    private UserPresenter userPresenter;
    private static int CURRENT_INDEX = INDEX_LOGIN;

    private SignInFragment signInFragment;
    private SignUpFragment signUpFragment;
    private ResetPasswordFragment resetPasswordFragment;

    private Account account;

    SharedPreferences sharedPreferencesDangNhap;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userPresenter = new UserPresenterImpl(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public UserPresenter getUserPresenter() {
        return userPresenter;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_user;
    }

    @Override
    public void initializeComponents() {
        sharedPreferencesDangNhap = getSharedPreferences("luudangnhap", MODE_PRIVATE);
        showSignInFragment();
    }

    @Override
    public void registerListeners() {

    }

    public void showSignInFragment() {
        if (signInFragment == null) {
            signInFragment = new SignInFragment();
        }
        if (!signInFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frm_view, signInFragment)
                    .show(signInFragment)
                    .commit();
        } else {
            if (signUpFragment != null && !signUpFragment.isHidden()) {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                        .hide(signUpFragment)
                        .show(signInFragment)
                        .commit();
            }
            if (resetPasswordFragment != null && !resetPasswordFragment.isHidden()) {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                        .hide(resetPasswordFragment)
                        .show(signInFragment)
                        .commit();
            }
        }

        CURRENT_INDEX = INDEX_LOGIN;
        invalidateOptionsMenu();
    }

    public void showSignUpFragment() {
        if (signUpFragment == null) {
            signUpFragment = new SignUpFragment();
        }

        if (!signUpFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                    .add(R.id.frm_view, signUpFragment)
                    .show(signUpFragment)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                    .show(signUpFragment)
                    .commit();
        }
        if (signInFragment != null && !signInFragment.isHidden()) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                    .hide(signInFragment)
                    .commit();
        }
        if (resetPasswordFragment != null && !resetPasswordFragment.isHidden()) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                    .hide(resetPasswordFragment)
                    .commit();
        }
        CURRENT_INDEX = INDEX_REGISTER;
        invalidateOptionsMenu();
    }

    public void showResetPasswordFragment() {
        if (resetPasswordFragment == null) {
            resetPasswordFragment = new ResetPasswordFragment();
        }

        if (!resetPasswordFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                    .add(R.id.frm_view, resetPasswordFragment)
                    .hide(signInFragment)
                    .show(resetPasswordFragment)
                    .commit();
        } else {
            if (signInFragment != null && !signInFragment.isHidden()) {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .hide(signInFragment)
                        .show(resetPasswordFragment)
                        .commit();
            }
        }

        CURRENT_INDEX = INDEX_RESET_PASSWORD;
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*if (CURRENT_INDEX == INDEX_REGISTER) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        return true;*/
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (CURRENT_INDEX == INDEX_LOGIN) {
            Intent intent = new Intent();
//        intent.putExtra(INTENT_USER, thanhVienModel);
            setResult(RESULT_CANCELED, intent);
            //Toast.makeText(this, "onSuccess... setResult rồi", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        } else {
            showSignInFragment();
        }
    }

    @Override
    public void onSuccess(ThanhVienModel thanhVienModel) {
        SharedPreferences.Editor editor = sharedPreferencesDangNhap.edit();
        editor.putString("mauser", thanhVienModel.getMathanhvien());
        editor.putString("hinhanh", thanhVienModel.getHinhanh());
        editor.putString("diachi", thanhVienModel.getDiachi());
        editor.putString("email", thanhVienModel.getEmail());
        editor.putBoolean("islogin", true);
        editor.putString("hoten", thanhVienModel.getHoten());
        editor.putString("sodienthoai", thanhVienModel.getSodienthoai());
        editor.commit();
        Toast.makeText(this, "onSuccess ..." + thanhVienModel.getHoten(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
//        intent.putExtra(INTENT_USER, thanhVienModel);
        setResult(MainActivity.RESULT_OK, intent);
        //Toast.makeText(this, "onSuccess... setResult rồi", Toast.LENGTH_SHORT).show();
        this.finish();
    }

    @Override
    public void onFailure(String error) {
        Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
