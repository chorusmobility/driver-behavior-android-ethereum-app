package demo.technology.chorus.chorusdemo.uport;

//http://developer.uport.me/guides.html#download-the-mobile-app
//    {
//        "@context":"http://schema.org",
//            "@type":"Person",
//            "name":"Agent Smith",
//            "address":"23fga3r2hh87ddhq98dhas8dz101j9f449w0",
//            "avatar": {
//        "uri": "https://ipfs.infura.io/ipfs/QmaqGAeHmwAi44T6ZrSuu3yxwiyHPxoE1rHGmKxeCuZbS7DBX"
//    },
//        "country": "US"
//        "network":"rinkeby",
//            "publicEncKey": "dgH1devHn5MhAcph+np8MI4ZLB2kJWqRc4NTwtAj6Fs="
//        "publicKey":"0x04016751595cf2f1429367d6c83a826526g613b4f7574af55ded0364f0fb34600bceba9211e5864ae616d7e83b5e3c79f1c913b40c8d38c64952fef383fd3ad637",
//    }

public class Payload {
    public String context;
    public String type;
    public String name;
    public UportAvatar avatar;
    public String address;
    public String country;
    public String network;
    public String publicEncKey;
    public String publicKey;

    public Payload() {
    }

    public Payload(String context, String type, String name, UportAvatar avatar, String address, String country, String network, String publicEncKey, String publicKey) {
        this.context = context;
        this.type = type;
        this.name = name;
        this.avatar = avatar;
        this.address = address;
        this.country = country;
        this.network = network;
        this.publicEncKey = publicEncKey;
        this.publicKey = publicKey;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UportAvatar getAvatar() {
        return avatar;
    }

    public void setAvatar(UportAvatar avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getPublicEncKey() {
        return publicEncKey;
    }

    public void setPublicEncKey(String publicEncKey) {
        this.publicEncKey = publicEncKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
