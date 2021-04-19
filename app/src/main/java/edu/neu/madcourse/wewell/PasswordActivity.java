package edu.neu.madcourse.wewell;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordActivity extends AppCompatActivity {
    EditText userEmail;
    Button userPass;
//    TextView userBack;
    FirebaseAuth firebaseAuth;
    TextView loginOptional;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_password);
        userEmail = findViewById(R.id.etUserEmail);
        userPass = findViewById(R.id.btnForgotPass);
        firebaseAuth = FirebaseAuth.getInstance();
        loginOptional = findViewById(R.id.textviewlo);



        userPass.setOnClickListener(new View.OnClickListener() {
            public void ShowToast(Context context, String info) {
                Toast toast = Toast.makeText(context, Html.fromHtml("<font color=black ><b>" + info + "</b></font>"), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            }
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(userEmail.getText().toString())) {
                    //Toast.makeText(PasswordActivity.this, "Email Address can't be empty.", Toast.LENGTH_LONG).show();
                    ShowToast(PasswordActivity.this, "Email Address can't be empty.");
                } else {
                    firebaseAuth.sendPasswordResetEmail(userEmail.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
//                                        Toast.makeText(PasswordActivity.this,
//                                                "Password send to your email", Toast.LENGTH_LONG).show();
                                        ShowToast(PasswordActivity.this, "Check your email to reset password");
                                    } else {
                                        Toast.makeText(PasswordActivity.this,
                                                task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

//        userBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(PasswordActivity.this, LoginActivity.class));
//            }
//        });
        loginOptional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PasswordActivity.this, LoginActivity.class));
            }
        });
    }
}
