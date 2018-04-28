package demo.technology.chorus.chorusdemo;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import java.io.IOException;

import demo.technology.chorus.chorusdemo.infura.InfuraConstants;
import demo.technology.chorus.chorusdemo.processing.OkHttpRequestProcessing;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChorusApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        testInfuraRequest();
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
}
