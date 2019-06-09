package com.mcc.tv.utility;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class ExoPlayerUtilities {
    public SimpleExoPlayer exoPlayer;
    public SimpleExoPlayerView exoPlayerView;

    public ExoPlayerUtilities(SimpleExoPlayerView exoView){
        exoPlayerView = exoView;
    }

    public void initExoplayerLive(String url, Context context){
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoFactory);
        LoadControl loadControl = new DefaultLoadControl();

        exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);

        Handler mHandler = new Handler();
        String userAgent = Util.getUserAgent(context, "User Agent");
        DataSource.Factory dataSourceFactory = new DefaultHttpDataSourceFactory(
                userAgent, null,
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                1800000,
                true);
        HlsMediaSource mediaSource = new HlsMediaSource(Uri.parse(url), dataSourceFactory, 1800000,
                mHandler, null);
        exoPlayerView.setPlayer(exoPlayer);
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.prepare(mediaSource);
    }
    public void playPauseExoPlayer(){
        exoPlayer.setPlayWhenReady(!exoPlayer.getPlayWhenReady());
    }
    public void pauseExoplayer(){
        if(exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false);
        }
    }
    public boolean isPlaying() {
        return exoPlayer != null && exoPlayer.getPlayWhenReady();
    }

    public void closeExoPlayer() {
        if(exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
        }
    }
}
