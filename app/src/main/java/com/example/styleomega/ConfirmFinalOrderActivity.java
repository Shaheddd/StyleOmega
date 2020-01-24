package com.example.styleomega;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.styleomega.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity
{
    private EditText nameEditText, emailEditText, phoneEditText, addressEditText, cityEditText;
    private Button confirmOrderButton;

    private String totalAmount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalAmount = getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "Total Price = $ " +totalAmount, Toast.LENGTH_SHORT).show();

        confirmOrderButton = (Button) findViewById(R.id.confirm_final_order_button);

        nameEditText = (EditText) findViewById(R.id.shipment_name);
        emailEditText = (EditText) findViewById(R.id.shipment_email);
        phoneEditText = (EditText) findViewById(R.id.shipment_phone_number);
        addressEditText = (EditText) findViewById(R.id.shipment_address);
        cityEditText = (EditText) findViewById(R.id.shipment_city);

        confirmOrderButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CheckValues();
            }
        });
    }

    private void CheckValues()
    {
        if (TextUtils.isEmpty(nameEditText.getText().toString()))
        {
            Toast.makeText(this, "Please Enter Your Full Name", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(emailEditText.getText().toString()))
        {
            Toast.makeText(this, "Please Enter Your Email Address", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(phoneEditText.getText().toString()))
        {
            Toast.makeText(this, "Please Enter Your Phone Number", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this, "Please Enter Your Address", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(cityEditText.getText().toString()))
        {
            Toast.makeText(this, "Please Enter The City You Reside In", Toast.LENGTH_SHORT).show();
        }

        else
        {
            ConfirmOrder();
        }
    }

    private void ConfirmOrder()
    {
        final String saveCurrentDate, saveCurrentTime;

        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format((callForDate.getTime()));

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format((callForDate.getTime()));

        final DatabaseReference orderReference = FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(Prevalent.loginUser.getEmail());

        HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("TotalAmount", totalAmount);
        orderMap.put("Username", nameEditText.getText().toString());
        orderMap.put("Email", emailEditText.getText().toString());
        orderMap.put("PhoneNumber", phoneEditText.getText().toString());
        orderMap.put("Address", addressEditText.getText().toString());
        orderMap.put("City", cityEditText.getText().toString());
        orderMap.put("Date", saveCurrentDate);
        orderMap.put("Time", saveCurrentTime);
        orderMap.put("State", "Not Shipped");

        orderReference.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
               if (task.isSuccessful())
               {
                   FirebaseDatabase.getInstance().getReference().child("Cart List")
                           .child("User View").child(Prevalent.loginUser.getEmail())
                           .removeValue().addOnCompleteListener(new OnCompleteListener<Void>()
                   {
                       @Override
                       public void onComplete(@NonNull Task<Void> task)
                       {
                           if(task.isSuccessful())
                           {
                               Toast.makeText(ConfirmFinalOrderActivity.this, "Your Order Has Been Completed", Toast.LENGTH_SHORT).show();

                               Intent intent = new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                               startActivity(intent);
                               finish();
                           }

                       }
                   });
               }
            }
        });

    }
}
