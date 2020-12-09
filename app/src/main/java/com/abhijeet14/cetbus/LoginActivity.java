package com.abhijeet14.cetbus;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity {
private EditText busEmailText,busPassText;
private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        busEmailText = findViewById(R.id.login_email_text);
        busPassText = findViewById(R.id.login_password_text);

        mAuth = FirebaseAuth.getInstance();
    }

    public void doForgotPassword(View view) {
        startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
    }

    public void doLogin(View view) {
        final android.app.AlertDialog pd = new SpotsDialog.Builder().setTheme(R.style.Custom).setContext(this).build();
        pd.setMessage("Please Wait");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        String email = busEmailText.getText().toString();
        String password = busPassText.getText().toString();
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(this, "Fields Cannot be left blank", Toast.LENGTH_SHORT).show();
        }else{
            pd.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    pd.dismiss();
                    if(task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Successfully Logged in", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }
}
