package com.example.inventoryapp.Outbound;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class Outbound {

    private long Id;
    private String ItemCode;
    private BigDecimal StockToRemove;
    private BigDecimal StockAvailable;
    private BigDecimal StockAfterOutbound ;
    private String UnitName ;
    private String UserName;
    //private String AuthorisedBy;
    //private String UsedBy;
    private String DepartmentName;
    //private String Purpose;
    private Timestamp DateOfEntry;
    private BigDecimal TotalPrice;
    private Timestamp OutboundDate;
    private String InforOutboundNo ;
    private String Remark;

    public long getId() { return Id;}
    public void setId(long id) {Id = id;}
    public String getItemCode() {return ItemCode;}
    public void setItemCode(String itemCode) {ItemCode = itemCode;}
    public BigDecimal getStockToRemove() {return StockToRemove;}
    public void setStockToRemove(BigDecimal stockToRemove) {StockToRemove = stockToRemove;}
    public BigDecimal getStockAvailable() {return StockAvailable;}
    public void setStockAvailable(BigDecimal stockAvailable) {StockAvailable = stockAvailable;}
    public BigDecimal getStockAfterOutbound() {return StockAfterOutbound;}
    public void setStockAfterOutbound(BigDecimal stockAfterOutbound) {StockAfterOutbound = stockAfterOutbound;}
    public String getUnitName() {return UnitName;}
    public void setUnitName(String unitName) {UnitName = unitName;}
    public String getUserName() {return UserName;}
    public void setUserName(String userName) {UserName = userName;}
//    public String getAuthorisedBy() {return AuthorisedBy;}
//    public void setAuthorisedBy(String authorisedBy) {AuthorisedBy = authorisedBy;}
//    public String getUsedBy() {return UsedBy;}
//    public void setUsedBy(String usedBy) {UsedBy = usedBy;}
    public String getDepartmentName() {return DepartmentName;}
    public void setDepartmentName(String departmentName) {DepartmentName = departmentName;}
//    public String getPurpose() {return Purpose;}
//    public void setPurpose(String purpose) {Purpose = purpose;}
    public Timestamp getDateOfEntry() {return DateOfEntry;}
    public void setDateOfEntry(Timestamp dateOfEntry) {DateOfEntry = dateOfEntry;}
    public BigDecimal getTotalPrice() {return TotalPrice;}
    public void setTotalPrice(BigDecimal totalPrice) {TotalPrice = totalPrice;}
    public Timestamp getOutboundDate() {return OutboundDate;}
    public void setOutboundDate(Timestamp outboundDate) {OutboundDate = outboundDate;}
    public String getInforOutboundNo() {return InforOutboundNo;}
    public void setInforOutboundNo(String inforOutboundNo) {InforOutboundNo = inforOutboundNo;}
    public String getRemark() {return Remark;}
    public void setRemark(String remark) {Remark = remark;}

    public static String fromObjToJsonStr(Outbound outbound)
    {
        Gson gson = new Gson();
        String inboundJsonStr = gson.toJson(outbound);
        return inboundJsonStr;
    }

    public static Outbound fromJsonStrToObj(String JsonStr)
    {
        //Gson gson = new Gson();
        Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class, new Outbound.DotNetDateDeserializer()).create();
        //Type listType = new TypeToken<ArrayList<Inbound>>(){}.getType();
        Outbound outbound = gson.fromJson(JsonStr, Outbound.class);
        return  outbound;
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
