package demo.technology.chorus.chorusdemo.view.start;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigInteger;

import demo.technology.chorus.chorusdemo.DataManager;
import demo.technology.chorus.chorusdemo.R;
import demo.technology.chorus.chorusdemo.integration.infura.IInfuraResponseListener;
import demo.technology.chorus.chorusdemo.integration.infura.InfuraSession;
import demo.technology.chorus.chorusdemo.service.events.BalanceUpdateEvent;
import demo.technology.chorus.chorusdemo.service.events.ShowMessageEvent;
import demo.technology.chorus.chorusdemo.utils.ChorusTextUtils;
import demo.technology.chorus.chorusdemo.view.base.BaseAddressActivity;
import demo.technology.chorus.chorusdemo.view.main.MapsActivity;
import demo.technology.chorus.chorusdemo.view.settings.SettingsActivity;

public class ChorusBalanceActivity extends BaseAddressActivity {

    private ImageView rainBowImageView;
    private TextView rainBowTextView;
    private TextView addressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_main);
        initView();
        initSeekBar();
        initFragments(savedInstanceState);

    }

    private void initView() {
        updateBalanceText();
        hintTextView = findViewById(R.id.hintTextView);
        seekBar = findViewById(R.id.progressBar);
        rainBowImageView = findViewById(R.id.rainbow_view);
        rainBowTextView = findViewById(R.id.ratingTextView);
        addressTextView = findViewById(R.id.walletTextView);
        TextView welcomeTextView = findViewById(R.id.welcomeText);
        addressTextView.setText(DataManager.getInstance().getUserModel().getWallet().getAddress());
        welcomeTextView.setText(ChorusTextUtils.getWelcomeText() + ", " +
                DataManager.getInstance().getUserModel().getPrivateInfo().getName() + "!\nYour score is:");
        rainBowTextView.setText(ChorusTextUtils.formatDouble1(DataManager.getInstance().getRatingModel().getMainDriverRating()));
    }

    @Override
    public void openOnSwipeAction() {
        startActivity(new Intent(ChorusBalanceActivity.this, MapsActivity.class),
                ActivityOptionsCompat.makeSceneTransitionAnimation(ChorusBalanceActivity.this,
                        generatePairFromView(rainBowImageView),
                        generatePairFromView(rainBowTextView),
                        generatePairFromView(addressTextView)).toBundle());
    }

    @Override
    public void initSwipeBar() {

    }

    private void initFragments(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            //fm.beginTransaction().replace(R.id.statContainer, new MainStatFragment(), "Stat").commitAllowingStateLoss();
        }
    }

    public void onSettingsSelected(View view) {
        startActivity(new Intent(ChorusBalanceActivity.this, SettingsActivity.class),
                ActivityOptionsCompat.makeSceneTransitionAnimation(ChorusBalanceActivity.this,
                        generatePairFromView(addressTextView)).toBundle());
    }

    public void onBlockchainShowSelected(View view) {
        openWalletRinkebyData();
    }

    private void updateBalanceText() {
        Double amount = DataManager.getInstance().getUserModel().getWallet().getAmount();
        ((TextView) findViewById(R.id.balanceText)).setText("or " + (amount == null ? 0 : ChorusTextUtils.formatDouble1(amount)) + " Tokens");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BalanceUpdateEvent event) {
        updateBalanceText();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //INFURA INTEGRATION TEST REQUEST WITH ASKING FOR THE BALANCE
        InfuraSession.createSession(DataManager.getInstance().getUserModel());
        InfuraSession.getBalance(new IInfuraResponseListener() {
            @Override
            public void waitForStringResponse(String response) {
                //EventBus.getDefault().post(new ShowMessageEvent("Balance: " + response));
            }

            @Override
            public void waitForBigIntResponse(BigInteger response) {

            }
        });
        InfuraSession.killSession();
    }
}
