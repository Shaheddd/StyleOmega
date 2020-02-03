package com.example.styleomega.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.styleomega.Interface.ItemClickListener;
import com.example.styleomega.R;

public class InquiryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView textSubject;
    public TextView textMessage;
    public ItemClickListener itemClickListener;

    public InquiryViewHolder(@NonNull View view)
    {
        super(view);

        textSubject = view.findViewById(R.id.inquiry_display_subject);
        textMessage = view.findViewById(R.id.inquiry_display_message);

    }

    public void setItemClickListener (ItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick (View view)
    {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
