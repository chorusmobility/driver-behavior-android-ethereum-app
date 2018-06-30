package demo.technology.chorus.chorusdemo.integration.infura;

import demo.technology.chorus.chorusdemo.interfaces.InfuraActions;
import demo.technology.chorus.chorusdemo.model.RatingModel;

/*
Select Browser or Internet to open the web browser.
Select Menu.
Select More then Settings.
Finally select Enable JavaScript.
* */
public class JavaScriptWrapper implements InfuraActions {

    @Override
    public void initRideSession() {

    }

    @Override
    public void getBalance(IInfuraResponseListener responseListener) {
        responseListener.waitForStringResponse("");
    }

    @Override
    public void deposit(IInfuraResponseListener responseListener) {

    }

    @Override
    public void finishRideSession(RatingModel result, IInfuraResponseListener responseListener) {

    }

}
