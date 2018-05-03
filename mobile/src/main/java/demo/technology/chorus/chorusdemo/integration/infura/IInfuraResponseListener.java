package demo.technology.chorus.chorusdemo.integration.infura;

import java.math.BigInteger;

public interface IInfuraResponseListener {
    public void waitForStringResponse(String response);
    public void waitForBigIntResponse(BigInteger response);
}
