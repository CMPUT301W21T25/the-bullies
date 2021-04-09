package com.example.cmput301w21t25.managers;

import android.icu.text.UFormat;
import android.location.Location;
import android.os.Build;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.cmput301w21t25.FirestoreCommentCallback;
import com.example.cmput301w21t25.FirestoreStringCallback;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.forum.Comment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * The forum manager is responsible for fetching comments, as well as sorting them in a particular
 * order such that they're nested by "in response to" and ordered by date (oldest to newest)
 */
public class ForumManager {

    public ForumManager() { }

    /**
     * Base Code from: Matthew Mombrea, "How to create nested comments in Java / Android"
     * Accessed through COMPUTERWORLD; Published July 16, 2014
     * URL: https://www.computerworld.com/article/2696636/how-to-create-nested-comments-in-java---android.html
     * Alterations made by Eden
     * Sorts comments in a nested fashion such that a particular question's responses are listed
     * directly below it by order of date
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Comment> nestedComments(ArrayList<Comment> comments) {
        ArrayList<Comment> orderedForum = new ArrayList<Comment>();
        ArrayList<Comment> toDelete = new ArrayList<Comment>();

        //Sort the comments by date to preserve sensical order
        comments.sort(Comparator.comparing(comment -> comment.getCommentDate()));

        //First add new thread comments that have no parents (were passed empty parentID string
        //upon construction)
        for (int i = 0; i < comments.size(); i++) {
            Comment comment = comments.get(i);
            if (comment.getCommentParent().equals("")) {
                orderedForum.add(comment);
                toDelete.add(comment);
            }
        }

        if (toDelete.size() > 0) {
            comments.removeAll(toDelete);
            toDelete.clear();

        }

        //The outer loop runs while the size of the array of comments passed is greater than zero,
        //as in some instances the parents of children comments will also be children, so they would
        //not be present to check against in the orderedForum list on a first pass
        while (comments.size() > 0) {
            for (int i = 0; i < comments.size(); i++) {
                Comment childComment = comments.get(i);
                //The current child is checked against each existing comment in the orderedForum
                //list for a parent
                for (int j = 0; j < orderedForum.size(); j++) {
                    Comment parentComment = orderedForum.get(j);
                    //If the parent is found, it is added to the orderedForum list and removed from
                    //the comments list. If not, it will be returned to on the next pass of the
                    // outer loop after new potential parents have been added to the orderedForum list
                    if (parentComment.getCommentID().equals(childComment.getCommentParent())) {
                        //The number of children is incremented so that the child is properly
                        //placed in the list (e.g., if the parent is at index 4 and currently has
                        //2 children, its children are at index 5 and 6. When another child is found,
                        //the children count is incremented and the new child is placed at the parent
                        //index + its children count, 4 + 3, which means it is at index 7, the
                        //properly ordered index
                        parentComment.setCommentChildren(parentComment.getCommentChildren() + 1);
                        orderedForum.add((j + parentComment.getCommentChildren()), childComment);
                        toDelete.add(childComment);
                        continue;
                    }
                }
            }
            //Delete
            if (toDelete.size() > 0) {
                comments.removeAll(toDelete);
                toDelete.clear();
            }
        }

        return orderedForum;
    }

    /////
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ExperimentManager expManager = new ExperimentManager();

    /**
     *
     * @param comment
     * @param commenterID
     * @param commentParent
     */
    public void FB_CreateComment(String experimentID, String comment,String commenterID,String commentParent,String respondingTo){
        // Create a new experiment Hash Map this is the datatype stored in firebase for documents
        Map<String,Object> experimentDoc  = new HashMap<>();
        experimentDoc.put("comment", comment);
        experimentDoc.put("commenterName", "");
        experimentDoc.put("commenterID", commenterID);
        experimentDoc.put("commentParent",commentParent);
        experimentDoc.put("commentDate", new Date());
        experimentDoc.put("respondingTo", respondingTo);

        // Add a new Experiment with a generated ID
        db.collection("Comments")
                .add(experimentDoc)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        db.collection("Experiments").document(experimentID).get()

                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            DocumentSnapshot document = task.getResult();
                                            if(document.exists()){
                                                ArrayList<String> currentComments = (ArrayList<String>)document.getData().get("commentKeys");
                                                currentComments.add(documentReference.getId());
                                                UserManager mana = new UserManager();
                                                mana.FB_FetchUserInfo(commenterID, new FirestoreStringCallback() {
                                                    @Override
                                                    public void onCallback(ArrayList<String> list) {
                                                        documentReference.update("commenterName", list.get(1));
                                                    }
                                                });
                                                expManager.FB_UpdateCommentKeys(currentComments,experimentID);
                                            }
                                        }
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    //security stuff to make debuging easier
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding experiment", e);
                    }
                });
    }
    public void FB_FetchComments(Experiment exp, ArrayAdapter<Comment> commentAdapter, ArrayList<Comment> comments){
        expManager.FB_FetchCommentKeys(exp.getFb_id(), new FirestoreStringCallback() {
            @Override
            public void onCallback(ArrayList<String> list) {
                Log.d("FORUM_TEST2:", String.valueOf(list));
                if(!list.isEmpty()){
                    Log.d("YA-DB TEST: ", "calling the fetch" );
                    db.collection("Comments")
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @RequiresApi(api = Build.VERSION_CODES.N)
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                                    comments.clear();
                                    commentAdapter.notifyDataSetChanged();
                                    for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                                        if(list.contains(doc.getId())){
                                            Comment temp =doc.toObject(Comment.class);
                                            temp.setCommentID(doc.getId());
                                            comments.add(temp);
                                        }
                                    }
                                    ArrayList<Comment> sorted = nestedComments(comments);
                                    comments.addAll(sorted);
                                    commentAdapter.notifyDataSetChanged();
                                }
                            });
                }

            }
        });
    }
}
