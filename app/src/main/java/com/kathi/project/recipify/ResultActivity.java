package com.kathi.project.recipify;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class ResultActivity extends AppCompatActivity {
    TextView foodTextView, warningView;
    Button hyperLinkView2, hyperLinkView3, nutrientView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_view_test);

        foodTextView = findViewById(R.id.food_textview);
        imageView = findViewById(R.id.imageView);

        Intent intent = getIntent();
        String TextValue = intent.getStringExtra("result");
        Bitmap bitmap = (Bitmap) intent.getParcelableExtra("image");

        foodTextView.setText(TextValue);
        imageView.setImageBitmap(bitmap);

    }
}
