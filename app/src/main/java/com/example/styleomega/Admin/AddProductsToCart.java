package com.example.styleomega.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.styleomega.HomeActivity;
import com.example.styleomega.Model.Products;
import com.example.styleomega.Prevalent.Prevalent;
import com.example.styleomega.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddProductsToCart extends AppCompatActivity
{

    private Button addToCartButton, ShareButton;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productName, productDescription, productPrice;
    private String productIdentity = "", State = "Normal";



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products_to_cart);

        productIdentity = getIntent().getStringExtra("ProductID");

        addToCartButton = (Button) findViewById(R.id.add_products_to_cart_button);
        ShareButton = (Button) findViewById(R.id.share_product_button);
        numberButton = (ElegantNumberButton) findViewById(R.id.number_button);
        productImage = (ImageView) findViewById(R.id.product_image_details);
        productName = (TextView) findViewById(R.id.product_name_details);
        productDescription = (TextView) findViewById(R.id.product_description_details);
        productPrice = (TextView) findViewById(R.id.product_price_details);

        getProductDetails(productIdentity);

        addToCartButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(State.equals("Order Dispatched") || State.equals("Order Placed"))
                {
                    Toast.makeText(AddProductsToCart.this, "You Can Purchase More Products, Once Your Order Has Been Confirmed", Toast.LENGTH_LONG).show();
                }

                else
                {
                    addingToTheCartList();
                }
            }
        });

        ShareButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent shareProduct = new Intent();
                shareProduct.setAction(Intent.ACTION_SEND);

                String message = productName.getText().toString() + "\n" + productDescription.getText().toString()
                        + "\n" + productPrice.getText().toString();

                shareProduct.putExtra(Intent.EXTRA_TEXT, message);
                shareProduct.setType("text/plain");

                Intent sendProduct = Intent.createChooser(shareProduct, null);
                startActivity(sendProduct);
            }
        });
    }

            @Override
            protected void onStart()
                 {
                    super.onStart();

                    CheckStateOfOrder();
                 }

            private void addingToTheCartList()
            {
                String saveCurrentTime, saveCurrentDate;

                Calendar callForDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                saveCurrentDate = currentDate.format((callForDate.getTime()));

                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                saveCurrentTime = currentTime.format((callForDate.getTime()));

                final DatabaseReference cartListReference = FirebaseDatabase.getInstance().getReference().child("Cart List");

                final HashMap<String, Object> cartMap = new HashMap<>();
                cartMap.put("ProductID", productIdentity);
                cartMap.put("ProductName", productName.getText().toString());
                cartMap.put("Price", productPrice.getText().toString());
                cartMap.put("Date", saveCurrentDate);
                cartMap.put("Time", saveCurrentTime);
                cartMap.put("Quantity", numberButton.getNumber());
                cartMap.put("Discount", "");

                cartListReference.child("User View").child(Prevalent.loginUser.getEmail()).child("Products")
                        .child(productIdentity)
                        .updateChildren(cartMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if (task.isSuccessful())
                                {
                                    cartListReference.child("Admin View").child(Prevalent.loginUser.getEmail()).child("Products")
                                            .child(productIdentity)
                                            .updateChildren(cartMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>()
                                            {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {
                                                    if (task.isSuccessful())
                                                    {
                                                        Toast.makeText(AddProductsToCart.this, "Added To The Cart List.", Toast.LENGTH_SHORT).show();

                                                        Intent intent = new Intent(AddProductsToCart.this, HomeActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                }
                            }
                        });


            }


    private void getProductDetails(String productIdentity)
    {
        DatabaseReference productReference = FirebaseDatabase.getInstance().getReference().child("Products");

        productReference.child(productIdentity).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    Products products = dataSnapshot.getValue(Products.class);

                    productName.setText(products.getName());
                    productDescription.setText(products.getDescription());
                    productPrice.setText(products.getPrice());
                    Picasso.get().load(products.getImage()).into(productImage);

                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void CheckStateOfOrder()
    {
        DatabaseReference orderReference;
        orderReference = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.loginUser.getEmail());
        orderReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String shippingState = dataSnapshot.child("State").getValue().toString();

                    if (shippingState.equals("Shipped"))
                    {
                        State = "Order Dispatched";

                    }

                    else if (shippingState.equals("Not Shipped"))
                    {
                        State = "Order Placed";
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
