package com.kathi.project.recipify;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


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

        imageView.setImageBitmap(bitmap);


        int check = 0;

//        for(int i=0; i<allergies.size();i++){
//            if(TextValue.contains(allergies.get(i))){
//                warningView.setText("Do not eat. May contain " + allergies.get(i));
//                check = 1;
//            }
//        }
//        if(check == 0){
//            warningView.setBackgroundResource(R.drawable.warning_box_green);
//            warningView.setText("Safe to consume");
//        }

        attachDatabaseReadListener();
    }

    private void attachDatabaseReadListener(){

        if(mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    final ResultJSON json = dataSnapshot.getValue(ResultJSON.class);
                    Toast.makeText(ResultActivity.this, json.getPrediction(), Toast.LENGTH_LONG).show();
                    foodTextView.setText(json.getPrediction());

                    hyperLinkView1.setText(json.getNames().get(0));
                    hyperLinkView2.setText(json.getNames().get(1));
                    hyperLinkView3.setText(json.getNames().get(2));

                    final Intent browserIntent = new Intent(Intent.ACTION_VIEW);

                    hyperLinkView1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                browserIntent.setData(Uri.parse(json.getRecepies().get(0)));
                                startActivity(browserIntent);
                        }
                    });

                    hyperLinkView2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                browserIntent.setData(Uri.parse(json.getRecepies().get(1)));
                                startActivity(browserIntent);

                        }
                    });

                    hyperLinkView3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                browserIntent.setData(Uri.parse(json.getRecepies().get(2)));
                                startActivity(browserIntent);
                        }
                    });

                    Double calories = json.getCalories();
                    caloriesView.setText("Cal: "+calories+" Kcal/100g");

                    Long fat = json.getFat();
                    fatView.setText("Fat: "+fat+" g");


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
