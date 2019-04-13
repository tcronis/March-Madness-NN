package MainFolder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.sql.*;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class AccessDatabase {    
    
    public AccessDatabase() {

    }

    public void connectToDatabase() {
        
        Connection dbConnection = null;
        
        try {
            //JDBC Driver string
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            //Get URL of the database        
            String dbUrl = "jdbc:sqlserver://localhost;databaseName=MarchMadness;";
            //connect to database with its URL and the username and password to login
            dbConnection = DriverManager.getConnection(dbUrl, "kole", "school_sucks"); 
    
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR, DRIVER NOT FOUND");
            System.exit(1);
        } catch (SQLException e) {
            System.out.println("ERROR, SQL EXCEPTION");
            System.exit(2);
        }
    }

    //****************************************************************************
    //This collection of functions will read the CSV data by statistical category
    //****************************************************************************
    public void fillTables(Connection dbConnection) throws SQLException {        

        winLossPercent(dbConnection);
        offensiveScoring(dbConnection);
        defensiveScoring(dbConnection);
        fieldGoalPercent(dbConnection);
        fieldGoalPercentDefense(dbConnection);
        threePointsPerGame(dbConnection);
        threePointPercentage(dbConnection);
        threePointPercentageDefense(dbConnection);
        freeThrowPercentage(dbConnection);
        reboundMargin(dbConnection);
        assistsPerGame(dbConnection);
        assistTurnoverRatio(dbConnection);
        blocksPerGame(dbConnection);
        stealsPerGame(dbConnection);
        turnoversPerGame(dbConnection);
        turnoverMargin(dbConnection);
        foulsPerGame(dbConnection);
    }

    private void readCSV(String path) {
        
        Path csvPath = Paths.get(path);        

        CSVParser parser = new CSVParserBuilder()
        .withSeparator(',')
        .withIgnoreQuotations(true)
        .withEscapeChar('l') //gonna have to delimit each file myself.
        .build();

        try {
            BufferedReader br = Files.newBufferedReader(csvPath);
            CSVReader reader = new CSVReaderBuilder(br)
            .withCSVParser(parser)
            .build();
        } catch (IOException e) {};

        
    }

    private void winLossPercent(Connection dbConnection) {
        
        try {
            String query = "INSERT INTO winLossPercent VALUES ()";
            Statement statement = dbConnection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }       
    }   
    private void offensiveScoring(Connection dbConnection) {
        
    }
    private void defensiveScoring(Connection dbConnection) {
        
    }
    private void scoringMargin(Connection dbConnection) {
        
    }
    private void fieldGoalPercent(Connection dbConnection) {
        
    }
    private void fieldGoalPercentDefense(Connection dbConnection) {
        
    }
    private void threePointsPerGame(Connection dbConnection) {
        
    }
    private void threePointPercentage(Connection dbConnection) {
        
    }
    private void threePointPercentageDefense(Connection dbConnection) {
        
    }
    private void freeThrowPercentage(Connection dbConnection) {
        
    }
    private void reboundMargin(Connection dbConnection) {
        
    }
    private void assistsPerGame(Connection dbConnection) {
        
    }
    private void assistTurnoverRatio(Connection dbConnection) {
        
    }
    private void blocksPerGame(Connection dbConnection) {
        
    }
    private void stealsPerGame(Connection dbConnection) {
        
    }
    private void turnoversPerGame(Connection dbConnection) {
        
    }
    private void turnoverMargin(Connection dbConnection) {
        
    }
    private void foulsPerGame(Connection dbConnection) {
        
    }

    // ResultSet dbResponse = statement.executeQuery(query);//send the query, then store the db response
    //     while (dbResponse.next()) {
    //         System.out.println(""+dbResponse.getInt("ID") + "\t, "+dbResponse.getString("RANKING")+"\t"+dbResponse.getString("COLLEGE")+
    //         "\t"+dbResponse.getString("WIN")+"\t"+dbResponse.getString("LOSS")+"\t"+dbResponse.getString("PCT")+"\n");
    //     }
}