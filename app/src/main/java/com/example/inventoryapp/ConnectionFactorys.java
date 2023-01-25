package com.example.inventoryapp;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionFactorys {

    // Declaring Server ip, username, database name and password
    public static final String SERVER = "172.30.54.85:1436";//"172.20.28.141";
    public static final String USER = "sa_remote";
    public static final String PASS = "godrej@123";
    public static final String DATABASE = "SSDInventory";
    public static final String ConnectionURL = "jdbc:jtds:sqlserver://"+SERVER+";user="+USER+";password="+PASS+";databaseName="+DATABASE+";useCursorsAlways=true;IntegratedSecurity=true;authenticationScheme=NTLM;";
    public static final String SQLdriver = "net.sourceforge.jtds.jdbc.Driver";

    @SuppressLint("NewApi")
    public static Connection getConnection() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        try
        {
            Class.forName(SQLdriver).newInstance();
            connection = DriverManager.getConnection(ConnectionURL);
        }
        catch (SQLException se)
        {
            Log.e("error here 1 : ", se.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            Log.e("error here 2 : ", e.getMessage());
        }
        catch (Exception e)
        {
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;


    }
}
