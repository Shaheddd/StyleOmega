package com.example.styleomega.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.styleomega.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainingProductsActivity extends AppCompatActivity
{

    private Button ApplyChangesButton, DeleteProductButton;
    private EditText productName, productPrice, productDescription;
    private ImageView imageView;

    private String productIdentity = "";
    private DatabaseReference productsReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintaining_products);


        productIdentity = getIntent().getStringExtra("ProductID");

        productsReference = FirebaseDatabase.getInstance().getReference().child("Products").child(productIdentity);

        ApplyChangesButton = findViewById(R.id.apply_changes_button);
        productName = findViewById(R.id.maintain_product_name);
        productPrice = findViewById(R.id.maintain_product_price);
        productDescription = findViewById(R.id.maintain_product_description);
        imageView = findViewById(R.id.maintain_product_image);
        DeleteProductButton = findViewById(R.id.delete_product_button);

        displaySpecificProductInformation();

        ApplyChangesButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                applyChanges();
            }
        });

        DeleteProductButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DeleteProduct();
            }
        });

    }

    private void DeleteProduct()
    {
        productsReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                Intent intent = new Intent(AdminMaintainingProductsActivity.this, AdminCategoryActivity.class);
                startActivity(intent);
                finish();

                Toast.makeText(AdminMaintainingProductsActivity.this, "The Product Has Been Removed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyChanges()
    {
        String name = productName.getText().toString();
        String price = productPrice.getText().toString();
        String description = productDescription.getText().toString();

        if (name.equals(""))
        {
            Toast.makeText(this, "Please Enter The Product Name", Toast.LENGTH_SHORT).show();
        }

        else if (price.equals(""))
        {
            Toast.makeText(this, "Please Enter The Product Price", Toast.LENGTH_SHORT).show();
        }

        else if (description.equals(""))
        {
            Toast.makeText(this, "Please Enter The Product Description", Toast.LENGTH_SHORT).show();
        }

        else
        {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("ProductID", productIdentity);
            productMap.put("Description", description);
            productMap.put("Price", price);
            productMap.put("Name", name);

            productsReference.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>()
            {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(AdminMaintainingProductsActivity.this, "Details Successfully Updated", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AdminMaintainingProductsActivity.this, AdminCategoryActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            });
        }
    }

    private void displaySpecificProductInformation()
    {
        productsReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String name = dataSnapshot.child("Name").getValue().toString();
                    String price = dataSnapshot.child("Price").getValue().toString();
                    String description = dataSnapshot.child("Description").getValue().toString();
                    String image = dataSnapshot.child("Image").getValue().toString();

                    productName.setText(name);
                    productPrice.setText(price);
                    productDescription.setText(description);
                    Picasso.get().load(image).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
