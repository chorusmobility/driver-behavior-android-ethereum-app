package demo.technology.chorus.chorusdemo;

import demo.technology.chorus.chorusdemo.model.PrivateInfo;
import demo.technology.chorus.chorusdemo.model.RatingModel;
import demo.technology.chorus.chorusdemo.model.UserModel;
import demo.technology.chorus.chorusdemo.model.WalletModel;

public class DataManager {
    private static DataManager instance;
    private static UserModel userModel;
    private static RatingModel ratingModel;

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
            userModel = new UserModel(new WalletModel(), new PrivateInfo("Will"));
            ratingModel = new RatingModel(10d, 10d, 10d, 10d, 10d);
        }
        return instance;
    }

    public UserModel getUserModel(){
        return userModel;
    }

    public RatingModel getRatingModel() {
        return ratingModel;
    }
}
