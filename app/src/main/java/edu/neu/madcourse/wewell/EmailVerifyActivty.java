package edu.neu.madcourse.wewell;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class EmailVerifyActivty extends AppCompatActivity {

    ImageView backBtn;
    Button conToSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verify_activty);

        backBtn = (ImageView) findViewById(R.id.btnBackArrowCont);
        conToSignIn = (Button) findViewById(R.id.btn_cont_sign_in);

        conToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmailVerifyActivty.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmailVerifyActivty.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}