package com.vudn.myfood.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.vudn.myfood.R;

public class LienHeNhaPhatTrienDialog extends Dialog{
    public LienHeNhaPhatTrienDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_lien_he_npt);
        try {
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }
}
