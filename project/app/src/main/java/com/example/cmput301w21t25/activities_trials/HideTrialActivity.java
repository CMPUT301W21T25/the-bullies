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
import com.example.cmput301w21t25.customAdapters.CustomListUser;
import com.example.cmput301w21t25.customDialogs.HideTrialDialogFragment;
import com.example.cmput301w21t25.customDialogs.ShowTrialDialogFragment;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.managers.ExperimentManager;
import com.example.cmput301w21t25.managers.UserManager;
import com.example.cmput301w21t25.user.User;

import java.util.ArrayList;

/**
 * @author Eden
 * The hide trial activity displays a list of users who have added trials to your experiment. You
 * can select users to hide their trials, then select them once again to show their trials
 */
public class HideTrialActivity extends AppCompatActivity implements HideTrialDialogFragment.OnFragmentInteractionListenerHide, ShowTrialDialogFragment.OnFragmentInteractionListenerShow {

    ListView userListView;
    ArrayAdapter<User> userArrayAdapter;

    private String userID;
    private Experiment exp;

    private ArrayList<User> allUsers = new ArrayList<>();
    private ArrayList<User> hiddenUsers = new ArrayList<>();
    private UserManager userManager = new UserManager();
    private ExperimentManager experimentManager = new ExperimentManager();

    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_hide_trials);

        /*setup the custom toolbar!
         */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Test objects
//        User user1 = new User("User1", "user1@example.com");
//        User user2 = new User("User2", "user2@example.com");
//        User user3 = new User("User3", "user3@example.com");
//
//        allUsers.add(user1);
//        allUsers.add(user2);
//        allUsers.add(user3);
//        hiddenUsers.add(user2);

        userID = getIntent().getStringExtra("USER_ID");
        exp = (Experiment) getIntent().getSerializableExtra("EXPERIMENT");

        userArrayAdapter = new CustomListUser(this, allUsers, hiddenUsers);
        //@Yalmaz pass adapter to function that gets user list/hidden user list

        userListView = findViewById(R.id.hide_trials_list);
        userListView.setAdapter(userArrayAdapter);
        userManager.FB_FetchContributors(exp,userArrayAdapter,allUsers);
        userManager.FB_FetchHidden(exp,userArrayAdapter,hiddenUsers);

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

    @Override
    public void hideUser(Integer position) {
        //@Yalmaz db call to add user to hidden list
//        User temp = allUsers.get(position);
//        hiddenUsers.add(temp);
//        userArrayAdapter.notifyDataSetChanged();
        User hide = allUsers.get(position);
        hiddenUsers.add(hide);
        userArrayAdapter.notifyDataSetChanged();
        ArrayList<String> temp = exp.getHiddenUsersKeys();
        temp.add(allUsers.get(position).getUserID());
        experimentManager.FB_UpdateHiddenUserKeys(temp,exp.getFb_id());
    }

    @Override
    public void showUser(Integer position) {
        //@Yalmaz db call to remove user from hidden list
//        User temp = allUsers.get(position);
//        hiddenUsers.remove(temp);
//        userArrayAdapter.notifyDataSetChanged();
        ArrayList<String> temp = exp.getHiddenUsersKeys();
        temp.remove(allUsers.get(position).getUserID());
        experimentManager.FB_UpdateHiddenUserKeys(temp,exp.getFb_id());
    }
}
