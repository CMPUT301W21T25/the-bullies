package com.example.cmput301w21t25.customAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.trials.Trial;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Eden
 * A custom array adapter used to display trials in list views
 */
public class CustomListTrial extends ArrayAdapter<Trial> {
    private ArrayList<Trial> trials;
    private Context context;
    private Date date;
    private String dateString;

    public CustomListTrial(Context context, ArrayList<Trial> trials) {
        super(context,0,trials);
        this.trials = trials;
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        //The content view is not displayed if the adapter is empty
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.trial_content, parent, false);
        }

        Trial trial = trials.get(position);

        TextView experimentName = view.findViewById(R.id.trial_parent_name);
        TextView trialDate = view.findViewById(R.id.trial_date);

        date = trial.getDate();
        dateString = formatDate(date);

        experimentName.setText(trial.getExperimentName() + " Trial");
        trialDate.setText(dateString);

        return view;
    }

    /**
     *
     * @param date
     * The date the experiment was created
     * @return
     * A formatted version of the date (String)
     */
    private String formatDate(Date date) {

        SimpleDateFormat condensedDate = new SimpleDateFormat("MM-dd-yyyy");
        String formattedDate = condensedDate.format(date);
        return formattedDate;
    }

}