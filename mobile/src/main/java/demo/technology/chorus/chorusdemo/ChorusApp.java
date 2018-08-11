package demo.technology.chorus.chorusdemo;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import demo.technology.chorus.chorusdemo.integration.etherscan.EtherScanConstants;
import demo.technology.chorus.chorusdemo.integration.infura.InfuraConstants;
import demo.technology.chorus.chorusdemo.model.EtherScanResponse;
import demo.technology.chorus.chorusdemo.processing.OkHttpRequestProcessing;
import demo.technology.chorus.chorusdemo.service.events.BalanceUpdateEvent;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChorusApp extends MultiDexApplication {
    private static ChorusApp instance;
    private static DataManager dataManager;
    private static final boolean USE_INTEGRATION_TEST = true;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        dataManager = DataManager.getInstance();

        if (USE_INTEGRATION_TEST) {
            //testInfuraRequest();
            testTokenBalanceEtherScan();
        }
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
        //GET AVAILABLE METHODS

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
        if (dataManager != null && dataManager.getCredentials() != null) {
            //OkHttpRequestProcessing.runGet(EtherScanConstants.getEtherScanLink(dataManager.getCredentials().getAddress()), new Callback() {
            OkHttpRequestProcessing.runGet(EtherScanConstants.getEtherTokenScanLink(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("OkHttpException", "" + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBody = response.body().string();
                    final EtherScanResponse etherScanResponse = new Gson().fromJson(responseBody, EtherScanResponse.class);

                    if (etherScanResponse != null) {

                        //TODO: BEWARE of that 10^18 multiplier in case of using normal ERC20 token
                        EventBus.getDefault().post(new BalanceUpdateEvent(10000000000000000d * etherScanResponse.getResult()));
                        //EventBus.getDefault().post(new ShowMessageEvent("Screen is " + nameOfScreen()));
                    }
                }
            });
        }
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
        } else if (density == 1.5f) {
            return "hdpi";
        } else if (density == 2.0f) {
            return "xhdpi";
        } else if (density == 3.0f) {
            return "xxhdpi";
        } else if (density == 4.0f) {
            return "xxxhdpi";
        } else return density + "";
    }
}
