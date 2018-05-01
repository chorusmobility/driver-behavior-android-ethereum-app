package demo.technology.chorus.chorusdemo.view.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.MapStyleOptions;

import demo.technology.chorus.chorusdemo.DataManager;
import demo.technology.chorus.chorusdemo.R;
import demo.technology.chorus.chorusdemo.utils.ChorusTextUtils;
import demo.technology.chorus.chorusdemo.view.base.BaseLocationActivity;

public class MapsActivity extends BaseLocationActivity {

    @Override
    public void openOnSwipeAction() {
        //STOP TRIP
        //OPEN START SCREEN WITH DIALOG
        closeActivity();
    }

    @Override
    public void initSwipeBar() {
        seekBar = findViewById(R.id.progressBar);
        hintTextView = findViewById(R.id.hintTextView);
    }

    @Override
    public void setMapMode() {
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle_night));
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        CameraPosition cameraPosition = new CameraPosition.Builder().
                target(mMap.getCameraPosition().target).
                tilt(69).
                zoom(17).
                bearing(0).
                build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        initSeekBar();
        initMap(initFragments(savedInstanceState));
        initWalletView();
        ((TextView)findViewById(R.id.ratingTextView)).setText(ChorusTextUtils.formatDouble1(DataManager.getInstance().getRatingModel().getMainDriverRating()));
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

    @Override
    public void onBackPressed() {

    }

    public void closeActivity() {
        super.onBackPressed();
    }
}
