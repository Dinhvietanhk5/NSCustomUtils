package com.newsoft.permission.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class PermissionUtils {

    public static boolean neverAskAgainSelected(androidx.fragment.app.Fragment activity, final String permission) {
        final boolean prevShouldShowStatus = getRatinaleDisplayStatus(activity, permission);
        @SuppressLint({"NewApi", "LocalSuppress"}) final boolean currShouldShowStatus = activity.shouldShowRequestPermissionRationale(permission);
        return prevShouldShowStatus != currShouldShowStatus;
    }

    public static void setShouldShowStatus(final androidx.fragment.app.Fragment context, final String permission) {
        SharedPreferences genPrefs = context.getActivity().getSharedPreferences("GENERIC_PREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = genPrefs.edit();
        editor.putBoolean(permission, true);
        editor.commit();
    }

    public static boolean getRatinaleDisplayStatus(final androidx.fragment.app.Fragment context, final String permission) {
        SharedPreferences genPrefs = context.getActivity().getSharedPreferences("GENERIC_PREFERENCES", Context.MODE_PRIVATE);
        return genPrefs.getBoolean(permission, false);
    }

}
