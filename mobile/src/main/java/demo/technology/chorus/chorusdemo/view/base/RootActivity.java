package demo.technology.chorus.chorusdemo.view.base;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import org.greenrobot.eventbus.EventBus;

import demo.technology.chorus.chorusdemo.R;
import demo.technology.chorus.chorusdemo.service.events.PermissionReceivedEvent;
import demo.technology.chorus.chorusdemo.view.main.MainMapStatFragment;
import demo.technology.chorus.chorusdemo.view.settings.SettingsFragment;
import demo.technology.chorus.chorusdemo.view.start.BalanceFragment;

import static demo.technology.chorus.chorusdemo.view.base.BaseLocationFragment.PERMISSIONS_REQUEST_LOCATION;

public class RootActivity extends FragmentActivity {
    Fragment balanceFragment;
    Fragment mapFragment;
    Fragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);

        if (savedInstanceState == null) {
            initAllFragments();
            addFirstFragmentFromFlow();
        }
    }

    private void addFirstFragmentFromFlow() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.mapContainer, balanceFragment, "BalanceFragment").commitAllowingStateLoss();
    }

    public void showMainStatFragment() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.mapContainer, getMapFragment(), "MainMapStatFragment").commitAllowingStateLoss();
    }

    public void showSettingsFragment() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.mapContainer, getSettingsFragment(), "SettingsFragment").commitAllowingStateLoss();
    }

    private void initAllFragments() {
        balanceFragment = new BalanceFragment();
        mapFragment = new MainMapStatFragment();
        settingsFragment = new SettingsFragment();
    }

    private Fragment getMapFragment(){
        if (mapFragment == null) {
            mapFragment = new MainMapStatFragment();
        }
        return mapFragment;
    }

    private Fragment getSettingsFragment(){
        if (settingsFragment == null) {
            settingsFragment = new SettingsFragment();
        }
        return settingsFragment;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    EventBus.getDefault().post(new PermissionReceivedEvent());

                } else {
                    // permission denied
                }
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
        Fragment MainMapStatFragment = getSupportFragmentManager().findFragmentByTag("MainMapStatFragment");
        if (MainMapStatFragment == null || !MainMapStatFragment.isResumed()) {
            super.onBackPressed();
        }
    }

    public void forceBack(){
        super.onBackPressed();
    }
}
