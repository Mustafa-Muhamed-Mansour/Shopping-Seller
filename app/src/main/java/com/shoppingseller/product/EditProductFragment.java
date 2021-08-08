package com.shoppingseller.product;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shoppingseller.R;
import com.shoppingseller.model.CategoryModel;
import com.shoppingseller.model.ProductModel;

public class EditProductFragment extends Fragment
{

    View root;
    NavController navController;
    ImageView editProductImage;
    EditText editTextProductName, editTextProductDescription, editTextProductPrice;
    TextView textViewEditNumberQuantity;
    Spinner editChooseCategory, editChooseStatue;
    NumberPicker editNumberPicker;
    Button buttonEdit;

    String chooseCategory, chooseStatus;

    FirebaseAuth firebaseAuth;
    DatabaseReference updateReference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_edit_product, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        firebaseAuth = FirebaseAuth.getInstance();
        updateReference = FirebaseDatabase.getInstance().getReference();

        editProductImage = root.findViewById(R.id.img_edit_product);
        editTextProductName = root.findViewById(R.id.edit_product_name);
        editTextProductDescription = root.findViewById(R.id.edit_product_description);
        editTextProductPrice = root.findViewById(R.id.edit_product_price);
        textViewEditNumberQuantity = root.findViewById(R.id.edit_txt_number_quantity);
        editNumberPicker = root.findViewById(R.id.edit_number_picker_quantity);
        editNumberPicker.setMinValue(0);
        editNumberPicker.setMaxValue(20);
        textViewEditNumberQuantity.setText(String.valueOf(editNumberPicker));
        String productImage = getArguments().getString("product_image");
        String productName = getArguments().getString("product_name");
        String productDescription = getArguments().getString("product_description");
        String productPrice = getArguments().getString("product_price");
        String productNumberQuantity = getArguments().getString("number_quantity");
        Glide
                .with(getContext())
                .load(productImage)
                .into(editProductImage);
        editTextProductName.setText(productName);
        editTextProductDescription.setText(productDescription);
        editTextProductPrice.setText(productPrice);
        textViewEditNumberQuantity.setText(productNumberQuantity);
        editNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener()
        {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal)
            {
                textViewEditNumberQuantity.setText(String.valueOf(newVal));
            }
        });
        editChooseCategory = root.findViewById(R.id.choose_edit_category);
        String category = getArguments().getString("choose_category");
        ArrayAdapter<CharSequence> adapterCategory = ArrayAdapter.createFromResource(getContext(), R.array.choose_category, android.R.layout.simple_spinner_item);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editChooseCategory.setAdapter(adapterCategory);
        editChooseCategory.setSelection(getChooseCategory(editChooseCategory, category));
        editChooseCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                chooseCategory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                Toast.makeText(getActivity(), "Nothing", Toast.LENGTH_SHORT).show();
            }
        });
        editChooseStatue = root.findViewById(R.id.choose_edit_status);
        String status = getArguments().getString("choose_status");
        ArrayAdapter<CharSequence> adapterStatus = ArrayAdapter.createFromResource(getContext(), R.array.choose_status, android.R.layout.simple_spinner_item);
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editChooseStatue.setAdapter(adapterStatus);
        editChooseStatue.setSelection(getChooseStatus(editChooseStatue, status));
        editChooseStatue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                chooseStatus = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                Toast.makeText(getActivity(), "Nothing", Toast.LENGTH_SHORT).show();
            }
        });
        buttonEdit = root.findViewById(R.id.btn_update);
        buttonEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                String nameProduct = editTextProductName.getText().toString();
                String descriptionProduct = editTextProductDescription.getText().toString();
                String priceProduct = editTextProductPrice.getText().toString();
                String numberQuantityProduct = textViewEditNumberQuantity.getText().toString();

                String product_id = getArguments().getString("product_id");
                String categ_id = getArguments().getString("category_id");

                if (TextUtils.isEmpty(nameProduct))
                {
                    editTextProductName.setError(getString(R.string.please_enter_product_name));
                    editTextProductName.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(descriptionProduct))
                {
                    editTextProductDescription.setError(getString(R.string.please_enter_product_description));
                    editTextProductDescription.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(priceProduct))
                {
                    editTextProductPrice.setError(getString(R.string.please_enter_product_price));
                    editTextProductPrice.requestFocus();
                    return;
                }

                else
                {
                    ProductModel productModel = new ProductModel(product_id, categ_id, chooseCategory);
                    CategoryModel categoryModel = new CategoryModel(product_id, nameProduct, descriptionProduct, priceProduct, productImage, chooseCategory, categ_id, chooseStatus, numberQuantityProduct);

                    updateReference
                            .child("Product")
                            .child(firebaseAuth.getCurrentUser().getUid())
                            .child(product_id)
                            .setValue(productModel);

                    updateReference
                            .child("Category")
                            .child(firebaseAuth.getCurrentUser().getUid())
                            .child(categ_id)
                            .setValue(categoryModel);

                    updateReference
                            .child("Categories")
                            .child(chooseCategory)
                            .child(categ_id)
                            .setValue(categoryModel);

                    getActivity().onBackPressed();
                }
            }
        });
    }

    private int getChooseCategory(Spinner editChooseCategory, String category)
    {
        int index = 0;

        for (int i=0;i<editChooseCategory.getCount();i++)
        {
            if (editChooseCategory.getItemAtPosition(i).equals(category))
            {
                index = i;
            }
        }
        return index;
    }

    private int getChooseStatus(Spinner editChooseStatue, String status)
    {
        int index = 0;

        for (int i=0;i<editChooseStatue.getCount();i++)
        {
            if (editChooseStatue.getItemAtPosition(i).equals(status))
            {
                index = i;
            }
        }
        return index;
    }

}