package demo.technology.chorus.chorusdemo.integration.infura;

import android.annotation.SuppressLint;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import demo.technology.chorus.chorusdemo.BuildConfig;
import demo.technology.chorus.chorusdemo.model.RatingModel;
import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;

public class IpfsProcessing {

    private static String IPFS_PROXY_URL = "https://ipfs.infura.io/ipfs/";
    private static Boolean IpfsRunning;

    public static void postRatingResultIPFS(RatingModel result, IInfuraResponseListener responseListener) {
        String divider = "+";
        String resultString = result.getMainDriverRating() + divider + result.getAccelerationRating() + divider +
                result.getSpeedingRating() + divider + result.getBreakingRating() + divider + result.getPhoningRating();

        String startTripCoordinate = null;
        String endTripCoordinate = null;

        if (isIpfsRuning()) {
            responseListener.waitForStringResponse(uploadToIpfs(resultString));
        } else {
            responseListener.waitForStringResponse(null);
        }
    }

    //check the status of IPFS service
    @SuppressLint("LongLogTag")
    public static boolean isIpfsRuning() {
        if (IpfsRunning != null)
            return IpfsRunning;

        try {
            IPFS ipfs = new IPFS(IPFS_PROXY_URL);
            IpfsRunning = true;
        } catch (Exception e) {
            Log.e("Failed to connnect IPFS service:", e.toString());
            IpfsRunning = false;
        }

        return IpfsRunning;
    }

    //
    //Upload data to IPFS and return the HASH uri
    public static String uploadToIpfs(String data) {
        try {
            IPFS ipfs = new IPFS(IPFS_PROXY_URL);
            //ipfs.refs.local();
            NamedStreamable.ByteArrayWrapper file = new NamedStreamable.ByteArrayWrapper("ppkpub-odin.data", data.getBytes());
            List<MerkleNode> merkleNodeList = ipfs.add(file);
            Log.e("Util.uploadToIpfs()", "addResult: " + merkleNodeList.toString());
            //JSONObject tmp_obj = new JSONObject(addResult);
            //String hash = tmp_obj.optString("Hash");
            //if (hash == null)
            //    return null;

            //return "ipfs:" + hash;
            return "ipfs:";
        } catch (Exception e) {
            Log.e("Util.uploadToIpfs() err", e.toString());
            return null;
        }
    }

    public static String getIpfsData(String ipfs_hash_address) {
        try {
            IPFS ipfs = new IPFS(IPFS_PROXY_URL);
            Multihash filePointer = Multihash.fromBase58(ipfs_hash_address);
            byte[] fileContents = ipfs.cat(filePointer);
            return new String(fileContents);
        } catch (Exception e) {
            System.out.println("Util.getIpfsData() error:" + e.toString());

            String tmp_url = IPFS_PROXY_URL + ipfs_hash_address;
            System.out.println("Using IPFS Proxy to fetch:" + tmp_url);

            return getPage(tmp_url);
        }
    }

    public static String getPage(String urlString) {
        return getPage(urlString, 1);

    }

    public static String getPage(String urlString, int retries) {
        try {
            Log.i("Getting URL: ", urlString);
            doTrustCertificates();
            URL url = new URL(urlString);
            HttpURLConnection connection = null;
            connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);
            connection.addRequestProperty("User-Agent", BuildConfig.APPLICATION_ID + " " + BuildConfig.VERSION_NAME);
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setReadTimeout(10000);
            connection.connect();

            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = rd.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
            //System.out.println (sb.toString());

            return sb.toString();
        } catch (Exception e) {
            Log.e("Fetch URL error: ", e.toString());
        }
        return "";
    }

    public static void doTrustCertificates() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };
    }
}
