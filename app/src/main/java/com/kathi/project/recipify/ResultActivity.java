package com.kathi.project.recipify;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class ResultActivity extends AppCompatActivity {
    TextView textViewResult;
    ImageView imageResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_view);

        textViewResult = findViewById(R.id.textViewResult);
        imageResult = findViewById(R.id.imageViewResult);

        Intent intent = getIntent();
        String TextValue = intent.getStringExtra("result");
        Bitmap bitmap = (Bitmap) intent.getParcelableExtra("image");

        textViewResult.setText(TextValue);
        imageResult.setImageBitmap(bitmap);

    }
}
