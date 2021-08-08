package com.shoppingseller.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shoppingseller.R;
import com.shoppingseller.adapter.CategoryAdapter;
import com.shoppingseller.model.CategoryModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment
{

    View root;
    NavController navController;
    RecyclerView recyclerProductView;
    ArrayList<CategoryModel> categoryModels;
    CategoryAdapter adapter;

    FirebaseAuth firebaseAuth;
    DatabaseReference retriveReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        recyclerProductView = root.findViewById(R.id.r_v_product);
        recyclerProductView.setLayoutManager(new LinearLayoutManager(root.getContext(), RecyclerView.VERTICAL, false));
        recyclerProductView.addItemDecoration(new DividerItemDecoration(root.getContext(), DividerItemDecoration.VERTICAL));
        recyclerProductView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        categoryModels = new ArrayList<>();
        adapter = new CategoryAdapter(categoryModels);
        recyclerProductView.setAdapter(adapter);

        firebaseAuth = FirebaseAuth.getInstance();
        retriveReference = FirebaseDatabase.getInstance().getReference().child("Category").child(firebaseAuth.getCurrentUser().getUid());
        retriveReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {

                categoryModels.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    CategoryModel model = dataSnapshot.getValue(CategoryModel.class);
                    categoryModels.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}