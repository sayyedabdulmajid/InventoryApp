package com.example.inventoryapp.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventoryapp.HTTPSClass;
import com.example.inventoryapp.Inbound.Inbound;
import com.example.inventoryapp.Item.Item;
import com.example.inventoryapp.Outbound.Outbound;
import com.example.inventoryapp.R;
import com.example.inventoryapp.UpdateTimeTask;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class OutboundActivity extends AppCompatActivity {

    Button QRScanOutboundButton,UpdateOutboundBtn;
    Button DateButton;
    ImageButton backButton;
    AutoCompleteTextView SearchBoxAutoTxtV;
    EditText ItemCodeTxtBox,QtyEditTxt,PricePerUnitEditTxt,TotalValueEditTxt,UnitEditTxt;
    EditText RemarkMultiEditTxt,DescriptionEditTxt,CurrentStockEditTxt,NewStockEditTxt,OutboundOrderEditTxt;
    TextView UserProf;
    String UserName;
    Item ItemReceivedFromUrl;
    Outbound OutboundObj;
    String ItemJsonFormatStr;
    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outbound);
        setAllVariableWithUIElement();

        setUserName();
        UserProf =findViewById(R.id.OUserNameTextView);
        UserProf.setText(UserName);

        backButton=findViewById(R.id.OBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OutboundActivity.this,MenuActivity.class));
            }
        });
        QRScanOutboundButton =findViewById(R.id.QRScanOutboundBtn);
        QRScanOutboundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IntentIntegrator intentIntegrator =new IntentIntegrator(OutboundActivity.this);
                intentIntegrator.setPrompt("Scan QR Code");
                intentIntegrator.initiateScan();
            }
        });
        UpdateOutboundBtn =findViewById(R.id.UpdateOutboundBtn);
        UpdateOutboundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(areAllFieldsFilled()){
                        OutboundObj.setStockToRemove(BigDecimal.valueOf(Double.valueOf(QtyEditTxt.getText().toString().trim())));
                        OutboundObj.setStockAvailable(BigDecimal.valueOf(Double.valueOf(CurrentStockEditTxt.getText().toString().trim())));
                        OutboundObj.setStockAfterOutbound(BigDecimal.valueOf(Double.valueOf(NewStockEditTxt.getText().toString())));
                        OutboundObj.setTotalPrice(BigDecimal.valueOf(Double.valueOf(TotalValueEditTxt.getText().toString())));

                        OutboundObj.setItemCode(ItemCodeTxtBox.getText().toString());
                        //InboundItem.setDescription(DescriptionEditTxt.getText().toString());
                        OutboundObj.setUnitName(UnitEditTxt.getText().toString());

                        OutboundObj.setUserName(UserName);

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        String timestamp = formatter.format(new Date());
                        Timestamp ts = Timestamp.valueOf(timestamp);
                        // Todo Date and Time to set from current date time instance
                        OutboundObj.setOutboundDate(ts);

                        OutboundObj.setInforOutboundNo(OutboundOrderEditTxt.getText().toString());
                        OutboundObj.setRemark(RemarkMultiEditTxt.getText().toString());
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    HTTPSClass httpsClass = new HTTPSClass(OutboundActivity.this);
                                    String url = "https://172.30.54.85/Outbounds/SaveData";
                                    String response = httpsClass.postData(url, Outbound.classToMap(OutboundObj));
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                if(response != null && response.equals("{\"success\":true}"))
                                                {
                                                    ItemReceivedFromUrl = Item.fromJsonStrToObj(ItemJsonFormatStr);
                                                    ClearAllUIElementText();
                                                    Toast.makeText(OutboundActivity.this, "Update Success", Toast.LENGTH_LONG).show();
                                                }
                                                else
                                                {
                                                    Toast.makeText(OutboundActivity.this, "Update Failed", Toast.LENGTH_LONG).show();
                                                }
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                    Log.e("InboundActivity", "Error sending itemcode to Item Controller", e);
                                }
                            }
                        });

                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        DateButton = findViewById(R.id.datePickerOutboundDate);

//        pass mHandler when no separate runnable is required.
//        mHandler = new Handler();
//        new UpdateTimeTask(DateButton, mHandler,"HH:mm:ss").execute();
//        when using internal runnable the time not getting update after some time

        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
