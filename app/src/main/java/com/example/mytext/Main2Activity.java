package com.example.mytext;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageView;
    private Button etRead;
    private Button etWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        imageView = findViewById(R.id.hzw);
        etRead = findViewById(R.id.read);
        etWrite = findViewById(R.id.write);
        etRead.setOnClickListener(this);
        etWrite.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.read:
               readFromSD("猫.jpg");
                break;
            case R.id.write:
                saveToSD("hzw.jpg");
                break;
        }

    }

    private void readFromSD(String filename) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                return;

            }
        }
        //写SD卡的步骤
        //1.获取SD的DownLoad目录，创建需要存储的文件
        String path = Environment.getExternalStoragePublicDirectory("").getPath()
                + File.separator+Environment.DIRECTORY_PICTURES;
        File file = new File(path,filename);
        try {
          //2.创建file的文件输入流
            FileInputStream inputStream = new FileInputStream(file);
          //3.将文件流写入imageview
          imageView.setImageBitmap(BitmapFactory.decodeStream(inputStream));
          //4.关闭文件输入流
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //SD卡：外部的公有文件夹，需要申请运行时权限
    private void saveToSD(String filename) {
        //1.申请写SD的权限，要求Android的版本大于6.0（Build.VERSION_CODES.M）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return;

            }
        }
        //写SD卡的步骤
        //1.获取SD的DownLoad目录，创建需要存储的文件
        String path = Environment.getExternalStoragePublicDirectory("").getPath()
                + File.separator+Environment.DIRECTORY_PICTURES;
        File file = new File(path,filename);
        try {
            if (file.createNewFile()){
                //2.获取ImageView的Bitmap图片对象
                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                //3.将Bitmap对象写入SD卡
                FileOutputStream outputStream = new FileOutputStream(file);

                //带缓冲
//                BufferedOutputStream bufferedOutput = new BufferedOutputStream(outputStream);
                bitmap.compress(Bitmap.CompressFormat.JPEG,100, outputStream);
                //4.关闭请求的资源
                outputStream.flush();
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"权限申请被拒绝",Toast.LENGTH_LONG).show();
            return;
        }
        switch (requestCode){
            case 1:
                saveToSD("hzw.jpg");
                break;
        }
    }
}
