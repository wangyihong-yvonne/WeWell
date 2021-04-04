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

import edu.neu.madcourse.wewell.service.UserService;

import static android.view.View.OnClickListener;

public class SignUpActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    Button signup;
    Button login;


    FirebaseAuth firebaseAuth;
    UserService userService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signlogin);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        signup = findViewById(R.id.btnSignup);
        login = findViewById(R.id.btnLogin);
        firebaseAuth = FirebaseAuth.getInstance();
        userService = new UserService();
        signup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(password.getText().toString())) {
                    Toast.makeText(SignUpActivity.this, "Email Address or Password can't be empty.", Toast.LENGTH_LONG).show();
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),
                            password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        firebaseAuth.getCurrentUser().sendEmailVerification()
                                                .addOnCompleteListener(task1 -> {
                                                    if (task1.isSuccessful()) {
                                                        Toast.makeText(SignUpActivity.this, "Welcome to Wewell. Please check your email for verification",
                                                                Toast.LENGTH_LONG).show();
                                                        email.setText("");
                                                        password.setText("");
                                                        //create corresponding userId to Firestore
                                                        userService.saveUser(firebaseAuth.getCurrentUser().getUid());
                                                    } else {
                                                        Toast.makeText(SignUpActivity.this, task1.getException().getMessage(),
                                                                Toast.LENGTH_LONG).show();
                                                    }

                                                });
                                    } else {
                                        Toast.makeText(SignUpActivity.this, task.getException().getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
            });

        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }
}
