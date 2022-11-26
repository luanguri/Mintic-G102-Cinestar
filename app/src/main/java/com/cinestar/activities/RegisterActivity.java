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
import com.google.firebase.auth.AuthResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity {

    //Declaración de objetos=============================================================================================================
    TextInputEditText mTxtInpUserName;
    TextInputEditText mTxtInpEmailR;
    TextInputEditText mTxtInpPasswordR;
    TextInputEditText mTxtInpConfirmPassword;
    Button mBtnRegister;
    AuthProviders mAuthProvider;
    UsersProvider mUsersProvider;
    AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    //Instancias de objetos o busqueda de IDs=============================================================================================
        mTxtInpUserName = findViewById(R.id.TxtInpUserName);
        mTxtInpEmailR = findViewById(R.id.TxtInpEmailR);
        mTxtInpPasswordR = findViewById(R.id.TxtInpPasswordR);
        mTxtInpConfirmPassword = findViewById(R.id.TxtInpConfirmPassword);
        mBtnRegister = findViewById(R.id.BtnRegister);

        mAlertDialog =new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento....")
                .setCancelable(false).build();

        mAuthProvider = new AuthProviders();
        mUsersProvider = new UsersProvider();

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });
    }

    private void Register() {
        String username = mTxtInpUserName.getText().toString();
        String email = mTxtInpEmailR.getText().toString();
        String password = mTxtInpPasswordR.getText().toString();
        String confirmpassword = mTxtInpConfirmPassword.getText().toString();

    //Condicionales para validación de user, email y password============================================================================
        if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmpassword.isEmpty()){
            if (isEmailValid(email)){ //Ver verificar método formato de correo abajo
                if(password.equals(confirmpassword)){ //Confirmar si las contraseña son iguales
                    if(password.length() >=6){
                        createUser(username, email, password);//Ver método abajo
                    }else {
                        Toast.makeText(this, "La contraseña debe tener 6 caracteres", Toast.LENGTH_SHORT).show();
                    }
                }else {
                        Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "El correo no es valido", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }
    //Método crear usuario===============================================================================================================
    private void createUser(final String username, final String email, String password) {
        mAlertDialog.show();
        mAuthProvider.register(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mAlertDialog.dismiss();
                if (task.isSuccessful()){
                    String id = mAuthProvider.getUid();
                    User user=new User();
                    user.setId(id);
                    user.setEmail(email);
                    user.setUsername(username);
                    user.setPassword(password);

                    //Este método crea la colección "Users" den Faribase.
                    mUsersProvider.create(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mAlertDialog.dismiss();
                            if (task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Registro guardado", Toast.LENGTH_SHORT).show();
                                //El método "intent" finaliza la tarea del Register si se cumple el registro del usuario e inicia el login.
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else {
                                Toast.makeText(RegisterActivity.this, "Registro fallido", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Toast.makeText(RegisterActivity.this, "Registro completo", Toast.LENGTH_SHORT).show();
                }else {
                    mAlertDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Registro fallido", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    //Método para verificar si el formato de email es válido=============================================================================
    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}