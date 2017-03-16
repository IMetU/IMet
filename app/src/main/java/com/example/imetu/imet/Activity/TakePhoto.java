package com.example.imetu.imet.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.imetu.imet.Image.ResultHolder;
import com.example.imetu.imet.R;
import com.flurgle.camerakit.CameraListener;
import com.flurgle.camerakit.CameraView;
import com.flurgle.camerakit.Size;

public class TakePhoto extends AppCompatActivity {
    private CameraView cameraView;
    private Bitmap myPhoto;
    private final int REQUEST_CODE_IMAGEPREVIEW = 25;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        cameraView = (CameraView)findViewById(R.id.camera);
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

    public void CaptureImage(View view) {
        cameraView.setCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(byte[] jpeg) {
                super.onPictureTaken(jpeg);
                myPhoto = BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length);
                ResultHolder.dispose();
                ResultHolder.setImage(myPhoto);
                ResultHolder.setNativeCaptureSize(cameraView.getPreviewSize());
                Intent intent = new Intent(TakePhoto.this, ImagePreview.class);
                startActivityForResult(intent, REQUEST_CODE_IMAGEPREVIEW);
            }
        });
        cameraView.captureImage();

    }
}
