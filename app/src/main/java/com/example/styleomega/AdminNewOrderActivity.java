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

import com.example.styleomega.Model.AdminsOrders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrderActivity extends AppCompatActivity
{
    private RecyclerView ordersList;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_order);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Orders");

        ordersList = findViewById(R.id.orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<AdminsOrders> options = new FirebaseRecyclerOptions.Builder<AdminsOrders>()
                .setQuery(databaseReference, AdminsOrders.class).build();

        FirebaseRecyclerAdapter<AdminsOrders, AdminOrderViewHolder> adapter =
               new FirebaseRecyclerAdapter<AdminsOrders, AdminOrderViewHolder>(options)
               {
                   @Override
                   protected void onBindViewHolder(@NonNull AdminOrderViewHolder adminOrderViewHolder, final int i, @NonNull final AdminsOrders adminsOrders)
                   {
                       adminOrderViewHolder.userName.setText("Username : " + adminsOrders.getUsername());
                       adminOrderViewHolder.userEmail.setText("Email : " + adminsOrders.getEmail());
                       adminOrderViewHolder.userPhoneNumber.setText("Phone Number : " + adminsOrders.getPhoneNumber());
                       adminOrderViewHolder.userShippingAddress.setText("Shipping Address : " + adminsOrders.getAddress() + " " + adminsOrders.getCity());
                       adminOrderViewHolder.userDateTime.setText("Order at : " + adminsOrders.getDate());
                       adminOrderViewHolder.userTotalPrice.setText("Total Amount = $ " + adminsOrders.getTotalAmount());

                       adminOrderViewHolder.showOrderButton.setOnClickListener(new View.OnClickListener()
                       {
                           @Override
                           public void onClick(View v)
                           {

                               String UserID = getRef(i).getKey();

                               Intent intent = new Intent(AdminNewOrderActivity.this, DisplayAdminUserProductsActivity.class);
                               intent.putExtra("usersID", UserID);
                               startActivity(intent);
                           }
                       });

                       adminOrderViewHolder.itemView.setOnClickListener(new View.OnClickListener()
                       {
                           @Override
                           public void onClick(View v)
                           {
                               CharSequence options [] = new CharSequence[]
                                       {
                                               "Yes",
                                               "No"
                                       };

                               AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrderActivity.this);
                               builder.setTitle("Have You Shipped This Product?");

                               builder.setItems(options, new DialogInterface.OnClickListener()
                               {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which)
                                   {
                                       if (which == 0)
                                       {
                                           String UserID = getRef(i).getKey();

                                           RemoveOrder(UserID);
                                       }

                                       else
                                       {
                                           finish();
                                       }
                                   }
                               });

                               builder.show();

                           }
                       });
                   }

                   @NonNull
                   @Override
                   public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                   {
                       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                       return new AdminOrderViewHolder(view);
                   }
               };

        ordersList.setAdapter(adapter);
        adapter.startListening();
    }



    public static class AdminOrderViewHolder extends RecyclerView.ViewHolder
    {

        public TextView userName, userEmail, userPhoneNumber, userTotalPrice, userDateTime, userShippingAddress;

        public Button showOrderButton;

        public AdminOrderViewHolder(@NonNull View itemView)
        {
            super(itemView);

            userName = itemView.findViewById(R.id.orders_username);
            userEmail = itemView.findViewById(R.id.order_email);
            userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
            userTotalPrice = itemView.findViewById(R.id.order_total_price);
            userDateTime = itemView.findViewById(R.id.order_date_time);
            userShippingAddress = itemView.findViewById(R.id.order_address_city);
            showOrderButton = itemView.findViewById(R.id.show_all_products_button);
        }
    }

    private void RemoveOrder(String userID)
    {
        databaseReference.child(userID).removeValue();
    }

}
