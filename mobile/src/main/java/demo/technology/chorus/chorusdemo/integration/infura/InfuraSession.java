package demo.technology.chorus.chorusdemo.integration.infura;

import android.annotation.SuppressLint;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.joor.Reflect;
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
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import demo.technology.chorus.chorusdemo.BuildConfig;
import demo.technology.chorus.chorusdemo.ChorusApp;
import demo.technology.chorus.chorusdemo.DataManager;
import demo.technology.chorus.chorusdemo.model.RatingModel;
import demo.technology.chorus.chorusdemo.model.UserModel;
import demo.technology.chorus.chorusdemo.service.events.BalanceUpdateEvent;
import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;

import static demo.technology.chorus.chorusdemo.integration.EthConstants.GWEI;
import static demo.technology.chorus.chorusdemo.integration.etherscan.EtherScanConstants.ETHER_TOKEN_ADDRESS;
import static demo.technology.chorus.chorusdemo.integration.smartcontractintegration.SmartContractConstants.CONTRACT_ADDRESS_RINKEBY;

//INSPIRED BY https://github.com/WarSame/BeamItUp/blob/c9499e30f14bfbf302932e3555e8992a9166f1a0/app/src/main/java/com/example/graeme/beamitup/Session.java

public class InfuraSession {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static String IPFS_PROXY_URL = "https://ipfs.infura.io/ipfs/";

    private static volatile Web3j web3j;
    private static UserModel account;
    private static boolean isAlive;
    private static ExecutorService executorService;
    private static Boolean IpfsRunning;

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

//        executorService.execute(() -> {
//            try {
//                Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
//                Log.d("WEB3ClientVersion", "WEB3ClientVersion: " + web3ClientVersion.getWeb3ClientVersion());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });

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

    public static void deposit(final IInfuraResponseListener responseListener) {
        executorService.execute(() -> {
            InfuraSession.createSession(DataManager.getInstance().getUserModel());
            try {
                final TransactionManager transactionManager = new RawTransactionManager(web3j, DataManager.getInstance().getCredentials());
                TransactionReceipt transferReceipt = new RemoteCall<>(() -> {
                    Transfer transfer = new Transfer(web3j, transactionManager);
                    Field field = transfer.getClass().getDeclaredField("GAS_LIMIT");
                    field.setAccessible(true);
                    field.set(transfer, BigInteger.valueOf(60000));
                    return (TransactionReceipt) Reflect.on(transfer)
                            .call("send", CONTRACT_ADDRESS_RINKEBY, BigDecimal.valueOf(1 * GWEI), Convert.Unit.WEI).get();
                }).send();

                String hash = transferReceipt.getTransactionHash();
                Log.i("Wallet Deposit", "Transaction complete, view it at https://rinkeby.etherscan.io/tx/"
                        + hash);

                responseListener.waitForStringResponse(hash);
                //getBalance(null);
                ChorusApp.getInstance().testTokenBalanceEtherScan();
            } catch (Exception e) {
                Log.d("get balance failed:", e.getMessage(), e);
                responseListener.waitForStringResponse(null);
                InfuraSession.killSession();
            }
        });
    }

