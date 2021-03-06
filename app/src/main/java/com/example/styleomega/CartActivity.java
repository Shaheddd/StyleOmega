package com.example.styleomega;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.styleomega.Admin.AddProductsToCart;
import com.example.styleomega.Model.Cart;
import com.example.styleomega.Prevalent.Prevalent;
import com.example.styleomega.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity
{

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private Button NextProcessButton;
    private TextView textTotalAmount, firstTextMessage;

    private int finalTotalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = (RecyclerView) findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        NextProcessButton = (Button) findViewById(R.id.next_process_button);
        textTotalAmount = (TextView) findViewById(R.id.total_price);
        firstTextMessage = (TextView) findViewById(R.id.first_message);

        NextProcessButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                textTotalAmount.setText("Total Price = $" +String.valueOf(finalTotalPrice));
                Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price", String.valueOf(finalTotalPrice));
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    protected void onStart()
    {
        super.onStart();

        CheckStateOfOrder();

        final DatabaseReference cartListReference = FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListReference.child("User View").child(Prevalent.loginUser.getEmail()).child("Products")
                ,Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull final Cart cart)
            {
                cartViewHolder.textProductQuantity.setText("Quantity = " +cart.getQuantity());
                cartViewHolder.textProductPrice.setText("Price = $ " +cart.getPrice() );
                cartViewHolder.textProductName.setText(cart.getProductName());

                int oneTypeProductTotalPrice = ((Integer.valueOf(cart.getPrice()))) * Integer.valueOf(cart.getQuantity());
                finalTotalPrice = finalTotalPrice + oneTypeProductTotalPrice;


                cartViewHolder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        CharSequence options [] = new CharSequence[]
                                {
                                        "Edit",
                                        "Delete"
                                };

                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options");

                        builder.setItems(options, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if (which == 0)
                                {
                                    Intent intent = new Intent(CartActivity.this, AddProductsToCart.class);
                                    intent.putExtra("ProductID", cart.getProductID());
                                    startActivity(intent);
                                }

                                if (which == 1)
                                {
                                    cartListReference.child("User View").child(Prevalent.loginUser.getEmail())
                                            .child("Products").child(cart.getProductID()).removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>()
                                            {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {
                                                    if (task.isSuccessful())
                                                    {
                                                        Toast.makeText(CartActivity.this, "Your Item is Deleted Successfully", Toast.LENGTH_SHORT).show();

                                                        Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                }
                            }
                        });

                        builder.show();

                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
               View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
               CartViewHolder cartViewHolder = new CartViewHolder(view);

               return cartViewHolder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

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
                    String userName = dataSnapshot.child("Username").getValue().toString();

                    if (shippingState.equals("Shipped"))
                    {
                        textTotalAmount.setText("Greetings " + userName + "\n Your Order Has Been Dispatched");
                        recyclerView.setVisibility(View.GONE);

                        firstTextMessage.setVisibility(View.VISIBLE);
                        firstTextMessage.setText("Your Order Has Now Been Dispatched");
                        NextProcessButton.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "You Will Be Allowed To Carry Out More Orders, Once Your Order Has Been Received", Toast.LENGTH_SHORT).show();

                    }

                    else if (shippingState.equals("Not Shipped"))
                    {
                        textTotalAmount.setText("Shipping State = Not Shipped");
                        recyclerView.setVisibility(View.GONE);

                        firstTextMessage.setVisibility(View.VISIBLE);
                        NextProcessButton.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "You Will Be Allowed To Carry Out More Orders, Once Your Order Has Been Received", Toast.LENGTH_SHORT).show();
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
