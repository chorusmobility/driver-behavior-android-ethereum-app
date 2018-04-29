package demo.technology.chorus.chorusdemo.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import demo.technology.chorus.chorusdemo.ChorusApp;
import demo.technology.chorus.chorusdemo.R;

public class ToastUtil {

    public static void showCustomToast(String text) {
        LayoutInflater inflater = (LayoutInflater) ChorusApp.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast, null);
        Toast toast = Toast.makeText(ChorusApp.getInstance(), text, Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.setText(text);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.show();
    }

}
