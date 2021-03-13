package com.example.cmput301w21t25;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class UserManager {
    private ArrayList<User> userList;


    /**
     * please look over this before continuing work
     * Database stuff starting here ill add proper comments later in a bit of a rush atm
     * methods to get and set to database use as u please attributes used in this section are listed below
     * -YA
     * todo:
     *  -Usermangaer can make new user doc in DB(Done)
     *  -Usermanager can delete existing doc in DB(Done)
     *  -Usermanager can summon user doc in DB()
     * */
    //attaching firebase good comments to come later please bear with me -YA
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //extra attributes to make ur life easier:
    private User selectedUser;
    public void FB_AddUser(User user){
        db.collection("UserProfile").document().set(user);
    }
    public void FB_DeleteUser(String id){
        db.collection("UserProfile").document(id).delete();
    }
    public User FB_GetUser(String id){

        DocumentReference docRef = db.collection("UserProfile").document(id);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                selectedUser = documentSnapshot.toObject(User.class);
            }
        });
        return selectedUser;
    }
    /**
     * End of database stuff -YA
     * */



    public UserManager() {
    }

    public void newUser() {
    }

    public void deleteUser() {
    }
}
