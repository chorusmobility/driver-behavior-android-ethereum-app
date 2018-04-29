package demo.technology.chorus.chorusdemo.model;

public class UserModel {
    private WalletModel wallet;
    private PrivateInfo privateInfo;

    public UserModel(WalletModel wallet, PrivateInfo privateInfo) {
        this.wallet = wallet;
        this.privateInfo = privateInfo;
    }

    public WalletModel getWallet() {
        return wallet;
    }

    public PrivateInfo getPrivateInfo() {
        return privateInfo;
    }
}
