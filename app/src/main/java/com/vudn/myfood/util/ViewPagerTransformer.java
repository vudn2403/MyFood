package com.vudn.myfood.util;

import android.support.v4.view.ViewPager;
import android.view.View;

public class ViewPagerTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View view, float position) {
        float MIN_SCALE = 0.75f;
        int pageWidth = view.getWidth();

        if (position < -1) { // [-Infinity,-1)
            // Ẩn page
            view.setAlpha(0);

        } else if (position <= 0) { // [-1,0]
            // Sử dụng kiểu chuyển màn hình mặc định cho page -1, 0
            view.setAlpha(1);
            view.setTranslationX(0);
            view.setScaleX(1);
            view.setScaleY(1);

        } else if (position <= 1) { // (0,1]
            // Làm mờ page
            view.setAlpha(1 - position);

            // Thay đổi kiểu chuyển màn hình mặc định
            view.setTranslationX(pageWidth * -position);

            // Scale page phía dưới (between MIN_SCALE and 1)
            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

        } else { // (1,+Infinity]
            // Ẩn page
            view.setAlpha(0);
        }
    }
}
