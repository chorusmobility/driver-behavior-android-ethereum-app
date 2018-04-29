package demo.technology.chorus.chorusdemo.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

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
}
