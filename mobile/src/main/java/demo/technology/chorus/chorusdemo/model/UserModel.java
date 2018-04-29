package demo.technology.chorus.chorusdemo.model;

public class UserModel {
    private WalletModel wallet;

    public UserModel(WalletModel wallet) {
        this.wallet = wallet;
    }

    public WalletModel getWallet() {
        return wallet;
    }
}
