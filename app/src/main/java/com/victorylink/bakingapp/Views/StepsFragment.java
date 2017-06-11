package com.victorylink.bakingapp.Views;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    SimpleExoPlayer player;

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
    RelativeLayout relativeLayoutContainer;
    @BindView(R.id.step_controller)
    LinearLayout linearController;
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
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            currentStepId = savedInstanceState.getInt(BakingConstants.CURRENT_SELECTED_ITEM);
            displayStepNo(currentStepId);
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
        if (isTab)
            linearController.setVisibility(View.GONE);
        else
            linearController.setVisibility(View.VISIBLE);
        createExoPlayer(mBackingResponses.getSteps().get(currentStepId).getVideoURL());
        displayStepNo(currentStepId);
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

    private void displayStepNo(int currentStepId) {
        preparePlayer(mBackingResponses.getSteps().get(currentStepId).getVideoURL());
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
        player.stop();
        player.release();
        player = null;
    }

}
