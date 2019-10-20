package com.kathi.project.recipify;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class AllergyActivity extends AppCompatActivity {


    CheckBox meatCheck, dairyCheck, nutsCheck, seaCheck, glutenCheck, soyCheck, diabeticCheck, lowFatCheck;
    Button submitButton;
    List<String> allergyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergy);
        meatCheck = findViewById(R.id.meatBox);
        dairyCheck = findViewById(R.id.dairyBox);
        nutsCheck = findViewById(R.id.nutBox);
        seaCheck = findViewById(R.id.seafoodBox);
        glutenCheck = findViewById(R.id.glutenBox);
        soyCheck = findViewById(R.id.soyBox);
        diabeticCheck = findViewById(R.id.diabeticBox);
        submitButton = findViewById(R.id.submitButton);
        lowFatCheck = findViewById(R.id.lowFatBox);

        allergyList = new ArrayList<>();

        final Intent cameraIntent = new Intent(AllergyActivity.this, CameraActivity.class);


        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (meatCheck.isChecked()) {
                    allergyList.add("chicken");
                    allergyList.add("mutton");
                    allergyList.add("pork");
                    allergyList.add("beef");
                    allergyList.add("eggs");
                }
                if (dairyCheck.isChecked()) {
                    allergyList.add("cheese");
                    allergyList.add("milk");
                    allergyList.add("yogurt");
                    allergyList.add("curd");

                }
                if (nutsCheck.isChecked()) {
                    allergyList.add("peanuts");
                    allergyList.add("groundnuts");
                    allergyList.add("pea nuts");
                    allergyList.add("cashew nuts");
                }
                if (seaCheck.isChecked()) {
                    allergyList.add("fish");
                    allergyList.add("lobster");
                    allergyList.add("shrimp");
                    allergyList.add("prawn");
                }
                if (glutenCheck.isChecked()) {
                    allergyList.add("flour");
                    allergyList.add("wheat");
                    allergyList.add("starch");
                    allergyList.add("corn");
                    allergyList.add("gluten");
                }
                if (soyCheck.isChecked()) {
                    allergyList.add("soy beans");
                    allergyList.add("soy");
                    allergyList.add("soy milk");
                    allergyList.add("soy sauce");
                    allergyList.add("soybean oil");
                    allergyList.add("Soybean oil");

                }
                if (diabeticCheck.isChecked()){
                    allergyList.add("flour");
                    allergyList.add("sugar");
                    allergyList.add("bread");
                }
                if(lowFatCheck.isChecked()){
                    allergyList.add("Vegetable Oil");
                    allergyList.add("cheese");
                    allergyList.add("butter");
                }

                //Displaying the message on the toast
                Toast.makeText(getApplicationContext(), allergyList.toString(), Toast.LENGTH_LONG).show();
                cameraIntent.putStringArrayListExtra("allergies",(ArrayList<String>) allergyList);
                AllergyActivity.this.startActivity(cameraIntent);
            }

        });
    }
}