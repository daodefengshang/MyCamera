package com.devtf.belial.lite;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawableResource;
import com.bumptech.glide.load.resource.transcode.GlideBitmapDrawableTranscoder;
import com.devtf.belial.camera.Constants;
import com.devtf.belial.camera.core.CameraManager;
import com.devtf.belial.camera.util.DeviceUtil;
import com.devtf.belial.camera.util.ImageUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Button openCamera;
    private ImageView croppedPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openCamera = (Button) findViewById(R.id.open_camera);
        croppedPhoto = (ImageView) findViewById(R.id.display_crop_photo);
        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                } else {
                    CameraManager.getInstance().openCamera(MainActivity.this);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0 :
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    CameraManager.getInstance().openCamera(MainActivity.this);
                }else {
                    Toast.makeText(MainActivity.this, "请打开相机和读写存储卡权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CROP && resultCode == RESULT_OK) {
            Bitmap bitmap= ImageUtils.decodeBitmapWithOrientationMax(data.getData().getPath(),
                    DeviceUtil.getScreenWidth(this), DeviceUtil.getScreenHeight(this));
            croppedPhoto.setImageBitmap(bitmap);
        }

    }
}
