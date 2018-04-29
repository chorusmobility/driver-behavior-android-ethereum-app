package demo.technology.chorus.chorusdemo;

import demo.technology.chorus.chorusdemo.model.UserModel;
import demo.technology.chorus.chorusdemo.model.WalletModel;

public class DataManager {
    private static DataManager instance;
    private static UserModel userModel;

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
            userModel = new UserModel(new WalletModel());
        }
        return instance;
    }
}
