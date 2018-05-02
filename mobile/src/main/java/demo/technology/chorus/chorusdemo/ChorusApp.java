package demo.technology.chorus.chorusdemo;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import demo.technology.chorus.chorusdemo.integration.etherscan.EtherScanConstants;
import demo.technology.chorus.chorusdemo.integration.infura.InfuraConstants;
import demo.technology.chorus.chorusdemo.model.EtherScanResponse;
import demo.technology.chorus.chorusdemo.processing.OkHttpRequestProcessing;
import demo.technology.chorus.chorusdemo.service.events.BalanceUpdateEvent;
import demo.technology.chorus.chorusdemo.service.events.ShowMessageEvent;
import demo.technology.chorus.chorusdemo.utils.ChorusTextUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChorusApp extends MultiDexApplication {
    private static ChorusApp instance;
    private static DataManager dataManager;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        dataManager = DataManager.getInstance();

        testInfuraRequest();
        //testTokenBalanceEtherScan();
    }

    public String nameOfScreen() {
        // return 1.0 if it's MDPI
        // return 1.5 if it's HDPI
        // return 2.0 if it's XHDPI
        // return 3.0 if it's XXHDPI
        // return 4.0 if it's XXXHDPI
        float density = getResources().getDisplayMetrics().density;
        if (density == 1.0f) {
            return "mdpi";
        } else if (density == 1.5f){
            return "hdpi";
        } else if (density == 2.0f) {
            return "xhdpi";
        } else if (density == 3.0f) {
            return "xxhdpi";
        } else if (density == 4.0f) {
            return "xxxhdpi";
        } else return density + "";
    }

    public static ChorusApp getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void testInfuraRequest() {
        OkHttpRequestProcessing.runGet("https://api.infura.io/v1/jsonrpc/rinkeby/methods", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("OkHttpException", "" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("OkHttpResponse", response.body().string());
            }
        });
        OkHttpRequestProcessing.runPost(InfuraConstants.RINKEBY, "", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("OkHttpException", "" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("OkHttpResponse", response.body().string());

            }
        });
    }

    public void testTokenBalanceEtherScan() {
        OkHttpRequestProcessing.runGet(EtherScanConstants.getEtherScanLink(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("OkHttpException", "" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                final EtherScanResponse etherScanResponse = new Gson().fromJson(responseBody, EtherScanResponse.class);

                if (etherScanResponse != null) {
                    EventBus.getDefault().post(new BalanceUpdateEvent(etherScanResponse.getResult()));
                    //EventBus.getDefault().post(new ShowMessageEvent("Screen is " + nameOfScreen()));
                   // EventBus.getDefault().post(new ShowMessageEvent("Balance of 0x0 Ether Wallet with OmiseGo Token is " +
                    //                ChorusTextUtils.formatDouble4(etherScanResponse.getResult()) + " OMG"));
                }
            }
        });
    }
}
