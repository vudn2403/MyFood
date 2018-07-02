package com.vudn.myfood.presenter.user;

import com.google.firebase.auth.FirebaseUser;
import com.vudn.myfood.model.user.MyUser;
import com.vudn.myfood.model.user.ThanhVienModel;

public interface UserPresenter {
    void signUp(FirebaseUser user);
    void signIn(FirebaseUser user);
    void onSignUpSuccess(ThanhVienModel thanhVienModel);
    void onSignUpFailure(String error);
    void onSignInSuccess(ThanhVienModel thanhVienModel);
    void onSignInFailure(String error);
}
