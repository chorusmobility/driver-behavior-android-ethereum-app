package demo.technology.chorus.chorusdemo.view.main;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MapStyleOptions;

import demo.technology.chorus.chorusdemo.R;
import demo.technology.chorus.chorusdemo.vibrator.Vibration;
import demo.technology.chorus.chorusdemo.view.BaseLocationActivity;

public class MapsActivity extends BaseLocationActivity {

    @Override
    public void setMapMode() {
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle_night));
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        initSeekBar();
        initMap(initFragments(savedInstanceState));
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

    private SupportMapFragment initFragments(Bundle savedInstanceState) {
        SupportMapFragment supportMapFragment = null;

        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            supportMapFragment =  SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.mapContainer, supportMapFragment, "Map").commitAllowingStateLoss();
            fm.beginTransaction().replace(R.id.statContainer, new MainStatFragment(), "Stat").commitAllowingStateLoss();
        } else {
            supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentByTag("Map");
        }
        return supportMapFragment;
    }

    private void initMap(SupportMapFragment supportMapFragment) {
        mLocationPermissionGranted = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        mFusedLocationProviderClient = LocationServices
                .getFusedLocationProviderClient(this);

        if (!mLocationPermissionGranted) {
            // Permission is not granted. need to show permission ask dialog
            requestFineLocationPermission();
        }

        supportMapFragment.getMapAsync(this);
    }

    public void onAddressClick(View view) {
//        try {
//            startActivity(new Intent(Intent.ACTION_VIEW).setData(
//                    Uri.parse("https://etherscan.io/address/" + ((TextView) view).getText().toString())));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Chorus Token Wallet Address", ((TextView) view).getText());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(MapsActivity.this, "Address copied into clipboard", Toast.LENGTH_LONG).show();
    }

}
