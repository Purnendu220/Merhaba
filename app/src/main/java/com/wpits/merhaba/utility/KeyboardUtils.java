package com.wpits.merhaba.utility;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by MyU10 on 12/27/2016.
 */

public class KeyboardUtils {

    private static InputMethodManager imm;

    public static void open(View view, Context context) {
        if (imm == null)
            imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void close(View view, Context context) {
        if (imm == null)
            imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
