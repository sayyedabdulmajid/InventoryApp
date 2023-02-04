package com.example.inventoryapp;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AutoComplete {
    private Context context;
    private AutoCompleteTextView autoCompleteTextView;
    private List<String> values;
    private List<JSONObject> objects;
    private ArrayAdapter<String> adapter;

    public AutoComplete(Context context, AutoCompleteTextView autoCompleteTextView) {
        this.context = context;
        this.autoCompleteTextView = autoCompleteTextView;
        this.values = new ArrayList<>();
        this.objects = new ArrayList<>();
    }

    public void setJsonArray(JSONArray jsonArray) {
        objects.clear();
        values.clear();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                objects.add(object);
                String str = object.getString("ShortDescription") + " | " + object.getString("ItemCode");
                values.add(str);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void notify(JSONArray jsonArray)
    {
        setJsonArray(jsonArray);
        adapter.notifyDataSetChanged();
    }
    public void setAdapter() {
        adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_dropdown_item_1line, values);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(3);
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            JSONObject object = objects.get(position);
            try {
                autoCompleteTextView.setText(object.getString("ItemCode"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
    }
}