package com.kathi.project.recipify;


import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent allergyIntent = new Intent(MainActivity.this, AllergyActivity.class);
        MainActivity.this.startActivity(allergyIntent);
    }
}
