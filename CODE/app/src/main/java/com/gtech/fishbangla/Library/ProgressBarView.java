package com.gtech.fishbangla.Library;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.gtech.fishbangla.R;
import com.gtech.fishbangla.databinding.DialogProgressbarBinding;

public class ProgressBarView extends DialogFragment {
    DialogProgressbarBinding progressbarBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //View rootView = inflater.inflate(R.layout.dialog_progressbar, container);
        if (progressbarBinding == null) {
            progressbarBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_progressbar, container, false);
        }

        return progressbarBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        try {
            super.onActivityCreated(savedInstanceState);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            if (getDialog() != null) {
                getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
            }
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        progressbarBinding = null;
    }
}