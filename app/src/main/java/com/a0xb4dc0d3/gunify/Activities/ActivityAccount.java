package com.a0xb4dc0d3.gunify.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.a0xb4dc0d3.gunify.MainActivity;
import com.a0xb4dc0d3.gunify.R;
import com.a0xb4dc0d3.gunify.Objects.Weapon;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivityAccount extends AppCompatActivity {

    private FirebaseAuth gAuth;
    private FirebaseAuth.AuthStateListener gAuthListener;
    private DatabaseReference gDatabase, gWeaponDB;

    //buttons
    private Button gSignoutButton; //, gConfigButton, gCreateButton;
    private ImageButton gConfigButton, gCreateButton;

    //listview
    private ListView gListView;

    //first time session
    private Boolean flag = false;


    @Override
    protected void onStart() {
        super.onStart();
        gAuth.addAuthStateListener(gAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //ListView Reference
       gListView = (ListView) findViewById(R.id.listView);

       //Buttons References
        gCreateButton = (ImageButton) findViewById(R.id.btn_create);
        gConfigButton = (ImageButton) findViewById(R.id.btn_config);
        gSignoutButton = (Button) findViewById(R.id.btn_logout);

        //Go to Add Activity
        gCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityAccount.this, ActivityAdd.class);
                startActivity(intent);
            }
        });

        //Go to Settings
        gConfigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityAccount.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        //Go to Main Activity
        gSignoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Signout basic
                if (gAuth.getCurrentUser() != null){
                    gAuth.signOut();
                    if (gAuth.getCurrentUser() == null ){
                        Intent intent = new Intent(ActivityAccount.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        gAuth = FirebaseAuth.getInstance();
        gAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //Usuario existente
                if (firebaseAuth.getCurrentUser() != null){
                    //Referencia en donde se encuentra la información de nuestros usuarios
                    gDatabase = FirebaseDatabase.getInstance().getReference().child("users");
                    //Entrar a la instancia de un usuario especìfico
                    gDatabase.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Current username
                            String currentUsername = dataSnapshot.child("username").getValue().toString();
                            //Show once
                            while (flag.booleanValue() == false){
                                stoast("Bienvenido " + currentUsername);
                                flag = true;
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    gWeaponDB = FirebaseDatabase.getInstance().getReference().child("weapons");
                    //Using Firebase UI for FirebaseListAdapter
                    FirebaseListAdapter<Weapon> weaponFirebaseListAdapter = new FirebaseListAdapter<Weapon>(
                            ActivityAccount.this, //Context
                            Weapon.class, //Class
                            R.layout.listview_row, //Layout
                            gWeaponDB //Database
                    ) {
                        //TextViews from listview_row layout
                        TextView gModel, gCategory, gPrice;

                        @Override
                        protected void populateView(View v, Weapon model, int position) {
                            //Populating each listview_row with content like iteration
                            gModel = (TextView) v.findViewById(R.id.tv_model);
                            gCategory = (TextView) v.findViewById(R.id.tv_category);
                            gPrice = (TextView) v.findViewById(R.id.tv_price);

                            gModel.setText(model.model);
                            gCategory.setText(model.category);
                            gPrice.setText(model.price);
                        }
                    };

                    //Setting adapter for ListView
                    gListView.setAdapter(weaponFirebaseListAdapter);

                } else {
                    //Else go to MainActivity!
                    startActivity(new Intent(ActivityAccount.this, MainActivity.class));
                    finish();
                }
            }
        };
    }

    public void stoast(String message){
        Toast.makeText(ActivityAccount.this, message, Toast.LENGTH_SHORT).show();
    }


}
