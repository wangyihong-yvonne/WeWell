package edu.neu.madcourse.wewell;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import edu.neu.madcourse.wewell.service.UserService;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = SignInActivity.class.getSimpleName();

    private static final int GOOGLE_SIGN_IN = 101;
    private static final int EMAIL_SIGN_IN = 102;

    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // already signed in
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        Button googleButton = (Button) findViewById(R.id.sign_in_with_google);
        googleButton.setOnClickListener(v -> onGoogleSignIn());

        Button emailButton = (Button) findViewById(R.id.sign_in_with_email);
        emailButton.setOnClickListener(v -> onEmailSignIn());

        userService = new UserService();

    }

    public void onGoogleSignIn() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient client = GoogleSignIn.getClient(this, gso);

        Intent signInIntent = client.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    public void onEmailSignIn() {
        // todo
        // use startActivityForResult(signInIntent, EMAIL_SIGN_IN);
        startActivity(new Intent(SignInActivity.this, LoginActivity.class));
        finish();
    }

    // [START auth_fui_result]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(SignInActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

        } else if (resultCode == EMAIL_SIGN_IN) {
            // todo
        }
    }
    // [END auth_fui_result]

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            saveUserInfo();
                            startMainActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void saveUserInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // write to shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.current_user_email), user.getEmail());
        editor.putString(getString(R.string.current_user_name), user.getDisplayName());
        editor.putString(getString(R.string.current_user_id), user.getUid());
        // todo apply() changes the in-memory SharedPreferences object immediately
        //  but writes the updates to disk asynchronously. Alternatively,
        //  you can use commit() to write the data to disk synchronously.
        //  But because commit() is synchronous, you should avoid calling it
        //  from your main thread because it could pause your UI rendering.
        editor.commit();

        userService.saveUser(user.getUid());
    }

    private void startMainActivity() {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}