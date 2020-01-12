package com.example.styleomega;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class AdminProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product);

        Toast.makeText(this, "Welcome, Admin", Toast.LENGTH_SHORT).show();
    }
}
