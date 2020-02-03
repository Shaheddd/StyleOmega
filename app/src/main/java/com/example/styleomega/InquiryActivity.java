package com.example.styleomega;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.styleomega.Model.Inquiry;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class InquiryActivity extends AppCompatActivity
{
    private TextView ProductName;
    private EditText inquirySubject, inquiryMessage;
    private Button Submit;
    private String saveCurrentDate, saveCurrentTime, inquiryID, productName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);

        ProductName = (TextView) findViewById(R.id.inquiry_product_name);
        inquirySubject = findViewById(R.id.inquiry_subject);
        inquiryMessage = findViewById(R.id.inquiry_message);
        Submit = findViewById(R.id.inquiry_submit_button);

        productName = getIntent().getStringExtra("Name");

        ProductName.setText(productName);

        Submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SubmitInquiry();
                Toast.makeText(InquiryActivity.this, "Your Inquiry Has Been Processed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(InquiryActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

    }

    private void SubmitInquiry()
    {
        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format((callForDate.getTime()));

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format((callForDate.getTime()));

        inquiryID = saveCurrentDate + saveCurrentTime;

        DatabaseReference updateReference = FirebaseDatabase.getInstance().getReference().child("Inquiries");

        Inquiry inquiry = new Inquiry (productName, inquirySubject.getText().toString().trim(),
                inquiryMessage.getText().toString().trim());

        updateReference.child(inquiryID).setValue(inquiry);
    }
}
