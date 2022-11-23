package com.cinestar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    //PROFILE OBJECT DECLARATION =============================================================================
    TextInputEditText mtxtInNombre;
    Button mbtnSaveProfile;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //OBJET INSTANCES / SEARCH ID ========================================================================
        mtxtInNombre = findViewById(R.id.txtInNombre);
        mbtnSaveProfile = findViewById(R.id.btnSaveProfile);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

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
        String id = mAuth.getCurrentUser().getUid();
        Map<String,Object> map = new HashMap<>();
        map.put("username", username);
        mFirestore.collection("Users").document(id).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(ProfileActivity.this, GeneroActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(ProfileActivity.this, "Almacenamiento fallido", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}