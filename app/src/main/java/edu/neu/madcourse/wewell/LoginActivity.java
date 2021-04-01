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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText email;
    EditText pass;
    Button login;
    Button forgotPass;
    Button back;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.Email2);
        pass = findViewById(R.id.Pass2);
        login = findViewById(R.id.btnSogin);
        forgotPass = findViewById(R.id.btnUserForgottPass);
        back = findViewById(R.id.button2back);
        firebaseAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(pass.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "Email Address or Password can't be empty.", Toast.LENGTH_LONG).show();
                } else {
                    firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),
                            pass.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Please verify your email address"
                                                    , Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(LoginActivity.this, task.getException().getMessage()
                                                , Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });


        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, PasswordActivity.class));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

    }
}
