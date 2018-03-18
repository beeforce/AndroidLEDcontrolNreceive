package com.example.ggg.myapplication;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by GGG on 19/3/2561.
 */

public class ControlPresenter implements ControlContract.presenter {

    private ControlContract.view mView;
    private Context mContext;

    public ControlPresenter(Context context,ControlContract.view view){
        this.mView = view;
        this.mContext = context;
    }


    @Override
    public void initTime() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        String date = dateFormat.format(new Date());
        String time = timeFormat.format(new Date());
        mView.setTime(date,time);
    }
}
