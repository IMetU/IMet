package com.example.imetu.imet.Activity;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.imetu.imet.Image.ResultHolder;
import com.example.imetu.imet.R;

public class ImagePreview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        Bitmap bitmap = ResultHolder.getImage();

        if (bitmap == null){
            setResult(RESULT_CANCELED);
            finish();
            return;
        }
        ImageView imageView = (ImageView)findViewById(R.id.ivImagePreview);
        imageView.setImageBitmap(bitmap);
    }

    public void ImageOK(View view) {
        setResult(RESULT_OK);
        finish();
    }

    public void ImageCancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}
