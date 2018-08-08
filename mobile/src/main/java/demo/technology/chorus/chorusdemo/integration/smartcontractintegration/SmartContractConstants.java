package demo.technology.chorus.chorusdemo.integration.smartcontractintegration;

//Using smart contract, methods and values convertation:
//https://ethereum.stackexchange.com/questions/3514/how-to-call-a-contract-method-using-the-eth-call-json-rpc-api

public class SmartContractConstants {

        public static String CONTRACT_ADDRESS_MAIN = "";
        public static String CONTRACT_ADDRESS_RINKEBY = "0x24146eaA44c62Ad2d9BC02d15016D59A63162627";//"0xF74D92829bDc9B3bE591Fcdf20a58873370B4225";//"0xE2e3f23352282281247600885E9E59D456AaE494";//"0x5d41B922e9271D368D2eb37927172Ed3066dD849";
        public static String CONTRACT_ADDRESS_ROPSTEN = "";

        public static String CONTRACT_METHOD_ADDRESS_GET_RATE_MAIN = "";
        public static String CONTRACT_METHOD_ADDRESS_GET_RATE_RINKEBY = "";
        public static String CONTRACT_METHOD_ADDRESS_GET_RATE = "";

        public static String CONTRACT_METHOD_ADDRESS_POST_RATE_MAIN = "";
        public static String CONTRACT_METHOD_ADDRESS_POST_RATE_RINKEBY = "";
        public static String CONTRACT_METHOD_ADDRESS_POST_RATE_ROPSTEN = "";

        public static String SC_INTERFACE = "[ { \"constant\": true, \"inputs\": [], \"name\": \"name\", \"outputs\": [ { \"name\": \"\", \"type\": \"string\", \"value\": \"Chorus\" } ], \"payable\": false, \"stateMutability\": \"view\", \"type\": \"function\" }, { \"constant\": false, \"inputs\": [ { \"name\": \"_spender\", \"type\": \"address\" }, { \"name\": \"_value\", \"type\": \"uint256\" } ], \"name\": \"approve\", \"outputs\": [ { \"name\": \"success\", \"type\": \"bool\" } ], \"payable\": false, \"stateMutability\": \"nonpayable\", \"type\": \"function\" }, { \"constant\": true, \"inputs\": [], \"name\": \"totalSupply\", \"outputs\": [ { \"name\": \"\", \"type\": \"uint256\", \"value\": \"8e+45\" } ], \"payable\": false, \"stateMutability\": \"view\", \"type\": \"function\" }, { \"constant\": false, \"inputs\": [ { \"name\": \"_from\", \"type\": \"address\" }, { \"name\": \"_to\", \"type\": \"address\" }, { \"name\": \"_value\", \"type\": \"uint256\" } ], \"name\": \"transferFrom\", \"outputs\": [ { \"name\": \"success\", \"type\": \"bool\" } ], \"payable\": false, \"stateMutability\": \"nonpayable\", \"type\": \"function\" }, { \"constant\": true, \"inputs\": [], \"name\": \"decimals\", \"outputs\": [ { \"name\": \"\", \"type\": \"uint8\", \"value\": \"18\" } ], \"payable\": false, \"stateMutability\": \"view\", \"type\": \"function\" }, { \"constant\": false, \"inputs\": [ { \"name\": \"_value\", \"type\": \"uint256\" } ], \"name\": \"burn\", \"outputs\": [ { \"name\": \"success\", \"type\": \"bool\" } ], \"payable\": false, \"stateMutability\": \"nonpayable\", \"type\": \"function\" }, { \"constant\": true, \"inputs\": [ { \"name\": \"\", \"type\": \"address\" } ], \"name\": \"balanceOf\", \"outputs\": [ { \"name\": \"\", \"type\": \"uint256\", \"value\": \"0\" } ], \"payable\": false, \"stateMutability\": \"view\", \"type\": \"function\" }, { \"constant\": false, \"inputs\": [ { \"name\": \"_from\", \"type\": \"address\" }, { \"name\": \"_value\", \"type\": \"uint256\" } ], \"name\": \"burnFrom\", \"outputs\": [ { \"name\": \"success\", \"type\": \"bool\" } ], \"payable\": false, \"stateMutability\": \"nonpayable\", \"type\": \"function\" }, { \"constant\": true, \"inputs\": [ { \"name\": \"_adress\", \"type\": \"address\" } ], \"name\": \"balanceOfAddress\", \"outputs\": [ { \"name\": \"\", \"type\": \"uint256\", \"value\": \"0\" } ], \"payable\": false, \"stateMutability\": \"view\", \"type\": \"function\" }, { \"constant\": true, \"inputs\": [], \"name\": \"symbol\", \"outputs\": [ { \"name\": \"\", \"type\": \"string\", \"value\": \"COR\" } ], \"payable\": false, \"stateMutability\": \"view\", \"type\": \"function\" }, { \"constant\": false, \"inputs\": [ { \"name\": \"_to\", \"type\": \"address\" }, { \"name\": \"_value\", \"type\": \"uint256\" } ], \"name\": \"transfer\", \"outputs\": [], \"payable\": false, \"stateMutability\": \"nonpayable\", \"type\": \"function\" }, { \"constant\": false, \"inputs\": [ { \"name\": \"_spender\", \"type\": \"address\" }, { \"name\": \"_value\", \"type\": \"uint256\" }, { \"name\": \"_extraData\", \"type\": \"bytes\" } ], \"name\": \"approveAndCall\", \"outputs\": [ { \"name\": \"success\", \"type\": \"bool\" } ], \"payable\": false, \"stateMutability\": \"nonpayable\", \"type\": \"function\" }, { \"constant\": true, \"inputs\": [ { \"name\": \"\", \"type\": \"address\" }, { \"name\": \"\", \"type\": \"address\" } ], \"name\": \"allowance\", \"outputs\": [ { \"name\": \"\", \"type\": \"uint256\", \"value\": \"0\" } ], \"payable\": false, \"stateMutability\": \"view\", \"type\": \"function\" }, { \"inputs\": [ { \"name\": \"initialSupply\", \"type\": \"uint256\", \"index\": 0, \"typeShort\": \"uint\", \"bits\": \"256\", \"displayName\": \"initial Supply\", \"template\": \"elements_input_uint\", \"value\": \"8000000000000000000000000000\" }, { \"name\": \"tokenName\", \"type\": \"string\", \"index\": 1, \"typeShort\": \"string\", \"bits\": \"\", \"displayName\": \"token Name\", \"template\": \"elements_input_string\", \"value\": \"Chorus\" }, { \"name\": \"tokenSymbol\", \"type\": \"string\", \"index\": 2, \"typeShort\": \"string\", \"bits\": \"\", \"displayName\": \"token Symbol\", \"template\": \"elements_input_string\", \"value\": \"COR\" } ], \"payable\": false, \"stateMutability\": \"nonpayable\", \"type\": \"constructor\" }, { \"anonymous\": false, \"inputs\": [ { \"indexed\": true, \"name\": \"from\", \"type\": \"address\" }, { \"indexed\": true, \"name\": \"to\", \"type\": \"address\" }, { \"indexed\": false, \"name\": \"value\", \"type\": \"uint256\" } ], \"name\": \"Transfer\", \"type\": \"event\" }, { \"anonymous\": false, \"inputs\": [ { \"indexed\": true, \"name\": \"from\", \"type\": \"address\" }, { \"indexed\": false, \"name\": \"value\", \"type\": \"uint256\" } ], \"name\": \"Burn\", \"type\": \"event\" } ]";

}
