package demo.technology.chorus.chorusdemo.model;

import static demo.technology.chorus.chorusdemo.integration.EthConstants.GWEI;

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
        try {
            return result == null ? 0 : (Double.parseDouble(result) / GWEI);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return -1d;
        }
    }

    public void setResult(String result) {
        this.result = result;
    }
}
