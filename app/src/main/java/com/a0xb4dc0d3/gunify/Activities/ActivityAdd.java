package com.a0xb4dc0d3.gunify.Activities;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.a0xb4dc0d3.gunify.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ActivityAdd extends AppCompatActivity {

    EditText gModel, gPrice;
    RadioGroup gCategory;
    RadioButton gCategory1, gCategory2, gCategory3;
    Button gAddGun;
    //Index of Radio button
    public int gIndex;
    public String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //References
        gModel = (EditText) findViewById(R.id.etxt_model);
        gPrice = (EditText) findViewById(R.id.etxt_price);

        gCategory = (RadioGroup) findViewById(R.id.rg_category);
        gCategory1 = (RadioButton) findViewById(R.id.rb_category1);
        gCategory2 = (RadioButton) findViewById(R.id.rb_category2);
        gCategory3 = (RadioButton) findViewById(R.id.rb_category3);

        gAddGun = (Button) findViewById(R.id.btn_add);

        //Add gun on click
        gAddGun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAdd();
            }
        });

    }

    //Method
    private void doAdd(){
        String model = gModel.getText().toString().trim();
        String price = gPrice.getText().toString().trim();

        gCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                //Get id of on checked category radio button
                gIndex = group.indexOfChild(group.findViewById(group.getCheckedRadioButtonId()));
                if (gIndex == 0){
                    category = "escopeta";
                } else if (gIndex == 1){
                    category = "rifle";
                } else if (gIndex == 2){
                    category = "pistola";
                }
            }
        });

        //If index is 0 = pistol, 1 = rifle , 2 = shotgun else Error
        if (!TextUtils.isEmpty(model) && !TextUtils.isEmpty(price) && gIndex >= 0){

            //FirebaseDatabase
            DatabaseReference gDatabase, currentWeaponDB;
            //Creating weapons child.
            gDatabase = FirebaseDatabase.getInstance().getReference().child("weapons");

            //Create current weapon
            currentWeaponDB = gDatabase.push();

            //Content of weapon
            currentWeaponDB.child("category").setValue(category);
            currentWeaponDB.child("model").setValue(model);
            currentWeaponDB.child("price").setValue("$"+price+" CLP");

            //Success!
            stoast("Arma creada");

        } else {
            stoast("Rellene el formulario de arma");
        }
    }

    public void stoast(String message){
        Toast.makeText(ActivityAdd.this, message, Toast.LENGTH_SHORT).show();
    }

}
