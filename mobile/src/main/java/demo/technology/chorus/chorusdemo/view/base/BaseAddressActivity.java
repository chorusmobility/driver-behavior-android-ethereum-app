package demo.technology.chorus.chorusdemo.view.base;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import demo.technology.chorus.chorusdemo.DataManager;
import demo.technology.chorus.chorusdemo.R;
import demo.technology.chorus.chorusdemo.service.events.ShowMessageEvent;
import demo.technology.chorus.chorusdemo.utils.SnackBarUtil;
import demo.technology.chorus.chorusdemo.utils.vibrator.Vibration;

import static demo.technology.chorus.chorusdemo.integration.etherscan.EtherScanConstants.ETHER_TOKEN_ADDRESS;
import static demo.technology.chorus.chorusdemo.integration.smartcontractintegration.SmartContractConstants.CONTRACT_ADDRESS_RINKEBY;

public abstract class BaseAddressActivity extends FragmentActivity {

    protected SeekBar seekBar;
    protected TextView hintTextView;

    public void onAddressClick(View view) {
        copyToClipBoard(((TextView) view).getText());
        EventBus.getDefault().post(new ShowMessageEvent("Address copied into clipboard"));
    }

    private void copyToClipBoard(CharSequence text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Chorus Token Wallet Address", text);
        clipboard.setPrimaryClip(clip);
    }

    public void openWalletData() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW).setData(
                    Uri.parse("https://etherscan.io/address/" + DataManager.getInstance().getUserModel().getWallet().getAddress())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openWalletRinkebyData() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW).setData(
                    //Uri.parse("https://rinkeby.etherscan.io/address/" + CONTRACT_ADDRESS_RINKEBY)));
                    Uri.parse("https://rinkeby.etherscan.io/token/" + ETHER_TOKEN_ADDRESS + "?a=" + DataManager.getInstance().getUserModel().getWallet().getAddress())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void initWalletView() {
        TextView walletTextView = findViewById(R.id.walletTextView);
        if (walletTextView == null) return;
        walletTextView.setText(DataManager.getInstance().getUserModel().getWallet().getAddress());
    }

    protected void initSeekBar() {
        initSwipeBar();
        if (seekBar == null) return;

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            private void processFinishState() {
                Vibration.getInstance().vibrate();
                openOnSwipeAction();
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

    public abstract void openOnSwipeAction();

    public abstract void initSwipeBar();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ShowMessageEvent event) {
        SnackBarUtil.showSnackBarCustom(findViewById(R.id.coordinatorLayout), event.getEventText());
        View view = findViewById(R.id.logo);
        if (view != null) {
            view.setVisibility(View.GONE);
        }
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

    protected Pair<View, String> generatePairFromView(View view) {
        return Pair.create(view, ViewCompat.getTransitionName(view));
    }
}
