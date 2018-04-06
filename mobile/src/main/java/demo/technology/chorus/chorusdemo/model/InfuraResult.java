package demo.technology.chorus.chorusdemo.model;

public class InfuraResult {
    public String result;
    public String id;

    public InfuraResult() {
    }

    public InfuraResult(String result, String id) {
        this.result = result;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getResult() {
        return result;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
