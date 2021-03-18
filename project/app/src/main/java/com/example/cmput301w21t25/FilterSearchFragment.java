package com.example.cmput301w21t25;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

public class FilterSearchFragment extends DialogFragment{
    private EditText keywordSentence;
    private ChipGroup chipGroup;
    //private String[] keywords;
    //TODO: learn how chips work?? How to record the return??
    private ArrayList<String> tags;

    /**
     * This method sets the buttons to be clicked. If the user enters the correct keywords/filters,
     * this will be returned to the SearchActivity to be processed
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_filter,null);
        keywordSentence = view.findViewById(R.id.keyword_edit_text);
        chipGroup = view.findViewById(R.id.chip_group);

        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setNegativeButton("CANCEL",null)
                .setPositiveButton("FILTER", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String sentence = keywordSentence.getText().toString();

                        if(sentence == ""){
                            //TODO: this is not working? whay???
                            Toast.makeText(view.getContext(), "Please enter a keyword", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            //keywords = sentence.split(" ");
                            //TODO: Return the keywords/chips to be used to sort? Or use SearchManager


                        }
                    }
                }).create();

    }
}
