package com.example.cmput301w21t25;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * @author Samadhi
 * This is a custom toolbar which can be added to any activity/view!
 * Note: in the XML for the activity/view make sure to put this code in:
 *   <include
 *         layout = "@layout/custom_toolbar"/>
 */
public class CustomToolbar extends AppCompatActivity {
    String colour;
    //More menu items can be added if needed!
    MenuItem home;
    MenuItem userSettings;

    public CustomToolbar(String colour, Context context){
        this.colour = colour;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();

        //setting the colour of the toolbar :)
        if(colour == "yellow") {
            menuInflater.inflate(R.menu.toolbar_menu, menu);
        }
        else{
            menuInflater.inflate(R.menu.toolbar_menu_blue, menu);
        }
        return true;
    }

    //
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.home_button:
                return true;
            case R.id.settings_button:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


