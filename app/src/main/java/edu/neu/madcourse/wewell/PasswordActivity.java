package edu.neu.madcourse.wewell;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordActivity extends AppCompatActivity {
    EditText userEmail;
    Button userPass;
    Button userBack;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_password);
        userEmail = findViewById(R.id.etUserEmail);
        userPass = findViewById(R.id.btnForgotPass);
        userBack = findViewById(R.id.buttonGoback);
        firebaseAuth = FirebaseAuth.getInstance();

        userPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(userEmail.getText().toString())) {
                    Toast.makeText(PasswordActivity.this, "Email Address can't be empty.", Toast.LENGTH_LONG).show();
                } else {
                    firebaseAuth.sendPasswordResetEmail(userEmail.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(PasswordActivity.this,
                                                "Password send to your email", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(PasswordActivity.this,
                                                task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

        userBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PasswordActivity.this, LoginActivity.class));
            }
        });
    }
}
