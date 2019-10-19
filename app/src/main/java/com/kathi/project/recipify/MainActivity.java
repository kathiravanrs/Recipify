package com.kathi.project.recipify;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    Intent intent;

    String[] colors = {"#96CC7A", "#EA705D", "#66BBCC"};

    public static final String PREF_USER_FIRST_TIME = "user_first_time";
    boolean isUserFirstTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isUserFirstTime = Boolean.valueOf(Utils.readSharedSetting(MainActivity.this, PREF_USER_FIRST_TIME, "true"));

        Intent introIntent = new Intent(MainActivity.this, PagerActivity.class);
        introIntent.putExtra(PREF_USER_FIRST_TIME, isUserFirstTime);

        if (isUserFirstTime)
            MainActivity.this.startActivity(introIntent);
        else {
            Intent allergyIntent = new Intent(MainActivity.this, AllergyActivity.class);
            MainActivity.this.startActivity(allergyIntent);
        }
    }
}
