package com.mcc.tv.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.mcc.tv.R;
import com.mcc.tv.utility.ActivityUtils;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SplashActivity extends AppCompatActivity {

    // init variables
    private Activity mActivity;
    private static final int SPLASH_DURATION = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // to view full screen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initVariables();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initFunctionality();
    }

    private void initVariables() {
        mActivity = SplashActivity.this;
    }

    private void initView() {
        setContentView(R.layout.activity_splash);
    }

    private void initFunctionality() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityUtils.getInstance().invokeActivity(mActivity, MainActivity.class, true);
            }
        }, SPLASH_DURATION);
    }
}