package com.cinestar.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cinestar.R;
import com.cinestar.models.User;
import com.cinestar.providers.AuthProviders;
import com.cinestar.providers.UsersProvider;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentSnapshot;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {

    //CREACIÓN DE OBJETOS DEL REGISTER =======================================================================
    TextInputEditText mTxtInpEmail;
    TextInputEditText mTxtInpPassword;
    Button mBtnLogin;
    SignInButton mbtnSingInGoogle;
    TextView mTxtRegister;
    private GoogleSignInClient mGoogleSignInClient;
    private final int REQUEST_CODE_GOOGLE = 1;
    AuthProviders mAuthProvider;
    UsersProvider mUsersProvider;
    AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SEARCH ID===========================================================================================
        mTxtInpEmail = findViewById(R.id.TxtInpEmail);
        mTxtInpPassword = findViewById(R.id.TxtInpPassword);
        mBtnLogin = findViewById(R.id.BtnLogin);
        mbtnSingInGoogle = findViewById(R.id.btnSingInGoogle);
        mTxtRegister = findViewById(R.id.TxtRegister);

        mAlertDialog =new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento....")
                .setCancelable(false).build();

        //INSTANCIAMOS LOS PROVEHEDORES======================================================================
        //Autenticacion
        mAuthProvider = new AuthProviders();
        //Usuarios en firebase
        mUsersProvider = new UsersProvider();



        //CONFIGURE GOOGLE SIGN IN============================================================================
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        //LOGIN BUTTON========================================================================================
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(); //LOGIN METHOD
            }
        });

        //SING IN GOOGLE BUTTON================================================================================
        mbtnSingInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });

        //TxtRegister INSTANCE================================================================================
        mTxtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    // [START signin]
    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE);
    }

    /*// [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

     */
    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == REQUEST_CODE_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("ERROR", "Google sign in failed", e);
            }
        }
    }

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        mAlertDialog.show();
        mAuthProvider.googleLogin(account)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            /*Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                             */
                            String id = mAuthProvider.getUid();
                            checkUserExist(id);
                        }else {
                            mAlertDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Log.w("ERROR", "Error signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }
    private void checkUserExist(final String id) {

        mUsersProvider.getUser(id)
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    mAlertDialog.dismiss();
                    Intent intent = new Intent(MainActivity.this, GeneroActivity.class);
                    startActivity(intent);
                }else {
                    String email = mAuthProvider.getEmail();
                    User user=new User();
                    user.setEmail(email);
                    user.setId(id);
                    mUsersProvider.create(user)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mAlertDialog.dismiss();
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(MainActivity.this, "Almacenamiento de usuario fallido", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
    //LOGIN METHOD ===============================================================================================================
    private void login() {
        String email = mTxtInpEmail.getText().toString();
        String password = mTxtInpPassword.getText().toString();
        mAlertDialog.show();
        mAuthProvider.login(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mAlertDialog.dismiss();
                if (task.isSuccessful()){
                    Intent intent = new Intent(MainActivity.this, GeneroActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, "El email y la contraseña no son correctas", Toast.LENGTH_LONG).show();
                }
            }
        });
        Log.d("campo", "email: " + email);
        Log.d("campo", "password: " + password);
    }
}



