package com.shoppingseller.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.firebase.auth.FirebaseAuth;
import com.shoppingseller.R;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class MainFragment extends Fragment
{
    View root;
    NavController navController;
    MeowBottomNavigation meowBottomNavigation;

    final static int ID_HOME_PRODUCT = 1;
    final static int ID_ADD_NEW_PRODUCT = 2;
    final static int ID_LOGOUT = 3;

    FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_main, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        meowBottomNavigation = root.findViewById(R.id.meow_bottom_nav);
        meowBottomNavigation.add(new MeowBottomNavigation.Model(ID_HOME_PRODUCT, R.drawable.ic_home));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(ID_ADD_NEW_PRODUCT, R.drawable.ic_add_new_prodyct));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(ID_LOGOUT, R.drawable.ic_logout));
        meowBottomNavigation.show(0, true);

        firebaseAuth = FirebaseAuth.getInstance();

        meowBottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>()
        {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model)
            {
                switch (model.getId())
                {
                    case ID_HOME_PRODUCT:
                        navController.navigate(R.id.action_mainFragment_to_homeFragment);
                        break;
                    case ID_ADD_NEW_PRODUCT:
                        navController.navigate(R.id.action_mainFragment_to_addNewProductFragment);
                        break;
                    case ID_LOGOUT:
                        firebaseAuth.signOut();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder
                                .setTitle("Are you Sure to Logout?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        navController.navigate(R.id.action_mainFragment_to_loginFragment);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        dialog.cancel();
                                    }
                                });
                        builder.show();
                        break;
                    default:
                        Toast.makeText(root.getContext(), "Comming Soon", Toast.LENGTH_SHORT).show();
                }
                return null;
            }
        });
    }
}