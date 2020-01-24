package com.example.styleomega;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.tv.TvContract;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity
{

    private ImageView sweaters, dresses, tShirts, sportsTShirts;
    private ImageView glasses, backpacks, caps, shoes;
    private ImageView headsets, laptops, watches, mobilePhones;

    private Button LogoutButton, CheckOrdersButton;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        LogoutButton = (Button) findViewById(R.id.admin_logout_button);
        CheckOrdersButton = (Button) findViewById(R.id.check_orders_button);

        LogoutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });

        CheckOrdersButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminNewOrderActivity.class);
                startActivity(intent);
            }
        });

        sweaters = (ImageView) findViewById(R.id.sweater);
        dresses = (ImageView) findViewById(R.id.dresses);
        tShirts = (ImageView) findViewById(R.id.t_shirt);
        sportsTShirts = (ImageView) findViewById(R.id.shirt);

        glasses = (ImageView) findViewById(R.id.glasses);
        backpacks = (ImageView) findViewById(R.id.backpack);
        caps = (ImageView) findViewById(R.id.caps);
        shoes = (ImageView) findViewById(R.id.shoes);

        headsets = (ImageView) findViewById(R.id.headset);
        laptops = (ImageView) findViewById(R.id.laptop);
        watches = (ImageView) findViewById(R.id.watches);
        mobilePhones = (ImageView) findViewById(R.id.phones);

        sweaters.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                intent.putExtra("category", "Sweaters");
                startActivity(intent);
            }
        });

        dresses.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                intent.putExtra("category", "Dresses");
                startActivity(intent);
            }
        });

        tShirts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                intent.putExtra("category", "T Shirts");
                startActivity(intent);
            }
        });

        sportsTShirts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                intent.putExtra("category", "Sports T Shirts");
                startActivity(intent);
            }
        });

        glasses.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                intent.putExtra("category", "Glasses");
                startActivity(intent);
            }
        });

        backpacks.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                intent.putExtra("category", "Backpacks");
                startActivity(intent);
            }
        });

        caps.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                intent.putExtra("category", "Caps");
                startActivity(intent);
            }
        });

        shoes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                intent.putExtra("category", "Shoes");
                startActivity(intent);
            }
        });

        headsets.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                intent.putExtra("category", "Headsets");
                startActivity(intent);
            }
        });

        laptops.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                intent.putExtra("category", "Laptops");
                startActivity(intent);
            }
        });

        watches.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                intent.putExtra("category", "Watches");
                startActivity(intent);
            }
        });

        mobilePhones.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
                intent.putExtra("category", "Mobile Phones");
                startActivity(intent);
            }
        });
    }
}
