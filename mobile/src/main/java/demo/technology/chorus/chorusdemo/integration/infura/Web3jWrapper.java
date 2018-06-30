package demo.technology.chorus.chorusdemo.integration.infura;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.joor.Reflect;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import demo.technology.chorus.chorusdemo.DataManager;
import demo.technology.chorus.chorusdemo.interfaces.InfuraActions;
import demo.technology.chorus.chorusdemo.model.RatingModel;
import demo.technology.chorus.chorusdemo.service.events.BalanceUpdateEvent;

import static demo.technology.chorus.chorusdemo.integration.EthConstants.GWEI;
import static demo.technology.chorus.chorusdemo.integration.etherscan.EtherScanConstants.ETHER_TOKEN_ADDRESS;
import static demo.technology.chorus.chorusdemo.integration.infura.InfuraBase.getNonce;
import static demo.technology.chorus.chorusdemo.integration.infura.InfuraBase.processIpfs;
import static demo.technology.chorus.chorusdemo.integration.smartcontractintegration.SmartContractConstants.CONTRACT_ADDRESS_RINKEBY;

public class Web3jWrapper implements InfuraActions {
    private ExecutorService executorService;
    private volatile Web3j web3j;

    public Web3jWrapper(ExecutorService executorService, Web3j _web3j) {
        this.executorService = executorService;
        this.web3j = _web3j;
    }

    @Override
    public void initRideSession() {
        try {
            Function function = new Function("initDriverTrip",
                    Arrays.asList(new org.web3j.abi.datatypes.Address(DataManager.getInstance().getUserModel().getWallet().getAddress())),
                    Arrays.asList(new TypeReference<Uint256>() {
                    }));
            String data = FunctionEncoder.encode(function);
            Transaction transaction = Transaction.createEthCallTransaction(
                    DataManager.getInstance().getUserModel().getWallet().getAddress(), CONTRACT_ADDRESS_RINKEBY, data);
            EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            String value = ethCall.getValue();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getBalance(IInfuraResponseListener responseListener) {
        try {
            Function function = new Function("balanceOf",
                    Arrays.asList(new org.web3j.abi.datatypes.Address(DataManager.getInstance().getUserModel().getWallet().getAddress())),
                    Arrays.asList(new TypeReference<Uint256>() {
                    }));
            String encodedFunction = FunctionEncoder.encode(function);
            EthCall ethCall = web3j.ethCall(Transaction.createEthCallTransaction(CONTRACT_ADDRESS_RINKEBY,
                    //DataManager.getInstance().getUserModel().getWallet().getAddress(),
                    ETHER_TOKEN_ADDRESS,
                    encodedFunction), DefaultBlockParameterName.LATEST).sendAsync().get();
            Log.d("get balance result:", "AMOUNT " + ethCall.getValue());
            String value = ethCall.getValue().substring(2);
            BigInteger bigInteger = new BigInteger(value, 16);
            Log.d("get balance result:", "AMOUNT " + bigInteger);
            if (responseListener != null) {
                responseListener.waitForStringResponse(bigInteger.toString());
            }
            EventBus.getDefault().post(new BalanceUpdateEvent(bigInteger.doubleValue() / GWEI));
            InfuraSession.killSession();
        } catch (Exception e) {
            Log.d("get balance failed:", e.getMessage(), e);
            if (responseListener != null) {
                responseListener.waitForStringResponse(null);
            }
            InfuraSession.killSession();
        }
    }

    @Override
    public void deposit(IInfuraResponseListener responseListener) {
        executorService.execute(() -> {
            InfuraSession.createSession(DataManager.getInstance().getUserModel());
            try {
                final TransactionManager transactionManager = new RawTransactionManager(web3j, DataManager.getInstance().getCredentials());
                TransactionReceipt transferReceipt = new RemoteCall<>(() -> {
                    Transfer transfer = new Transfer(web3j, transactionManager);
                    Field field = transfer.getClass().getDeclaredField("GAS_LIMIT");
                    field.setAccessible(true);
                    field.set(transfer, BigInteger.valueOf(600000));
                    return (TransactionReceipt) Reflect.on(transfer)
                            .call("send", CONTRACT_ADDRESS_RINKEBY, BigDecimal.valueOf(1 * GWEI), Convert.Unit.WEI).get();
                }).send();

                String hash = transferReceipt.getTransactionHash();
                Log.i("Wallet Deposit", "Transaction complete, view it at https://rinkeby.etherscan.io/tx/"
                        + hash);

                responseListener.waitForStringResponse(hash);
                getBalance(null);
            } catch (Exception e) {
                Log.d("get balance failed:", e.getMessage(), e);
                responseListener.waitForStringResponse(null);
                InfuraSession.killSession();
            }
        });
    }

    @Override
    public void finishRideSession(RatingModel result, IInfuraResponseListener responseListener) {
        //CALCULATE AMOUNT HERE
        long _amount;

        //FUZZY LOGIC for Rewarding DB
        if (result.getMainDriverRating() > 75 && result.getMainDriverRating() <= 100) {
            //SEND Reward
            //beneficiary.transfer();
            _amount = 1 + Double.valueOf(result.getMainDriverRating() / 10).longValue();

        } else if (result.getMainDriverRating() > 60 && result.getMainDriverRating() <= 75) {
            //nothing changed
            //Send refund bid_limit
            _amount = 1;

        } else {
            //PANISHMENT
            _amount = 1 - (Double.valueOf(1 - (result.getMainDriverRating() / 10.0)).longValue());
        }
        try {

            BigInteger value = Convert.toWei(String.valueOf(_amount), Convert.Unit.ETHER).toBigInteger();
            Function function = new Function("withdraw",
                    Arrays.asList(new Uint256(value)),
                    Arrays.asList(new TypeReference<Uint256>() {
                    }));

            String data = FunctionEncoder.encode(function);

            executorService.execute(() -> {
                try {
                    Transaction transaction = Transaction.createFunctionCallTransaction(CONTRACT_ADDRESS_RINKEBY, getNonce(DataManager.getInstance().getCredentials().getAddress()),
                            web3j.ethGasPrice().send().getGasPrice(), BigInteger.valueOf(6500000), //web3j.ethGasPrice().send().getGasPrice() or Transaction.DEFAULT_GAS
                            DataManager.getInstance().getUserModel().getWallet().getAddress(), data);

                    EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();


                    String response = ethCall.getValue().toString();
                    Log.i("Withdraw to wallet", "Response " + response);
                    responseListener.waitForStringResponse(response);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            //Consult return data processing with https://github.com/ethjava/web3j-sample/blob/bd04ba59ac77f3334eeef55eac7b76311e23e169/src/main/java/com/ethjava/TokenClient.java
            processIpfs(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
