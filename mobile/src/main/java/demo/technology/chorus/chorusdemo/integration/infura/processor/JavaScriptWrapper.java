package demo.technology.chorus.chorusdemo.integration.infura.processor;

import java.util.concurrent.ExecutorService;

import demo.technology.chorus.chorusdemo.integration.infura.IInfuraResponseListener;
import demo.technology.chorus.chorusdemo.interfaces.InfuraActions;
import demo.technology.chorus.chorusdemo.model.RatingModel;

/*
Select Browser or Internet to open the web browser.
Select Menu.
Select More then Settings.
Finally select Enable JavaScript.
* */
public class JavaScriptWrapper implements InfuraActions {
    private ExecutorService executorService;

    public JavaScriptWrapper(ExecutorService executorService) {
        this.executorService = executorService;
    }

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
