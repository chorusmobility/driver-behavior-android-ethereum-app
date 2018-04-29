package demo.technology.chorus.chorusdemo.service.events;

public class ShowMessageEvent {
    private String eventText;

    public ShowMessageEvent(String eventText) {
        this.eventText = eventText;
    }

    public String getEventText() {
        return eventText;
    }
}
