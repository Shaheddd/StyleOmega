package com.example.styleomega;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.styleomega.Model.Inquiry;
import com.example.styleomega.ViewHolder.InquiryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewInquiryActivity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inquiry);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Inquiries");

        recyclerView = findViewById(R.id.inquiry_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions <Inquiry> options = new FirebaseRecyclerOptions.Builder<Inquiry>()
                .setQuery(databaseReference, Inquiry.class).build();

        FirebaseRecyclerAdapter <Inquiry, InquiryViewHolder> adapter = new FirebaseRecyclerAdapter<Inquiry, InquiryViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull InquiryViewHolder inquiryViewHolder, int i, @NonNull Inquiry inquiry)
            {
                inquiryViewHolder.textSubject.setText("Subject : " +inquiry.getProductSubject());
                inquiryViewHolder.textMessage.setText("Message : " +inquiry.getProductSubject());
            }

            @NonNull
            @Override
            public InquiryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inquiry_layout, parent,false);
                InquiryViewHolder inquiryViewHolder = new InquiryViewHolder(view);
                return  inquiryViewHolder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
