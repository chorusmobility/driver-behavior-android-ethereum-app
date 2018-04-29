package demo.technology.chorus.chorusdemo.model;

public class WalletModel {
    private String address;

    public WalletModel(String address) {
        this.address = address;
    }

    public WalletModel() {
        this.address = "0x5c5699e507463E1A234d417B7CA3399aC93bC8cF";
    }

    public String getAddress() {
        return address;
    }
}
