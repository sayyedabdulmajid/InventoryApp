package com.example.inventoryapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inventoryapp.R;
import com.example.inventoryapp.User.DaoUser;
import com.example.inventoryapp.User.DbHelperUser;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
                intent = new Intent(LoginActivity.this, MainActivity2.class);
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
                    Map<String,String>DataToPost = new HashMap<String,String>();
                    DataToPost.put("username",username.toString());
                    DataToPost.put("password",password.toString());
                    //CertLoader.loadCertNew(LoginActivity.this);
                    //CertLoader.getData("https://172.30.54.85:80");
                    //getData("https://172.30.54.85:80");
                    //postData("https://172.30.54.85:80/Users/JSONDetails",DataToPost);
                    //postData("https://172.30.54.85:80/Users/JSONDetails", username.toString(), password.toString());
                    DaoUser IUser = new DbHelperUser();
                    if (IUser.getUserByUserNameAndPassword(username.toString(), password.toString()) != null) {
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

        private void getData(String url) {
            try {
                TrustManager[] trustAllCerts = new TrustManager[] {
                        new X509TrustManager() {
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return null;
                            }
                            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                            }
                            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                            }
                        }
                };
                SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
                HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    // Data retrieved successfully
                    InputStream inputStream = connection.getInputStream();
                    String response = convertStreamToString(inputStream);
                    Log.d("HTTP Response", response);;
                    // Do something with the response
                } else {
                    // Error occurred
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        private String convertStreamToString(InputStream inputStream) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append('\n');
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return stringBuilder.toString();
        }

        private void postData(String url, Map<String, String> data) {
            try {
                TrustManager[] trustAllCerts = new TrustManager[] {
                        new X509TrustManager() {
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return null;
                            }
                            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                            }
                            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                            }
                        }
                };
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
                HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, String> param : data.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                byte[] postDataBytes = postData.toString().getBytes("UTF-8");
                connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                OutputStream os = connection.getOutputStream();
                os.write(postDataBytes);
                os.flush();
                os.close();
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    // Data posted successfully
                } else {
                    // Error occurred
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

//        public String performPostCall(String requestURL,
//                                      String UserName, String Password) throws IOException {
//            URL url = new URL(requestURL);
//            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
//            conn.setReadTimeout(10000);
//            conn.setConnectTimeout(15000);
//            conn.setRequestMethod("POST");
//            conn.setDoInput(true);
//            conn.setDoOutput(true);
//
//            Uri.Builder builder = new Uri.Builder()
//                    .appendQueryParameter("firstParam", Password);
//            String query = builder.build().getEncodedQuery();
//
//            OutputStream os = conn.getOutputStream();
//            BufferedWriter writer = new BufferedWriter(
//                    new OutputStreamWriter(os, "UTF-8"));
//            writer.write(query);
//            writer.flush();
//            writer.close();
//            os.close();
//
//            conn.connect();
//            return "";
//        }
//        private void trustSelfSignedSSL() {
//
//
////                SSLContext ctx = SSLContext.getInstance("TLS");
////                X509TrustManager tm = new X509TrustManager() {
////                    public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException { }
////                    public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException { }
////                    public X509Certificate[] getAcceptedIssuers() { return null; }
////                };
////                ctx.init(null, new TrustManager[] { tm }, null);
////                SSLContext.setDefault(ctx);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//        private void postData(String url, Map<String, String> data) {
//            try {
//                trustSelfSignedSSL();
//                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
//                connection.setRequestMethod("POST");
//                connection.setDoOutput(true);
//                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                StringBuilder postData = new StringBuilder();
//                for (Map.Entry<String, String> param : data.entrySet()) {
//                    if (postData.length() != 0) postData.append('&');
//                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
//                    postData.append('=');
//                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
//                }
//                byte[] postDataBytes = postData.toString().getBytes("UTF-8");
//                connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
//                OutputStream os = connection.getOutputStream();
//                os.write(postDataBytes);
//                os.flush();
//                os.close();
//                int responseCode = connection.getResponseCode();
//                if (responseCode == HttpURLConnection.HTTP_OK) {
//                    // Data posted successfully
//                } else {
//                    // Error occurred
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
    }
}