package demo.technology.chorus.chorusdemo.interfaces;

import android.content.Context;
import android.webkit.JavascriptInterface;

import org.greenrobot.eventbus.EventBus;

import demo.technology.chorus.chorusdemo.service.events.ResponseJsStatusEvent;

public class WebAppInterface {
    Context mContext;

    /** Instantiate the interface and set the context */
    public WebAppInterface(Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(int requestId, int code) {
        EventBus.getDefault().post(new ResponseJsStatusEvent(requestId, code));
    }
}
