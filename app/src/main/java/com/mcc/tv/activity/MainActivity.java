package com.mcc.tv.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.mcc.tv.BuildConfig;
import com.mcc.tv.data.constant.AppConstants;
import com.mcc.tv.listeners.ItemClickListener;
import com.mcc.tv.model.Program;
import com.mcc.tv.R;
import com.mcc.tv.adapter.ProgramListAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.mcc.tv.utility.AppUtility;
import com.mcc.tv.utility.DataUtilities;
import com.mcc.tv.utility.ExoPlayerUtilities;
import com.mcc.tv.utility.YoutubePlayerUtilities;


import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Activity mActivity;
    //Views
    private RecyclerView recyclerView;
    private SimpleExoPlayerView exoPlayerView;
    private FloatingActionButton playButton;
    private TextView seeAllTextView;
    private ImageView fullscreenButton;
    private Dialog mFullScreenDialog;

    //Data
    private ArrayList<Program> programList;
    private ProgramListAdapter programListAdapter;

    private ExoPlayerUtilities exoplayerUtilities;
    private YoutubePlayerUtilities youtubePlayerUtilities;

    //Flags
    private boolean isYoutubePlaying;
    private boolean mExoPlayerFullscreen;
    private boolean isDialogOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariable();
        initView();
        initFunctionality();
        initListeners();
        loadData();
        initTVPlayer(AppConstants.TV_SOURCE);
    }

    private void initVariable() {
        programList = new ArrayList<>();
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mActivity = MainActivity.this;

        initToolbar();
        initDrawer();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        playButton = (FloatingActionButton) findViewById(R.id.playButton);
        seeAllTextView = (TextView) findViewById(R.id.seeAllTextView);
        fullscreenButton = (ImageView) findViewById(R.id.fullscreenButton);
    }

    private void initFunctionality() {
        programListAdapter = new ProgramListAdapter(programList, getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(programListAdapter);
    }

    private void initListeners() {
        playButton.setOnClickListener(this);
        seeAllTextView.setOnClickListener(this);
        fullscreenButton.setOnClickListener(this);
    }

    private void loadData() {
        programList.addAll(DataUtilities.getProgramList(getApplicationContext()));
        programListAdapter.notifyDataSetChanged();
    }
    //endregion

    //region Methods for initializing TV player
    private void initTVPlayer(String url) {
        if (url != null) {
            if (url.contains("http")) {
                findViewById(R.id.youtubeFragment).setVisibility(View.GONE);
                isYoutubePlaying = false;
                initFullscreenDialog();
                exoPlayerView = (SimpleExoPlayerView) findViewById(R.id.exo_player_view);
                exoplayerUtilities = new ExoPlayerUtilities(exoPlayerView);
                exoplayerUtilities.initExoplayerLive(url, getApplicationContext());
                playButton.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_pause));
            } else {
                findViewById(R.id.exo_player_view).setVisibility(View.GONE);
                findViewById(R.id.fullscreenButton).setVisibility(View.GONE);
                findViewById(R.id.playButton).setVisibility(View.GONE);
                isYoutubePlaying = true;
                YouTubePlayerFragment youtubeFragment = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtubeFragment);
                youtubePlayerUtilities = new YoutubePlayerUtilities(youtubeFragment);
                youtubePlayerUtilities.initYoutubePlayer(url);
            }
        } else {
            Toast.makeText(this, "The url is not valid!", Toast.LENGTH_LONG).show();
        }
    }

    public void playPause() {
        if (isYoutubePlaying) {
            youtubePlayerUtilities.playPauseYoutubePlayer();
        } else {
            if (exoplayerUtilities.isPlaying()) {
                playButton.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_play));
            } else {
                playButton.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_pause));
            }
            exoplayerUtilities.playPauseExoPlayer();
        }
    }
    //endregion

    private void initFullscreenDialog() {
        mExoPlayerFullscreen = false;
        mFullScreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                switchScreen();
                super.onBackPressed();
            }
        };
    }

    private void openFullscreenDialog() {
        exoPlayerView.setUseController(true);
        ((ViewGroup) exoPlayerView.getParent()).removeView(exoPlayerView);
        mFullScreenDialog.addContentView(exoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenDialog.show();
        mExoPlayerFullscreen = true;
    }

    private void closeFullscreenDialog() {
        exoPlayerView.setUseController(false);
        ((ViewGroup) exoPlayerView.getParent()).removeView(exoPlayerView);
        ((RelativeLayout) findViewById(R.id.player_container)).addView(exoPlayerView);
        mFullScreenDialog.dismiss();
        mExoPlayerFullscreen = false;
    }

    private void switchScreen() {
        if (mExoPlayerFullscreen) {
            fullscreenButton.setImageResource(R.drawable.ic_fullscreen);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            //closeFullscreenDialog();
        } else {
            fullscreenButton.setImageResource(R.drawable.ic_fullscreen_exit);
            //openFullscreenDialog();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }
    //endregion

    public void seeAll() {
        if (isYoutubePlaying) {

        } else {
            if (exoplayerUtilities.isPlaying() && !isDialogOpen) {
                playButton.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_pause));
                exoplayerUtilities.pauseExoplayer();
            }
        }
        Intent intent = new Intent(this, AllProgramActivity.class);
        startActivity(intent);

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.playButton:
                playPause();
                break;
            case R.id.seeAllTextView:
                seeAll();
                break;
            case R.id.fullscreenButton:
                switchScreen();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!AppUtility.isNetworkAvailable(this)) {
            AppUtility.noInternetWarning(findViewById(R.id.drawer_layout), this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (!isYoutubePlaying) {
            playButton.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_play));
            exoplayerUtilities.pauseExoplayer();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (!isYoutubePlaying) {
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                openFullscreenDialog();
            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                closeFullscreenDialog();
            }
        }
    }
}
