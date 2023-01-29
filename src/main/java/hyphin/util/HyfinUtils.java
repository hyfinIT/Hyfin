package hyphin.util;

import hyphin.model.User;
import org.springframework.web.servlet.ModelAndView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HyfinUtils {

    private static final long RESTORE_PERIOD_MILLIS = 7776000000L;

    public static final Map<Integer, String> CCY_PAIRS_DICTIONARY = new HashMap<>();

    static {
        CCY_PAIRS_DICTIONARY.put(0, "EUR/USD");
        CCY_PAIRS_DICTIONARY.put(1, "GBP/USD");
        CCY_PAIRS_DICTIONARY.put(2, "USD/JPY");
    }

    public static ModelAndView modelAndView(String viewName) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(viewName);
        return mav;
    }

    public static boolean canRestoreAccount(User user) {
        return System.currentTimeMillis() - user.getDeletionDate() < RESTORE_PERIOD_MILLIS;
    }

    public static String toPercentage(double n) {
        return String.format("%.0f", n * 100) + "%";
    }

    public static String toPercentage(double n, int digits) {
        return String.format("%." + digits + "f", n * 100) + "%";
    }

    /**
     * If you need to skip zeros at the end, use "#" instead of "0"
     */
    public static String formatDecimal(Double d, int decimalLength) {
        String pattern = "#.";

        for (int i = 0; i < decimalLength; i++) {
            pattern = pattern.concat("0");
        }

        return new DecimalFormat(pattern).format(d).replace(",", ".");
    }

    public static String formatDecimal(Double d) {
        String pattern = "#.0000";

        return new DecimalFormat(pattern).format(d).replace(",", ".");    }

    public static void main(String[] args) {
        System.out.println(formatDecimal(137728.3995959967));
    }

    public static String formatDecimalToMoney(Double d) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMAN);
        DecimalFormat df = (DecimalFormat) nf;

        String format = df.format(d).replaceAll("\\.", ",");

        if (format.charAt(format.length() - 3) == ',') {
            format = format.substring(0, format.length() - 3) + '.' + format.substring(format.length() - 2);
        }
        else if (format.charAt(format.length() - 2) == ',') {
            format = format.substring(0, format.length() - 2) + '.' + format.substring(format.length() - 1) + "0";
        }
        else {
            format = format + ".00";
        }

        return format;
    }

    public static String formatDecimalToMoneyWithoutZeros(Double d) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMAN);
        DecimalFormat df = (DecimalFormat) nf;

        String format = df.format(d).replaceAll("\\.", ",");

        if (format.charAt(format.length() - 3) == ',') {
            format = format.substring(0, format.length() - 3) + '.' + format.substring(format.length() - 2);
        }
        if (format.charAt(format.length() - 2) == ',') {
            format = format.substring(0, format.length() - 2) + '.' + format.substring(format.length() - 1) + "0";
        }

        return format;
    }

    public static String dropCents(String money) {
        if (money.charAt(money.length() - 3) == '.') {
            return money.substring(0, money.length() - 3);
        }
        if (money.charAt(money.length() - 2) == '.') {
            return money.substring(0, money.length() - 2);
        }
        return money;
    }

    public static Double round(Double d) {
        return Math.round(d * 100.0) / 100.0;
    }
}
