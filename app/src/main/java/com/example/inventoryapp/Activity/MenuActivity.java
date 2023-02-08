package com.example.inventoryapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.inventoryapp.R;

public class MenuActivity extends AppCompatActivity {

    Button InboundBtn, OutboundBtn, ReportBtn;
    ImageButton BackBtn;
    TextView UserProf;
    String UserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setUserName();
        UserProf =findViewById(R.id.UserNameTextView);
        UserProf.setText(UserName);
        InboundBtn = findViewById(R.id.InboundBtn);
        OutboundBtn = findViewById(R.id.OutboundBtn);
        ReportBtn = findViewById(R.id.ReportBtn);
        BackBtn = findViewById(R.id.BackBtn);
        InboundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this,InboundActivity.class));
            }
        });
        OutboundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this,OutboundActivity.class));
            }
        });
        ReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this,ReportActivity.class));
            }
        });
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this,LoginActivity.class));
            }
        });
    }
    private void setUserName()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        UserName = preferences.getString("UserName", null);
        if(UserName==null)
        {
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }
    }
}