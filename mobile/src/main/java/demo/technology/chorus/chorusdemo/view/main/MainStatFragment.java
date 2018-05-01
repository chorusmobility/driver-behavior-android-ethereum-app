package demo.technology.chorus.chorusdemo.view.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import demo.technology.chorus.chorusdemo.DataManager;
import demo.technology.chorus.chorusdemo.R;
import demo.technology.chorus.chorusdemo.model.RatingModel;
import demo.technology.chorus.chorusdemo.utils.ChorusTextUtils;

public class MainStatFragment extends Fragment {
    private View fragmentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.stat_layout, container, false);
        RatingModel ratingModel = DataManager.getInstance().getRatingModel();
        ((TextView) fragmentView.findViewById(R.id.aggressiveTextView)).setText(ChorusTextUtils.formatDouble1(ratingModel.getAccelerationRating()));
        ((TextView) fragmentView.findViewById(R.id.excessiveSpeedingTextView)).setText(ChorusTextUtils.formatDouble1(ratingModel.getSpeedingRating()));
        ((TextView) fragmentView.findViewById(R.id.hardBreakingTextView)).setText(ChorusTextUtils.formatDouble1(ratingModel.getBreakingRating()));
        ((TextView) fragmentView.findViewById(R.id.phoneUsageTextView)).setText(ChorusTextUtils.formatDouble1(ratingModel.getPhoningRating()));
        return fragmentView;
    }
}
