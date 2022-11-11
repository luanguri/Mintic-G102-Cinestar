package com.cinestar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    //Creación de objetos================================================
    TextInputEditText mTxtInpUserName;
    TextInputEditText mTxtInpEmailR;
    TextInputEditText mTxtInpPasswordR;
    TextInputEditText mTxtInpConfirmPassword;
    Button mBtnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Busqueda de IDs===============================================
        mTxtInpUserName=findViewById(R.id.TxtInpUserName);
        mTxtInpEmailR=findViewById(R.id.TxtInpEmailR);
        mTxtInpPasswordR=findViewById(R.id.TxtInpPasswordR);
        mTxtInpConfirmPassword=findViewById(R.id.TxtInpConfirmPassword);
        mBtnRegister=findViewById(R.id.BtnRegister);


        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });

    }

    private void Register() {
        String username=mTxtInpUserName.getText().toString();
        String email=mTxtInpEmailR.getText().toString();
        String password=mTxtInpPasswordR.getText().toString();
        String confirmpassword=mTxtInpConfirmPassword.getText().toString();

        if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmpassword.isEmpty()){
            if (isEmailValid(email)){
                if(password.equals(confirmpassword)){
                    if(password.length() >=6){
                        createUser(email,password);
                    }else {
                        Toast.makeText(this, "Las contraseñas debe tener 6 caracteres", Toast.LENGTH_SHORT).show();
                        }
                }else{
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
                    }
            }else {
                Toast.makeText(this, "El correo no es valido", Toast.LENGTH_LONG).show();
                }
        }else {
            Toast.makeText(this, "Llenar todos los campos", Toast.LENGTH_LONG).show();
        }
    }

    private void createUser(String email, String password) {
    }

   public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }
}