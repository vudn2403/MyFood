package com.vudn.myfood.view.user;

import com.vudn.myfood.model.user.ThanhVienModel;

public interface UserView {
    void onSuccess(ThanhVienModel thanhVienModel);
    void onFailure(String error);
}
