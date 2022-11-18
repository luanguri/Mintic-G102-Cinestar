package com.cinestar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    //Creación de objeto Register
    TextInputEditText mTxtInpEmail;
    TextInputEditText mTxtInpPassword;
    Button mBtnLogin;
    TextView mTxtRegister;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Busqueda de ID
        mTxtInpEmail = findViewById(R.id.TxtInpEmail);
        mTxtInpPassword = findViewById(R.id.TxtInpPassword);
        mBtnLogin = findViewById(R.id.BtnLogin);
        mTxtRegister = findViewById(R.id.TxtRegister);
        mAuth = FirebaseAuth.getInstance();


        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        //Instancia del TxtRegister
        mTxtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login() {
        String email = mTxtInpEmail.getText().toString();
        String password = mTxtInpPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(MainActivity.this,SeleccionarGeneroActivity.class);
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