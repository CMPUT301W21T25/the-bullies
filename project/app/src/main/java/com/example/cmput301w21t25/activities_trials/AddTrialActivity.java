package com.example.cmput301w21t25.activities_trials;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.activities_main.CreatedExperimentsActivity;
import com.example.cmput301w21t25.activities_user.MyUserProfileActivity;
import com.example.cmput301w21t25.customAdapters.CustomListTrial;
import com.example.cmput301w21t25.customDialogs.UploadTrialDialogFragment;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.managers.TrialManager;
import com.example.cmput301w21t25.trials.Trial;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * This activity is used to add new trials and view unpublished trials
 */
public class AddTrialActivity extends AppCompatActivity implements UploadTrialDialogFragment.OnFragmentInteractionListenerUpload {

    ListView trialListView;
    ArrayAdapter<Trial> trialArrayAdapter;
    FloatingActionButton addTrialButton;

    private String userID;
    private Experiment exp;
    private String expID;

    private TrialManager trialManager;
    private  ArrayList<Trial> trialList = new ArrayList<Trial>();

    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_trial_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.custom_Blue_dark));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        trialManager = new TrialManager();

        userID = getIntent().getStringExtra("USER_ID");
        exp = (Experiment) getIntent().getSerializableExtra("TRIAL_PARENT");
        expID = exp.getFb_id();
        //FB_FetchTrialKeys(expID,userID,exp);
        trialArrayAdapter = new CustomListTrial(this, trialList);
        trialManager.FB_UpdateTrialAdapter(exp,trialArrayAdapter,trialList,userID);
        addTrialButton = findViewById(R.id.trial_create_button);
        trialListView = findViewById(R.id.add_trial_list);


        trialListView.setAdapter(trialArrayAdapter);

        trialListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new UploadTrialDialogFragment(position).show(getSupportFragmentManager(), "UPLOAD_TRIAL");
            }
        });

        addTrialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Launches a conduct trial activity based on type of experiment

                Intent switchScreen = new Intent(AddTrialActivity.this, ChooseConductActivity.class);
                switchScreen.putExtra("USER_ID", userID);
                switchScreen.putExtra("TRIAL_PARENT", exp);
                startActivity(switchScreen);
            }
        });
    }

    //Toolbar Menu setup!
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu_blue,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.home_button:
                Intent home = new Intent(AddTrialActivity.this, CreatedExperimentsActivity.class);
                home.putExtra("USER_ID", userID);
                startActivity(home);
                return true;
            case R.id.settings_button:
                Intent user_settings = new Intent(AddTrialActivity.this, MyUserProfileActivity.class);
                user_settings.putExtra("USER_ID", userID);
                user_settings.putExtra("prevScreen", "Browse");
                startActivity(user_settings);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Published a trial at the position clicked on in the trial list view
     * @param position
     * The index of the trial you want to publish in the list
     */
    @Override
    public void publishTrial(Integer position) {
        Trial temp = trialList.get(position);
        temp.setPublished(true);
        trialManager.FB_UpdatePublished(true, temp.getTrialId());
        trialArrayAdapter.notifyDataSetChanged();
    }
}
