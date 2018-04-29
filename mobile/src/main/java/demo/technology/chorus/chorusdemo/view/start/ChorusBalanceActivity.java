package demo.technology.chorus.chorusdemo.view.start;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import demo.technology.chorus.chorusdemo.R;
import demo.technology.chorus.chorusdemo.vibrator.Vibration;
import demo.technology.chorus.chorusdemo.view.main.MapsActivity;

public class ChorusBalanceActivity extends FragmentActivity {

    private ImageView rainBowImageView;
    private TextView rainBowTextView;
    private TextView addressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_main);
        initSeekBar();
        initFragments(savedInstanceState);
    }

    private void initSeekBar() {
        SeekBar seekBar = (SeekBar) findViewById(R.id.progressBar);
        final TextView hintTextView = (TextView) findViewById(R.id.hintTextView);
        //seekBar.setScaleY(3f);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            private void processFinishState() {
                Vibration.getInstance().vibrate();
                startActivity(new Intent(ChorusBalanceActivity.this, MapsActivity.class),
                        ActivityOptionsCompat.makeSceneTransitionAnimation(ChorusBalanceActivity.this,
                                generatePairFromView(rainBowImageView),
                                generatePairFromView(rainBowTextView),
                                generatePairFromView(addressTextView)).toBundle());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                hintTextView.setVisibility(View.GONE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar.getProgress() != 100) {
                    seekBar.setProgress(0);
                } else {
                    processFinishState();
                    seekBar.setProgress(0);
                }
                hintTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initFragments(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            //fm.beginTransaction().replace(R.id.statContainer, new MainStatFragment(), "Stat").commitAllowingStateLoss();
        }
    }

    private Pair<View, String> generatePairFromView(View view) {
        return Pair.create(view, ViewCompat.getTransitionName(view));
    }
}
