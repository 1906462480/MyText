package com.example.mytext;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //1.定义控件对象
    private EditText etUsername;
    private EditText etPassword;
    private CheckBox chRemember;
    private Button etLogin;
    private String fileName = "login.text" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //2.获取控件对象
        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        chRemember = findViewById(R.id.reach);
        etLogin = findViewById(R.id.login);
        //3.获取存储的用户信息，若有则写入
//        String username = readPrfs();
        String username = readData(fileName);
        if (username != null){
            etUsername.setText(username);
        }
        //4.设置登录按钮的点击事件的监听器
        etLogin.setOnClickListener(this);
    }

    private String readPrfs() {
        SharedPreferences sp = getSharedPreferences("userInfo",MODE_PRIVATE);
        return sp.getString("username","");
    }

    //5.处理点击事件
    public void onClick(View view) {
        //5.1获取输入的用户名和密码
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        //5.2
        if (chRemember.isChecked()) {
            savePref(username);
            saveData(fileName,username);
            saveDataPrivate(fileName,username);
            readData(fileName);
        }
        else{
            clearPref();
            deleteDataFile(fileName);
            deletePrivateExStorage(fileName);

        }
        switch (view.getId()) {

            case R.id.login:
                if (username.equals("hsy") && password.equals("12345")) {
                    Toast.makeText(MainActivity.this,"登陆成功",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this,"登陆失败",Toast.LENGTH_LONG).show();
                }
        }
    }

    private void saveDataPrivate(String fileName, String username) {
        //内部存储目录：data/data/包名/files/
        try {
            //1.打开文件输出流
            File file = new File(getExternalFilesDir(""),fileName); //path+filename
            FileOutputStream out = new  FileOutputStream(file);
            //2.创建BufferedWriter对象
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            //3.写入对象
            writer.write(username);
            //4.关闭数据流
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //读取
    private String readData(String fileName) {
        String data = null;
        try {
            FileInputStream in = openFileInput(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            data = reader.readLine();
            reader.close();
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    //2.内部保存
    //保存
    private void saveData(String filename, String username) {
        //内部存储目录：data/data/包名/files/
        try {
            //1.打开文件输出流
            FileOutputStream out = openFileOutput(fileName, Context.MODE_PRIVATE);
            //2.创建BufferedWriter对象
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            //3.写入对象
            writer.write(username);
            //4.关闭数据流
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearPref() {
        SharedPreferences.Editor editor = getSharedPreferences("userInfo",MODE_PRIVATE).edit();
        editor.clear().apply();
    }

    private void savePref(String username) {
        SharedPreferences.Editor editor = getSharedPreferences("userInfo",MODE_PRIVATE).edit();
        editor.putString("username",username);
        editor.apply();
    }
    // 删除内部存储文件
    private void deleteDataFile(String fileName) {
        if (deleteFile(fileName)) {
            Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
        }
    }
    // 删除外部私有存储的文件
    private void deletePrivateExStorage(String fileName) {
        File file = new File(getExternalFilesDir(""), fileName);
        if (file.isFile()) {
            if (file.delete()) {
                Toast.makeText(this, "删除外部公有文件成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "删除外部公有文件失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

}