package com.example.cmput301w21t25.custom;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.example.cmput301w21t25.forum.Comment;
import com.example.cmput301w21t25.user.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Eden
 * A custom array adapter used to display users in list views
 */
public class CustomListUser extends ArrayAdapter<User> {
    private ArrayList<User> allUsers;
    private ArrayList<User> hiddenUsers;
    private Context context;
    private User user;

    public CustomListUser(Context context, ArrayList<User> allUsers, ArrayList<User> hiddenUsers) {
        super(context,0,allUsers);
        this.allUsers = allUsers;
        this.context = context;
        this.hiddenUsers = hiddenUsers;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        //The content view is not displayed if the adapter is empty
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.user_content, parent, false);
        }

        user = allUsers.get(position);

        TextView userName = view.findViewById(R.id.userName);
        TextView userEmail = view.findViewById(R.id.userContactInfo);

        LinearLayout background = view.findViewById(R.id.userListBackground);
        Drawable contentBackground = background.getBackground();

        //Grey out the background if the user is hidden
        if (hiddenUsers.contains(user)) {
            contentBackground.setTint(context.getResources().getColor(R.color.custom_Grey));
        }
        if (!hiddenUsers.contains(user)) {
            contentBackground.setTint(context.getResources().getColor(R.color.design_default_color_background));
        }

        userName.setText(user.getName());
        userEmail.setText(user.getEmail());

        return view;
    }
}