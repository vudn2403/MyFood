package com.vudn.myfood.adapter.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.vudn.myfood.view.main.BookmarkFragment;
import com.vudn.myfood.view.main.HomeFragment;
import com.vudn.myfood.view.main.ProfileFragment;
import com.vudn.myfood.view.main.OrderListFragment;

public class MainMenuAdapter extends FragmentStatePagerAdapter {
    private static final int POSITION_HOME_PAGER = 0;
    private static final int POSITION_PROFILE_PAGER = 3;
    private static final int POSITION_BOOKMARK_PAGER = 1;
    private static final int POSITION_ORDERS_PAGER = 2;
    private int numberOfTabs;
    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;
    private BookmarkFragment bookmarkFragment;
    private OrderListFragment ordersFragment;

    public MainMenuAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
        if (homeFragment == null){
            homeFragment = new HomeFragment();
        }
        if(profileFragment == null){
            profileFragment = new ProfileFragment();
        }
        if (bookmarkFragment == null){
            bookmarkFragment = new BookmarkFragment();
        }
        if(ordersFragment == null){
            ordersFragment = new OrderListFragment();
        }
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        switch (position) {
            case POSITION_HOME_PAGER:
                frag = homeFragment;
                break;

            case POSITION_PROFILE_PAGER:
                frag = profileFragment;
                break;

            case POSITION_BOOKMARK_PAGER:
                frag = bookmarkFragment;
                break;

            case POSITION_ORDERS_PAGER:
                frag = ordersFragment;
                break;

            default:
                break;
        }
        return frag;
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

}
