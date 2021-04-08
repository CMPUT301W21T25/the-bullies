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
import com.example.cmput301w21t25.activities_main.SearchExperimentsActivity;
import com.example.cmput301w21t25.activities_user.MyUserProfileActivity;
import com.example.cmput301w21t25.custom.CustomListUser;
import com.example.cmput301w21t25.custom.HideTrialDialogFragment;
import com.example.cmput301w21t25.custom.ShowTrialDialogFragment;
import com.example.cmput301w21t25.custom.UploadTrialDialogFragment;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.user.User;

import java.util.ArrayList;

public class HideTrialActivity extends AppCompatActivity {

    ListView userListView;
    ArrayAdapter<User> userArrayAdapter;

    private String userID;
    private Experiment exp;

    private ArrayList<User> allUsers = new ArrayList<>();
    private ArrayList<User> hiddenUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_hide_trials);

        /*setup the custom toolbar!
         */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userID = getIntent().getStringExtra("USER_ID");
        exp = (Experiment) getIntent().getSerializableExtra("EXPERIMENT");

        userArrayAdapter = new CustomListUser(this, allUsers, hiddenUsers);
        //Pass adapter to function that gets user list/hidden user list

        userListView = findViewById(R.id.hide_trials_list);
        userListView.setAdapter(userArrayAdapter);

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (hiddenUsers.contains(allUsers.get(position))) {
                    new ShowTrialDialogFragment(position).show(getSupportFragmentManager(), "SHOW_USER_TRIALS");
                }
                else {
                    new HideTrialDialogFragment(position).show(getSupportFragmentManager(), "HIDE_USER_TRIALS");
                }
            }
        });
    }

    /**
     * This event is menu setup!
     * @param menu this is the menu being integrated
     * @return true to indicate there is a menu (return false to turn off)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    /**
     * This event is for menu item setup
     * @param item these are items that will be added to the menu
     * @return @return true to indicate there is this item (return false to turn off)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.home_button:
                Intent home = new Intent(HideTrialActivity.this, CreatedExperimentsActivity.class);
                home.putExtra("USER_ID", userID);
                startActivity(home);
                return true;
            case R.id.settings_button:
                Intent user_settings = new Intent(HideTrialActivity.this, MyUserProfileActivity.class);
                user_settings.putExtra("USER_ID", userID);
                //I think this will work but have to check
                user_settings.putExtra("TRIAL_PARENT", exp);
                user_settings.putExtra("prevScreen", "HideTrial");
                startActivity(user_settings);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
