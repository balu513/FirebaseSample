package com.example.basicdemo.view;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.basicdemo.R;
import com.example.basicdemo.model.Player;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FirebaseDataBaseOperationsActiivty extends AppCompatActivity{

    private static final String TAG = "FirebaseDataBaseOperationsActiivty";
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private DatabaseReference query;
    private RecyclerView recylerview;
    final List<Player> list = new ArrayList<Player>();
    private PPlayersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recylerview = (RecyclerView) findViewById(R.id.listviewPlayers);
        recylerview.setLayoutManager(new LinearLayoutManager(this));
         adapter = new PPlayersAdapter(this, list);
        recylerview.setAdapter(adapter);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        getUserInfo();
        getToken();

         database = FirebaseDatabase.getInstance();
         myRef = database.getReference("CricketPlayer3");
       query = myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        ((Button)findViewById(R.id.btnAddPlayer)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlayer();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
       getPlayersListFromFireBase();
        getPlayersOfAllUsers();

    }

    private void addPlayer() {
        final int id = new Random().nextInt(1000);
        final String name = ((EditText)findViewById(R.id.txtPlayerName)).getText().toString();
        final String country = ((EditText)findViewById(R.id.txtCountry)).getText().toString();
        myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(new Player(id,name,country));
    }

    /**
     * Get all records of all usrers who are all registed to this app
     */
    private void getPlayersOfAllUsers() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    list.clear();
                    // Hashmap<user, hashmap>
                    Iterator it = ((HashMap) dataSnapshot.getValue()).entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        Log.d(TAG, pair.getKey() + " = " + pair.getValue());

                        //Hashmap<randompushId, hashmap>
                        HashMap hashMap = (HashMap) pair.getValue();


                        Iterator it2 = hashMap.entrySet().iterator();
                        while (it2.hasNext()) {
                            Map.Entry pair2 = (Map.Entry) it2.next();
                            Log.d(TAG, pair2.getKey() + " = " + pair2.getValue());

                            // Hashmap<String, String>
                            HashMap hashMap2 = (HashMap) pair2.getValue();
                            String name = (String) hashMap2.get("name");
                            String country = (String) hashMap2.get("country");
                            long id = (long) hashMap2.get("id");
                            list.add(new Player(id, name, country));
                        }
                    }
                }
                Log.d(TAG, "ALL USERS DATA ->> Size: " + list.size() + "  List Players: " + list);
            }

            ;

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

    // Get only loggedIn user data
    private void getPlayersListFromFireBase()
    {
        final List<Player> list  = new ArrayList<Player>();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    list.clear();

                    //Hashmap<randompushId, hashmap>
                    Iterator it = ((HashMap)dataSnapshot.getValue()).entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry)it.next();
                        Log.d(TAG,pair.getKey() + " = " + pair.getValue());

                        //hashmap<String, String>
                        HashMap hashMap = (HashMap) pair.getValue();
                        String name = (String) hashMap.get("name");
                        String country = (String)hashMap.get("country");
                        long id= (long) hashMap.get("id");
                        list.add(new Player(id,name,country));
                    }
                }
                refreshRecylerView();
                Log.d(TAG, "Size: "+list.size()+"  List Players: "+list);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });



    }

    private void refreshRecylerView() {
        Collections.sort(list);
        adapter.notifyDataSetChanged();

    }

    private void getToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Toast.makeText(FirebaseDataBaseOperationsActiivty.this, token, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void getUserInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();

            Log.d(TAG,"name: "+name);
            Log.d(TAG,"email: "+email);
            Log.d(TAG,"photo url: "+photoUrl);
            Log.d(TAG,"email verified: "+emailVerified);
            Log.d(TAG,"udi: "+uid);

        }
    }

}
