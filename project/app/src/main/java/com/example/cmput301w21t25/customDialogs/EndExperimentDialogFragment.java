package com.example.cmput301w21t25.customDialogs;

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

/**
 * @author Eden
 * A dialog fragment that appears when an experiment owner clicks on an item in the HideTrialActivity
 * list view. It prompts the owner as to whether they want to hide the selected user's trials that
 * have been uploaded to the owner's experiment.
 */
public class EndExperimentDialogFragment extends DialogFragment {

    public interface OnFragmentInteractionListenerEnd {
        void endExperiment();
    }

    private OnFragmentInteractionListenerEnd listener;

    public EndExperimentDialogFragment() { }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListenerEnd){
            listener = (OnFragmentInteractionListenerEnd) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.end_experiment_fragment, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("               End the Experiment?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                    }
                })
                .setPositiveButton("End", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.endExperiment();
                    }}).create();
    }
}
