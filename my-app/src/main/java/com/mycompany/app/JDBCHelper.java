package com.mycompany.app;
import java.sql.*;

public class JDBCHelper
{
    private static Connection connection;

    static
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch ( ClassNotFoundException e )
        {
            System.out.println( "Driver class not found" );
        }
    }

    public static Connection getConnection() throws SQLException
    {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/curiculum", "root", "focus3V");
        return connection;
    }

    public static void closeConnection( Connection con ) throws SQLException
    {
        if ( con != null )
        {
            con.close();
        }
    }

    public static void closePreparedStatement( PreparedStatement stmt ) throws SQLException
    {
        if ( stmt != null )
        {
            stmt.close();
        }
    }

    public static void closeResultSet( ResultSet rs ) throws SQLException
    {
        if ( rs != null )
        {
            rs.close();
        }
    }

}