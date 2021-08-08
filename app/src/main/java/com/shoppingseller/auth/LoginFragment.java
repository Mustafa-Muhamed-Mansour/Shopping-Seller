package com.shoppingseller.auth;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.shoppingseller.R;

public class LoginFragment extends Fragment
{

    View root;
    NavController navController;
    EditText editEmail, editPassword;
    TextView textForgetPassword;
    Button buttonLoginFacebook, buttonLoginGoogle;
    ImageButton imageButtonLogin;
    ProgressBar progressLoadingDialog;

    FirebaseAuth firebaseAuth;
    GoogleSignInClient googleSignInClient;
    GoogleSignInOptions googleSignInOptions;

    static final int RC_SIGN_IN = 9001;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_login, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        editEmail = root.findViewById(R.id.edit_email_login);
        editPassword = root.findViewById(R.id.edit_password_login);
        textForgetPassword = root.findViewById(R.id.txt_forget_password);
        buttonLoginFacebook = root.findViewById(R.id.btn_login_facebook);
        buttonLoginGoogle = root.findViewById(R.id.btn_login_google);
        imageButtonLogin = root.findViewById(R.id.img_btn_login);
        progressLoadingDialog = root.findViewById(R.id.progress_bar_login);

        firebaseAuth = FirebaseAuth.getInstance();

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getActivity(), googleSignInOptions);

        buttonLoginFacebook.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });
        buttonLoginGoogle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                signIn();
            }
        });
        imageButtonLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();

                if (TextUtils.isEmpty(email))
                {
                    editEmail.setError(getString(R.string.please_enter_your_email));
                    editEmail.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(password))
                {
                    editPassword.setError(getString(R.string.please_enter_your_password));
                    editPassword.requestFocus();
                    return;
                }
                else
                {
                    progressLoadingDialog.setVisibility(View.VISIBLE);
                    imageButtonLogin.setVisibility(View.INVISIBLE);
                    firebaseAuth
                            .signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        progressLoadingDialog.setVisibility(View.INVISIBLE);
                                        imageButtonLogin.setVisibility(View.VISIBLE);
                                        navController.navigate(R.id.action_loginFragment_to_mainFragment);
                                    }
                                    else
                                    {
                                        progressLoadingDialog.setVisibility(View.INVISIBLE);
                                        imageButtonLogin.setVisibility(View.VISIBLE);
                                        Toast.makeText(v.getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void signIn()
    {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try
            {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
                editEmail.setText(account.getEmail());
            }
            catch (ApiException e)
            {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken)
    {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth
                .signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            editEmail.setText(user.getEmail());
                            Toast.makeText(getContext(), "اي الإيميل القمر ده", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}