package com.example.inventoryapp.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventoryapp.AutoComplete;
import com.example.inventoryapp.HTTPSClass;
import com.example.inventoryapp.Inbound.Inbound;
import com.example.inventoryapp.Item.Item;
import com.example.inventoryapp.R;
import com.example.inventoryapp.User.User;
import com.example.inventoryapp.Utility;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InboundActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener {


    private EditText InboundQtyEditText,CurrentStockEditTxt,TotalPriceEditTxt;
    private EditText InvoiceNumberEditTxt,InboundOrderEditTxt,DescriptionEditTxt;
    private EditText UnitEditTxt,NewStockEditTxt, RequisitionNumEditTxt,SupplierEditTxt,RemarkMultiEditTxt;
    private Spinner IndenterSpinner;
    private DatePickerDialog DatePickerDialog;
    private Button DateButton;
    TextView ItemCodeTxtBox;
    AutoCompleteTextView atv,atv2;
    TextView UserProf;

    Item ItemReceivedFromUrl;
    Inbound InboundItem = new Inbound();
    AutoComplete atc;
    private ArrayAdapter<String> adapterAutoComplete;
    private List<String> searchResults;
    String[] days = new String[]{"19070125078", "190701250708977", "190701255477", "190701276890", "19070154775", "1907012322323", "1907012244"};
    String UserName;

    Button InboundScanBtn;

    String ItemJsonFormatStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbound);
        setUserName();
        UserProf =findViewById(R.id.IUserNameTextView);
        UserProf.setText(UserName);

        setAllVariableWithUIElement();
        InboundScanBtn =findViewById(R.id.IInboundScanButton);
        InboundScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IntentIntegrator intentIntegrator =new IntentIntegrator(InboundActivity.this);
                intentIntegrator.setPrompt("Scan QR Code");
                intentIntegrator.initiateScan();
            }
        });

        searchResults = new ArrayList<>();
        atv = findViewById(R.id.SearchBoxAutoTxtView2);
        atv2 = findViewById(R.id.ISearchBoxAutoTxtView);
        atc = new AutoComplete(this, atv2);
        atc.setAdapter();
        adapterAutoComplete = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, searchResults);
        atv.setAdapter(adapterAutoComplete);
        atv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 3) {
                    sendSearchData(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        atv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchResults != null && !searchResults.isEmpty()) {
                    atv.showDropDown();
                }
            }
        });

        initDatePicker();
        DateButton = findViewById(R.id.IDatePickerInboundDate);
        DateButton.setText(getTodaysDate());

        ImageButton backButton=findViewById(R.id.IImageButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InboundActivity.this,MenuActivity.class));
            }
        });

        IndenterSpinner = findViewById(R.id.ISpinnerIndenter);
        new NetworkFetchSpinnerData().execute();

        Button upd1=findViewById(R.id.IUpdateInboundBtn);
        upd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if(areAllFieldsFilled()){
                        InboundItem.setStockToAdd(BigDecimal.valueOf(Double.valueOf(InboundQtyEditText.getText().toString().trim())));
                        InboundItem.setStockAvailable(BigDecimal.valueOf(Double.valueOf(CurrentStockEditTxt.getText().toString().trim())));
                        InboundItem.setStockAfterInbound(BigDecimal.valueOf(Double.valueOf(NewStockEditTxt.getText().toString())));
                        InboundItem.setTotalPrice(BigDecimal.valueOf(Double.valueOf(TotalPriceEditTxt.getText().toString())));

                        InboundItem.setItemCode(ItemCodeTxtBox.getText().toString());
                        InboundItem.setDescription(DescriptionEditTxt.getText().toString());
                        InboundItem.setUnitName(UnitEditTxt.getText().toString());

                        InboundItem.setUserName("Majid");
                        InboundItem.setRequestedBy(IndenterSpinner.getSelectedItem().toString());

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        String timestamp = formatter.format(new Date());
                        Timestamp ts = Timestamp.valueOf(timestamp);

                        InboundItem.setInboundDate(ts);

                        InboundItem.setChallenNo(InvoiceNumberEditTxt.getText().toString());
                        InboundItem.setInforInboundNo(InboundOrderEditTxt.getText().toString());
                        InboundItem.setRequisitionRef(RequisitionNumEditTxt.getText().toString());
                        InboundItem.setVendorName(SupplierEditTxt.getText().toString());
                        InboundItem.setRemark(RemarkMultiEditTxt.getText().toString());
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    HTTPSClass httpsClass = new HTTPSClass(InboundActivity.this);
                                    String url = "https://172.30.54.85/Inbounds/SaveData";
                                    String response = httpsClass.postData(url, Inbound.classToMap(InboundItem));
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                if(response != null && response.equals("{\"success\":true}"))
                                                {
                                                    ItemReceivedFromUrl = Item.fromJsonStrToObj(ItemJsonFormatStr);
                                                    ClearAllUIElementText();
                                                    Toast.makeText(InboundActivity.this, "Update Success", Toast.LENGTH_LONG).show();
                                                }
                                                else
                                                {
                                                    Toast.makeText(InboundActivity.this, "Update Failed", Toast.LENGTH_LONG).show();
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
    }

    private boolean areAllFieldsFilled() throws Exception {
        if (ItemCodeTxtBox.getText().toString().trim().isEmpty()) {
            ItemCodeTxtBox.setError("Item Code is required");
            return false;
        } else if (InboundQtyEditText.getText().toString().trim().isEmpty()) {
            InboundQtyEditText.setError("Inbound Qty is required and cannot be zero");
            return false;
        }else if (Float.parseFloat(InboundQtyEditText.getText().toString().trim())==0.0) {
            InboundQtyEditText.setError("Inbound Qty is required and cannot be zero");
            return false;
        }  else if (CurrentStockEditTxt.getText().toString().trim().isEmpty()) {
            CurrentStockEditTxt.setError("Current Stock is required and cannot be zero");
            return false;
        }else if (Float.parseFloat(CurrentStockEditTxt.getText().toString().trim())==0.0) {
            CurrentStockEditTxt.setError("Current Stock is required and cannot be zero");
            return false;
        }else if (TotalPriceEditTxt.getText().toString().trim().isEmpty()) {
            TotalPriceEditTxt.setError("Total price is required and cannot be zero");
            return false;
        }else if (Float.parseFloat(TotalPriceEditTxt.getText().toString().trim())==0.0) {
            TotalPriceEditTxt.setError("Total price is required and cannot be zero");
            return false;
        }else if (IndenterSpinner.getSelectedItem().toString().trim().isEmpty()) {
            Toast.makeText(this, "Intender is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (InvoiceNumberEditTxt.getText().toString().trim().isEmpty()) {
            InvoiceNumberEditTxt.setError("Invoice Number is required");
            return false;
        } else if (InboundOrderEditTxt.getText().toString().trim().isEmpty()) {
            InboundOrderEditTxt.setError("Inbound order is required");
            return false;
        } else if (DateButton.getText().toString().trim().isEmpty()) {
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
        }else if (RequisitionNumEditTxt.getText().toString().trim().isEmpty()) {
            RequisitionNumEditTxt.setError("Stock After Inbound is required");
            return false;
        }else if (SupplierEditTxt.getText().toString().trim().isEmpty()) {
            SupplierEditTxt.setError("Stock After Inbound is required");
            return false;
        }else if (RemarkMultiEditTxt.getText().toString().trim().isEmpty()) {
            RemarkMultiEditTxt.setError("Stock After Inbound is required");
            return false;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        Toast.makeText(InboundActivity.this, "Inbound Updated", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String choice=parent.getItemAtPosition(position).toString();
        Toast.makeText(getApplicationContext(),choice,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getApplicationContext(),"Nothing Selected",Toast.LENGTH_LONG).show();
    }

    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                DateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        DatePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }
    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    public void openDatePicker(View view)
    {
        DatePickerDialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult intentResult =IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (intentResult!=null){
            if (intentResult.getContents()==null){
                Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_SHORT).show();
            }else{
                String itemCode = intentResult.getContents().trim();
                getItemCodeDetails(itemCode);
                //ItemCodeTxtBox.setText(itemCode);
            }

        }else{
            super.onActivityResult(requestCode,resultCode,data);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private class NetworkFetchSpinnerData extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            String[] spinnerDataArray = null;
            try {
                HTTPSClass httpsClass = new HTTPSClass(InboundActivity.this);
                String url = getString(R.string.UserIndentorListLink);
                String DataAsJsonArrayStr = httpsClass.postData(url);
                spinnerDataArray = Utility.ConvJSONArrayPropToStrArray(DataAsJsonArrayStr,"UserName");
                Log.d("response list", spinnerDataArray.toString());
            } catch (IOException e) {
                Log.e("Error", e.toString());
                ///Toast.makeText(InboundActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
            return spinnerDataArray;
        }

        @Override
        protected void onPostExecute(String[] spinnerData) {
            if(spinnerData.length!=0)
            {
                spinnerData = Utility.InsertStrInStrArrayAtPos(spinnerData,"",0);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(InboundActivity.this, android.R.layout.simple_spinner_item, spinnerData);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                IndenterSpinner.setAdapter(adapter);
            }

        }
    }

    private void sendSearchData(final String searchData) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String,String> dataToPost = new HashMap<String,String>();
                    dataToPost.put("term",searchData);
                    HTTPSClass httpsClass = new HTTPSClass(InboundActivity.this);
                    String url = "https://172.30.54.85/Items/GetItemCodeByKeyword";
                    String dataAsJsonArrayStr = httpsClass.postData(url, dataToPost);
                    searchResults = Utility.ConvJSONArrayPropToStrList(dataAsJsonArrayStr,"ItemCode");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (searchResults == null) {
                                searchResults = new ArrayList<>();
                            }
                            try {
                                atc.notify(new JSONArray(dataAsJsonArrayStr));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            adapterAutoComplete.notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    Log.e("MainActivity", "Error sending search data", e);
                }
            }
        });
    }
    private void getItemCodeDetails(final String ItemCodeStr) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String,String> dataToPost = new HashMap<String,String>();
                    dataToPost.put("ItemCode",ItemCodeStr);
                    dataToPost.put("UserName",UserName);
                    HTTPSClass httpsClass = new HTTPSClass(InboundActivity.this);
                    String url = "https://172.30.54.85/Inbounds/GetInboundBlank";
                    String ItemJsonFormatStr = httpsClass.postData(url, dataToPost);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if(ItemJsonFormatStr!=null)
                                {
                                    InboundItem = Inbound.fromJsonStrToObj(ItemJsonFormatStr);
                                    //InboundItem.setStockAvailable(InboundItem.getStockAvailable());
                                    ItemCodeTxtBox.setText ( InboundItem.getItemCode() );
                                    UnitEditTxt.setText(InboundItem.getUnitName() );
                                    CurrentStockEditTxt.setText( InboundItem.getStockAvailable().toString() );
                                    DescriptionEditTxt.setText(InboundItem.getDescription());
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
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Map<String,String> dataToPost = new HashMap<String,String>();
//                    dataToPost.put("ItemCode",ItemCodeStr);
//                    HTTPSClass httpsClass = new HTTPSClass(InboundActivity.this);
//                    String url = "https://172.30.54.85/Items/GetItemByItemCode";
//                    String ItemJsonFormatStr = httpsClass.postData(url, dataToPost);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                if(ItemJsonFormatStr!=null)
//                                {
//                                    ItemReceivedFromUrl = Item.fromJsonStrToObj(ItemJsonFormatStr);
//                                }
//                            } catch (Exception e) {
//                                throw new RuntimeException(e);
//                            }
//                        }
//                    });
//                } catch (Exception e) {
//                    Log.e("InboundActivity", "Error sending itemcode to Item Controller", e);
//                }
//            }
//        });
    }

    private void setAllVariableWithUIElement()
    {
        ItemCodeTxtBox =findViewById(R.id.IItemCodeEditTxt);
        InboundQtyEditText =findViewById(R.id.IInboundQtyEditText);
        InboundQtyEditText.addTextChangedListener(new TextWatcher() {
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
        InboundQtyEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                CalculateNewStockValue();
            }
        });
        CurrentStockEditTxt =findViewById(R.id.ICurrentStockEditTxt);
        TotalPriceEditTxt =findViewById(R.id.ITotalPriceEditTxt);

        InvoiceNumberEditTxt =findViewById(R.id.IInvoiceNumberEditTxt);
        InboundOrderEditTxt =findViewById(R.id.IInboundOrderEditTxt);
        DescriptionEditTxt =findViewById(R.id.IDescriptionEditTxt);
        UnitEditTxt =findViewById(R.id.IUnitEditTxt);

        NewStockEditTxt =findViewById(R.id.INewStockEditTxt);
        RequisitionNumEditTxt =findViewById(R.id.IRequisitionNumEditTxt);
        SupplierEditTxt =findViewById(R.id.ISupplierEditTxt);
        RemarkMultiEditTxt =findViewById(R.id.IRemarkMultiEditTxt);

    }

    private void setUserName()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        UserName = preferences.getString("UserName", null);
        if(UserName==null)
        {
            startActivity(new Intent(InboundActivity.this,LoginActivity.class));
            finish();
        }
    }
    private void CalculateNewStockValue()
    {
        try
        {
            String currentStock = CurrentStockEditTxt.getText().toString().trim();
            String inboundQty = InboundQtyEditText.getText().toString().trim();

            if (!currentStock.isEmpty() && !inboundQty.isEmpty() &&
                    Float.parseFloat(currentStock) != 0.0 && Float.parseFloat(inboundQty) != 0.0) {
                float iq = Float.parseFloat(inboundQty);
                float cs = Float.parseFloat(currentStock);
                float ns = iq + cs;
                NewStockEditTxt.setText(String.valueOf(ns));
//                if(ItemReceivedFromUrl!=null)
//                {
//                    float totalPrice = iq * Float.parseFloat(ItemReceivedFromUrl.getPrice().toString());
//                    TotalPriceEditTxt.setText(String.valueOf(totalPrice));
//                }
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
        InboundQtyEditText.setText("");
        CurrentStockEditTxt.setText("");
        TotalPriceEditTxt.setText("");
        InvoiceNumberEditTxt.setText("");
        InboundOrderEditTxt.setText("");
        DescriptionEditTxt.setText("");
        UnitEditTxt.setText("");
        NewStockEditTxt.setText("");
        RequisitionNumEditTxt.setText("");
        SupplierEditTxt.setText("");
        RemarkMultiEditTxt.setText("");
        IndenterSpinner.setSelection(0);
        ClearAllUIElementError();
    }
    private void ClearAllUIElementError()
    {
        ItemCodeTxtBox.setError(null);
        InboundQtyEditText.setError(null);
        CurrentStockEditTxt.setError(null);
        NewStockEditTxt.setError(null);
        TotalPriceEditTxt.setError(null);
        InvoiceNumberEditTxt.setError(null);
        DateButton.setError(null);
        DescriptionEditTxt.setError(null);
        UnitEditTxt.setError(null);
        RequisitionNumEditTxt.setError(null);
        SupplierEditTxt.setError(null);
        RemarkMultiEditTxt.setError(null);
    }

}