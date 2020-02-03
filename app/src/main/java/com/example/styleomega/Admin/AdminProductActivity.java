package com.example.styleomega.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.styleomega.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminProductActivity extends AppCompatActivity
{

    private String Categories, Description, Price, NameOfProduct;
    private String saveCurrentDate, saveCurrentTime;
    private Button AddNewProductButton;
    private ImageView InputProductImage;
    private EditText InputProductName, InputProductDescription, InputProductPrice;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String RandomKeyOfProduct;
    private String DownloadImageUrl;
    private StorageReference ImagesOfProductReference;
    private DatabaseReference ProductReference;
    private ProgressDialog loadingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product);

        Categories = getIntent().getExtras().get("category").toString();

        ImagesOfProductReference = FirebaseStorage.getInstance().getReference().child("Product Images");

        ProductReference = FirebaseDatabase.getInstance().getReference().child("Products");

        AddNewProductButton = (Button) findViewById(R.id.add_new_product);
        InputProductImage = (ImageView) findViewById(R.id.select_a_product_image);
        InputProductName = (EditText) findViewById(R.id.product_name);
        InputProductDescription = (EditText) findViewById(R.id.product_description);
        InputProductPrice = (EditText) findViewById(R.id.product_price);
        loadingBar = new ProgressDialog(this);

        InputProductImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                OpenGallery();
            }
        });

        AddNewProductButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ValidateProductData();

            }
        });

    }

    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GalleryPick && resultCode == RESULT_OK && data != null)
        {
            ImageUri = data.getData();
            InputProductImage.setImageURI(ImageUri);

        }
    }

    private void ValidateProductData()
    {
        Description = InputProductDescription.getText().toString();
        Price = InputProductPrice.getText().toString();
        NameOfProduct = InputProductName.getText().toString();

        if (ImageUri == null)
        {
            Toast.makeText(this, "Image of the Product is Compulsory", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Please Enter a Valid Product Description", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(Price))
        {
            Toast.makeText(this, "Please Enter a Valid Product Price", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(NameOfProduct))
        {
            Toast.makeText(this, "Please Enter a Valid Product Name", Toast.LENGTH_SHORT).show();
        }

        else
        {
            StoreInformationOfProduct();
        }
    }

    private void StoreInformationOfProduct()
    {

        loadingBar.setTitle("Adding a New Product");
        loadingBar.setMessage("Adding a New Product, Please Wait");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        RandomKeyOfProduct = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = ImagesOfProductReference.child(ImageUri.getLastPathSegment()+ RandomKeyOfProduct + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(AdminProductActivity.this, "Error : " +message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(AdminProductActivity.this, "Image has been Successfully Uploaded", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
                {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        DownloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {

                            DownloadImageUrl = task.getResult().toString();

                            Toast.makeText(AdminProductActivity.this, "Image Has Been Received", Toast.LENGTH_SHORT).show();

                            SaveProductInformationToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInformationToDatabase()
    {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("ProductID", RandomKeyOfProduct);
        productMap.put("Date", saveCurrentDate);
        productMap.put("Time", saveCurrentDate);
        productMap.put("Description", Description);
        productMap.put("Image", DownloadImageUrl);
        productMap.put("Category", Categories);
        productMap.put("Price", Price);
        productMap.put("Name", NameOfProduct);

        ProductReference.child(RandomKeyOfProduct).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(AdminProductActivity.this, AdminCategoryActivity.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(AdminProductActivity.this, "Product Has Been Added", Toast.LENGTH_SHORT).show();
                        }
                        
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminProductActivity.this, "Error : " +message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
