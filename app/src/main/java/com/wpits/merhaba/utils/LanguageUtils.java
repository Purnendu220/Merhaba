package com.wpits.merhaba.utils;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;



import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class LanguageUtils {


    public static String numberConverter(int value)
    {
        String localLanguage=NumberFormat.getInstance(Locale.getDefault()).format(value);
        return localLanguage;
    }
    public static String numberConverter(float value)
    {
        NumberFormat format = DecimalFormat.getInstance();
        format.setRoundingMode(RoundingMode.FLOOR);
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(1);
        return format.format(value);
       // String localLanguage=NumberFormat.getNumberInstance(Locale.getDefault()).format(value);
        //return localLanguage;
    }
    public static String numberConverter(double value)
    {
        NumberFormat format = DecimalFormat.getInstance();
        format.setRoundingMode(RoundingMode.CEILING);
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(1);
        return format.format(value);

    }
    public static String numberConverter(double value,int decimalDigits)
    {
        NumberFormat format = DecimalFormat.getInstance();
        format.setRoundingMode(RoundingMode.CEILING);
        format.setMinimumFractionDigits(decimalDigits);
        format.setMaximumFractionDigits(decimalDigits);
        DecimalFormat df = new DecimalFormat("###.##");
        return df.format(value);

    }
    public static String promoConverter(double value,int decimalDigits)
    {
       /* NumberFormat format = DecimalFormat.getInstance();
        format.setRoundingMode(RoundingMode.FLOOR);
        format.setMinimumFractionDigits(decimalDigits);
        format.setMaximumFractionDigits(decimalDigits);*/
        return String.format("%.2f", value);

    }
    public static String currencyConverter(long value)
    {
        String localLanguage=NumberFormat.getNumberInstance(Locale.getDefault()).format(value);
        return localLanguage;
    }



    public static void setText(TextView textViewId, int numberOfViews, String viewString) {
            textViewId.setText(Html.fromHtml("<b>"+numberConverter(numberOfViews)+"</b> "));
            textViewId.append(viewString);

    }


}
