package com.example.cmput301w21t25.histograms;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.cmput301w21t25.FilterSearchFragment;
import com.example.cmput301w21t25.R;
/**
 * This class creates a fragment which allows the user to select which type of graph they would like
 * to view.
 */
public class HistogramFragment extends DialogFragment {
    private OnFragmentInteractionListener listener;
    Button graphButton;
    Button plotButton;

    public interface OnFragmentInteractionListener{
        void onButtonPressed(Button button);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof HistogramFragment.OnFragmentInteractionListener){
            listener = (HistogramFragment.OnFragmentInteractionListener) context;
        }
        else{
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_histogram,null);

        graphButton = view.findViewById(R.id.graph_button);
        plotButton = view.findViewById(R.id.plot_button);

        //if we create a graph
        graphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onButtonPressed(graphButton);
            }
        });

        //if we create a plot
        plotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onButtonPressed(plotButton);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setNegativeButton("CANCEL", null)
                .create();
    }
}
