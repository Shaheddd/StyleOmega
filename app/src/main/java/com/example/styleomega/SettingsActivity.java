package com.example.styleomega;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.styleomega.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity
{

    private CircleImageView profileImageView;
    private EditText fullNameEditText, userPhoneEditText, addressEditText;
    private TextView changeProfileTextButton, closeTextButton, saveTextButton;
    private Button SecurityQuestionButton;

    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePictureReference;
    private String checker = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        storageProfilePictureReference = FirebaseStorage.getInstance().getReference().child("Profile Pictures");

        profileImageView = (CircleImageView) findViewById(R.id.settings_profile_image);

        fullNameEditText = (EditText) findViewById(R.id.settings_full_name);
        userPhoneEditText = (EditText) findViewById(R.id.settings_phone_number);
        addressEditText = (EditText) findViewById(R.id.settings_address);

        changeProfileTextButton = (TextView) findViewById(R.id.change_profile_image_button);
        closeTextButton = (TextView) findViewById(R.id.close_settings_button);
        saveTextButton = (TextView) findViewById(R.id.update_account_settings_button);

        SecurityQuestionButton = findViewById(R.id.settings_security_questions_button);

        DisplayUserInformation(profileImageView, fullNameEditText, userPhoneEditText, addressEditText);

        closeTextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        SecurityQuestionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SettingsActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check", "settings");
                startActivity(intent);
            }
        });

        saveTextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Once this button is clicked, the User can change their picture in the Gallery
                if (checker.equals("clicked"))
                {
                    userInformationSaved();
                }

                else
                {
                    updateOnlyUserInformation();
                }
            }
        });


        // This code will execute the If condition above and call the method userInformationSaved
        changeProfileTextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checker = "clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(SettingsActivity.this);
            }
        });
    }

    private void updateOnlyUserInformation()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap <String, Object> userMap = new HashMap<>();
        userMap.put("Username", fullNameEditText.getText().toString());
        userMap.put("PhoneNumber", userPhoneEditText.getText().toString());
        userMap.put("Address", addressEditText.getText().toString());

        reference.child(Prevalent.loginUser.getEmail()).updateChildren(userMap);

        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
        Toast.makeText(SettingsActivity.this, "Account Information Has Been Updated Successfully", Toast.LENGTH_SHORT).show();

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            profileImageView.setImageURI(imageUri);
        }

        else
        {
            Toast.makeText(this, "Please Select an Image", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
            finish();
        }
    }

    private void userInformationSaved()
    {
        if (TextUtils.isEmpty(fullNameEditText.getText().toString()))
        {
            Toast.makeText(this, "Username is Compulsory", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(userPhoneEditText.getText().toString()))
        {
            Toast.makeText(this, "Phone Number is Compulsory", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this, "Address is Compulsory", Toast.LENGTH_SHORT).show();
        }

        else if (checker.equals("clicked"))
        {
            uploadImage();
        }
    }

    private void uploadImage()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait while we Update your Account Information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(imageUri != null)
        {
            final StorageReference fileReference = storageProfilePictureReference.child(Prevalent.loginUser.getEmail() + ".jpg");

            uploadTask = fileReference.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation()
            {
                @Override
                public Object then(@NonNull Task task) throws Exception
                {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>()
            {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if(task.isSuccessful())
                    {
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap <String, Object> userMap = new HashMap<>();
                        userMap.put("Username", fullNameEditText.getText().toString());
                        userMap.put("PhoneNumber", userPhoneEditText.getText().toString());
                        userMap.put("Address", addressEditText.getText().toString());
                        userMap.put("Image", myUrl);

                        reference.child(Prevalent.loginUser.getEmail()).updateChildren(userMap);

                        progressDialog.dismiss();

                        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                        Toast.makeText(SettingsActivity.this, "Account Information Has Been Updated Successfully", Toast.LENGTH_SHORT).show();

                        finish();
                    }

                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this, "An Error Has Occurred", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        else
        {
            Toast.makeText(this, "The Image Has Not Been Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void DisplayUserInformation(final CircleImageView profileImageView, final EditText fullNameEditText, final EditText userPhoneEditText, final EditText addressEditText)
    {
        // Check the Reference for the Specific User who is logged in
        // Searching through Email as a Parent Node ID
        DatabaseReference UserReference = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.loginUser.getEmail());

        UserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                // Checking to see if the User already exists
                if (dataSnapshot.exists())
                {
                    // If the Image exists, the data will be displayed on the Settings Activity for the User
                    if (dataSnapshot.child("Image").exists())
                    {
                        String Image = dataSnapshot.child("Image").getValue().toString();
                        String PhoneNumber = dataSnapshot.child("PhoneNumber").getValue().toString();
                        String Username = dataSnapshot.child("Username").getValue().toString();
                        String Address = dataSnapshot.child("Address").getValue().toString();

                        Picasso.get().load(Image).into(profileImageView);
                        fullNameEditText.setText(Username);
                        userPhoneEditText.setText(PhoneNumber);
                        addressEditText.setText(Address);


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

}
