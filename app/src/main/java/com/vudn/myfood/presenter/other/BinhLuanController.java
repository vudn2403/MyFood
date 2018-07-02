package com.vudn.myfood.presenter.other;



import com.vudn.myfood.model.comment.BinhLuanModel;

import java.util.List;

public class BinhLuanController {
    private BinhLuanModel binhLuanModel;

    public  BinhLuanController(){
        binhLuanModel = new BinhLuanModel();
    }

    public void ThemBinhLuan(String maQuanAn, BinhLuanModel binhLuanModel, List<String> listHinh){
        binhLuanModel.ThemBinhLuan(maQuanAn,binhLuanModel,listHinh);
    }
}
