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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ActivityRegister extends AppCompatActivity {

    //Button
    private Button gRegisterButton;
    //EditTexts
    private EditText gUsernameField, gEmailField, gPasswordField;

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
        setContentView(R.layout.activity_register);

        //Authentication instance
        gAuth = FirebaseAuth.getInstance();

        //Reference
        gRegisterButton = (Button) findViewById(R.id.btn_register);
        gUsernameField = (EditText) findViewById(R.id.etxt_username);
        gEmailField = (EditText) findViewById(R.id.etxt_email);
        gPasswordField = (EditText) findViewById(R.id.etxt_password);

        //Go to Activity Account
        gRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });

        gAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //Si se encuentra una sesión abrir Login para que se encargue de logear.
                if (firebaseAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(ActivityRegister.this, ActivityLogin.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

    }

    private void startRegister(){
        final String username, email, password;
        username = gUsernameField.getText().toString().trim();
        email = gEmailField.getText().toString().trim();
        password = gPasswordField.getText().toString().trim();

        //Validations
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            //Create an Email / Password
            gAuth.createUserWithEmailAndPassword(email, password)
                    //This is an Event Listener occurs when we are creating the user in the firebase db ...
                    // 'When the registration is complete'
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //Validate it first
                            if (task.isSuccessful()){
                                gAuth.signInWithEmailAndPassword(email, password);

                                //Creating references...
                                DatabaseReference gDatabase, currentUserDB;
                                gDatabase = FirebaseDatabase.getInstance().getReference().child("users");
                                currentUserDB = gDatabase.child(gAuth.getCurrentUser().getUid());

                                //Content of user
                                currentUserDB.child("username").setValue(username);
                                currentUserDB.child("email").setValue(email);

                                stoast("Cuenta creada con éxito");


                            } else {
                                //Display error
                                stoast("Error al crear la cuenta");
                            }
                        }
                    });
        } else {
            stoast("Complete el formulario de registro");
        }

    }

    public void stoast(String message){
        Toast.makeText(ActivityRegister.this, message, Toast.LENGTH_SHORT).show();
    }


}
