package com.example.inventoryapp.Item;

import net.sourceforge.jtds.jdbc.DateTime;

import java.math.BigDecimal;
import java.sql.Timestamp;

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
        public static final String LongDescription = "ShortDescription";
        public static final String Location = "ShortDescription";
        public static final String CreatedOn = "ShortDescription";
        public static final String UpdatedOn = "ShortDescription";
        public static final String CreatedBy = "ShortDescription";
        public static final String UpdatedBy = "ShortDescription";
        public static final String AvailableStock = "ShortDescription";
        public static final String ReOrderLevel = "ShortDescription";
        public static final String MinStockLevel = "ShortDescription";
        public static final String UnitId = "ShortDescription";
        public static final String Price = "ShortDescription";
        public static final String Disable = "ShortDescription";
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
    private BigDecimal Price;
    private boolean Disable;
}
