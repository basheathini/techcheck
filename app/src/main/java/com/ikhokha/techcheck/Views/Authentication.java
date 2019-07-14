package com.ikhokha.techcheck.Views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.ikhokha.techcheck.R;

public class Authentication extends AppCompatActivity {
    EditText username, password;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    TextView proceed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        username = findViewById(R.id.email_address);
        password = findViewById(R.id.password);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.spin_kit);
        proceed = findViewById(R.id.proceed);
        Sprite doubleBounce = new Circle();
        progressBar.setIndeterminateDrawable(doubleBounce);
    }
    public void Authenticate(final View view){
        username.setEnabled(false);
        password.setEnabled(false);
        proceed.setText("");
        progressBar.setVisibility(View.VISIBLE);
        String email = username.getText().toString().trim();
        String code = password.getText().toString().trim();
        if (TextUtils.isEmpty(email)){
            username.setError("Please provide email address.");
        }else if (TextUtils.isEmpty(code)){
            password.setError("Please provide password.");
        }else {
            firebaseAuth.signInWithEmailAndPassword(email, code)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                username.setEnabled(true);
                                password.setEnabled(true);
                                proceed.setText(R.string.authenticate);
                                progressBar.setVisibility(View.INVISIBLE);
                            }else {
                                startActivity(new Intent(Authentication.this, ShoppingMenu.class));
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    username.setError("Please check email.");
                    username.setError("Please check password.");
                    username.setEnabled(true);
                    password.setEnabled(true);
                    proceed.setText(R.string.authenticate);
                    progressBar.setVisibility(View.INVISIBLE);

                }
            });
        }
    }
}
