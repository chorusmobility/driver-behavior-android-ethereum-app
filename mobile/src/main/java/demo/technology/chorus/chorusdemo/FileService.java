package demo.technology.chorus.chorusdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;

import demo.technology.chorus.chorusdemo.service.events.ShowMessageEvent;
import demo.technology.chorus.chorusdemo.utils.ChorusTextUtils;

public class FileService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DataManager.getInstance();
        DataManager.executorService.execute(() -> {
            //Thread.sleep(5000);
            //System.gc();
            File file = ChorusTextUtils.createFileFromInputStream(ChorusApp.getInstance().getResources().openRawResource(R.raw.utc), "utc");
            try {
                DataManager.getInstance().setCredentials(WalletUtils.loadCredentials("1qaz2wsX@", file));
                //System.gc();
                EventBus.getDefault().post(new ShowMessageEvent("Credentials loaded"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CipherException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}
