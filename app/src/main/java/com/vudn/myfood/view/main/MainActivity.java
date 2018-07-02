package com.vudn.myfood.view.main;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.google.firebase.auth.FirebaseAuth;
import com.vudn.myfood.R;
import com.vudn.myfood.adapter.main.MainMenuAdapter;
import com.vudn.myfood.base.BaseActivity;
import com.vudn.myfood.base.Key;
import com.vudn.myfood.model.restaurant.QuanAnModel;

import com.vudn.myfood.receiver.NetworkChangeReceiver;
import com.vudn.myfood.util.ViewPagerTransformer;
import com.vudn.myfood.view.restaurant.ThemQuanAnActivity;
import com.vudn.myfood.view.other.AboutUsActivity;
import com.vudn.myfood.view.other.PolicyActivity;
import com.vudn.myfood.view.search.SearchActivity;
import com.vudn.myfood.view.user.UserActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "MainActivity";
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 10001;
    //View pager tab
    private static final int INDEX_HOME = 0;
    private static final int INDEX_PROFILE = 3;
    private static final int INDEX_BOOKMARK = 1;
    private static final int INDEX_ORDERS = 2;
    private static int CURRENT_INDEX = INDEX_HOME;

    //Drawer layout ft Toolbar ft Navigation view
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navMenu;

    //Nav header
    private View navHeader;
    private ImageView imgBackground;
    private CircleImageView imgAvatar;
    private TextView txtName;
    private TextView txtPhone;
    private MenuItem btnSignOut;
    private MenuItem btnDeXuat;

    //Tab layout ft View pager
    private MainMenuAdapter mainMenuAdapter;
    private ViewPager vpgMainMenu;
    private TabLayout tabPagerTitle;

    private SearchView searchView;
    private HomeFragment homeFragment;
    //Shared
    private SharedPreferences sharedPreferences;

    private List<QuanAnModel> quanAnModels;

    NetworkChangeReceiver networkChangeReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void initializeComponents() {
        quanAnModels = new ArrayList<>();
        //get Shared
        sharedPreferences = getSharedPreferences("luudangnhap", MODE_PRIVATE);

        //init drawer layout ft navigation view
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navMenu = findViewById(R.id.nav_menu);

        //init tab layout ft view pager
        vpgMainMenu = findViewById(R.id.vpg_main_menu);
        tabPagerTitle = findViewById(R.id.tbl_main_title_menu);

        //init navigation header
        initializeNavHeader();

        //setup nav, view pager, action toggle
        setUpMainViewPager();
        addActionBarToggle();

        homeFragment = (HomeFragment) mainMenuAdapter.getItem(INDEX_HOME);

        NetworkChangeReceiver receiver = new NetworkChangeReceiver();
        final IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(receiver, filter);
    }

    @Override
    public void registerListeners() {
        setUpNavigationViewListener();
    }

    private void initializeNavHeader() {
        navHeader = navMenu.getHeaderView(0);
        imgBackground = navHeader.findViewById(R.id.img_background);
        imgAvatar = navHeader.findViewById(R.id.img_avatar);
        txtName = navHeader.findViewById(R.id.txt_name);
        txtPhone = navHeader.findViewById(R.id.txt_phone);
        btnSignOut = navMenu.getMenu().findItem(R.id.nav_sign_out);
        btnDeXuat = navMenu.getMenu().findItem(R.id.nav_de_xuat_quan_an);
        imgAvatar.setOnClickListener(this);
        setUpNavHeader();
    }

    private void setUpNavHeader() {
        if (sharedPreferences.getBoolean("islogin", false)) {
            Glide.with(this).load(sharedPreferences.getString("hinhanh", "")).into(imgAvatar);
            txtName.setText(sharedPreferences.getString("hoten", ""));
            txtPhone.setText(sharedPreferences.getString("sodienthoai", ""));
            btnSignOut.setVisible(true);
            btnDeXuat.setVisible(true);

        } else {
            Glide.with(this).load(R.drawable.ic_user_2).into(imgAvatar);
            txtName.setText("Đăng nhập");
            txtPhone.setText("");
            btnSignOut.setVisible(false);
            btnDeXuat.setVisible(false);
        }
    }

    private void setUpNavigationViewListener() {
        navMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_home:
                        drawerLayout.closeDrawers();
                        vpgMainMenu.setCurrentItem(INDEX_HOME);
                        return true;

                    case R.id.nav_profile:
                        drawerLayout.closeDrawers();
                        vpgMainMenu.setCurrentItem(INDEX_PROFILE);
                        return true;

                    case R.id.nav_bookmark:
                        drawerLayout.closeDrawers();
                        vpgMainMenu.setCurrentItem(INDEX_BOOKMARK);
                        return true;

                    case R.id.nav_orders:
                        drawerLayout.closeDrawers();
                        vpgMainMenu.setCurrentItem(INDEX_ORDERS);
                        return true;

                    case R.id.nav_de_xuat_quan_an:
                        Intent iDeXuat = new Intent(MainActivity.this, ThemQuanAnActivity.class);
                        startActivity(iDeXuat);
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.nav_about_us:
                        //TODO
                        Intent intentAbout = new Intent(MainActivity.this, AboutUsActivity.class);
                        startActivity(intentAbout);
                        drawerLayout.closeDrawers();

                        return true;

                    case R.id.nav_privacy_policy:
                        //TODO
                        Intent intentPolicy = new Intent(MainActivity.this, PolicyActivity.class);
                        startActivity(intentPolicy);
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.nav_sign_out:
                        signOut();
                        drawerLayout.closeDrawers();
                        return true;

                    default:
                        drawerLayout.closeDrawers();
                        return true;
                }
            }
        });
    }

    private void addActionBarToggle() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle
                (this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                navMenu.getMenu().getItem(vpgMainMenu.getCurrentItem()).setChecked(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    private void setUpMainViewPager() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mainMenuAdapter = new MainMenuAdapter(fragmentManager, 4);
        vpgMainMenu.setAdapter(mainMenuAdapter);
        vpgMainMenu.setOffscreenPageLimit(3);
        vpgMainMenu.setOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(tabPagerTitle));
        vpgMainMenu.setPageTransformer(true, new ViewPagerTransformer());
        vpgMainMenu.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                invalidateOptionsMenu();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabPagerTitle.setupWithViewPager(vpgMainMenu);
        tabPagerTitle.setTabsFromPagerAdapter(mainMenuAdapter);
        tabPagerTitle.getTabAt(INDEX_HOME).setIcon(R.drawable.ic_home);
        tabPagerTitle.getTabAt(INDEX_PROFILE).setIcon(R.drawable.ic_profile);
        tabPagerTitle.getTabAt(INDEX_BOOKMARK).setIcon(R.drawable.ic_bookmark);
        tabPagerTitle.getTabAt(INDEX_ORDERS).setIcon(R.drawable.ic_orders);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) menuItem.getActionView();
        setUpSearchView();
        return true;
    }

    private void setUpSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        ComponentName componentName = new ComponentName(getBaseContext(), MainActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
        searchView.setIconifiedByDefault(false);
        searchView.setFocusable(false);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    searchView.clearFocus();
                    //searchView.setFocusable(false);
                    Intent iSearch = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(iSearch);
                }
            }
        });
        /*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    searchView.clearFocus();
                    Log.d(TAG, "onQueryTextSubmit: Search key " + query);
                    if (vpgMainMenu.getCurrentItem() != INDEX_HOME) {
                        vpgMainMenu.setCurrentItem(INDEX_HOME);
                    }
                    homeFragment.searchRestaurant(query);
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });*/
    }

    public void signIn() {
        Intent iDangNhap = new Intent(MainActivity.this, UserActivity.class);
        startActivityForResult(iDangNhap, Key.RC_SIGN_IN);
    }

    public void signOut() {
        final android.support.v7.app.AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Light_Dialog_NoActionBar);
        } else {
            builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
        }
        builder.setTitle("Bạn muốn đăng xuất?")
                .setCancelable(false)
                .setPositiveButton("Đồng ý".toUpperCase(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        FirebaseAuth.getInstance().signOut();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("islogin", false);
                        editor.commit();
                        updateUI();
                    }
                })
                .setNegativeButton("Hủy".toUpperCase(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.d(TAG, "onActivityResult: Chạy vào activity result");
        if (requestCode == Key.RC_SIGN_IN) {
            //Log.d(TAG, "onActivityResult: kiểm tra đúng request code");
            if (resultCode == MainActivity.RESULT_OK) {
                //Log.d(TAG, "onActivityResult: kiểm tra đúng result code");
                //ThanhVienModel thanhVienModel = data.getParcelableExtra(UserActivity.INTENT_USER);
                updateUI();
                //Log.d(TAG, "onActivityResult: lấy thanhvien ra" + thanhVienModel.getMathanhvien());
            } else {
                updateUI();
            }
        }
        if (requestCode == Key.RC_ORDER) {
            if (resultCode == RESULT_OK) {
                if (mainMenuAdapter.getItem(INDEX_ORDERS) instanceof OrderListFragment) {
                    OrderListFragment orderListFragment = (OrderListFragment) mainMenuAdapter.getItem(INDEX_ORDERS);
                    orderListFragment.updateUI();
                }
            }
        }

        if (requestCode == Key.RC_WIFI){
            //chechConnect();
        }
    }

    public void updateUI() {
        setUpNavHeader();
        if (mainMenuAdapter.getItem(INDEX_BOOKMARK) instanceof BookmarkFragment) {
            BookmarkFragment bookmarkFragment = (BookmarkFragment) mainMenuAdapter.getItem(INDEX_BOOKMARK);
            bookmarkFragment.updateUI();
        }

        if (mainMenuAdapter.getItem(INDEX_ORDERS) instanceof OrderListFragment) {
            OrderListFragment orderListFragment = (OrderListFragment) mainMenuAdapter.getItem(INDEX_ORDERS);
            orderListFragment.updateUI();
        }

        if (mainMenuAdapter.getItem(INDEX_PROFILE) instanceof ProfileFragment) {
            ProfileFragment profileFragment = (ProfileFragment) mainMenuAdapter.getItem(INDEX_PROFILE);
            profileFragment.updateUI();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        chechConnect();
    }

    private void chechConnect(){
        if (NetworkChangeReceiver.isConnected()){

        }else {
            final AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_NoActionBar);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("Vui lòng kiểm tra kết nối Internet của bạn!")
                    .setCancelable(false)
                    .setPositiveButton("Đồng ý".toUpperCase(), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            startActivityForResult(intent, Key.RC_WIFI);
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            Log.d(TAG, "onBackPressed: isDrawOpen" + vpgMainMenu.getCurrentItem());
            drawerLayout.closeDrawers();
            return;
        }

        Log.d(TAG, "onBackPressed: " + vpgMainMenu.getCurrentItem());
        if (vpgMainMenu.getCurrentItem() == INDEX_HOME) {
            final AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_NoActionBar);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("Bạn muốn thoát chương trình?")
                    .setCancelable(false)
                    .setPositiveButton("Đồng ý".toUpperCase(), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            MainActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("Hủy".toUpperCase(), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        } else {
            vpgMainMenu.setCurrentItem(INDEX_HOME);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_avatar:
                signIn();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (networkChangeReceiver != null){
            unregisterReceiver(networkChangeReceiver);
        }
        super.onDestroy();
    }
}