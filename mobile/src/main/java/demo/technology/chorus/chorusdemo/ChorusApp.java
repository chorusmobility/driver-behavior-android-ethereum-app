package demo.technology.chorus.chorusdemo;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import demo.technology.chorus.chorusdemo.etherscan.EtherScanConstants;
import demo.technology.chorus.chorusdemo.infura.InfuraConstants;
import demo.technology.chorus.chorusdemo.model.EtherScanResponse;
import demo.technology.chorus.chorusdemo.processing.OkHttpRequestProcessing;
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
        testTokenBalanceEtherScan();
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

    private void testTokenBalanceEtherScan() {
        OkHttpRequestProcessing.runGet(EtherScanConstants.getEtherScanLink(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("OkHttpException", "" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                final EtherScanResponse etherScanResponse = new Gson().fromJson(responseBody, EtherScanResponse.class);
                final NumberFormat formatter = new DecimalFormat("#0.0000");

                if (etherScanResponse != null) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(instance, "Balance of 0x0 Ether Wallet with OmiseGo Token is " +
                                    formatter.format(etherScanResponse.getResult()), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}
