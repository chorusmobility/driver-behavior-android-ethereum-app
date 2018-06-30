package demo.technology.chorus.chorusdemo.interfaces;

import demo.technology.chorus.chorusdemo.integration.infura.IInfuraResponseListener;
import demo.technology.chorus.chorusdemo.model.RatingModel;

public interface InfuraActions {
    void initRideSession();
    void getBalance(IInfuraResponseListener responseListener);
    void deposit(IInfuraResponseListener responseListener);
    void finishRideSession(RatingModel result, IInfuraResponseListener responseListener);
}
