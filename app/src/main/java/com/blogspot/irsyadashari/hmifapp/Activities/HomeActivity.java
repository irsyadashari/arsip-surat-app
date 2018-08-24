package com.blogspot.irsyadashari.hmifapp.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blogspot.irsyadashari.hmifapp.R;
import com.blogspot.irsyadashari.hmifapp.model.Surat;
import com.blogspot.irsyadashari.hmifapp.model.UserInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private String displayName;
    private String profPicUri;

    private List<UserInformation> userList;

    private DatabaseReference mDatabaseReference;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mDatabase;
    private String userID;

    //getting the display name and profpic
    private DatabaseReference zoneUser;
    private DatabaseReference generalRef;
    private UserInformation userBio;//the user those who logged in


    private Button signOutBtn;
    private Button inputArsipBtn;
    private Button lihatArsipBtn;

    private CircleImageView userProfilePic;

    private TextView greetUserText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //firebase variable init
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("MArsip_Profile_Pics");
        mDatabaseReference.keepSynced(true);
        zoneUser = mDatabase.getReference().child("MUsers");
        zoneUser.keepSynced(true);

        userBio = new UserInformation();

        //widget init
        signOutBtn = findViewById(R.id.signOutBtn);
        inputArsipBtn = findViewById(R.id.inputArsipBtn);
        lihatArsipBtn = findViewById(R.id.lihatArsipBtn);
        userProfilePic = findViewById(R.id.userProfilePic);
        greetUserText = findViewById(R.id.greetUserText); //I SPENT 6 FUCKING HOURS ON THE FUCKING GOOGLE ONLY BECAUSE THIS FUCKING PIECE OF CODE


        userID = mUser.getUid(); // getting the user id on firebase


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(mUser!=null){
                    userID = mUser.getUid();
                }else{

                }
            }
        };
       zoneUser.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               UserInformation userInformation = dataSnapshot.child(userID).getValue(UserInformation.class);
               String displayNameValue = userInformation.getDisplayName();
               String imageUri = userInformation.getImage();

               greetUserText.setText("Hi "+ displayNameValue);

               Uri myUri = Uri.parse(imageUri);
               try {
                   Bitmap bitmap = MediaStore.Images.Media.getBitmap(HomeActivity.this.getContentResolver(), myUri);
                   userProfilePic.setImageBitmap(bitmap);
               } catch (IOException e) {
                   e.printStackTrace();
                   //kalo gagal
               }
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });


      /* zoneUser.child(userID).child("displayName").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               greetUserText.setText(dataSnapshot.getValue().toString());
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });*/

        //TODO:SET THIS LATER AFTER GETTING THE DB REF
//        String greetings = "Hi "+displayName;
//        greetUserText.setText(greetings);

        //TODO: SET THE USER PROFPIC
        //userProfilePic.setImageBitmap();

        signOutBtn.setOnClickListener(this);
        inputArsipBtn.setOnClickListener(this);
        lihatArsipBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.signOutBtn:
                mAuth.signOut();
                startActivity(new Intent(HomeActivity.this,MainActivity.class));
                finish();
                break;

            case R.id.lihatArsipBtn:
                //GOTO lihat arsip Activity
                startActivity(new Intent(HomeActivity.this,LihatArsip.class));
                break;

            case R.id.inputArsipBtn:
                //GOTO Input Arsip Activity
                startActivity(new Intent(HomeActivity.this,InputArsip.class));
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*zoneUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {//this method also called when the activity starts
                UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
                displayName = userInformation.getDisplayName();
                profPicUri = userInformation.getImage();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener!= null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
