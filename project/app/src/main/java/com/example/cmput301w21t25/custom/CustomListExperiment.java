package com.example.cmput301w21t25.custom;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.experiments.Experiment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Eden
 * A custom array adapter to display experiments in a list view
 */
public class CustomListExperiment extends ArrayAdapter<Experiment> {
    private ArrayList<Experiment> experiments;
    private Context context;
    private Date date;
    private String dateString;

    public CustomListExperiment(Context context, ArrayList<Experiment> experiments) {
        super(context, 0, experiments);
        this.experiments = experiments;
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        //The content view is not displayed if the adapter is empty
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.exp_content, parent, false);
        }

        Experiment experiment = experiments.get(position);

        LinearLayout background = view.findViewById(R.id.background);
        Drawable contentBackground = background.getBackground();

        TextView experimentDescription = view.findViewById(R.id.exp_description_text_view);
        TextView experimentDate = view.findViewById(R.id.exp_date_text_view);
        TextView userName = view.findViewById(R.id.user_id_text_view);
        //We currently aren't displaying images
        //ImageView userImage = view.findViewById(R.id.imageView2);

        date = experiment.getDate();
        dateString = formatDate(date);

        experimentDescription.setText(experiment.getName());
        experimentDate.setText(dateString);
        userName.setText(experiment.getOwner());

        if (experiment.getType().equals("binomial")) {
            contentBackground.setTint(context.getResources().getColor(R.color.custom_Blue_light));
        }
        if (experiment.getType().equals("count")) {
            contentBackground.setTint(context.getResources().getColor(R.color.custom_Yellow_dark));
        }
        if (experiment.getType().equals("measurement")) {
            contentBackground.setTint(context.getResources().getColor(R.color.custom_Yellow_light));
        }
        if (experiment.getType().equals("nonnegative count")) {
            contentBackground.setTint(context.getResources().getColor(R.color.design_default_color_background));
        }

        return view;
    }

    /**
     *
     * @param date
     * The date the experiment was created
     * @return
     * A formatted version of the date (String)
     */
    String formatDate(Date date) {

        SimpleDateFormat condensedDate = new SimpleDateFormat("MM-dd-yyyy");
        String formattedDate = condensedDate.format(date);
        return formattedDate;
    }

}