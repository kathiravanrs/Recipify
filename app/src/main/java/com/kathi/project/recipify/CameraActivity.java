package com.kathi.project.recipify;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CameraActivity extends AppCompatActivity {


    private static final int INPUT_SIZE = 128;

    private Executor executor = Executors.newSingleThreadExecutor();
    private Button btnDetectObject, btnToggleCamera, btnToggleFlash;
    private CameraView cameraView;


    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mMessagesDatabaseReference;
    Intent myIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        cameraView = findViewById(R.id.cameraView);

        FirebaseApp.initializeApp(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("url");

        btnDetectObject = findViewById(R.id.btnDetectObject);
        btnToggleCamera = findViewById(R.id.btnToggleCamera);
        btnToggleFlash = findViewById(R.id.btnToggleFlash);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {
            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {

                Bitmap bitmap = cameraKitImage.getBitmap();
                saveImage(bitmap);
                uploadImage();
                bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);


                ArrayList<String> test = getIntent().getStringArrayListExtra("allergies");

                myIntent = new Intent(CameraActivity.this, ResultActivity.class);
                myIntent.putExtra("image", bitmap);
                myIntent.putExtra("imgPath", getFileLocation().getAbsoluteFile()+"/screen.jpg");
                myIntent.putStringArrayListExtra("allergies", test);


            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });


        btnToggleCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.toggleFacing();
            }
        });

        btnToggleFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraView.toggleFlash();
                Toast.makeText(CameraActivity.this, ""+cameraView.getFlash(), Toast.LENGTH_LONG).show();
            }
        });

        btnDetectObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.captureImage();
            }
        });

        initTensorFlowAndLoadModel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        cameraView.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initTensorFlowAndLoadModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    private void makeButtonVisible() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnDetectObject.setVisibility(View.VISIBLE);
            }
        });
    }

    private void saveImage(Bitmap bm){

        if(ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            Toast.makeText(CameraActivity.this,"Permission Not Granted", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(CameraActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    5);
        }
        else{
            File file = new File(getFileLocation(), "screen.jpg");
            Log.i("TAG", "" + file);
            if (file.exists())
                file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                Toast.makeText(CameraActivity.this, "SAVED"+getFileLocation(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(CameraActivity.this, "Failed", Toast.LENGTH_LONG).show();

            }
        }

    }

    public File getFileLocation(){
        return CameraActivity.this.getExternalFilesDir("test");
    }

    private void uploadImage() {

        String filePath = getFileLocation().getAbsoluteFile()+"/screen.jpg";

        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/image");

            ref.putFile(Uri.fromFile(new File (filePath)))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.d("Dwnldurl", "onSuccess: uri= "+ uri.toString());
                                    mMessagesDatabaseReference.setValue(uri.toString());
                                    mFirebaseDatabase.getReference().child("check").setValue(1);
                                    CameraActivity.this.startActivity(myIntent);

                                }
                            });
                            Toast.makeText(CameraActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(CameraActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }
}
