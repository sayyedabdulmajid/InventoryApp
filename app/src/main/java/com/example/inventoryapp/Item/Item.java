package com.example.inventoryapp.Item;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.inventoryapp.Activity.InboundActivity;
import com.example.inventoryapp.HTTPSClass;
import com.example.inventoryapp.Inbound.Inbound;
import com.example.inventoryapp.Unit.Unit;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import net.sourceforge.jtds.jdbc.DateTime;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Item {
    public  static final String TableName = "Items";

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getShortDescription() {
        return ShortDescription;
    }

    public void setShortDescription(String shortDescription) {
        ShortDescription = shortDescription;
    }

    public String getLongDescription() {
        return LongDescription;
    }

    public void setLongDescription(String longDescription) {
        LongDescription = longDescription;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public Timestamp getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        CreatedOn = createdOn;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public Timestamp getUpdatedOn() {
        return UpdatedOn;
    }

    public void setUpdatedOn(Timestamp updatedOn) {
        UpdatedOn = updatedOn;
    }

    public String getUpdatedBy() {
        return UpdatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        UpdatedBy = updatedBy;
    }

    public BigDecimal getAvailableStock() {
        return AvailableStock;
    }

    public void setAvailableStock(BigDecimal availableStock) {
        AvailableStock = availableStock;
    }

    public BigDecimal getReOrderLevel() {
        return ReOrderLevel;
    }

    public void setReOrderLevel(BigDecimal reOrderLevel) {
        ReOrderLevel = reOrderLevel;
    }

    public BigDecimal getMinStockLevel() {
        return MinStockLevel;
    }

    public void setMinStockLevel(BigDecimal minStockLevel) {
        MinStockLevel = minStockLevel;
    }

    public int getUnitId() {
        return UnitId;
    }

    public void setUnitId(int unitId) {
        UnitId = unitId;
    }

    public BigDecimal getPrice() {
        return Price;
    }

    public void setPrice(BigDecimal price) {
        Price = price;
    }

    public boolean isDisable() {
        return Disable;
    }

    public void setDisable(boolean disable) {
        Disable = disable;
    }

    public static class Column_Name {
        public static final String ItemCode = "ItemCode";
        public static final String ShortDescription = "ShortDescription";
        public static final String LongDescription = "LongDescription";
        public static final String Location = "Location";
        public static final String CreatedOn = "CreatedOn";
        public static final String UpdatedOn = "UpdatedOn";
        public static final String CreatedBy = "CreatedBy";
        public static final String UpdatedBy = "UpdatedBy";
        public static final String AvailableStock = "AvailableStock";
        public static final String ReOrderLevel = "ReOrderLevel";
        public static final String MinStockLevel = "MinStockLevel";
        public static final String UnitId = "UnitId";
        public static final String Price = "Price";
        public static final String Disable = "Disable";
    }
    private String ItemCode;
    private String ShortDescription;
    private String LongDescription;
    private String Location;
    private Timestamp CreatedOn;
    private Timestamp UpdatedOn;
    private String CreatedBy;
    private String UpdatedBy;
    private BigDecimal AvailableStock;
    private BigDecimal ReOrderLevel;
    private BigDecimal MinStockLevel;
    private int UnitId;
    private Unit unit;
    private BigDecimal Price;
    private boolean Disable;

    public static Item fromJson(JSONObject jsonObject) throws JSONException {
        Item item = new Item();
        item.setItemCode( jsonObject.getString(Item.Column_Name.ItemCode) );
        item.setShortDescription( jsonObject.getString(Item.Column_Name.ShortDescription) );
        item.setLongDescription( jsonObject.getString(Item.Column_Name.LongDescription) );
        item.setLocation( jsonObject.getString(Item.Column_Name.Location) );
        //Here Timestamp received from Https is "CreatedOn": "/Date(1675508709323)/",
        item.setCreatedOn(parseTimestamp(jsonObject.getString(Item.Column_Name.CreatedOn)));
        item.setUpdatedOn(parseTimestamp(jsonObject.getString(Item.Column_Name.UpdatedOn)));//Timestamp.valueOf(jsonObject.getString(Item.Column_Name.UpdatedOn))
        item.setCreatedBy( jsonObject.getString(Item.Column_Name.CreatedBy) );
        item.setUpdatedBy( jsonObject.getString(Item.Column_Name.UpdatedBy) );
        item.setAvailableStock(new BigDecimal(jsonObject.getString(Item.Column_Name.AvailableStock)));
        item.setReOrderLevel(new BigDecimal(jsonObject.getString(Item.Column_Name.ReOrderLevel)));
        item.setMinStockLevel(new BigDecimal(jsonObject.getString(Item.Column_Name.MinStockLevel)));
        item.setUnitId( jsonObject.getInt(Item.Column_Name.UnitId) );
        item.setPrice(new BigDecimal(jsonObject.getString(Item.Column_Name.Price)));
        item.setDisable( jsonObject.getBoolean(Item.Column_Name.Disable) );
        return item;
    }

    //Here Timestamp received from Https is "CreatedOn": "/Date(1675508709323)/",
    // to convert to TimestampObject
    // remove the /Date(...) part of the string and parse the remaining value as a long,
    // which represents the number of milliseconds since the Unix epoch.
    // The resulting value is then used to construct a Date object
    private static Timestamp parseTimestamp(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        try {
            int startIndex = value.indexOf("(") + 1;
            int endIndex = value.indexOf(")");
            String timestampValue = value.substring(startIndex, endIndex);
            long timeInMilliseconds = Long.parseLong(timestampValue);
            return new Timestamp(timeInMilliseconds);
        } catch (Exception e) {
            return null;
        }
    }

    public static String fromObjToJsonStr(Item item)
    {
        Gson gson = new Gson();
        String itemJsonStr = gson.toJson(item);
        return itemJsonStr;
    }

    public static Item fromJsonStrToObj(String JsonStr)
    {
        Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class, new Item.DotNetDateDeserializer()).create();
        Item item = gson.fromJson(JsonStr, Item.class);
        return  item;
    }

    private static class DotNetDateDeserializer implements JsonDeserializer<Timestamp> {
        @Override
        public Timestamp deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            String dateString = jsonElement.getAsString();
            dateString = dateString.replace("/Date(", "").replace(")/", "");
            long milliseconds = Long.parseLong(dateString);
            return new Timestamp(milliseconds);
        }
    }
    public static Map<String, String> classToMap(Object object) {
        Map<String, String> map = new HashMap<>();
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(object).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

}





//
//    String jsonString = "{\"timestamp\":\"2022-12-01 12:00:00\",\"datetime\":\"2022-12-01\",\"bigdecimal\":\"12345.6789\"}";
//
//    JSONObject jsonObject = new JSONObject(jsonString);
//    Timestamp timestamp = Timestamp.valueOf(jsonObject.getString("timestamp"));
//    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//    java.util.Date parsedDate = dateFormat.parse(jsonObject.getString("datetime"));
//    java.sql.Date datetime = new java.sql.Date(parsedDate.getTime());
//    BigDecimal bigdecimal = new BigDecimal(jsonObject.getString("bigdecimal"));
//
//    System.out.println("Timestamp: " + timestamp);
//            System.out.println("Datetime: " + datetime);
//            System.out.println("BigDecimal: " + bigdecimal);
//            }