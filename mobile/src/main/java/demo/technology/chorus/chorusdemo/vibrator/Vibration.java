package demo.technology.chorus.chorusdemo.vibrator;

import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import demo.technology.chorus.chorusdemo.ChorusApp;

import static android.content.Context.VIBRATOR_SERVICE;

public class Vibration {
    private static Vibrator vibrator;
    private static Vibration instance;

    public static Vibration getInstance() {
        if (instance == null) {
            instance = new Vibration();
        }
        if (vibrator == null) {
            vibrator = (Vibrator) ChorusApp.getInstance().getSystemService(VIBRATOR_SERVICE);
        }
        return instance;
    }

    public void vibrate(){
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrateFor500ms();
//          customVibratePatternNoRepeat();
        } else {
            Toast.makeText(ChorusApp.getInstance(), "Device does not support vibration", Toast.LENGTH_SHORT).show();
        }
    }

    private void vibrateFor500ms() {
        vibrator.vibrate(500);
    }

    private void customVibratePatternNoRepeat() {
        // 0 : Start without a delay
        // 400 : Vibrate for 400 milliseconds
        // 200 : Pause for 200 milliseconds
        // 400 : Vibrate for 400 milliseconds
        long[] mVibratePattern = new long[]{0, 400, 200, 400};

        // -1 : Do not repeat this pattern
        // pass 0 if you want to repeat this pattern from 0th index
        vibrator.vibrate(mVibratePattern, -1);

    }

}
