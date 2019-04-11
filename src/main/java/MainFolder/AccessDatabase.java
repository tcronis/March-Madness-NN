package MainFolder;

import java.sql.*;

public class AccessDatabase {

    public AccessDatabase() {

    }

    public void connectToDatabase() {
        Connection serverConnection = null;
        try {
            //JDBC Driver string
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            
            String serverUrl = "jdbc:sqlserver://localhost;databaseName=MarchMadness;";

            serverConnection = DriverManager.getConnection(serverUrl, "kole", "school_sucks");

            Statement stmt = serverConnection.createStatement();
            String SQL = "SELECT * FROM WINLOSS";
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                System.out.println(""+rs.getInt("ID") + "\t, "+rs.getString("RANKING")+"\t"+rs.getString("COLLEGE")+
                "\t"+rs.getString("WIN")+"\t"+rs.getString("LOSS")+"\t"+rs.getString("PCT")+"\n");
            }

        } catch (ClassNotFoundException e) {
            System.out.println("ERROR, DRIVER NOT FOUND");
            System.exit(1);
        } catch (SQLException e) {
            System.out.println("ERROR, SQL EXCEPTION");
            System.exit(2);
        }
    }
}