    private static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
    }

    @Deprecated
    public static void initRideSession(final IInfuraResponseListener responseListener) {

        try {
            Function function = new Function("initDriverTrip",
                    Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(DataManager.getInstance().getUserModel().getWallet().getAddress())),
                    Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                    }));
            String data = FunctionEncoder.encode(function);

            Transaction transaction = Transaction.createFunctionCallTransaction(DataManager.getInstance().getUserModel().getWallet().getAddress(), BigInteger.ZERO,
                    BigInteger.valueOf(22000000000L), BigInteger.valueOf(80000L),
                    CONTRACT_ADDRESS_RINKEBY, data);

            //Transaction transaction = Transaction.createEthCallTransaction(
            //        DataManager.getInstance().getUserModel().getWallet().getAddress(), CONTRACT_ADDRESS_RINKEBY, data);

            if (web3j == null) {
                createSession();
            }
            EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            String value = ethCall.getValue();
            responseListener.waitForStringResponse(value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public static void setBenefeciary() {
        try {
            Function function = new Function("setbeneficiary",
                    Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(DataManager.getInstance().getUserModel().getWallet().getAddress())),
                    Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
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

    public static void finishRideSession(final RatingModel result, IInfuraResponseListener responseListener) {
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
            if (_amount < 0) {
                _amount = 1;
            }

            Function function = new Function("withdraw",
                    Arrays.<Type>asList(new Uint256(BigInteger.valueOf(_amount * 1000000000000000000L))),
                    Arrays.<TypeReference<?>>asList(new TypeReference<org.web3j.abi.datatypes.Bool>() {
                    }));

            String data = FunctionEncoder.encode(function);
            Transaction transaction = Transaction.createFunctionCallTransaction(CONTRACT_ADDRESS_RINKEBY, BigInteger.ZERO,
                    BigInteger.valueOf(22000000000L), BigInteger.valueOf(80000L),
                    DataManager.getInstance().getUserModel().getWallet().getAddress(), data);

            EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();

            String response = ethCall.getValue().toString();
            Log.i("Withdraw to wallet", "Response " + response);

            //Consult return data processing with https://github.com/ethjava/web3j-sample/blob/bd04ba59ac77f3334eeef55eac7b76311e23e169/src/main/java/com/ethjava/TokenClient.java
//            if (value) {
//                postRatingResultIPFS(result, responseListener);
//            }
            responseListener.waitForStringResponse(response);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void postRatingResultIPFS(RatingModel result, IInfuraResponseListener responseListener) {
        String divider = "+";
        String resultString = result.getMainDriverRating() + divider + result.getAccelerationRating() + divider +
                result.getSpeedingRating() + divider + result.getBreakingRating() + divider + result.getPhoningRating();

        String startTripCoordinate = null;
        String endTripCoordinate = null;

        if (isIpfsRuning()) {
            responseListener.waitForStringResponse(uploadToIpfs(resultString));
        } else {
            responseListener.waitForStringResponse(null);
        }
    }

    //check the status of IPFS service
    @SuppressLint("LongLogTag")
    public static boolean isIpfsRuning() {
        if (IpfsRunning != null)
            return IpfsRunning;

        try {
            IPFS ipfs = new IPFS(IPFS_PROXY_URL);
            IpfsRunning = true;
        } catch (Exception e) {
            Log.e("Failed to connnect IPFS service:", e.toString());
            IpfsRunning = false;
        }

        return IpfsRunning;
    }

    //
    //Upload data to IPFS and return the HASH uri
    public static String uploadToIpfs(String data) {
        try {
            IPFS ipfs = new IPFS(IPFS_PROXY_URL);
            //ipfs.refs.local();
            NamedStreamable.ByteArrayWrapper file = new NamedStreamable.ByteArrayWrapper("ppkpub-odin.data", data.getBytes());
            List<MerkleNode> merkleNodeList = ipfs.add(file);
            Log.e("Util.uploadToIpfs()", "addResult: " + merkleNodeList.toString());
            //JSONObject tmp_obj = new JSONObject(addResult);
            //String hash = tmp_obj.optString("Hash");
            //if (hash == null)
            //    return null;

            //return "ipfs:" + hash;
            return "ipfs:";
        } catch (Exception e) {
            Log.e("Util.uploadToIpfs() err", e.toString());
            return null;
        }
    }

    public static String getIpfsData(String ipfs_hash_address) {
        try {
            IPFS ipfs = new IPFS(IPFS_PROXY_URL);
            Multihash filePointer = Multihash.fromBase58(ipfs_hash_address);
            byte[] fileContents = ipfs.cat(filePointer);
            return new String(fileContents);
        } catch (Exception e) {
            System.out.println("Util.getIpfsData() error:" + e.toString());

            String tmp_url = IPFS_PROXY_URL + ipfs_hash_address;
            System.out.println("Using IPFS Proxy to fetch:" + tmp_url);

            return getPage(tmp_url);
        }
    }

    public static String getPage(String urlString) {
        return getPage(urlString, 1);

    }

    public static String getPage(String urlString, int retries) {
        try {
            Log.i("Getting URL: ", urlString);
            doTrustCertificates();
            URL url = new URL(urlString);
            HttpURLConnection connection = null;
            connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);
            connection.addRequestProperty("User-Agent", BuildConfig.APPLICATION_ID + " " + BuildConfig.VERSION_NAME);
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setReadTimeout(10000);
            connection.connect();

            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = rd.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
            //System.out.println (sb.toString());

            return sb.toString();
        } catch (Exception e) {
            Log.e("Fetch URL error: ", e.toString());
        }
        return "";
    }

    public static void doTrustCertificates() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };
    }
}
