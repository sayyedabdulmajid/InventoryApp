package com.example.inventoryapp.Activity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inventoryapp.HTTPSClass;
import com.example.inventoryapp.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;


public class MainActivity extends AppCompatActivity {

    Button submitbutton;
    EditText inputdata;
    public static Context context;
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        // setup Strict mode policy
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        submitbutton = (Button) findViewById(R.id.SubmitButton);
        inputdata = (EditText) findViewById(R.id.InputData);
        context = getApplicationContext();
    }

    public void StartConnectivity(View view){
        // Variable to save input from user
        String SaveInputData = inputdata.toString();
        String response;
        HttpsURLConnection  urlConnection = null;
        try {
            //urlConnection =  CertLoader.setUpHttpsConnection(MainActivity.this,"https://172.30.54.85/Users/JSONDetails/5");
            //"https://172.30.54.85");
            Map<String,String> postD = new HashMap<String,String>();
            postD.put("id","6");
            HTTPSClass HTTPSClass = new HTTPSClass(MainActivity.this);
            HTTPSClass.postData("https://172.30.54.85/Users/JSONDetails",postD);
            response = HTTPSClass.getData("https://172.30.54.85/Users/Details/5");
            Log.d("http get response",response.toString());

//
//        //Toast.makeText(context, "Button is clicked", Toast.LENGTH_SHORT).show();
//
//        HttpsURLConnection urlConnection = setUpHttpsConnection("https://172.30.54.85");
//
//        try{
//            urlConnection.setDoOutput(true);
//            urlConnection.setRequestMethod("GET");
//            urlConnection.setChunkedStreamingMode(0);
//            urlConnection.setHostnameVerifier(new HostnameVerifier() {
//                @Override
//                public boolean verify(String hostname, SSLSession session) {
//                    //return true;
//                    HostnameVerifier hv =
//                            HttpsURLConnection.getDefaultHostnameVerifier();
//                    boolean st= hv.verify("172.30.54.85", session);
//                    Log.d("verify",Boolean.toString(st));
//                    return true;
//                }
//            });
//            int responseCode = urlConnection.getResponseCode();
//            StringBuilder response = new StringBuilder();
//            if (responseCode == HttpsURLConnection.HTTP_OK) {
//                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//                String line;
//                while ((line = br.readLine()) != null) {
//                    response.append(line);
//                }
//                br.close();
//            }
//            Log.d("http response",response.toString());
//            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
//            out.write(SaveInputData.getBytes());
//            out.flush();
//            out.close();
//
//            urlConnection.connect();

//            InputStream in = new BufferedInputStream(urlConnection.getInputStream());


        }catch (IOException ex){
            Log.e(TAG,"Failed to Post OutPutStream " + ex.toString());
        }
        finally {
            //urlConnection.disconnect();
        }

    }

    @SuppressLint("SdCardPath")
    public static HttpsURLConnection setUpHttpsConnection(String urlString)
    {
        try
        {
            // Load CAs from an InputStream
            // (could be from a resource or ByteArrayInputStream or ...)
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
//MainActivity.context.getAssets().open("server.crt")
            InputStream caInput = new BufferedInputStream(MainActivity.context.getResources().openRawResource(R.raw.servercrt));
            Certificate ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Create an SSLContext that uses our TrustManager
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);


            // Tell the URLConnection to use a SocketFactory from our SSLContext
            URL url = new URL(urlString);
            HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();
            urlConnection.setSSLSocketFactory(context.getSocketFactory());
            Log.e(TAG,"Connection Established ");

            return urlConnection;
        }
        catch (Exception ex)
        {
            Log.e(TAG,"Failed to establish SSL connection to server: " + ex.toString());
            return null;
        }
    }
    // Create an HostnameVerifier that hardwires the expected hostname.
    // Note that is different than the URL's hostname:
    // example.com versus example.org
    HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            HostnameVerifier hv =
                    HttpsURLConnection.getDefaultHostnameVerifier();
            return hv.verify("172.30.54.85", session);
        }
    };
}
