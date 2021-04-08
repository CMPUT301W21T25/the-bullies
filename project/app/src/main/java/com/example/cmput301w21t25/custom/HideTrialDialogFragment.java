package com.example.cmput301w21t25.custom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.cmput301w21t25.R;

public class HideTrialDialogFragment extends DialogFragment {

    public interface OnFragmentInteractionListenerHide {
        void hideUser(Integer position);
    }

    private OnFragmentInteractionListenerHide listener;
    private Integer position;

    public HideTrialDialogFragment(Integer position) {
        this.position = position;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListenerHide){
            listener = (OnFragmentInteractionListenerHide) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.hide_user_fragment, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                    }
                })
                .setPositiveButton("Hide", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.hideUser(position);
                    }}).create();
    }
}
