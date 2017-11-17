package com.a0xb4dc0d3.gunify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.a0xb4dc0d3.gunify.Activities.ActivityLogin;
import com.a0xb4dc0d3.gunify.Activities.ActivityRegister;

public class MainActivity extends AppCompatActivity {

    //Buttons
    Button gLoginButton;
    Button gRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Button reference
        gLoginButton = (Button) findViewById(R.id.btn_login);
        gRegisterButton = (Button) findViewById(R.id.btn_register);

        //Go to ActivityLogin
        gLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityLogin.class);
                startActivity(intent);
            }
        });

        //Go to ActivityRegister
        gRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityRegister.class);
                startActivity(intent);
            }
        });


    }
}
