package com.example.inventoryapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Utility {

    public static String[] ConvJSONObjPropToStrArray(String jsonString, String PropertyName) {
        //jsonString = "{\"property1\":\"value1\",\"property2\":[\"string1\",\"string2\",\"string3\"]}";
        //PropertyName = "property2";
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray property2 = jsonObject.getJSONArray(PropertyName);
            String[] property2Array = new String[property2.length()];
            for (int i = 0; i < property2.length(); i++) {
                property2Array[i] = property2.getString(i);
            }
            return property2Array;
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
    public static String[] ConvJSONArrayPropToStrArray(String JsonArrayStr, String PropertyName) {
        //String input = "[{\"id\":1,\"name\":\"John\"},{\"id\":2,\"name\":\"Jane\"}]";

        try {
            if(JsonArrayStr== null || PropertyName ==null)
            {
                return null;
            }
            JSONArray jsonArray = new JSONArray(JsonArrayStr);
            String[] propertyArray = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                propertyArray[i] = jsonObject.getString(PropertyName);
            }
            return propertyArray;
        } catch(JSONException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public static String[] InsertStrInStrArrayAtPos(String[] originalArray, String StrToInsert, int position){
        String[] newArray = new String[originalArray.length + 1];
        for (int i = 0, j = 0; i < newArray.length; i++) {
            if (i == position) {
                newArray[i] = StrToInsert;
            } else {
                newArray[i] = originalArray[j++];
            }
        }
        return newArray;
    }
    public static List<String> ConvJSONArrayPropToStrList(String JsonArrayStr, String PropertyName) {
        //String input = "[{\"id\":1,\"name\":\"John\"},{\"id\":2,\"name\":\"Jane\"}]";

        try {
            JSONArray jsonArray = new JSONArray(JsonArrayStr);
            List<String> propertyArray = new ArrayList<>();;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                propertyArray.add(jsonObject.getString(PropertyName));
            }
            return propertyArray;
        } catch(JSONException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
