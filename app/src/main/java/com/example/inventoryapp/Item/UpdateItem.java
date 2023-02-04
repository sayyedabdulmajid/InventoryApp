package com.example.inventoryapp.Item;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.inventoryapp.HTTPSClass;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class UpdateItem extends AsyncTask<String, Void, String> {
    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private Item item;
    private String url="https://172.30.54.85/Items/GetItemByItemCode";
    private Context context;

    public UpdateItem(Context context,Item item) {
        this.item = item;
        this.context=context;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            Map<String,String> dataToPost = new HashMap<String,String>();
            dataToPost.put("ItemCode",item.getItemCode());
            HTTPSClass httpsClass = new HTTPSClass(context);
            String ItemJsonFormatStr = httpsClass.postData(url, dataToPost);
            return ItemJsonFormatStr;
        } catch (Exception e) {
            Log.e("", "Error sending search data", e);
            return null;
        }
    }
    @Override
    protected void onPostExecute(String result) {
        try {
            if(result!=null)
            {
                item = Item.fromJsonStrToObj(result);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}