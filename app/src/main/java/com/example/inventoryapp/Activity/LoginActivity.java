package com.example.inventoryapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inventoryapp.HTTPSClass;
import com.example.inventoryapp.R;
import com.example.inventoryapp.User.DaoUser;
import com.example.inventoryapp.User.DbHelperUser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

public class LoginActivity extends AppCompatActivity {

    String username,password;
    EditText etUserName,etPassword;
    Button btnLogin;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        progressBar = (ProgressBar) findViewById(R.id.spin_kit);

        progressBar.setVisibility(View.GONE);
        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                username = etUserName.getText().toString();
                password = etPassword.getText().toString();
                LoginActivity.CheckLogin checkLogin = new LoginActivity.CheckLogin();
                checkLogin.execute("");
            }
        });
    }
    public class CheckLogin extends AsyncTask<String,String,String> {
        String z = "";
        Boolean isSuccess = false;
        Intent intent;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(LoginActivity.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess) {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("UserName", username.toString());
                editor.apply();

                intent = new Intent(LoginActivity.this, MenuActivity.class);
                intent.putExtra("UserName",username.toString());
                startActivity(intent);
                finish();
            }

        }


        @Override
        protected String doInBackground(String... params) {
            if (username.trim().equals("") || password.trim().equals(""))
                z = "Please enter Username and Password";
            else {
                try {

                    Map<String,String> UserDetails = new HashMap<String,String>();
                    UserDetails.put("UserName",username.toString());
                    UserDetails.put("Password",password.toString());
                    HTTPSClass HTTPSClass = new HTTPSClass(LoginActivity.this);
                    String httpLink = getString(R.string.UserVerifyLink);
                    if (!HTTPSClass.postData(httpLink,UserDetails).isEmpty()) {
                        z = "Login successful";
                        isSuccess = true;
                    } else {
                        z = "Invalid Credentials!";
                        isSuccess = false;
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = ex.getMessage();
                }
            }
            return z;
        }
    }
}