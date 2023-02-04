package com.example.inventoryapp.Inbound;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.inventoryapp.Unit.Unit;
import com.example.inventoryapp.User.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class Inbound {
    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getItemCode() {
        return ItemCode;
    }
    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public BigDecimal getStockToAdd() {
        return StockToAdd;
    }

    public void setStockToAdd(BigDecimal stockToAdd) {
        StockToAdd = stockToAdd;
    }

    public BigDecimal getStockAvailable() {
        return StockAvailable;
    }

    public void setStockAvailable(BigDecimal stockAvailable) {
        StockAvailable = stockAvailable;
    }

    public BigDecimal getStockAfterInbound() {
        return StockAfterInbound;
    }

    public void setStockAfterInbound(BigDecimal stockAfterInbound) {
        StockAfterInbound = stockAfterInbound;
    }

    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String unitName) {
        UnitName = unitName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getRequestedBy() {
        return RequestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        RequestedBy = requestedBy;
    }

    public String getRequisitionRef() {
        return RequisitionRef;
    }

    public void setRequisitionRef(String requisitionRef) {
        RequisitionRef = requisitionRef;
    }

    public String getChallenNo() {
        return ChallenNo;
    }

    public void setChallenNo(String challenNo) {
        ChallenNo = challenNo;
    }

    public String getVendorName() {
        return VendorName;
    }

    public void setVendorName(String vendorName) {
        VendorName = vendorName;
    }

    public Timestamp getDateOfEntry() {
        return DateOfEntry;
    }

    public void setDateOfEntry(Timestamp dateOfEntry) {
        DateOfEntry = dateOfEntry;
    }

    public BigDecimal getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        TotalPrice = totalPrice;
    }

    public Timestamp getInboundDate() {
        return InboundDate;
    }

    public void setInboundDate(Timestamp inboundDate) {
        InboundDate = inboundDate;
    }

    public String getInforInboundNo() {
        return InforInboundNo;
    }

    public void setInforInboundNo(String inforInboundNo) {
        InforInboundNo = inforInboundNo;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    private long Id;
    private String ItemCode;
    private String Description;
    private BigDecimal StockToAdd;
    private BigDecimal StockAvailable;
    private BigDecimal StockAfterInbound;


    //private int UnitId;
    //private Unit Unit;
    private String UnitName;
    //private int UserId;
    private String UserName;
    //private User User;

    private String RequestedBy;
    private String RequisitionRef;
    private String ChallenNo;
    private String VendorName;
    private Timestamp DateOfEntry;
    private BigDecimal TotalPrice;
    private Timestamp InboundDate;
    private String InforInboundNo;
    private String Remark ;

    public static String fromObjToJsonStr(Inbound inbound)
    {
        Gson gson = new Gson();
        String inboundJsonStr = gson.toJson(inbound);
        return inboundJsonStr;
    }

    public static Inbound fromJsonStrToObj(String JsonStr)
    {
        //Gson gson = new Gson();
        Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class, new DotNetDateDeserializer()).create();
        //Type listType = new TypeToken<ArrayList<Inbound>>(){}.getType();
        Inbound inbound = gson.fromJson(JsonStr, Inbound.class);
        return  inbound;
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

