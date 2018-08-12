package demo.technology.chorus.chorusdemo.view.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.MapStyleOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigInteger;

import demo.technology.chorus.chorusdemo.DataManager;
import demo.technology.chorus.chorusdemo.R;
import demo.technology.chorus.chorusdemo.integration.infura.IInfuraResponseListener;
import demo.technology.chorus.chorusdemo.integration.infura.InfuraSession;
import demo.technology.chorus.chorusdemo.service.events.RatingReadingStopEvent;
import demo.technology.chorus.chorusdemo.service.events.RatingUpdateEvent;
import demo.technology.chorus.chorusdemo.utils.ChorusTextUtils;
import demo.technology.chorus.chorusdemo.view.base.BaseLocationActivity;

public class MapsActivity extends BaseLocationActivity {
    private static final String MAP = "Map";
    private static final String STAT = "Stat";
    private static final int TILT = 69;
    private static final int ZOOM = 17;
    private static final int BEARING = 0;
    private TextView ratingTextView;

    @Override
    public void openOnSwipeAction() {

        EventBus.getDefault().post(new RatingReadingStopEvent());

        //STOP TRIP
        //OPEN START SCREEN WITH DIALOG
        InfuraSession.createSession(DataManager.getInstance().getUserModel());
        InfuraSession.finishRideSession(DataManager.getInstance().getRatingModel(), new IInfuraResponseListener() {
            @Override
            public void waitForStringResponse(String response) {
                InfuraSession.killSession();
                closeActivity();
            }

            @Override
            public void waitForBooleanResponse(Boolean response) {
                InfuraSession.killSession();
                closeActivity();
            }

            @Override
            public void waitForBigIntResponse(BigInteger response) {

            }
        });

        showWaitingDialog();

        //for the test purposes
        //InfuraSession.killSession();
        //closeActivity();
    }

    private void showWaitingDialog() {
        //TURN TIMEOUT
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
                tilt(TILT).
                zoom(ZOOM).
                bearing(BEARING).
                build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ((TextView)findViewById(R.id.walletTextView)).setText(DataManager.getInstance().getUserModel().getWallet().getAddress());
        initSeekBar();
        initMap(initFragments(savedInstanceState));
        initWalletView();
        ratingTextView = findViewById(R.id.ratingTextView);
        updateRatingUI();
    }

    private void updateRatingUI() {
        ratingTextView.setText(ChorusTextUtils.formatDouble1(DataManager.getInstance().getRatingModel().getMainDriverRating()));
    }

    private SupportMapFragment initFragments(Bundle savedInstanceState) {
        SupportMapFragment supportMapFragment;

        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            supportMapFragment =  SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.mapContainer, supportMapFragment, MAP).commitAllowingStateLoss();
            fm.beginTransaction().replace(R.id.statContainer, new MainStatFragment(), STAT).commitAllowingStateLoss();
        } else {
            supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentByTag(MAP);
        }
        return supportMapFragment;
    }

    private void initMap(SupportMapFragment supportMapFragment) {
        locationPermissionGranted = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        mFusedLocationProviderClient = LocationServices
                .getFusedLocationProviderClient(this);

        if (!locationPermissionGranted) {
            // Permission is not granted. need to show permission ask dialog
            requestFineLocationPermission();
        }

        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onBackPressed() {

    }

    public void closeActivity() {
        DataManager.saveData();
        super.onBackPressed();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RatingUpdateEvent event) {
        updateRatingUI();
    }

}
