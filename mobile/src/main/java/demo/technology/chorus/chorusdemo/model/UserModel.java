package demo.technology.chorus.chorusdemo.model;

public class UserModel {
    //https://github.com/liu-hong/infura/blob/master/src/main/java/com/infura/example/CreateWallet.java
    private WalletModel wallet;
    private PrivateInfo privateInfo;
    private boolean useFakeData;

    public UserModel(WalletModel wallet, PrivateInfo privateInfo) {
        this.wallet = wallet;
        this.privateInfo = privateInfo;
        this.useFakeData = false;
    }

    public WalletModel getWallet() {
        return wallet;
    }

    public PrivateInfo getPrivateInfo() {
        return privateInfo;
    }

    public boolean isUseFakeData () {return useFakeData;}

    public void  setUseFakeData(boolean use) { useFakeData = use;}
}
