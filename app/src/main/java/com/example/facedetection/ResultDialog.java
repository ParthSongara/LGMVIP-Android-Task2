package com.example.facedetection;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ResultDialog extends DialogFragment {

    private static final String ARG_RESULT_TEXT = "result_text";

    public static ResultDialog newInstance(String resultText) {
        ResultDialog fragment = new ResultDialog();
        Bundle args = new Bundle();
        args.putString(ARG_RESULT_TEXT, resultText);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resultdialog, container, false);

        TextView tvResultText = view.findViewById(R.id.tvResultText);
        if (getArguments() != null) {
            String resultText = getArguments().getString(ARG_RESULT_TEXT);
            tvResultText.setText(resultText);
        }

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }
}
