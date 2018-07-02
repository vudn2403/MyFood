package com.vudn.myfood.view.other;

import android.view.Menu;
import android.view.MenuItem;

import com.vudn.myfood.R;
import com.vudn.myfood.base.BaseActivity;

public class PolicyActivity extends BaseActivity {
    @Override
    protected int getContentView() {
        return R.layout.activity_policy;
    }

    @Override
    protected void initializeComponents() {

    }

    @Override
    protected void registerListeners() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chính sách sử dụng");
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
}
