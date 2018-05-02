package demo.technology.chorus.chorusdemo.view.settings;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import demo.technology.chorus.chorusdemo.DataManager;
import demo.technology.chorus.chorusdemo.R;
import demo.technology.chorus.chorusdemo.view.base.BaseAddressActivity;

public class SettingsActivity extends BaseAddressActivity {

    @Override
    public void openOnSwipeAction() {

    }

    @Override
    public void initSwipeBar() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ((TextView)findViewById(R.id.walletTextView)).setText(DataManager.getInstance().getUserModel().getWallet().getAddress());
    }

    public void goBack(View view) {
        finish();
    }


//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        NavUtils.navigateUpFromSameTask(this);
//    }
}
