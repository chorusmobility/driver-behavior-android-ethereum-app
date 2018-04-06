package demo.technology.chorus.chorusdemo.infura;

import demo.technology.chorus.chorusdemo.processing.OkHttpRequestProcessing;

public class InfuraRequestBuilder {

    public static final String serializeData(Object classData){
        return OkHttpRequestProcessing.getGson().toJson(classData);
    }

    public static final Object parceData(String rawData, Class classType){
        return OkHttpRequestProcessing.getGson().fromJson(rawData, classType);
    }
}
