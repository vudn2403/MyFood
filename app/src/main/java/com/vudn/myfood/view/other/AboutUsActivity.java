package com.vudn.myfood.view.other;

import android.view.Menu;
import android.view.MenuItem;

import com.vudn.myfood.R;
import com.vudn.myfood.base.BaseActivity;

public class AboutUsActivity extends BaseActivity {
    @Override
    protected int getContentView() {
        return R.layout.activity_about_us;
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
        getSupportActionBar().setTitle("Giới thiệu về chúng tôi");
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
