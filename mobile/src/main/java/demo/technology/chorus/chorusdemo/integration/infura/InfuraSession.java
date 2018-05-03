package demo.technology.chorus.chorusdemo.integration.infura;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import demo.technology.chorus.chorusdemo.DataManager;
import demo.technology.chorus.chorusdemo.model.UserModel;
import demo.technology.chorus.chorusdemo.service.events.BalanceUpdateEvent;

import static demo.technology.chorus.chorusdemo.integration.EthConstants.GWEI;
import static demo.technology.chorus.chorusdemo.integration.etherscan.EtherScanConstants.ETHER_TOKEN_ADDRESS;
import static demo.technology.chorus.chorusdemo.integration.smartcontractintegration.SmartContractConstants.CONTRACT_ADDRESS_RINKEBY;

//INSPIRED BY https://github.com/WarSame/BeamItUp/blob/c9499e30f14bfbf302932e3555e8992a9166f1a0/app/src/main/java/com/example/graeme/beamitup/Session.java

public class InfuraSession {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static Web3j web3j;
    private static UserModel account;
    private static boolean isAlive;
    private static ExecutorService executorService;

    private InfuraSession() {
    }

    @Deprecated
    static void createSession() {//For testing
        executorService = Executors.newCachedThreadPool();
        web3j = buildWeb3j();

    }

    public static void createSession(UserModel account) {
        executorService = Executors.newCachedThreadPool();
        InfuraSession.account = account;
        InfuraSession.isAlive = true;
        InfuraSession.web3j = buildWeb3j();
    }

    private static Web3j buildWeb3j() {
        web3j = Web3jFactory.build(
                new HttpService(InfuraConstants.RINKEBY));

        executorService.execute(() -> {
            try {
                Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
                Log.d("WEB3ClientVersion", "WEB3ClientVersion: " + web3ClientVersion.getWeb3ClientVersion());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return web3j;
    }

    public static void killSession() {
        InfuraSession.account = null;
        InfuraSession.isAlive = false;
        InfuraSession.web3j = null;
    }

    public static UserModel getUserDetails() {
        return InfuraSession.account;
    }

    public static Web3j getWeb3j() {
        return InfuraSession.web3j;
    }

    public static boolean isAlive() {
        return InfuraSession.isAlive;
    }

    public static void getBalance(IInfuraResponseListener responseListener) {
        try {
            Function function = new Function("balanceOf",
                    Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(DataManager.getInstance().getUserModel().getWallet().getAddress())),
                    Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                    }));
            String encodedFunction = FunctionEncoder.encode(function);
            EthCall ethCall = web3j.ethCall(Transaction.createEthCallTransaction(CONTRACT_ADDRESS_RINKEBY,
                    ETHER_TOKEN_ADDRESS, encodedFunction), DefaultBlockParameterName.LATEST).sendAsync().get();
            String value = ethCall.getValue().substring(2);
            BigInteger bigInteger = new BigInteger(value, 16);
            Log.d("get balance result:", "AMOUNT " + bigInteger);
            responseListener.waitForStringResponse(bigInteger.toString());
            EventBus.getDefault().post(new BalanceUpdateEvent(bigInteger.doubleValue()/GWEI));
        } catch (Exception e) {
            Log.d("get balance failed:", e.getMessage(), e);
            responseListener.waitForStringResponse(null);
        }
    }
}
