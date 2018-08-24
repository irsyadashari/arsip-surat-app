package com.blogspot.irsyadashari.hmifapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.blogspot.irsyadashari.hmifapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class SignUpActivity extends AppCompatActivity {

    String displayNameUserInput;

    private ImageButton profilepic;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText displaytNameEditText;
    private Button createAccountBtn;

    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private StorageReference mFirebaseStorage;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ProgressDialog mProgressDialog;
    private Uri resultUri = null;
    private final static int GALLERY_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("MUsers");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mFirebaseStorage = FirebaseStorage.getInstance().getReference().child("MArsip_Profile_Pics");

        mProgressDialog = new ProgressDialog(this);


        //Connecting all the widgets
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        displaytNameEditText = findViewById(R.id.displaytNameEditText);

        //image button config
        profilepic = findViewById(R.id.profilePic);
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);
            }
        });

        //createAccountButton config
        createAccountBtn = findViewById(R.id.createAccountBtn);
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: 1.cek jika pasword dan confirmpassword sama
                //TODO: 2.createUserWithEmailAndPassword(email, password)
                //TODO: 3.jika no.1 benar,SIGN IN kan user ke MainActivity

                createNewAccount();

            }
        });

    }

    private void createNewAccount() {

        final String unameUserInput = emailEditText.getText().toString().trim();
        String passwordUserInput = passwordEditText.getText().toString().trim();
        String conPasswordUserInput = confirmPasswordEditText.getText().toString().trim();
        displayNameUserInput = displaytNameEditText.getText().toString().trim();

        if(!TextUtils.isEmpty(unameUserInput) && !TextUtils.isEmpty(passwordUserInput)
                && !TextUtils.isEmpty(conPasswordUserInput) && !TextUtils.isEmpty(displayNameUserInput))
        {
            mProgressDialog.setMessage("Creating Account...");
            mProgressDialog.show();

            if(resultUri != null)
            {
                if(conPasswordUserInput.equals(passwordUserInput) )//confirmPassword == password
                {
                    mAuth.createUserWithEmailAndPassword(unameUserInput,passwordUserInput)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    if(authResult != null)
                                    {
                                        StorageReference imagePath = mFirebaseStorage.child("MArsip_Profile_Pics")
                                                .child(resultUri.getLastPathSegment());

                                        imagePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                String userid = mAuth.getCurrentUser().getUid();

                                                //adding all the user bio (in this case, only displayname and profile pic)
                                                DatabaseReference currentUserDb = mDatabaseReference.child(userid);
                                                currentUserDb.child("displayName").setValue(displayNameUserInput);
                                                currentUserDb.child("Image").setValue(resultUri.toString());


                                                mProgressDialog.dismiss();
                                                Toast.makeText(SignUpActivity.this,"Sign Up successful",Toast.LENGTH_SHORT).show();
                                                Intent gotoMain = new Intent(SignUpActivity.this, MainActivity.class );
                                                startActivity(gotoMain);
                                                finish();
                                            }
                                        });
                                    }
                                }
                            });
                }
                else
                {   //confirmPassword != password
                    Toast.makeText(SignUpActivity.this,"Confirm password doesn't match",Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                }
            }
            else
                {
                Toast.makeText(SignUpActivity.this,"set the profile pic first",Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }

        }
        else {
            Toast.makeText(SignUpActivity.this,"Fill the blank",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_CODE && resultCode == RESULT_OK)
        {
            Uri mImageUri = data.getData();

            CropImage.activity(mImageUri)
                    .setAspectRatio(1,1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri(); // getting the cropped picture by user image

                profilepic.setImageURI(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }
}
