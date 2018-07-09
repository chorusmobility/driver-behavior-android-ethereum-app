package demo.technology.chorus.chorusdemo.integration.infura;

import demo.technology.chorus.chorusdemo.integration.infura.processor.JavaScriptWrapper;
import demo.technology.chorus.chorusdemo.integration.infura.processor.RequestWrapper;
import demo.technology.chorus.chorusdemo.integration.infura.processor.Web3jWrapper;
import demo.technology.chorus.chorusdemo.interfaces.InfuraActions;
import demo.technology.chorus.chorusdemo.model.RatingModel;
import demo.technology.chorus.chorusdemo.model.UserModel;

//INSPIRED BY https://github.com/WarSame/BeamItUp/blob/c9499e30f14bfbf302932e3555e8992a9166f1a0/app/src/main/java/com/example/graeme/beamitup/Session.java
public class InfuraSession extends InfuraBase {

    private InfuraSession() {
    }

    @Deprecated
    static void createSession() { //For testing purposes
        initSession();
    }

    protected static InfuraActions getInfuraProcessor() {
        switch (INFURA_PROCESSOR_TYPE) {
            case JS: return new JavaScriptWrapper(executorService);
            case WEB3J: return new Web3jWrapper(executorService, web3j);
            case RAW_REQUEST: return new RequestWrapper(executorService);
            default: return new Web3jWrapper(executorService, web3j);
        }
    }

    public static void createSession(UserModel account) {
        initSession();

        InfuraSession.account = account;
        InfuraSession.isAlive = true;
        infuraProcessingImpl = getInfuraProcessor();
    }

    public static void getBalance(IInfuraResponseListener responseListener) {
        infuraProcessingImpl.getBalance(responseListener);
    }

    public static void deposit(final IInfuraResponseListener responseListener) {
        infuraProcessingImpl.deposit(responseListener);
    }

    public static void finishRideSession(final RatingModel result, IInfuraResponseListener responseListener) {
        infuraProcessingImpl.finishRideSession(result, responseListener);
    }

    @Deprecated
    public static void initRideSession() {
        infuraProcessingImpl.initRideSession();
    }
}
