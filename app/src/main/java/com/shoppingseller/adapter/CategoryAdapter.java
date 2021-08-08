package com.shoppingseller.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shoppingseller.model.CategoryModel;
import com.shoppingseller.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>
{

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference removeRefrance = FirebaseDatabase.getInstance().getReference();
    ArrayList<CategoryModel> categoryModels;


    public CategoryAdapter(ArrayList<CategoryModel> categoryModels)
    {
        this.categoryModels = categoryModels;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position)
    {
        CategoryModel model = categoryModels.get(position);

        Glide
                .with(holder.itemView.getContext())
                .load(model.getCategory_image())
                .into(holder.imageProduct);
        holder.textProductName.setText(model.getCategory_name());
        holder.textProductDescription.setText(model.getCategory_description());
        holder.textProductPrice.setText("السعر: " +      model.getCategory_price() +  "ج.م.");
        holder.textChooseCategory.setText("نوع المُنتج: " +      model.getChoose_category());
        holder.textChooseStatus.setText("حالة المُنتج: " +      model.getChoose_status());
        holder.textNumberQuantity.setText("الكمية المُتاحة للمنتج: " +      model.getNumber_quantity());
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), v);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        switch (item.getItemId())
                        {
                            case R.id.edit_popup_menu:
                                Bundle editProduct = new Bundle();
                                editProduct.putString("product_image", model.getCategory_image());
                                editProduct.putString("product_name", model.getCategory_name());
                                editProduct.putString("product_description", model.getCategory_description());
                                editProduct.putString("product_price", model.getCategory_price());
                                editProduct.putString("choose_category", model.getChoose_category());
                                editProduct.putString("choose_status", model.getChoose_status());
                                editProduct.putString("number_quantity", model.getNumber_quantity());
                                editProduct.putString("category_id", model.getCategory_id());
                                editProduct.putString("product_id", model.getProduct_id());
                                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_editProductFragment, editProduct);
                                break;
                            case R.id.delete_popup_menu:
                                removeRefrance
                                        .child("Product")
                                        .child(firebaseAuth.getCurrentUser().getUid())
                                        .child(model.getProduct_id())
                                        .removeValue();
                                removeRefrance
                                        .child("Category")
                                        .child(firebaseAuth.getCurrentUser().getUid())
                                        .child(model.getCategory_id())
                                        .removeValue();
                                removeRefrance
                                        .child("Categories")
                                        .child(model.getChoose_category())
                                        .child(model.getCategory_id())
                                        .removeValue();
                                Toast.makeText(holder.itemView.getContext(), "Deleted is successfull", Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_mainFragment);
                                break;
                            default:
                                Toast.makeText(holder.itemView.getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return categoryModels.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder
    {

        CircleImageView imageProduct;
        TextView textProductName, textProductDescription, textProductPrice, textChooseCategory, textChooseStatus, textNumberQuantity;

        public CategoryViewHolder(@NonNull View itemView)
        {
            super(itemView);

            imageProduct = itemView.findViewById(R.id.item_img_product);
            textProductName = itemView.findViewById(R.id.item_txt_product_name);
            textProductDescription = itemView.findViewById(R.id.item_txt_product_description);
            textProductPrice = itemView.findViewById(R.id.item_txt_product_price);
            textChooseCategory = itemView.findViewById(R.id.item_txt_choose_category);
            textChooseStatus = itemView.findViewById(R.id.item_txt_choose_status);
            textNumberQuantity = itemView.findViewById(R.id.item_txt_number_quantity);
        }
    }
}
