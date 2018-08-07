package demo.technology.chorus.chorusdemo.integration.etherscan;

//Get ERC20-Token Account Balance for TokenContractAddress
//https://api.etherscan.io/api?module=account&action=tokenbalance&contractaddress=0x57d90b64a1a57749b0f932f1a3395792e12e7055&address=0xe04f27eb70e025b78871a2ad7eabe85e61212761&tag=latest&apikey=YourApiKeyToken
//Alternative curl -X POST --data '{"jsonrpc":"2.0","method":"eth_call","params":[{"to": "0x86fa049857e0209aa7d9e616f7eb3b3b78ecfdb0", "data":"0x70a082310000000000000000000000000b88516a6d22bf8e0d3657effbd41577c5fd4cb7"}, "latest"],"id":67}' -H "Content-Type: application/json" http://127.0.0.1:8545/

public class EtherScanConstants {
    private static final String HTTPS_API_ETHERSCAN_IO_API_MODULE_RINKEBY = "https://api-rinkeby.etherscan.io/api?module=account&action=balance&address=";
    private static final String HTTPS_API_ETHERSCAN_TOKEN_IO_API_MODULE_RINKEBY = "http://api-rinkeby.etherscan.io/api?module=account&action=tokenbalance&contractaddress=";
    private static final String HTTPS_API_ETHERSCAN_IO_API_MODULE_MAIN = "https://api.etherscan.io/api?module=account&action=tokenbalance&contractaddress=";
    private static final String ADDRESS = "&address=";
    private static final String TAG_LATEST_APIKEY = "&tag=latest&apikey=";
    private static String ETHER_SCAN_API_KEY = "EDEN486IK6XY87ZI4J6X5VU1FDQ1CZ9UEK";
    private static String ETHER_TOKEN_WALLET = "0x78B7Aa81C5afd58ef37c02b45DDA9d9Adb58446E";
    public static String ETHER_TOKEN_ADDRESS = "0xd80C5263f9aF0D7485f4DC05c951F6E9fb146B93";//"0x3cE342395103D540D60121d28CAf27121887862a";

    public static String getEtherTokenScanLink(){
        return HTTPS_API_ETHERSCAN_TOKEN_IO_API_MODULE_RINKEBY + ETHER_TOKEN_ADDRESS + ADDRESS + ETHER_TOKEN_WALLET +
                TAG_LATEST_APIKEY + ETHER_SCAN_API_KEY;
    }

    public static String getEtherScanLink(String address){
        return HTTPS_API_ETHERSCAN_IO_API_MODULE_RINKEBY + address +
                TAG_LATEST_APIKEY + ETHER_SCAN_API_KEY;
    }
}
