package com.mcc.tv.utility;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.mcc.tv.data.constant.AppConstants;


public class YoutubePlayerUtilities {
    private YouTubePlayerFragment youtubeFragment;
    public YoutubePlayerUtilities(YouTubePlayerFragment youTubePlayerFragment){
        youtubeFragment = youTubePlayerFragment;
    }
    public void initYoutubePlayer(final String url){
        youtubeFragment.initialize(AppConstants.YOUTUBE_API_KEY,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {
                        youTubePlayer.loadVideo(url);
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });

    }
    public void playPauseYoutubePlayer(){

    }
}
