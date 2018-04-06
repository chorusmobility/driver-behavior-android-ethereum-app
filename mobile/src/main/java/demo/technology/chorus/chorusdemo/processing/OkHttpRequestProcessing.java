package demo.technology.chorus.chorusdemo.processing;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpRequestProcessing {
    private static final String APPLICATION_JSON = "application/json";
    private static final String CONTENT_TYPE = "Content-Type";
    private static OkHttpClient client;
    private static Gson gson;

    public static OkHttpClient getClientInstance() {
        if (client == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
            client = new OkHttpClient();
            gson = new Gson();
        }
        return client;
    }

    public static void runPost(String url, String jsonSerializedHeader, Callback callback) {
        getClientInstance()
                .newCall(new Request.Builder()
                .url(url)
                .post(RequestBody.create (MediaType.parse(APPLICATION_JSON), jsonSerializedHeader))
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .build()).enqueue(callback);
    }

    public static void runGet(String url, Callback callback) {
        getClientInstance()
                .newCall(new Request.Builder()
                        .url(url)
                        .get()
                        .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .build()).enqueue(callback);
    }

    public static Gson getGson(){
        getClientInstance();
        return gson;
    }
}
