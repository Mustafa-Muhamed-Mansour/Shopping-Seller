package com.shoppingseller.auth;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shoppingseller.R;
import com.shoppingseller.model.UserModel;


public class SignUpFragment extends Fragment
{

    View root;
    NavController navController;
    EditText editName, editEmail, editPhone, editPassword, editCountry, editCity;
    ImageButton imageButtonSignUp;
    ProgressBar progressLoadingDialog;

    FirebaseAuth firebaseAuth;
    DatabaseReference userReference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_sign_up, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        editName = root.findViewById(R.id.edit_name_sign_up);
        editEmail = root.findViewById(R.id.edit_email_sign_up);
        editPhone = root.findViewById(R.id.edit_phone_sign_up);
        editPassword = root.findViewById(R.id.edit_password_sign_up);
        editCountry = root.findViewById(R.id.edit_country_sign_up);
        editCity = root.findViewById(R.id.edit_city_sign_up);
        imageButtonSignUp = root.findViewById(R.id.img_btn_sign_up);
        progressLoadingDialog = root.findViewById(R.id.progress_bar_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        userReference = FirebaseDatabase.getInstance().getReference();

        imageButtonSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String name = editName.getText().toString();
                String email = editEmail.getText().toString();
                String phone = editPhone.getText().toString();
                String password = editPassword.getText().toString();
                String country = editCountry.getText().toString();
                String city = editCity.getText().toString();

                if (TextUtils.isEmpty(name))
                {
                    editName.setError(getString(R.string.please_enter_your_name));
                    editName.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(email))
                {
                    editEmail.setError(getString(R.string.please_enter_your_email));
                    editEmail.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(phone))
                {
                    editPhone.setError(getString(R.string.please_enter_your_phone));
                    editPhone.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(password))
                {
                    editPassword.setError(getString(R.string.please_enter_your_password));
                    editPassword.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(country))
                {
                    editCountry.setError(getString(R.string.please_enter_your_country));
                    editCountry.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(city))
                {
                    editCity.setError(getString(R.string.please_enter_your_city));
                    editCity.requestFocus();
                    return;
                }
                else
                {
                    progressLoadingDialog.setVisibility(View.VISIBLE);
                    imageButtonSignUp.setVisibility(View.INVISIBLE);
                    firebaseAuth
                            .createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        String user_id = task.getResult().getUser().getUid();

                                        UserModel userModel = new UserModel(user_id, email, name, phone, country, city);
                                        userReference
                                                .child("Account an sellers")
                                                .child(firebaseAuth.getUid())
                                                .setValue(userModel);
                                        progressLoadingDialog.setVisibility(View.INVISIBLE);
                                        imageButtonSignUp.setVisibility(View.VISIBLE);
                                        navController.navigate(R.id.action_signUpFragment_to_loginFragment);
                                    }
                                    else
                                    {
                                        progressLoadingDialog.setVisibility(View.INVISIBLE);
                                        imageButtonSignUp.setVisibility(View.VISIBLE);
                                        Toast.makeText(v.getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}