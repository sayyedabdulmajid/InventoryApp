package com.example.inventoryapp.User;

import com.example.inventoryapp.ConnectionFactorys;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;


public class DbHelperUser implements DaoUser {
    private static final String TABLE_NAME = "Users";
    private static final String COLUMN_ID = "Id";
    private static final String COLUMN_USERNAME = "UserName";
    private static final String COLUMN_PASSWORD = "Password";

    public DbHelperUser()
    {
        //createTable();
    }

    @Override
    public User getUser(int id){
        Connection connection = ConnectionFactorys.getConnection();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM "+ TABLE_NAME +" WHERE id=" + id);
            if(rs.next())
            {
                User user = new User();
                user= extractUserFromResultSet(rs);
                connection.close();
                return user;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public User getUserByUserNameAndPasswordViaSQLServer(String user, String pass){
        Connection connection = ConnectionFactorys.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM "+ TABLE_NAME +" WHERE UserName=? AND Password=?");
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            {
                User _user = new User();
                _user= extractUserFromResultSet(rs);
                connection.close();
                return _user;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public User getUserByUserNameAndPassword(String user, String pass){
        Connection connection = ConnectionFactorys.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM "+ TABLE_NAME +" WHERE UserName=? AND Password=?");
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            {
                User _user = new User();
                _user= extractUserFromResultSet(rs);
                connection.close();
                return _user;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<User> getAllUsers() {
        Connection connection = ConnectionFactorys.getConnection();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM "+ TABLE_NAME);
            Set<User> users = new HashSet();
            while(rs.next())
            {
                User user = extractUserFromResultSet(rs);
                users.add(user);
            }
            return users;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    //@Override
    private boolean insertUser(User user) {
        Connection connection = ConnectionFactorys.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO "+ TABLE_NAME +" VALUES (NULL, ?, ?)");
            ps.setString(1, user.getName());
            ps.setString(2, user.getPass());
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

    //@Override
    private boolean updateUser(User user) {
        Connection connection = ConnectionFactorys.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE "+ TABLE_NAME +" SET UserName=?, Password=? WHERE ID=?");
            ps.setString(1, user.getName());
            ps.setString(2, user.getPass());
            ps.setInt(3, user.getId());
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

    //@Override
    private boolean deleteUser(int id) {
        Connection connection = ConnectionFactorys.getConnection();
        try {
            Statement stmt = connection.createStatement();
            int i = stmt.executeUpdate("DELETE FROM "+ TABLE_NAME +" WHERE ID=" + id);
            if(i == 1) {
                connection.close();
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId( rs.getInt(COLUMN_ID) );
        user.setName( rs.getString(COLUMN_USERNAME) );
        user.setPass( rs.getString(COLUMN_PASSWORD) );
        return user;
    }


    private static void createTable() {
        Connection connection = ConnectionFactorys.getConnection();
        try {
            Statement stmt = connection.createStatement();
            String queryIfNotExists = "if not exists (select * from sys.objects where name = '" + TABLE_NAME + "') ";

            String queryCreateTable = "CREATE TABLE [" + TABLE_NAME + "]("
                    + "[" + COLUMN_ID + "] [int] IDENTITY(1,1) NOT NULL,"
                    + "[" + COLUMN_USERNAME + "] [varchar](20) NULL,"
                    + "[" + COLUMN_PASSWORD + "] [varchar](20) NULL" + ",)";

            stmt.execute(queryIfNotExists + queryCreateTable);
        } catch (SQLException ex) {

        }
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