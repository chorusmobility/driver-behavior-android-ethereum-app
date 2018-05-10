package demo.technology.chorus.chorusdemo;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import demo.technology.chorus.chorusdemo.model.PrivateInfo;
import demo.technology.chorus.chorusdemo.model.RatingModel;
import demo.technology.chorus.chorusdemo.model.UserModel;
import demo.technology.chorus.chorusdemo.model.WalletModel;

public class DataManager {
    private static final String UM = "UM";
    private static final String RM = "RM";
    private static DataManager instance;
    private static UserModel userModel;
    private static RatingModel ratingModel;
    private static Gson gson;

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
        }
        return instance;
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
