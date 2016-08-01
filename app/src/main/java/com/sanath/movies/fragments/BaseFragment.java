package com.sanath.movies.fragments;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by sanathnandasiri on 8/1/16.
 */
public class BaseFragment extends Fragment {
    private Snackbar mSnackbar;

    protected void dismissSnackBar() {
        if (mSnackbar != null && mSnackbar.isShown()) {
            mSnackbar.dismiss();
        }
    }

    protected void showSnackBar(int messageId, int duration, int actionButtonName, View.OnClickListener clickListener) {
        mSnackbar = Snackbar.make(getView(), messageId, duration);
        mSnackbar.setAction(actionButtonName, clickListener).show();
    }
}
