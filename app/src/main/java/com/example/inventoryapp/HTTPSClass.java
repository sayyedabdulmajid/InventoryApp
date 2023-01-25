package com.example.inventoryapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

public class HTTPSClass {
//    private static final String CERT_FILE = "servercrt.crt";
//    private static final String CERT_TYPE = "X.509";
//    private static KeyStore keyStore;
//    private static SSLSocketFactory sslSocketFactory;
//    private static HttpsURLConnection urlConnection;
    private Context context;
    public HTTPSClass(Context context)
    {
        this.context = context;
    }
    HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            boolean st = hostname.equals(session.getPeerHost());
            Log.d("Common verifier", Boolean.toString(st)+" session.getPeerHost():"+session.getPeerHost());
            return hostname.equals(session.getPeerHost());
        }
    };
    @SuppressLint("SdCardPath")
    public  HttpsURLConnection setUpHttpsConnection(String urlString) {
        try {
            // Load CAs from an InputStream
            // (could be from a resource or ByteArrayInputStream or ...)
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = new BufferedInputStream(context.getResources().openRawResource(R.raw.servercrt));
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
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, tmf.getTrustManagers(), null);


            // Tell the URLConnection to use a SocketFactory from our SSLContext
            URL url = new URL(urlString);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setSSLSocketFactory(sslcontext.getSocketFactory());
            Log.e("setUpHttpsConnection", "Connection Established ");

            return urlConnection;
        } catch (Exception ex) {
            Log.e("setUpHttpsConnection", "Failed to establish SSL connection to server: " + ex.toString());
            return null;
        }
    }


    public void postData(String url, Map<String, String> params) throws IOException {
        HttpsURLConnection conn = setUpHttpsConnection(url);
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, String> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }

        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        //conn.setChunkedStreamingMode(0);
        conn.setHostnameVerifier(hostnameVerifier);

        OutputStream out = new BufferedOutputStream(conn.getOutputStream());
        out.write(postDataBytes);
        out.flush();
        out.close();

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            String response = readStream(conn.getInputStream());
            Log.e("postData:", response);
        } else {
            Log.e("postData:" , conn.getResponseMessage());
        }
    }

    public String getData(String url) throws IOException {
        HttpsURLConnection conn = setUpHttpsConnection(url);
        conn.setReadTimeout(15000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        //conn.setChunkedStreamingMode(0);
        conn.setHostnameVerifier(hostnameVerifier);
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            String response = readStream(conn.getInputStream());
            Log.e("getData:", "Server Response: " + response);
            return response;
        } else {
            Log.e("getData:", "Server Response: " + conn.getResponseMessage());
            return "";
        }
    }
    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }

}