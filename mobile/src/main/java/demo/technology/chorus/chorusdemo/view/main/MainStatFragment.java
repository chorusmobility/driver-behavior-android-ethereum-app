package demo.technology.chorus.chorusdemo.view.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import demo.technology.chorus.chorusdemo.DataManager;
import demo.technology.chorus.chorusdemo.R;
import demo.technology.chorus.chorusdemo.engine.simulation.ValueReceiver;
import demo.technology.chorus.chorusdemo.model.RatingModel;
import demo.technology.chorus.chorusdemo.service.events.RatingReadingStopEvent;
import demo.technology.chorus.chorusdemo.service.events.RatingUpdateEvent;
import demo.technology.chorus.chorusdemo.utils.ChorusTextUtils;

public class MainStatFragment extends Fragment {
    private static final String TRIP_STOPPED = "tripStopped";
    private static final int PERIOD = 5000;
    private static final int DELAY = 5000;
    private View fragmentView;
    private Timer timer;
    private AtomicBoolean tripStopped;
    private TextView aggressiveTextView, excessiveSpeedingTextView, hardBreakingTextView, phoneUsageTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.stat_layout, container, false);
        tripStopped = new AtomicBoolean(savedInstanceState != null && savedInstanceState.getBoolean(TRIP_STOPPED));
        initViews();
        updateRatingUI();
        if (!tripStopped.get() && DataManager.getInstance().getUserModel().isUseFakeData()) {
            updateDisplay();
        }
        return fragmentView;
    }

    private void initViews() {
        aggressiveTextView = fragmentView.findViewById(R.id.aggressiveTextView);
        excessiveSpeedingTextView = fragmentView.findViewById(R.id.excessiveSpeedingTextView);
        hardBreakingTextView = fragmentView.findViewById(R.id.hardBreakingTextView);
        phoneUsageTextView = fragmentView.findViewById(R.id.phoneUsageTextView);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(TRIP_STOPPED, tripStopped.get());
        super.onSaveInstanceState(outState);
    }

    private void updateRatingUI() {
        RatingModel ratingModel = DataManager.getInstance().getRatingModel();
        aggressiveTextView.setText(ChorusTextUtils.formatDouble1(ratingModel.getAccelerationRating()));
        excessiveSpeedingTextView.setText(ChorusTextUtils.formatDouble1(ratingModel.getSpeedingRating()));
        hardBreakingTextView.setText(ChorusTextUtils.formatDouble1(ratingModel.getBreakingRating()));
        phoneUsageTextView.setText(ChorusTextUtils.formatDouble1(ratingModel.getPhoningRating()));
    }

    private void updateRating() {
        ValueReceiver.updateRatingModel(DataManager.getInstance().getRatingModel());
        EventBus.getDefault().post(new RatingUpdateEvent());
    }

    private void updateDisplay() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!tripStopped.get()) {
                    updateRating();
                } else {
                    stopTimer();
                }
            }
        }, DELAY, PERIOD);
    }

    @Override
    public void onDetach() {
        stopTimer();
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        stopTimer();
        super.onDestroy();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RatingReadingStopEvent event) {
        tripStopped.set(true);
        stopTimer();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RatingUpdateEvent event) {
        updateRatingUI();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
