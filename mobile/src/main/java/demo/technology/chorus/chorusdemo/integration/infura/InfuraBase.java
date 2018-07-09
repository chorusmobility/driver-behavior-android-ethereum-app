package demo.technology.chorus.chorusdemo.integration.infura;

import android.util.Log;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import demo.technology.chorus.chorusdemo.DataManager;
import demo.technology.chorus.chorusdemo.integration.infura.processor.InfuraProcessorType;
import demo.technology.chorus.chorusdemo.interfaces.InfuraActions;
import demo.technology.chorus.chorusdemo.model.RatingModel;
import demo.technology.chorus.chorusdemo.model.UserModel;

import static demo.technology.chorus.chorusdemo.integration.smartcontractintegration.SmartContractConstants.CONTRACT_ADDRESS_RINKEBY;

public class InfuraBase {

    public static final InfuraProcessorType INFURA_PROCESSOR_TYPE = InfuraProcessorType.WEB3J;
    protected static final BigInteger ACCOUNT_UNLOCK_DURATION = BigInteger.valueOf(30);
    protected static final String WALLET_PASSWORD = "1qaz2wsX@";

    protected static volatile Web3j web3j;
    protected static UserModel account;
    protected static boolean isAlive;
    protected static ExecutorService executorService;
    protected static InfuraActions infuraProcessingImpl;

    protected static void initSession() {
        executorService = Executors.newCachedThreadPool();
        if (INFURA_PROCESSOR_TYPE == InfuraProcessorType.WEB3J) {
            web3j = buildWeb3j();
        }
    }

    protected static Web3j buildWeb3j() {
        if (INFURA_PROCESSOR_TYPE == InfuraProcessorType.WEB3J) {
            web3j = Web3jFactory.build(new HttpService(InfuraConstants.RINKEBY));
            executorService.execute(() -> {
                try {
                    Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
                    Log.d("WEB3ClientVersion", "WEB3ClientVersion: " + web3ClientVersion.getWeb3ClientVersion());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        return web3j;
    }

    public static void killSession() {
        InfuraSession.account = null;
        InfuraSession.isAlive = false;
        InfuraSession.web3j = null;
    }

    @Deprecated
    public static void setBenefeciary() {
        try {
            Function function = new Function("setbeneficiary",
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

    protected static BigInteger estimateGas(String encodedFunction, String from, String to) throws Exception {
        EthEstimateGas ethEstimateGas = web3j.ethEstimateGas(
                Transaction.createEthCallTransaction(from, to, encodedFunction))
                .sendAsync().get();
        return ethEstimateGas.getAmountUsed();
    }

    public static BigInteger getNonce(String address) throws ExecutionException, InterruptedException {
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                address, DefaultBlockParameterName.LATEST).sendAsync().get();

        return ethGetTransactionCount.getTransactionCount();
    }

    protected static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
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

    public static boolean processIpfs(RatingModel model) {
//            if (value) {
//                postRatingResultIPFS(result, responseListener);
//            }

// INFURA 405 Method not allowed
//            try {
//                Transaction transactionNew = Transaction.createFunctionCallTransaction(
//                        CONTRACT_ADDRESS_RINKEBY, getNonce(CONTRACT_ADDRESS_RINKEBY),  Transaction.DEFAULT_GAS, BigInteger.valueOf(6500000), DataManager.getInstance().getUserModel().getWallet().getAddress(), data);
//
//                org.web3j.protocol.core.methods.response.EthSendTransaction transactionResponse =
//                        web3j.ethSendTransaction(transactionNew).sendAsync().get();
//
//                String transactionHash = transactionResponse.getTransactionHash();
//                Log.i("Wallet Deposit", "Transaction complete, view it at https://rinkeby.etherscan.io/tx/"
//                        + transactionHash);
//
//                responseListener.waitForStringResponse(transactionHash);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }


//            org.web3j.protocol.core.methods.response.EthCall response = web3j.ethCall(
//                    Transaction.createEthCallTransaction(CONTRACT_ADDRESS_RINKEBY, DataManager.getInstance().getUserModel().getWallet().getAddress(), data),
//            DefaultBlockParameterName.LATEST).sendAsync().get();
//
//            List<Type> someTypes = FunctionReturnDecoder.decode(
//                    response.getValue(), function.getOutputParameters());
//
//            Log.i("Received Data", "" + someTypes);

        return true;
    }

//    private static boolean unlockAccount() throws Exception {
//        List<Object> attributes = new ArrayList<>(3);
//        attributes.add(CONTRACT_ADDRESS_RINKEBY);
//        attributes.add(WALLET_PASSWORD);
//
//        if (ACCOUNT_UNLOCK_DURATION != null) {
//            // Parity has a bug where it won't support a duration
//            // See https://github.com/ethcore/parity/issues/1215
//            attributes.add(ACCOUNT_UNLOCK_DURATION.longValue());
//        } else {
//            // we still need to include the null value, otherwise Parity rejects request
//            attributes.add(null);
//        }
//
//        return new Request<?, PersonalUnlockAccount>(
//                "personal_unlockAccount",
//                attributes,
//                web3j,
//                PersonalUnlockAccount.class);
//
//    }
}
