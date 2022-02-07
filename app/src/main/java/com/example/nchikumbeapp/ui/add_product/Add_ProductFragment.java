package com.example.nchikumbeapp.ui.add_product;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.nchikumbeapp.R;
import com.example.nchikumbeapp.UploadActivity;
import com.example.nchikumbeapp.databinding.FragmentAddProductBinding;
import com.example.nchikumbeapp.databinding.FragmentAddProductBinding;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Add_ProductFragment extends Fragment {

    private FragmentAddProductBinding binding;
    private EditText ProductName;
    private EditText ProductDescription;
    private EditText ProductPrice;
    private Button uploadBtn;
    ProgressBar progressBar;
    private ImageView imageView;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("image");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private Uri imageUri;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddProductBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ActivityResultLauncher <String> start = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if ( result != null){
                    imageUri = result;
                    uploadToFirebase(imageUri);
                }else
                    Toast.makeText(getContext(), "NO  picture Set", Toast.LENGTH_SHORT).show();
            }
        });






        ProductName = root.findViewById(R.id.ProductName);
        ProductDescription = root.findViewById(R.id.ProductDescription);
        ProductPrice = root.findViewById(R.id.ProductPrice);
        uploadBtn = root.findViewById(R.id.button);
        progressBar = root.findViewById(R.id.progressBar);
        imageView = root.findViewById(R.id.imageView);



        progressBar.setVisibility(View.INVISIBLE);



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                start.launch("image/*");
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((ProductDescription.getText().toString()).isEmpty())  {

                    ProductDescription.setError("Please Enter ProductDescription !!!");
                    return;

                }
                if ((ProductName.getText().toString()).isEmpty())    {

                    ProductName.setError("Please Enter ProductName !!!");

                    return;

                }
                if ((ProductPrice.getText().toString().isEmpty())){


                    ProductPrice.setError("Please Enter Product Price !!! ");
                    return;
                }

                if (imageUri != null){

                    uploadToFirebase(imageUri);

                }else{
                    Toast.makeText(getContext(), "Please Select Image", Toast.LENGTH_SHORT).show();
                }
            }
        });





        return root;
    }
    private void uploadToFirebase(Uri uri){

        final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Model model = new Model(uri.toString());
                        model.setProductName(ProductName.getText().toString());
                        model.setProductDescription(ProductDescription.getText().toString());
                        model.setProductPrice(ProductPrice.getText().toString());
                        String modelId = root.push().getKey();
                        root.child(modelId).setValue(model);
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                       // imageView.setImageResource(R.drawable.ic_baseline_account_circle_24);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "Uploading Failed !!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri mUri){

        ContentResolver cr = getActivity().getContentResolver();
        //getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }





    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

class Model{
    private String imageUrl;
    private String ProductName;
    private String ProductDescription;
    private String ProductPrice;
    public Model(){

    }



    public void setProductName(String productName) {
        ProductName = productName;
    }

    public void setProductDescription(String productDescription) {
        ProductDescription = productDescription;
    }
    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public String getProductName() {
        return ProductName;
    }

    public String getProductDescription() {
        return ProductDescription;
    }



    public String getProductPrice() {
        return ProductPrice;
    }

    public  Model (String v){
        this.imageUrl = v;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

