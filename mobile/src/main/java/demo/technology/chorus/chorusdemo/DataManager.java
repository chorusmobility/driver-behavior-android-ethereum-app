package demo.technology.chorus.chorusdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Handler;

import demo.technology.chorus.chorusdemo.integration.infura.processor.InfuraProcessorType;
import demo.technology.chorus.chorusdemo.model.PrivateInfo;
import demo.technology.chorus.chorusdemo.model.RatingModel;
import demo.technology.chorus.chorusdemo.model.UserModel;
import demo.technology.chorus.chorusdemo.model.WalletModel;
import demo.technology.chorus.chorusdemo.service.events.ShowMessageEvent;
import demo.technology.chorus.chorusdemo.utils.ChorusTextUtils;

import static demo.technology.chorus.chorusdemo.integration.infura.InfuraBase.INFURA_PROCESSOR_TYPE;

public class DataManager {
    private static final String UM = "UM";
    private static final String RM = "RM";
    private static DataManager instance;
    private static UserModel userModel;
    private static RatingModel ratingModel;
    private static Gson gson;
    private static Credentials credentials;
    public static ExecutorService executorService;

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
            String user = loadString(UM);
            String rating = loadString(RM);
            getGson();
            userModel = user == null ? new UserModel(new WalletModel(), new PrivateInfo("Will")) : gson.fromJson(user, UserModel.class);
            ratingModel = rating == null ?
                    new RatingModel(10d, 10d, 10d, 10d, 10d) :
                    gson.fromJson(rating, RatingModel.class);
            saveData();

            executorService = Executors.newSingleThreadExecutor();

            if (INFURA_PROCESSOR_TYPE == InfuraProcessorType.WEB3J) {

                Intent startCredentialsService = new Intent(ChorusApp.getInstance(), FileService.class);
                ChorusApp.getInstance().startService(startCredentialsService);
                //executorService.execute(() -> {

                        //Thread.sleep(5000);
                        //System.gc();
//                        File file = ChorusTextUtils.createFileFromInputStream(ChorusApp.getInstance().getResources().openRawResource(R.raw.utc), "utc");
//                        try {
//                            credentials = WalletUtils.loadCredentials("1qaz2wsX@", file);
//                            //System.gc();
//                            EventBus.getDefault().post(new ShowMessageEvent("Credentials loaded"));
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } catch (CipherException e) {
//                            e.printStackTrace();
//                        }
               // });
            }
        }
        return instance;
    }

    public void setCredentials(Credentials _credentials) {
        credentials = _credentials;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public RatingModel getRatingModel() {
        return ratingModel;
    }

    public static Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    public static void saveData() {
        saveString(UM, getGson().toJson(userModel));
        saveString(RM, getGson().toJson(ratingModel));
    }

    public static boolean saveString(String tag, String value) {
        try {
            SharedPreferences.Editor spe = PreferenceManager.getDefaultSharedPreferences(ChorusApp.getInstance().getApplicationContext()).edit();
            spe.putString(tag, value);
            spe.apply();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String loadString(String tag) {
        String value = null;
        try {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ChorusApp.getInstance().getApplicationContext());
            value = sp.getString(tag, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

}
