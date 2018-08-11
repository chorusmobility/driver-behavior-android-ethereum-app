package demo.technology.chorus.chorusdemo.model;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import demo.technology.chorus.chorusdemo.integration.etherscan.EtherScanConstants;
import demo.technology.chorus.chorusdemo.service.events.BalanceUpdateEvent;

public class WalletModel {
    private String address;
    private Double amount;

    public WalletModel(String address) {
        this.address = address;
        EventBus.getDefault().register(this);
    }

    public WalletModel() {
        this.address = EtherScanConstants.ETHER_TOKEN_WALLET;
        EventBus.getDefault().register(this);
    }

    public String getAddress() {
        return address;
    }

    public Double getAmount() {
        return amount;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BalanceUpdateEvent event) {
        amount = event.getAmount();
    }
}
