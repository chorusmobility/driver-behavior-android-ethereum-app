package demo.technology.chorus.chorusdemo.view.start;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigInteger;

import demo.technology.chorus.chorusdemo.ChorusApp;
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

import static demo.technology.chorus.chorusdemo.view.base.BaseLocationFragment.PERMISSIONS_REQUEST_LOCATION;

public class ChorusBalanceActivity extends BaseAddressActivity {

    private ImageView rainBowImageView;
    private TextView rainBowTextView;
    private TextView addressTextView;
    private MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_main);
        initView();
        initSeekBar();
        initFragments(savedInstanceState);
        initPermissions();
    }

    private void initPermissions() {
        if (!(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            requestFineLocationPermission();
        }
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
        updateRatingUI();
    }

    @Override
    public void openOnSwipeAction() {

        boolean wrapInScrollView = true;
        materialDialog = new MaterialDialog.Builder(this)
                .title("Ethereum blockchain is processing transaction")
                .titleGravity(GravityEnum.CENTER)
                .positiveText("Ok")
                .progress(true, 0)
                .onPositive((dialog, which) -> dismissDialog())
                .show();

        InfuraSession.deposit(new IInfuraResponseListener() {
            @Override
            public void waitForStringResponse(String response) {
                dismissDialog();

                if (!TextUtils.isEmpty(response) && response.length() > 2) {
                    ChorusBalanceActivity.this.runOnUiThread(() -> startActivity(new Intent(ChorusBalanceActivity.this, MapsActivity.class),
                            ActivityOptionsCompat.makeSceneTransitionAnimation(ChorusBalanceActivity.this,
                                    generatePairFromView(rainBowImageView),
                                    generatePairFromView(rainBowTextView),
                                    generatePairFromView(addressTextView)).toBundle()));
                } else {
                    EventBus.getDefault().post(new ShowMessageEvent("Deposit had not processed. Please check balance."));
                }
            }

            @Override
            public void waitForBooleanResponse(Boolean response) {
                dismissDialog();
            }

            @Override
            public void waitForBigIntResponse(BigInteger response) {
                dismissDialog();
            }
        });

    }

    private void dismissDialog() {
        if (materialDialog != null) {
            materialDialog.dismiss();
            materialDialog = null;
        }
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
        ((TextView) findViewById(R.id.balanceText)).setText((amount == null ? 0 : ChorusTextUtils.formatDouble1(amount)) + " Tokens");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BalanceUpdateEvent event) {
        updateBalanceText();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateRatingUI();
        //INFURA INTEGRATION TEST REQUEST WITH ASKING FOR THE BALANCE
        InfuraSession.createSession(DataManager.getInstance().getUserModel());
        InfuraSession.getBalance(new IInfuraResponseListener() {
            @Override
            public void waitForStringResponse(String response) {
                //EventBus.getDefault().post(new ShowMessageEvent("Balance: " + response));
            }

            @Override
            public void waitForBooleanResponse(Boolean response) {

            }

            @Override
            public void waitForBigIntResponse(BigInteger response) {

            }
        });
        InfuraSession.killSession();
    }

    private void updateRatingUI() {
        if (rainBowTextView != null) {
            rainBowTextView.setText(ChorusTextUtils.formatDouble1(DataManager.getInstance().getRatingModel().getMainDriverRating()));
        }
    }

    public void requestFineLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                }
            }
        }
    }
}
