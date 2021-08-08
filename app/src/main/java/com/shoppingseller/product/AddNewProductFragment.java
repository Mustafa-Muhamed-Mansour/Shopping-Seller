package com.shoppingseller.product;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shoppingseller.R;
import com.shoppingseller.model.CategoryModel;
import com.shoppingseller.model.ProductModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import static android.app.Activity.RESULT_OK;

public class AddNewProductFragment extends Fragment
{

    View root;
    NavController navController;
    ImageView imageProduct;
    EditText editProductName, editProductDescription, editProductPrice;
    TextView textQuantity;
    Button buttonDoneProduct;
    Spinner chooseSpinnerStatus, chooseSpinnerCategory;
    NumberPicker numberPickerProduct;

    String chooseStatus, chooseCategory;

    FirebaseAuth firebaseAuth;
    DatabaseReference productReference;
    StorageReference productImageReference;

    String productImage;
    final static int GALLERY_PICK = 1;
    Uri photoProductPathUri;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_add_new_product, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        imageProduct = root.findViewById(R.id.img_product);
        editProductName = root.findViewById(R.id.edit_name);
        editProductDescription = root.findViewById(R.id.edit_description);
        editProductPrice = root.findViewById(R.id.edit_price);
        textQuantity = root.findViewById(R.id.txt_number_quantity);
        buttonDoneProduct = root.findViewById(R.id.btn_done);
        chooseSpinnerStatus = root.findViewById(R.id.choose_status);
        ArrayAdapter<CharSequence> adapterStatus = ArrayAdapter.createFromResource(getContext(), R.array.choose_status, android.R.layout.simple_spinner_item);
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseSpinnerStatus.setAdapter(adapterStatus);
        chooseSpinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                chooseStatus = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                Toast.makeText(parent.getContext(), "Nothing", Toast.LENGTH_SHORT).show();
            }
        });
        chooseSpinnerCategory = root.findViewById(R.id.choose_category);
        ArrayAdapter<CharSequence> adapterCategory = ArrayAdapter.createFromResource(getContext(), R.array.choose_category, android.R.layout.simple_spinner_item);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseSpinnerCategory.setAdapter(adapterCategory);
        chooseSpinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                chooseCategory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                Toast.makeText(parent.getContext(), "Nothing", Toast.LENGTH_SHORT).show();
            }
        });
        numberPickerProduct = root.findViewById(R.id.number_picker_quantity);
        numberPickerProduct.setMinValue(0);
        numberPickerProduct.setMaxValue(20);
        textQuantity.setText(String.valueOf(numberPickerProduct.getValue()));
        numberPickerProduct.setOnValueChangedListener(new NumberPicker.OnValueChangeListener()
        {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal)
            {
                textQuantity.setText(String.valueOf(newVal));
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        productReference = FirebaseDatabase.getInstance().getReference();
        productImageReference = FirebaseStorage.getInstance().getReference();

        buttonDoneProduct.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String productName = editProductName.getText().toString();
                String productDescription = editProductDescription.getText().toString();
                String productPrice = editProductPrice.getText().toString();
                String numberQuantity = textQuantity.getText().toString();

                if (TextUtils.isEmpty(productName))
                {
                    editProductName.setError(getString(R.string.please_enter_product_name));
                    editProductName.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(productDescription))
                {
                    editProductDescription.setError(getString(R.string.please_enter_product_description));
                    editProductDescription.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(productPrice))
                {
                    editProductPrice.setError(getString(R.string.please_enter_product_price));
                    editProductPrice.requestFocus();
                    return;
                }
                if (photoProductPathUri == null)
                {
                    imageProduct.requestFocus();
                    return;
                }
                else
                {
                    String product_id = FirebaseDatabase.getInstance().getReference().push().getKey();
                    String user_id = firebaseAuth.getCurrentUser().getUid();
                    String category_id = FirebaseDatabase.getInstance().getReference().push().getKey();
                    getDataBase(product_id, user_id, productName, productDescription, productPrice, chooseCategory, category_id, chooseStatus, numberQuantity);
                }
            }
        });

        imageProduct.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkRequestPermission();
            }
        });
    }

    private void getDataBase(String product_id, String user_id, String productName, String productDescription, String productPrice, String chooseCategory, String category_id, String chooseStatus, String numberQuantity)
    {

        StorageReference filePath = productImageReference.child("Images_Product").child(photoProductPathUri.getLastPathSegment());
        UploadTask uploadTask = filePath.putFile(photoProductPathUri);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
        {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
            {
                if(!task.isSuccessful())
                {
                    throw task.getException();
                }
                return filePath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>()
        {
            @Override
            public void onComplete(@NonNull Task<Uri> task)
            {
                if (task.isSuccessful())
                {

                    Uri downloadUri = task.getResult();

                    productImage = downloadUri.toString();

                    ProductModel productModel = new ProductModel(product_id, category_id, chooseCategory);
                    CategoryModel categoryModel = new CategoryModel(product_id, productName, productDescription, productPrice, productImage, chooseCategory, category_id, chooseStatus, numberQuantity);

                    productReference
                            .child("Product")
                            .child(user_id)
                            .child(product_id)
                            .setValue(productModel);

                    productReference
                            .child("Category")
                            .child(firebaseAuth.getCurrentUser().getUid())
                            .child(category_id)
                            .setValue(categoryModel);

                    productReference
                            .child("Categories")
                            .child(chooseCategory)
                            .child(category_id)
                            .setValue(categoryModel);

                    Toast.makeText(getContext(), "Done, Product Save In Database", Toast.LENGTH_SHORT).show();
                    navController.navigate(R.id.action_addNewProductFragment_to_mainFragment);

                }
            }
        });
    }

    private void checkRequestPermission()
    {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_PICK);
        }
        else
        {
            openGallery();
        }
    }

    private void openGallery()
    {
        CropImage
                .activity()
                .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                .start(getActivity(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult activityResult = CropImage.getActivityResult(data);
            photoProductPathUri = activityResult.getUri();
        }

        Glide
                .with(getContext())
                .load(photoProductPathUri)
                .into(imageProduct);
    }
}