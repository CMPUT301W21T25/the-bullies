package com.example.cmput301w21t25;

import android.app.AlertDialog;
import android.app.Dialog;
import com.example.cmput301w21t25.managers.SearchManager;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.cmput301w21t25.experiments.Experiment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

/**
 * This method sets the buttons to be clicked. If the user enters the correct keywords/filters,
 * this will be returned to the SearchExperimentsActivity to be processed
 */

public class FilterSearchFragment extends DialogFragment{
    public SearchManager searchManager = new SearchManager();
    private ArrayList<Experiment> filteredExperiments;
    private OnFragmentInteractionListener listener;
    private EditText keywordSentence;
    private ChipGroup chipGroup;
    private StringBuilder allKeyWords = new StringBuilder("");

    public interface OnFragmentInteractionListener{
        void onOkPressed(String allKeywords);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_filter,null);
        keywordSentence = view.findViewById(R.id.keyword_edit_text);
        chipGroup = view.findViewById(R.id.chip_group);


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setNegativeButton(Html.fromHtml("<font color=#28527a>CANCEL</font>"),null)
                .setPositiveButton(Html.fromHtml("<font color=#28527a>FILTER</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        allKeyWords.append(keywordSentence.getText().toString());
                        //TODO: This can be combined with parse? Talk to EDEN
                        for(int i = 0; i <chipGroup.getChildCount();i++){
                             Chip chip = (Chip) chipGroup.getChildAt(i);
                             if(chip.isChecked()){
                                 allKeyWords.append(", " + chip.getText().toString().toLowerCase());
                             }
                        }
                        //TODO: Return the keywords/chips to be used to sort? Or use SearchManager
                        listener.onOkPressed(allKeyWords.toString());
                    }
                }).create();

    }
}