//                new UpdateTimeTask(DateButton, null,"HH:mm:ss").execute();
//                mHandler.postDelayed(this, 1000);
                String dateTimeFormat="dd/MM/yyyy hh:mm:ss";//"yyyy-mm-dd hh:mm:ss" or "dd/MM/yyyy hh:mm:ss"
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateTimeFormat, Locale.getDefault());
                String dateTime = simpleDateFormat.format(new Date());
                DateButton.setText(dateTime);
                mHandler.postDelayed(this, 1000);
            }
        };
        mHandler.post(mRunnable);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult intentResult =IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (intentResult!=null){
            if (intentResult.getContents()==null){
                Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_SHORT).show();
            }else{
                String itemCode = intentResult.getContents().trim();
                Toast.makeText(this, itemCode, Toast.LENGTH_SHORT).show();
                getItemCodeDetails(itemCode);
                //ItemCodeTxtBox.setText(itemCode);
            }

        }else{
            super.onActivityResult(requestCode,resultCode,data);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean areAllFieldsFilled() throws Exception {
        if (ItemCodeTxtBox.getText().toString().trim().isEmpty()) {
            ItemCodeTxtBox.setError("Item Code is required");
            return false;
        } else if (QtyEditTxt.getText().toString().trim().isEmpty()) {
            QtyEditTxt.setError("Inbound Qty is required and cannot be zero");
            return false;
        }else if (Float.parseFloat(QtyEditTxt.getText().toString().trim())==0.0) {
            QtyEditTxt.setError("Inbound Qty is required and cannot be zero");
            return false;
        }  else if (CurrentStockEditTxt.getText().toString().trim().isEmpty()) {
            CurrentStockEditTxt.setError("Current Stock is required and cannot be zero");
            return false;
        }else if (Float.parseFloat(CurrentStockEditTxt.getText().toString().trim())==0.0) {
            CurrentStockEditTxt.setError("Current Stock is required and cannot be zero");
            return false;
        }else if (TotalValueEditTxt.getText().toString().trim().isEmpty()) {
            TotalValueEditTxt.setError("Total price is required and cannot be zero");
            return false;
        }else if (Float.parseFloat(TotalValueEditTxt.getText().toString().trim())==0.0) {
            TotalValueEditTxt.setError("Total price is required and cannot be zero");
            return false;
        }else if (DateButton.getText().toString().trim().isEmpty()) {
            DateButton.setError("Description is required");
            return false;
        } else if (DescriptionEditTxt.getText().toString().trim().isEmpty()) {
            DescriptionEditTxt.setError("Description is required");
            return false;
        } else if (UnitEditTxt.getText().toString().trim().isEmpty()) {
            UnitEditTxt.setError("Stock After Inbound is required");
            return false;
        } else if (NewStockEditTxt.getText().toString().trim().isEmpty()) {
            NewStockEditTxt.setError("Stock After Inbound is required");
            return false;
        }else if (OutboundOrderEditTxt.getText().toString().trim().isEmpty()) {
            OutboundOrderEditTxt.setError("Outbound order no is required");
            return false;
        }else if (RemarkMultiEditTxt.getText().toString().trim().isEmpty()) {
            RemarkMultiEditTxt.setError("Stock After Inbound is required");
            return false;
        }
        return true;
    }

    private void getItemCodeDetails(final String ItemCodeStr) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String,String> dataToPost = new HashMap<String,String>();
                    dataToPost.put("ItemCode",ItemCodeStr);
                    dataToPost.put("UserName",UserName);
                    HTTPSClass httpsClass = new HTTPSClass(OutboundActivity.this);
                    String url = "https://172.30.54.85/Outbounds/GetOutboundBlank";
                    ItemJsonFormatStr = httpsClass.postData(url, dataToPost);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if(ItemJsonFormatStr!=null)
                                {
                                    OutboundObj = Outbound.fromJsonStrToObj(ItemJsonFormatStr);
                                    ItemCodeTxtBox.setText ( OutboundObj.getItemCode() );
                                    UnitEditTxt.setText(OutboundObj.getUnitName() );
                                    CurrentStockEditTxt.setText( OutboundObj.getStockAvailable().toString() );
                                }

                                //ExtractJsonObjectToItemClass(new JSONObject(ItemJsonFormatStr));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e("InboundActivity", "Error sending itemcode to Inbound Controller", e);
                }
            }
        });
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String,String> dataToPost = new HashMap<String,String>();
                    dataToPost.put("ItemCode",ItemCodeStr);
                    HTTPSClass httpsClass = new HTTPSClass(OutboundActivity.this);
                    String url = "https://172.30.54.85/Items/GetItemByItemCode";
                    String ItemJsonFormatStr = httpsClass.postData(url, dataToPost);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if(ItemJsonFormatStr!=null)
                                {
                                    ItemReceivedFromUrl = Item.fromJsonStrToObj(ItemJsonFormatStr);
                                    PricePerUnitEditTxt.setText(ItemReceivedFromUrl.getPrice().toString());
                                    DescriptionEditTxt.setText(ItemReceivedFromUrl.getLongDescription());
                                }
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e("OutboundActivity", "Error sending itemcode to Item Controller", e);
                }
            }
        });
    }


    private void setAllVariableWithUIElement()
    {
        ItemCodeTxtBox =findViewById(R.id.OItemCodeEditTxt);
        QtyEditTxt =findViewById(R.id.OQtyEditTxt);
        QtyEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CalculateNewStockValue();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        QtyEditTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                CalculateNewStockValue();
            }
        });
        PricePerUnitEditTxt =findViewById(R.id.PricePerUnitEditTxt);
        TotalValueEditTxt =findViewById(R.id.OTotalValueEditTxt);
        UnitEditTxt =findViewById(R.id.OUnitEditTxt);
        DescriptionEditTxt =findViewById(R.id.ODescriptionEditTxt);
        CurrentStockEditTxt =findViewById(R.id.OCurrentStockEditTxt);
        NewStockEditTxt =findViewById(R.id.ONewStockEditTxt);
        OutboundOrderEditTxt =findViewById(R.id.OOutboundOrderEditTxt);
        RemarkMultiEditTxt =findViewById(R.id.ORemarkMultiEditTxt);

    }

    private void setUserName()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        UserName = preferences.getString("UserName", null);
        if(UserName==null)
        {
            startActivity(new Intent(OutboundActivity.this,LoginActivity.class));
            finish();
        }
    }
    private void CalculateNewStockValue()
    {
        try
        {
            String currentStock = CurrentStockEditTxt.getText().toString().trim();
            String outboundQty = QtyEditTxt.getText().toString().trim();

            if (!currentStock.isEmpty() && !outboundQty.isEmpty() &&
                    Float.parseFloat(currentStock) != 0.0 && Float.parseFloat(outboundQty) != 0.0
                    && Float.parseFloat(currentStock) >= Float.parseFloat(outboundQty)) {
                float oq = Float.parseFloat(outboundQty);
                float cs = Float.parseFloat(currentStock);
                float ns = cs-oq;
                NewStockEditTxt.setText(String.valueOf(ns));
                if(ItemReceivedFromUrl!=null)
                {
                    float totalPrice = oq * Float.parseFloat(ItemReceivedFromUrl.getPrice().toString());
                    TotalValueEditTxt.setText(String.valueOf(totalPrice));
                }
            } else if (!currentStock.isEmpty()) {
                NewStockEditTxt.setText(currentStock);
            }
        }catch(Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }
    private void ClearAllUIElementText()
    {
        ItemCodeTxtBox.setText("");
        QtyEditTxt.setText("");
        PricePerUnitEditTxt.setText("");
        TotalValueEditTxt.setText("");
        UnitEditTxt.setText("");
        DescriptionEditTxt.setText("");
        CurrentStockEditTxt.setText("");
        NewStockEditTxt.setText("");
        OutboundOrderEditTxt.setText("");
        RemarkMultiEditTxt.setText("");

        ClearAllUIElementError();
    }
    private void ClearAllUIElementError()
    {

        ItemCodeTxtBox.setError(null);
        QtyEditTxt.setError(null);
        PricePerUnitEditTxt.setError(null);
        TotalValueEditTxt.setError(null);
        UnitEditTxt.setError("");
        DescriptionEditTxt.setError(null);
        CurrentStockEditTxt.setError(null);
        DateButton.setError(null);
        NewStockEditTxt.setError(null);
        OutboundOrderEditTxt.setError(null);
        RemarkMultiEditTxt.setError(null);
    }

}