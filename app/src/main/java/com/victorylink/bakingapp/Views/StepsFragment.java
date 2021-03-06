package com.victorylink.bakingapp.Views;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.victorylink.bakingapp.DataModel.BackingResponse;
import com.victorylink.bakingapp.Prefrences.BakingSharedPreference;
import com.victorylink.bakingapp.R;
import com.victorylink.bakingapp.Views.BaseViews.BaseFragment;
import com.victorylink.bakingapp.utilities.BakingConstants;

import java.io.IOException;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by aayman on 6/4/2017.
 */

public class StepsFragment extends BaseFragment {
    static BackingResponse mBackingResponses;
    static int currentStepId = 0;
    static SimpleExoPlayer player;
    long currentPeriodMillisecond = 0;

    @BindView(R.id.btn_previous)
    Button previousStep;
    @BindView(R.id.btn_next)
    Button nextStep;
    @BindView(R.id.tv_step)
    TextView stepCount;
    @BindView(R.id.steps_description)
    TextView stepDescription;
    @BindView(R.id.player_view)
    SimpleExoPlayerView exoPlayerView;
    @BindView(R.id.relative_step_video_container)
    LinearLayout relativeLayoutContainer;
    @BindView(R.id.step_controller)
    LinearLayout linearController;
    @BindView(R.id.img_thumbnail)
    ImageView imagethumbnail;
    @BindBool(R.bool.isTablet)
    boolean isTab;

    public static StepsFragment newInstance(BackingResponse BackingResponses, int index) {
        mBackingResponses = BackingResponses;
        currentStepId = index;
        return new StepsFragment();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BakingConstants.CURRENT_SELECTED_ITEM, currentStepId);
        if (player != null) {
            outState.putLong(BakingConstants.CURRENT_PLAYER_PERIOD, player.getCurrentPosition());
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            currentStepId = savedInstanceState.getInt(BakingConstants.CURRENT_SELECTED_ITEM);
            currentPeriodMillisecond = savedInstanceState.getLong(BakingConstants.CURRENT_PLAYER_PERIOD);
            displayStepNo(currentStepId);
            try {
                player.seekTo(currentPeriodMillisecond);
            } catch (Exception e) {
                player.seekTo(0);

            }

        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.layout_video_steps;
    }

    @Override
    protected String getFragmentName() {
        return StepsFragment.class.getSimpleName();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        //this will reset video start for each new one
        BakingSharedPreference bakingSharedPreference = new BakingSharedPreference(getActivity());

        try {

            int videoID = Integer.parseInt(bakingSharedPreference.retrieveStringFromSharedPreference(BakingConstants.VIDEO_ID));
            if (videoID == mBackingResponses.getSteps().get(currentStepId).getId())
                currentPeriodMillisecond = Long.parseLong(bakingSharedPreference.retrieveStringFromSharedPreference(BakingConstants.CURRENT_PLAYER_PERIOD));
            else {
                currentPeriodMillisecond = 0;
                bakingSharedPreference.saveStringToSharedPreference(BakingConstants.CURRENT_PLAYER_PERIOD, "0");
                bakingSharedPreference.saveStringToSharedPreference(BakingConstants.VIDEO_ID, mBackingResponses.getId() + "");

            }
        } catch (Exception e) {
            currentPeriodMillisecond = 0;
            bakingSharedPreference.saveStringToSharedPreference(BakingConstants.CURRENT_PLAYER_PERIOD, "0");
            bakingSharedPreference.saveStringToSharedPreference(BakingConstants.VIDEO_ID, mBackingResponses.getId() + "");

        }

        if (savedInstanceState == null)
            setupView();
    }

    private void setupView() {

        if (isTab)
            linearController.setVisibility(View.GONE);
        else
            linearController.setVisibility(View.VISIBLE);
        displayStepNo(currentStepId);
        displayImgThumb(mBackingResponses.getSteps().get(currentStepId).getThumbnailURL());
        previousStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentStepId > 0) {
                    currentStepId--;
                    displayStepNo(currentStepId);
                } else
                    Toast.makeText(getActivity(), getResources().getString(R.string.firstStep), Toast.LENGTH_SHORT).show();

            }
        });
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentStepId < mBackingResponses.getSteps().size() - 1) {
                    currentStepId++;
                    displayStepNo(currentStepId);
                } else
                    Toast.makeText(getActivity(), getResources().getString(R.string.lastStep), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void displayImgThumb(String thumbnailURL) {
        if (thumbnailURL.length() > 0) {
            Glide.with(getActivity())
                    .load(thumbnailURL)
                    .centerCrop()
                    .placeholder(R.drawable.cooking)
                    .crossFade()
                    .error(R.drawable.cooking)
                    .into(imagethumbnail);
        } else {
            imagethumbnail.setVisibility(View.GONE);
            exoPlayerView.setLayoutParams(new LinearLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT));

        }
    }

    private void displayStepNo(int currentStepId) {
        createExoPlayer(mBackingResponses.getSteps().get(currentStepId).getVideoURL());
        stepDescription.setText(mBackingResponses.getSteps().get(currentStepId).getDescription());
        stepCount.setText(getResources().getString(R.string.stepLabel) + " " + currentStepId);
        YoYo.with(Techniques.FadeInLeft)
                .duration(500)
                .repeat(1)
                .playOn(stepDescription);
    }

    private void createExoPlayer(String url) {
        //Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();
        // 2. Create the player
        player =
                ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
        exoPlayerView.setPlayer(player);
        preparePlayer(url);
        player.setPlayWhenReady(true);

        try {
            player.seekTo(currentPeriodMillisecond);
        } catch (Exception e) {
            player.seekTo(0);

        }


    }

    private void preparePlayer(String url) {
        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                Util.getUserAgent(getActivity(), getResources().getString(R.string.app_name)), bandwidthMeter);
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // This is the MediaSource representing the media to be played.
        Uri mp4VideoUri = Uri.parse(url);
        MediaSource videoSource = new ExtractorMediaSource(mp4VideoUri,
                dataSourceFactory, extractorsFactory, null, new ExtractorMediaSource.EventListener() {
            @Override
            public void onLoadError(IOException error) {
                Toast.makeText(getActivity(), getResources().getString(R.string.errorLoading), Toast.LENGTH_SHORT).show();
            }
        });
        // Prepare the player with the source.
        player.prepare(videoSource);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (player != null) {
            BakingSharedPreference bakingSharedPreference = new BakingSharedPreference(getActivity());
            bakingSharedPreference.saveStringToSharedPreference(BakingConstants.CURRENT_PLAYER_PERIOD, player.getCurrentPosition() + "");
            bakingSharedPreference.saveStringToSharedPreference(BakingConstants.VIDEO_ID, mBackingResponses.getSteps().get(currentStepId).getId() + "");
            player.stop();
            player.release();
            player = null;
        }
    }
}
