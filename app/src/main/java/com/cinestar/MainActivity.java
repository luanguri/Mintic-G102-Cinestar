package com.cinestar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    //Creación de objeto Register
    TextInputEditText mTxtInpEmail;
    TextInputEditText mTxtInpPassword;
    Button mBtnLogin;
    TextView mTxtRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Busqueda de ID
        mTxtInpEmail=findViewById(R.id.TxtInpEmail);
        mTxtInpPassword=findViewById(R.id.TxtInpPassword);
        mBtnLogin=findViewById(R.id.BtnLogin);
        mTxtRegister=findViewById(R.id.TxtRegister);


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
        Log.d("campo", "email: " + email);
        Log.d("campo", "password: " + password);
    }
}