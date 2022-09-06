package com.newsoft.nscustom.view.edittext;

import android.annotation.SuppressLint;
import android.os.Build;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class Utility {

    public static final boolean isAlphaNumericValidator(EditText et) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9\u00C0-\u00FF \\./-\\?]*");
        return pattern.matcher(et.getText()).matches();
    }

    public static final boolean isAlphaValidator(EditText et) {
        Pattern pattern = Pattern.compile("[A-z\u00C0-\u00ff \\./-\\?]*");
        return pattern.matcher(et.getText()).matches();
    }

    public static boolean isNumericValidator(EditText et) {
        return TextUtils.isDigitsOnly(et.getText());
    }

    public static final boolean isCreditCardValidator(EditText et) {
        try {
            return validateCardNumber(et.getText().toString());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validates the credit card number using the Luhn algorithm
     *
     * @param cardNumber the credit card number
     * @return
     */
    private static boolean validateCardNumber(String cardNumber) throws NumberFormatException {
        int sum = 0, digit, addend = 0;
        boolean doubled = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            digit = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (doubled) {
                addend = digit * 2;
                if (addend > 9) {
                    addend -= 9;
                }
            } else {
                addend = digit;
            }
            sum += addend;
            doubled = !doubled;
        }
        return (sum % 10) == 0;
    }

    @SuppressLint("SimpleDateFormat")
    public static final boolean isDateValidator(EditText et) {
        if (TextUtils.isEmpty(et.getText().toString()))
            return true;
        String s = et.getText().toString();
        String[] formats = TextUtils.isEmpty(s) ? new String[]{"DefaultDate", "DefaultTime", "DefaultDateTime"} : s.split(";");
        String value = et.getText().toString();
        for (String _format : formats) {
            DateFormat format;
            if ("DefaultDate".equalsIgnoreCase(_format)) {
                format = SimpleDateFormat.getDateInstance();
            } else if ("DefaultTime".equalsIgnoreCase(_format)) {
                format = SimpleDateFormat.getTimeInstance();
            } else if ("DefaultDateTime".equalsIgnoreCase(_format)) {
                format = SimpleDateFormat.getDateTimeInstance();
            } else {
                format = new SimpleDateFormat(_format);
            }
            Date date = null;
            try {
                date = format.parse(value);
            } catch (ParseException e) {
                return false;
            }
            if (date != null) {
                return true;
            }
        }
        return false;
    }

    public static final boolean isDomainValidator(EditText et) {
        Pattern pattern = Pattern.compile(String.valueOf(Build.VERSION.SDK_INT >= 8 ? Patterns.DOMAIN_NAME : Pattern.compile(".*")));
        return pattern.matcher(et.getText()).matches();
    }


    public static final boolean isEmail(EditText et) {
        Pattern pattern = Build.VERSION.SDK_INT >= 8 ? Patterns.EMAIL_ADDRESS : Pattern.compile(
                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+"
        );

        return pattern.matcher(et.getText()).matches();
    }

    public static final boolean isEmptyValidator(EditText et) {
        return TextUtils.getTrimmedLength(et.getText()) > 0;
    }

    public static final boolean isIpAddressValidator(EditText et) {
        Pattern pattern = Pattern.compile(String.valueOf(Build.VERSION.SDK_INT >= 8 ? Patterns.IP_ADDRESS : Pattern.compile(
                "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                        + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                        + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                        + "|[1-9][0-9]|[0-9]))")));
        return pattern.matcher(et.getText()).matches();
    }

    public static final boolean isPersonFullNameValidator(EditText et) {
        Pattern pattern = Pattern.compile("[\\p{L}- ]+");
        return pattern.matcher(et.getText()).matches();
    }

    public static final boolean isPersonNameValidator(EditText et) {
        Pattern pattern = Pattern.compile("[\\p{L}-]+");
        return pattern.matcher(et.getText()).matches();
    }

    public static final boolean isPhone(EditText et) {
        Pattern pattern = Pattern.compile(String.valueOf(Build.VERSION.SDK_INT >= 8 ? Patterns.PHONE : Pattern.compile(                                  // sdd = space, dot, or dash
                "(\\+[0-9]+[\\- \\.]*)?"                    // +<digits><sdd>*
                        + "(\\([0-9]+\\)[\\- \\.]*)?"               // (<digits>)<sdd>*
                        + "([0-9][0-9\\- \\.][0-9\\- \\.]+[0-9])")));
        return pattern.matcher(et.getText()).matches();
    }

    public static final boolean isWebUrlValidator(EditText et) {
        Pattern pattern = Pattern.compile(String.valueOf(Build.VERSION.SDK_INT >= 8 ? Patterns.WEB_URL : Pattern.compile(".*")));
        return pattern.matcher(et.getText()).matches();
    }

    public static boolean isNumericRangeValidator(EditText et, int min, int max) {
        try {
            int value = Integer.parseInt(et.getText().toString());
            return value >= min && value <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean isFloatNumericRangeValidator(EditText et, float floatmin, float floatmax) {
        try {
            float value = Float.parseFloat(et.getText().toString());
            return value >= floatmin && value <= floatmax;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean isRegexpValidator(EditText et, String regexp) {
        Pattern pattern = Pattern.compile(regexp);
        return pattern.matcher(et.getText()).matches();
    }


}
