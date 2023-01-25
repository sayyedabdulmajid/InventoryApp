package com.example.inventoryapp.Item;

import com.example.inventoryapp.ConnectionFactorys;
import com.example.inventoryapp.User.DaoUser;
import com.example.inventoryapp.User.User;

import net.sourceforge.jtds.jdbc.DateTime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;


public class DbHelperItem implements DaoItem {
    private static final String COLUMN_ID = "ItemCode";
    private static final String COLUMN_USERNAME = "UserName";
    private static final String COLUMN_PASSWORD = "Password";
    private String SelectSingleItem =  "SELECT * FROM " + Item.TableName + " WHERE "
                                        +Item.Column_Name.ItemCode+"=?";
    private String SelectAllItem =  "SELECT * FROM " + Item.TableName ;
    private String UpdateItem =  "UPDATE "+ Item.TableName +
            " SET "+Item.Column_Name.AvailableStock+"=?"
            +"=? WHERE "+Item.Column_Name.ItemCode+"=?";
    private String DeleteItem = "DELETE FROM "+ Item.TableName +" WHERE "+Item.Column_Name.ItemCode+"=?";

    public DbHelperItem()
    {
        //createTable();
    }

    @Override
    public Item getItem(String ItemCode){
        Connection connection = ConnectionFactorys.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(SelectSingleItem);
            ps.setString(1, ItemCode);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            {
                Item item = new Item();
                item= extractItemFromResultSet(rs);
                connection.close();
                return item;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<Item> getItems() {
        Connection connection = ConnectionFactorys.getConnection();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(SelectAllItem);
            Set<Item> items = new HashSet();
            while(rs.next())
            {
                Item item = extractItemFromResultSet(rs);
                items.add(item);
            }
            return items;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    //@Override
    private boolean updateUser(Item item) {
        Connection connection = ConnectionFactorys.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(UpdateItem);
            ps.setString(1, item.getAvailableStock().toString());
            ps.setString(2, item.getItemCode());
            int i = ps.executeUpdate();
            if(i == 1) {
                connection.close();
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }
    private Item extractItemFromResultSet(ResultSet rs) throws SQLException {
        Item item = new Item();
        item.setItemCode( rs.getString(Item.Column_Name.ItemCode) );
        item.setShortDescription( rs.getString(Item.Column_Name.ShortDescription) );
        item.setLongDescription( rs.getString(Item.Column_Name.LongDescription) );
        item.setLocation( rs.getString(Item.Column_Name.Location) );
        item.setCreatedOn(rs.getTimestamp(Item.Column_Name.CreatedOn));
        item.setUpdatedOn( rs.getTimestamp(Item.Column_Name.UpdatedOn) );
        item.setCreatedBy( rs.getString(Item.Column_Name.CreatedBy) );
        item.setUpdatedBy( rs.getString(Item.Column_Name.UpdatedBy) );
        item.setAvailableStock( rs.getBigDecimal(Item.Column_Name.AvailableStock) );
        item.setReOrderLevel( rs.getBigDecimal(Item.Column_Name.ReOrderLevel) );
        item.setMinStockLevel( rs.getBigDecimal(Item.Column_Name.MinStockLevel) );
        item.setUnitId( rs.getInt(Item.Column_Name.UnitId) );
        item.setPrice( rs.getBigDecimal(Item.Column_Name.Price) );
        item.setDisable( rs.getBoolean(Item.Column_Name.Disable) );
        return item;
    }
}
/**
 private static void createTable(Statement stmt) throws SQLException {
 stmt.execute("if exists (select * from sys.objects where name = 'Product_JDBC_Sample')"
 + "drop table Product_JDBC_Sample");

 String sql = "CREATE TABLE [Product_JDBC_Sample](" + "[ProductID] [int] IDENTITY(1,1) NOT NULL,"
 + "[Name] [varchar](30) NOT NULL," + "[ProductNumber] [nvarchar](25) NOT NULL,"
 + "[MakeFlag] [bit] NOT NULL," + "[FinishedGoodsFlag] [bit] NOT NULL," + "[Color] [nvarchar](15) NULL,"
 + "[SafetyStockLevel] [smallint] NOT NULL," + "[ReorderPoint] [smallint] NOT NULL,"
 + "[StandardCost] [money] NOT NULL," + "[ListPrice] [money] NOT NULL," + "[Size] [nvarchar](5) NULL,"
 + "[SizeUnitMeasureCode] [nchar](3) NULL," + "[WeightUnitMeasureCode] [nchar](3) NULL,"
 + "[Weight] [decimal](8, 2) NULL," + "[DaysToManufacture] [int] NOT NULL,"
 + "[ProductLine] [nchar](2) NULL," + "[Class] [nchar](2) NULL," + "[Style] [nchar](2) NULL,"
 + "[ProductSubcategoryID] [int] NULL," + "[ProductModelID] [int] NULL,"
 + "[SellStartDate] [datetime] NOT NULL," + "[SellEndDate] [datetime] NULL,"
 + "[DiscontinuedDate] [datetime] NULL," + "[rowguid] [uniqueidentifier] ROWGUIDCOL  NOT NULL,"
 + "[ModifiedDate] [datetime] NOT NULL,)";

 stmt.execute(sql);

 sql = "INSERT Product_JDBC_Sample VALUES ('Adjustable Time','AR-5381','0','0',NULL,'1000','750','0.00','0.00',NULL,NULL,NULL,NULL,'0',NULL,NULL,NULL,NULL,NULL,'2008-04-30 00:00:00.000',NULL,NULL,'694215B7-08F7-4C0D-ACB1-D734BA44C0C8','2014-02-08 10:01:36.827') ";
 stmt.execute(sql);

 sql = "INSERT Product_JDBC_Sample VALUES ('ML Bottom Bracket','BB-8107','0','0',NULL,'1000','750','0.00','0.00',NULL,NULL,NULL,NULL,'0',NULL,NULL,NULL,NULL,NULL,'2008-04-30 00:00:00.000',NULL,NULL,'694215B7-08F7-4C0D-ACB1-D734BA44C0C8','2014-02-08 10:01:36.827') ";
 stmt.execute(sql);

 sql = "INSERT Product_JDBC_Sample VALUES ('Mountain-500 Black, 44','BK-M18B-44','0','0',NULL,'1000','750','0.00','0.00',NULL,NULL,NULL,NULL,'0',NULL,NULL,NULL,NULL,NULL,'2008-04-30 00:00:00.000',NULL,NULL,'694215B7-08F7-4C0D-ACB1-D734BA44C0C8','2014-02-08 10:01:36.827') ";
 stmt.execute(sql);
 }
 **/