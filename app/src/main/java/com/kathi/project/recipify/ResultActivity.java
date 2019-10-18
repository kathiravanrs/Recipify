package com.kathi.project.recipify;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class ResultActivity extends AppCompatActivity {
    TextView foodTextView, warningView, caloriesView, fatView;
    Button hyperLinkView1, hyperLinkView2, hyperLinkView3;
    ImageView imageView;


    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mMessagesDatabaseReference;
    ChildEventListener mChildEventListener;


    ArrayList<String> allergies;
    String TextValue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_view_test);

        foodTextView = findViewById(R.id.food_textview);
        caloriesView = findViewById(R.id.calories_textview);
        fatView = findViewById(R.id.fat_textview);
        imageView = findViewById(R.id.imageView);
        warningView = findViewById(R.id.warning_textview);

        hyperLinkView1 = findViewById(R.id.link1_textview);
        hyperLinkView2 = findViewById(R.id.link2_textview);
        hyperLinkView3 = findViewById(R.id.link3_textview);


        FirebaseApp.initializeApp(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("result");

        Intent intent = getIntent();
        TextValue = intent.getStringExtra("result");
        String filePath = intent.getStringExtra("imgPath");
        allergies = getIntent().getStringArrayListExtra("allergies");


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        foodTextView.setText(TextValue);
        imageView.setImageBitmap(bitmap);

        try{
            JSONObject obj = new JSONObject(readJSONFromAsset());

            String prediction = obj.getString("prediction");
            foodTextView.setText(prediction);

            final JSONArray recipeArray = obj.getJSONArray("recepies");

            JSONArray ingredientsArray = obj.getJSONArray("ingredients");

            JSONArray nameArray = obj.getJSONArray("names");
            hyperLinkView1.setText(nameArray.getString(0));
            hyperLinkView2.setText(nameArray.getString(1));
            hyperLinkView3.setText(nameArray.getString(2));

            final Intent browserIntent = new Intent(Intent.ACTION_VIEW);

            hyperLinkView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        browserIntent.setData(Uri.parse(recipeArray.getString(0)));
                        startActivity(browserIntent);
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            });

            hyperLinkView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        browserIntent.setData(Uri.parse(recipeArray.getString(1)));
                        startActivity(browserIntent);

                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            });

            hyperLinkView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        browserIntent.setData(Uri.parse(recipeArray.getString(2)));
                        startActivity(browserIntent);

                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            });

            String calories = obj.getString("calories");
            caloriesView.setText("Cal: "+calories+" Kcal/100g");

            String fat = obj.getString("fat");
            fatView.setText("Fat: "+fat+" g");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSONerror", "JsonError");
        }

        int check = 0;

        for(int i=0; i<allergies.size();i++){
            if(TextValue.contains(allergies.get(i))){
                warningView.setText("Do not eat. May contain "+allergies.get(i));
                check = 1;
            }
        }
        if(check == 0){
            warningView.setBackgroundResource(R.drawable.warning_box_green);
            warningView.setText("Safe to consume");
        }

        attachDatabaseReadListener();
    }

    private void attachDatabaseReadListener(){

        if(mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    public String readJSONFromAsset() {
        String json;
        try {
            InputStream is = getAssets().open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
