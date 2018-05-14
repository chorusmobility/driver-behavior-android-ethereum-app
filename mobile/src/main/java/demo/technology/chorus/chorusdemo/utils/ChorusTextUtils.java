package demo.technology.chorus.chorusdemo.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

import demo.technology.chorus.chorusdemo.ChorusApp;

public class ChorusTextUtils {
    private static final NumberFormat formatter4 = new DecimalFormat("#0.0000");
    private static final NumberFormat formatter2 = new DecimalFormat("#0.00");
    private static final NumberFormat formatter1 = new DecimalFormat("#0.#");

    public static String getWelcomeText() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            return "Good Morning";
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            return "Good Afternoon";
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            return "Good Evening";
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            return "Good Night";
        }
        return "Hi";
    }

    public static String formatDouble1(double value) {
        return formatter1.format(value);
    }

    public static String formatDouble2(double value) {
        return formatter2.format(value);
    }

    public static String formatDouble4(double value) {
        return formatter4.format(value);
    }

    public static File createFileFromInputStream(InputStream inputStream, String fileName) {

        try{
            File f = new File(ChorusApp.getInstance().getCacheDir() + "/" + fileName);
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while((length=inputStream.read(buffer)) > 0) {
                outputStream.write(buffer,0,length);
            }

            outputStream.close();
            inputStream.close();

            return f;
        }catch (IOException e) {
            //Logging exception
        }

        return null;
    }
}
