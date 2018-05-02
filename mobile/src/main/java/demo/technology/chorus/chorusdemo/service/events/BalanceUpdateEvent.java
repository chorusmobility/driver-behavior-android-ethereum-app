package demo.technology.chorus.chorusdemo.service.events;

public class BalanceUpdateEvent {
    private Double amount;

    public BalanceUpdateEvent(Double amount) {
        this.amount = amount;
    }

    public Double getAmount() {
        return amount;
    }
}
