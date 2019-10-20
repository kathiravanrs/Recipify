package com.kathi.project.recipify;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
    ScrollView scroll;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        foodTextView = findViewById(R.id.food_textview);
        caloriesView = findViewById(R.id.calories_textview);
        fatView = findViewById(R.id.fat_textview);
        imageView = findViewById(R.id.imageView);
        warningView = findViewById(R.id.warning_textview);
        hyperLinkView1 = findViewById(R.id.link1_textview);
        hyperLinkView2 = findViewById(R.id.link2_textview);
        hyperLinkView3 = findViewById(R.id.link3_textview);
        scroll = findViewById(R.id.scrollView);
        linearLayout = findViewById(R.id.activity_main);


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
        imageView.setImageBitmap(bitmap);

        attachDatabaseReadListener();
    }

    private void attachDatabaseReadListener(){

        if(mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    final ResultJSON json = dataSnapshot.getValue(ResultJSON.class);

                    foodTextView.setText(json.getPrediction());
                    caloriesView.setText("Cal: " + json.getCalories() + " Kcal/100g");
                    fatView.setText("Fat: " + json.getFat() + " g");

                    try {
                        JSONObject ingredientsJSON = new JSONObject(readJSONFromAsset());
                        JSONObject itemList = ingredientsJSON.getJSONObject(json.getPrediction());
                        JSONArray ingredientsList = itemList.getJSONArray("Ingredients");
                        JSONArray nameList = itemList.getJSONArray("name");
                        final JSONArray recipeList = itemList.getJSONArray("Recipe");

                        hyperLinkView1.setText(nameList.getString(0));
                        hyperLinkView2.setText(nameList.getString(1));
                        hyperLinkView3.setText(nameList.getString(2));

                        final Intent browserIntent = new Intent(Intent.ACTION_VIEW);

                        hyperLinkView1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    browserIntent.setData(Uri.parse(recipeList.getString(0)));
                                    startActivity(browserIntent);
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }

                            }
                        });

                        hyperLinkView2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    browserIntent.setData(Uri.parse(recipeList.getString(1)));
                                    startActivity(browserIntent);
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }

                            }
                        });

                        hyperLinkView3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    browserIntent.setData(Uri.parse(recipeList.getString(2)));
                                    startActivity(browserIntent);
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        });

                        int check = 0;

                        for(int i=0; i<allergies.size();i++){
                            for(int j=0; j<ingredientsList.length();j++){
                                if(ingredientsList.getString(j).equals(allergies.get(i))){
                                    warningView.setText("May contain " + allergies.get(i));
                                    check = 1;
                                    scroll.setBackgroundColor(Color.parseColor("#F44336"));
                                    linearLayout.setBackgroundColor(Color.parseColor("#F44336"));
                                }
                            }
                        }
                        if(check == 0){
                            warningView.setText("Safe to consume");
                        }


                    }catch (JSONException e){
                        e.printStackTrace();
                    }


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
            InputStream is = getAssets().open("recipe.json");
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
