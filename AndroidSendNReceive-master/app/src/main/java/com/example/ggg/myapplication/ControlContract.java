package com.example.ggg.myapplication;

import android.widget.ImageView;

/**
 * Created by GGG on 19/3/2561.
 */

public interface ControlContract {

    interface view {

        void setTime(String date, String time);

        void setActive(boolean isActive, ImageView imageView);

    }

    interface presenter {

        void initTime();

    }

}
