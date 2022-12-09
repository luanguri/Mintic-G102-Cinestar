package com.cinestar.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cinestar.R;
import com.cinestar.models.User;
import com.cinestar.providers.AuthProviders;
import com.cinestar.providers.UsersProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import dmax.dialog.SpotsDialog;

public class ProfileActivity extends AppCompatActivity {

    //PROFILE OBJECT DECLARATION =============================================================================
    TextInputEditText mtxtInNombre;
    Button mbtnSaveProfile;
    AuthProviders mAuthProvider;
    UsersProvider mUsersProvider;
    AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //OBJET INSTANCES / SEARCH ID ========================================================================
        mtxtInNombre = findViewById(R.id.txtInNombre);
        mbtnSaveProfile = findViewById(R.id.btnSaveProfile);

        mAlertDialog =new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento....")
                .setCancelable(false).build();

        //INSTANCIAMOS LOS PROVEHEDORES======================================================================
        //Autenticacion
        mAuthProvider = new AuthProviders();
        //Usuarios en firebase
        mUsersProvider = new UsersProvider();

        //PROFILE BUTTON =====================================================================================
        mbtnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }
    private void register() {
        String username = mtxtInNombre.getText().toString();
        if (!username.isEmpty()) {
            updateUser(username); //Goes to the update user method
        }else {
            Toast.makeText(this, "Llena todos los campos", Toast.LENGTH_SHORT).show();
        }
    }
    //UPDATE USER METHOD ===============================================================================================================
    private void updateUser(String username) {
        String id = mAuthProvider.getUid();
        User user=new User();
        user.setUsername(username);
        user.setId(id);
        mAlertDialog.show();
        mUsersProvider.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mAlertDialog.dismiss();
                if (task.isSuccessful()) {
                    Intent intent = new Intent(ProfileActivity.this, GeneroActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    Toast.makeText(ProfileActivity.this, "No se almacenó el usuario en la base de datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}