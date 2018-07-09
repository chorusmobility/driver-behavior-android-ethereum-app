package demo.technology.chorus.chorusdemo.integration.infura.processor;

import java.util.concurrent.ExecutorService;

import demo.technology.chorus.chorusdemo.integration.infura.IInfuraResponseListener;
import demo.technology.chorus.chorusdemo.interfaces.InfuraActions;
import demo.technology.chorus.chorusdemo.model.RatingModel;

public class RequestWrapper implements InfuraActions {
    private ExecutorService executorService;

    public RequestWrapper(ExecutorService executorService) {
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
