package com.example.cmput301w21t25.customDialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.cmput301w21t25.R;

/**
 * @author Eden
 * A dialog fragment that prompts the user when they click on a trial they've created, asking
 * whether or not they want to upload the trial, as this action can't be undone.
 * Is called in AddTrialActivity
 */
public class UploadTrialDialogFragment extends DialogFragment {

    public interface OnFragmentInteractionListenerUpload {
        void publishTrial(Integer position);
    }

    private OnFragmentInteractionListenerUpload listener;
    private Integer position;

    public UploadTrialDialogFragment(Integer position) {
        this.position = position;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListenerUpload){
            listener = (OnFragmentInteractionListenerUpload) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.upload_trial_fragment, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setNegativeButton(Html.fromHtml("<font color=#28527a>CANCEL</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                    }
                })
                .setPositiveButton(Html.fromHtml("<font color=#28527a>UPLOAD</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.publishTrial(position);
                    }}).create();
    }
}
