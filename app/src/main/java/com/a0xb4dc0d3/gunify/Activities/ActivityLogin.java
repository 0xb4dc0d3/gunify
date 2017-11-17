package com.a0xb4dc0d3.gunify.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.a0xb4dc0d3.gunify.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityLogin extends AppCompatActivity {

    //Button
    private Button gLoginButton;

    //EditText
    private EditText gEmailField, gPasswordField;

    //Firebase Auth
    private FirebaseAuth gAuth;
    private FirebaseAuth.AuthStateListener gAuthListener;

    @Override
    protected void onStart() {
        super.onStart();
        gAuth.addAuthStateListener(gAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Reference
        gEmailField = (EditText) findViewById(R.id.etxt_email);
        gPasswordField = (EditText) findViewById(R.id.etxt_password);
        gLoginButton = (Button) findViewById(R.id.btn_login);

        //Authentication Instance
        gAuth = FirebaseAuth.getInstance();

        //Login on click
        gLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });

        //Escuchador de evento
        gAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null){
                    //stoast(firebaseAuth.getCurrentUser().getEmail()+":"+firebaseAuth.getCurrentUser().getUid());
                    Intent intent = new Intent(ActivityLogin.this, ActivityAccount.class);
                    startActivity(intent);
                    finish();
                    //Signout for debugging.
                    //gAuth.signOut();
                }
            }
        };

    }

    private void doLogin(){
        final String email, password;
        email = gEmailField.getText().toString().trim();
        password = gPasswordField.getText().toString().trim();

        //Validations in form
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            gAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                stoast("Sesión iniciada");
                            } else {
                                stoast("Error al iniciar sesión");
                            }
                        }
                    });
        } else {
            stoast("Complete el formulario para iniciar sesión");
        }

    }

    public void stoast(String message){
        Toast.makeText(ActivityLogin.this, message, Toast.LENGTH_SHORT).show();
    }


}
