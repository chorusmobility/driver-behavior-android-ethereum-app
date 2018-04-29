package demo.technology.chorus.chorusdemo.view.settings;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;

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
