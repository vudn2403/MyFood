package com.vudn.myfood.view.menu;

import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.vudn.myfood.R;
import com.vudn.myfood.adapter.menu.ThucDonQuanAnAdapter;
import com.vudn.myfood.base.BaseActivity;
import com.vudn.myfood.base.Key;
import com.vudn.myfood.model.restaurant.QuanAnModel;
import com.vudn.myfood.model.menu.ThucDonModel;

import java.util.ArrayList;
import java.util.List;

public class ThucDonActivity extends BaseActivity {
    ThucDonQuanAnAdapter thucDonQuanAnAdapter;
    ExpandableListView elvThucDon;
    QuanAnModel quanAnModel;
    List<ThucDonModel> thucDonModelList;

    @Override
    protected int getContentView() {
        return R.layout.activity_thuc_don;
    }

    @Override
    protected void initializeComponents() {

        quanAnModel = getIntent().getParcelableExtra(Key.INTENT_THUC_DON);
        if(quanAnModel == null || quanAnModel.getThucDons() == null){
            return;
        }
        getSupportActionBar().setTitle(quanAnModel.getTenquanan());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        elvThucDon = findViewById(R.id.elvThucDon);
        thucDonModelList = new ArrayList<>();
        thucDonModelList = quanAnModel.getThucDons();
        thucDonQuanAnAdapter = new ThucDonQuanAnAdapter(this, thucDonModelList);
        elvThucDon.setAdapter(thucDonQuanAnAdapter);
    }

    @Override
    protected void registerListeners() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return true;
    }
}
