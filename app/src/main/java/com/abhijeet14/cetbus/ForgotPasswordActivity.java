package com.abhijeet14.cetbus;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import dmax.dialog.SpotsDialog;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText forgotEmailText;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        forgotEmailText = findViewById(R.id.forgotpass_email_text);
        mAuth = FirebaseAuth.getInstance();
    }

    public void doSendResetLink(View view) {
        String email = forgotEmailText.getText().toString();
        final android.app.AlertDialog pd = new SpotsDialog.Builder().setTheme(R.style.Custom).setContext(this).build();
        pd.setMessage("Please Wait");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        if(TextUtils.isEmpty(email)){
            forgotEmailText.requestFocus();
            forgotEmailText.setError("Field Cannot be left blank");
        }else{
            pd.show();
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ForgotPasswordActivity.this, "Email Sent Successfully !! Check you email", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        pd.dismiss();
                        Toast.makeText(ForgotPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
