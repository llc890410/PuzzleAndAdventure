package com.example.PuzzleAndAdventure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private static final String ACTIVITY_TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText etId = (EditText)findViewById(R.id.etId);
        Button btnLogin = (Button)findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!etId.getText().toString().matches("")){
                    Log.d(LoginActivity.ACTIVITY_TAG,"輸入非空");

                    String ID = etId.getText().toString();
                    //設定Intent要前往的頁面
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    //把ID傳給下個activity
                    //建立Bundle並放入ID
                    Bundle bundle = new Bundle();
                    bundle.putString("ID",ID);
                    //將bundle交給intent
                    intent.putExtras(bundle);
                    //啟動Intent
                    startActivity(intent);
                    //結束LoginActivity
                    LoginActivity.this.finish();

                }
                else{
                    Log.d(LoginActivity.ACTIVITY_TAG,"輸入為空");
                    Toast.makeText(LoginActivity.this,"請輸入ID",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}