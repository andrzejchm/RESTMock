package io.appflate.restmock.androidsample.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by andrzejchm on 23/04/16.
 */
public class Utils {
    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                activity.findViewById(android.R.id.content).getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
