package com.example.inventoryapp.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.inventoryapp.Item.DaoItem;
import com.example.inventoryapp.Item.DbHelperItem;
import com.example.inventoryapp.R;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;


public class MainActivity2 extends AppCompatActivity {

    Button btnScanQrCode;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        btnScanQrCode = (Button) findViewById(R.id.btnScanQrCode);
        btnScanQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
                //Toast.makeText(MainActivity.this, "button released", Toast.LENGTH_LONG).show();
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
    }

    private void scanCode()
    {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Vol up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(false);
        options.setCaptureActivity(CaptureAct.class);
        barLaucher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(),result -> {

        MainActivity2.GetItem getItem = new MainActivity2.GetItem();
        getItem.execute(result.getContents());
        getItem.cancel(true);
    });

    public class GetItem extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        Intent intent;
        @Override
        protected void onPreExecute()

        {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r)
        {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(MainActivity2.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess)
            {
                intent = new Intent(MainActivity2.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

        }
        @Override
        protected String doInBackground(String... params)
        {
            String itemCode = params[0];
            if(itemCode==null)
                z = "Item code not is NULL";
            else
            {
                try
                {
                    DaoItem IItem = new DbHelperItem();
                    if(IItem.getItem(itemCode)!=null)
                    {
                        z = "Item Code Data successfully fetched";
                        isSuccess=true;
                    }
                    else
                    {
                        z = "Invalid Itemcode!";
                        isSuccess = false;
                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    z = ex.getMessage();
                }
            }
            return z;
        }
    }
}