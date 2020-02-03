package com.example.styleomega;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.styleomega.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity
{

    private String check = "";
    private TextView resetPassword, questionTitle;
    private EditText email, questionOne, questionTwo;
    private Button verifySecurityQuestionsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        check = getIntent().getStringExtra("check");

        resetPassword = findViewById(R.id.reset_password); //The ID of the Page Title
        questionTitle = findViewById(R.id.questions_title);
        email = findViewById(R.id.find_email);
        questionOne = findViewById(R.id.question_one);
        questionTwo = findViewById(R.id.question_two);
        verifySecurityQuestionsButton = findViewById(R.id.verify_security_questions_button);


    }

    @Override
    protected void onStart()
    {
        super.onStart();

        email.setVisibility(View.GONE);



        if (check.equals("settings"))
        {
            resetPassword.setText("Set Your Security Questions");
            questionTitle.setText("Set Your Answers For The Questions");
            verifySecurityQuestionsButton.setText("Set");

            displayAnswers();

            verifySecurityQuestionsButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                   setAnswers();

                }
            });

        }

        else if (check.equals("login"))
        {
            email.setVisibility(View.VISIBLE);

            verifySecurityQuestionsButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    verifyUser();
                }
            });
        }
    }


    private void setAnswers()
    {
        String answerQuestionOne = questionOne.getText().toString().toLowerCase();
        String answerQuestionTwo = questionTwo.getText().toString().toLowerCase();

        if (questionOne.equals("") && questionTwo.equals(""))
        {
            Toast.makeText(ResetPasswordActivity.this, "Please Answer The Questions", Toast.LENGTH_SHORT).show();
        }

        else
        {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(Prevalent.loginUser.getEmail());

            HashMap<String, Object> userDataMap = new HashMap<>();
            userDataMap.put("AnswerOne", answerQuestionOne);
            userDataMap.put("AnswerTwo", answerQuestionTwo);

            reference.child("Security Questions").updateChildren(userDataMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(ResetPasswordActivity.this, "Answers Successfully Updated", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(ResetPasswordActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                        }
                    });

        }
    }

    private void displayAnswers()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(Prevalent.loginUser.getEmail());

        reference.child("Security Questions").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String answerOne = dataSnapshot.child("AnswerOne").getValue().toString();
                    String answerTwo = dataSnapshot.child("AnswerTwo").getValue().toString();

                    questionOne.setText(answerOne);
                    questionTwo.setText(answerTwo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }


    private void verifyUser()
    {
        final String inputEmail = email.getText().toString();

        final String answerQuestionOne = questionOne.getText().toString().toLowerCase();
        final String answerQuestionTwo = questionTwo.getText().toString().toLowerCase();

        if (!inputEmail.equals("") && !answerQuestionOne.equals("") && !answerQuestionTwo.equals(""))
        {

            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(inputEmail);

            reference.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.exists())
                    {
                        String emailAddress = dataSnapshot.child("Email").getValue().toString();

                        if (dataSnapshot.hasChild("Security Questions"))
                        {
                            String answerOne = dataSnapshot.child("Security Questions").child("AnswerOne").getValue().toString();
                            String answerTwo = dataSnapshot.child("Security Questions").child("AnswerTwo").getValue().toString();

                            if (!answerOne.equals(answerQuestionOne))
                            {
                                Toast.makeText(ResetPasswordActivity.this, "The First Answer You Entered Is Incorrect", Toast.LENGTH_SHORT).show();
                            }

                            else if (!answerTwo.equals(answerQuestionTwo))
                            {
                                Toast.makeText(ResetPasswordActivity.this, "The Second Answer You Entered Is Incorrect", Toast.LENGTH_SHORT).show();
                            }

                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("New Password");

                                final EditText newPassword = new EditText(ResetPasswordActivity.this);
                                newPassword.setHint("Type In Your New Password");
                                builder.setView(newPassword);

                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        if (!newPassword.getText().toString().equals(""))
                                        {
                                            reference.child("Password").setValue(newPassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>()
                                                    {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task)
                                                        {
                                                            if(task.isSuccessful())
                                                            {
                                                                Toast.makeText(ResetPasswordActivity.this, "Your Password Has Been Changed", Toast.LENGTH_SHORT).show();

                                                                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });

                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        dialog.cancel();
                                    }
                                });

                                builder.show();
                            }
                        }

                        else
                        {
                            Toast.makeText(ResetPasswordActivity.this, "The Security Questions Do Not Exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    else
                    {
                        Toast.makeText(ResetPasswordActivity.this, "The Email Does Not Exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });
        }

        else
        {
            Toast.makeText(this, "Please Fill In The Necessary Fields", Toast.LENGTH_SHORT).show();
        }

    }
}
