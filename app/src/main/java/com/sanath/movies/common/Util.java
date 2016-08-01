package com.sanath.movies.common;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.sanath.movies.R;
import com.sanath.movies.models.Trailer;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by Sanath Nandasiri on 7/19/2016.
 */
public class Util {
    public static String getRating(String voteAverage) {
        try {
            double rate = Double.parseDouble(voteAverage);
            DecimalFormat df = new DecimalFormat("#.#");
            df.setRoundingMode(RoundingMode.CEILING);
            return df.format(rate);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static void setPreferencesChange(Context context, boolean isPreferencesChanged) {
        getDefaultSharedPreferences(context).edit().putBoolean("pref_key_setting_change", isPreferencesChanged).apply();
    }

    public static boolean isPreferencesChanged(Context context) {
        return getDefaultSharedPreferences(context).getBoolean("pref_key_setting_change", true);
    }

    public static String getDefaultSortOrder(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        return sharedPreferences.getString("pref_key_default_sort_order", context.getString(R.string.pref_val_popular));

    }

    private static SharedPreferences getDefaultSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public static void openYoutubeVideo(Context context, Trailer trailer) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailer.key));
            context.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(trailer.getVideoUrl()));
            context.startActivity(intent);
        }
    }
}
