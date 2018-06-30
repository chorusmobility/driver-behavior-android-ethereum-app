package demo.technology.chorus.chorusdemo.service.events;

public class ResponseJsStatusEvent {
    public int requestId;
    public int responseCode;

    public ResponseJsStatusEvent(int requestId, int responseCode) {
        this.requestId = requestId;
        this.responseCode = responseCode;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}
