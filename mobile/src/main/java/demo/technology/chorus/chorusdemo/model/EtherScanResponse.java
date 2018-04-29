package demo.technology.chorus.chorusdemo.model;

import static demo.technology.chorus.chorusdemo.EthConstants.GWEI;

public class EtherScanResponse {
    public String status;
    public String message;
    public String result;

    public EtherScanResponse(String status, String message, String result) {
        this.status = status;
        this.message = message;
        this.result = result;
    }

    public EtherScanResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Double getResult() {
        return result == null ? 0 : (Double.parseDouble(result) / GWEI);
    }

    public void setResult(String result) {
        this.result = result;
    }
}
