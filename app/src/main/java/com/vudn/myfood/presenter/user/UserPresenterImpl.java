package com.vudn.myfood.presenter.user;

import com.google.firebase.auth.FirebaseUser;
import com.vudn.myfood.model.user.MyUser;
import com.vudn.myfood.model.user.ThanhVienModel;
import com.vudn.myfood.view.user.UserView;

public class UserPresenterImpl implements UserPresenter {
    private UserView userView;
    private ThanhVienModel thanhVienModel;

    public UserPresenterImpl(UserView userView) {
        this.userView = userView;
        thanhVienModel = new ThanhVienModel(this);
    }

    @Override
    public void signUp(FirebaseUser user) {
        thanhVienModel.signUp(user);
    }

    @Override
    public void signIn(FirebaseUser user) {
        thanhVienModel.signIn(user);
    }

    @Override
    public void onSignUpSuccess(ThanhVienModel thanhVienModel) {
        userView.onSuccess(thanhVienModel);
    }

    @Override
    public void onSignUpFailure(String error) {
        userView.onFailure(error);
    }

    @Override
    public void onSignInSuccess(ThanhVienModel thanhVienModel) {
        userView.onSuccess(thanhVienModel);
    }

    @Override
    public void onSignInFailure(String error) {
        userView.onFailure(error);
    }
}
