package demo.technology.chorus.chorusdemo;

import android.app.Application;

import java.io.IOException;

import demo.technology.chorus.chorusdemo.infura.InfuraConstants;
import demo.technology.chorus.chorusdemo.processing.OkHttpRequestProcessing;

public class ChorusApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        testInfuraRequest();
    }

    private void testInfuraRequest() {
        try {
            OkHttpRequestProcessing.runPost(InfuraConstants.RINKEBY, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
