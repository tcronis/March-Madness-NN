package MainFolder;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class AccessDatabase {

    public Connection dbConnection;

    public AccessDatabase() {
        try {
            // JDBC Driver string
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            // Get URL of the database
            String dbUrl = "jdbc:sqlserver://localhost;databaseName=MarchMadness;";
            // connect to database with its URL and the username and password to login
            dbConnection = DriverManager.getConnection(dbUrl, "kole", "school_sucks");

        } catch (ClassNotFoundException e) {
            System.out.println("ERROR, DRIVER NOT FOUND");
            System.exit(1);
        } catch (SQLException e) {
            System.out.println("ERROR, SQL EXCEPTION");
            System.exit(2);
        }
    }

    

    // ****************************************************************************
    // This collection of functions will read the CSV data by statistical category
    // ****************************************************************************
    public void fillTables() throws SQLException {

        for (int i = 2015; i < 2020; i++) {
            String year = Integer.toString(i);
            winLossPercent(dbConnection, year, readCSV("resources/reg_season_data/" + year + "/win_loss.csv"));
            offensiveScoring(dbConnection, year, readCSV("resources/reg_season_data/" + year + "/scoring_offense.csv"));
            defensiveScoring(dbConnection, year, readCSV("resources/reg_season_data/" + year + "/scoring_defense.csv"));
            scoringMargin(dbConnection, year, readCSV("resources/reg_season_data/" + year + "/scoring_margin.csv"));
            fieldGoalPercent(dbConnection, year, readCSV("resources/reg_season_data/" + year + "/fg_percent_off.csv"));
            fieldGoalPercentDefense(dbConnection, year, readCSV("resources/reg_season_data/" + year + "/fg_percent_def.csv"));
            threePointsPerGame(dbConnection, year, readCSV("resources/reg_season_data/" + year + "/3p_game.csv"));
            threePointPercentage(dbConnection, year, readCSV("resources/reg_season_data/" + year + "/3p_percent.csv"));
            threePointPercentageDefense(dbConnection, year, readCSV("resources/reg_season_data/" + year + "/3p_defense.csv"));
            freeThrowPercentage(dbConnection, year, readCSV("resources/reg_season_data/" + year + "/free_throw_percent.csv"));
            reboundMargin(dbConnection, year, readCSV("resources/reg_season_data/" + year + "/rebound_margin.csv"));
            assistsPerGame(dbConnection, year, readCSV("resources/reg_season_data/" + year + "/assist_game.csv"));
            assistTurnoverRatio(dbConnection, year, readCSV("resources/reg_season_data/" + year + "/assist_turn_ratio.csv"));
            blocksPerGame(dbConnection, year, readCSV("resources/reg_season_data/" + year + "/blocks_game.csv"));
            stealsPerGame(dbConnection, year, readCSV("resources/reg_season_data/" + year + "/steals_game.csv"));
            turnoversPerGame(dbConnection, year, readCSV("resources/reg_season_data/" + year + "/turnovers_game.csv"));
            turnoverMargin(dbConnection, year, readCSV("resources/reg_season_data/" + year + "/turnover_margin.csv"));
            foulsPerGame(dbConnection, year, readCSV("resources/reg_season_data/" + year + "/fouls_game.csv"));
        }
    }

    private ArrayList<String[]> readCSV(String path) {
        ArrayList<String[]> nextItem = null;
        Path csvPath = Paths.get(path);

        CSVParser parser = new CSVParserBuilder().withSeparator(',').withIgnoreQuotations(true)
                .withIgnoreLeadingWhiteSpace(false).build();

        try {
            BufferedReader br = Files.newBufferedReader(csvPath);
            CSVReader reader = new CSVReaderBuilder(br).withCSVParser(parser).build();

            String[] nextLine;
            nextItem = new ArrayList<String[]>();

            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length == 1)
                    continue;
                nextItem.add(nextLine);
            }
        } catch (IOException e) {
            System.out.println("[AD] IOException");
        }
        ;
        return nextItem;
    }

    private void winLossPercent(Connection dbConnection, String year, ArrayList<String[]> csvData) {
        for (String[] data : csvData) {
            int numOfWins = Integer.valueOf(data[2]);
            int numOfLosses = Integer.valueOf(data[3]);
            double percentage = Double.valueOf(data[4]);
            BigDecimal roundedPercentage = BigDecimal.valueOf(percentage).setScale(3, RoundingMode.CEILING);

            if (data[1].contains("\'"))
                data[1] = data[1].replace("\'", "");

            try {
                // System.out.println(data[1]);
                String query = "INSERT winLossPercent (YEAR, TEAM_NAME, NUM_WINS, NUM_LOSSES, WIN_PERCENTAGE)"
                        + "VALUES(" + Integer.valueOf(year) + "," + "'" + data[1] + "'," + numOfWins + "," + numOfLosses
                        + "," + roundedPercentage + ");" + "\n";
                Statement statement = dbConnection.createStatement();
                statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void offensiveScoring(Connection dbConnection, String year, ArrayList<String[]> csvData) {
        for (String[] data : csvData) {
            int numOfGames = Integer.valueOf(data[2]);
            int numOfPoints = Integer.valueOf(data[4]);
            BigDecimal avgPoints = BigDecimal.valueOf((double) numOfPoints / numOfGames).setScale(3,
                    RoundingMode.CEILING);

            if (data[1].contains("\'"))
                data[1] = data[1].replace("\'", "");

            try {
                // System.out.println(data[1]);
                String query = "INSERT offensiveScoring (YEAR, TEAM_NAME, NUM_GAMES, NUM_POINTS, AVG_POINTS_PER_GAME)"
                        + "VALUES(" + Integer.valueOf(year) + "," + "'" + data[1] + "'," + numOfGames + ","
                        + numOfPoints + "," + avgPoints + ");" + "\n";
                Statement statement = dbConnection.createStatement();
                statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void defensiveScoring(Connection dbConnection, String year, ArrayList<String[]> csvData) {

        for (String[] data : csvData) {
            int numOfGames = Integer.valueOf(data[2]);
            int numOfPoints = Integer.valueOf(data[4]);
            BigDecimal avgPoints = BigDecimal.valueOf((double) numOfPoints / numOfGames).setScale(3,
                    RoundingMode.CEILING);

            if (data[1].contains("\'"))
                data[1] = data[1].replace("\'", "");

            try {
                // System.out.println(data[1]);
                String query = "INSERT defensiveScoring (YEAR, TEAM_NAME, NUM_GAMES, NUM_OPPONENT_POINTS, AVG_OPPONENT_POINTS_PER_GAME)"
                        + "VALUES(" + Integer.valueOf(year) + "," + "'" + data[1] + "'," + numOfGames + ","
                        + numOfPoints + "," + avgPoints + ");" + "\n";
                Statement statement = dbConnection.createStatement();
                statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void scoringMargin(Connection dbConnection, String year, ArrayList<String[]> csvData) {
        for (String[] data : csvData) {
            int numOfGames = Integer.valueOf(data[2]);
            int numOfPoints = Integer.valueOf(data[4]);
            int oppPoints = Integer.valueOf(data[6]);
            BigDecimal avgPointsPerGame = BigDecimal.valueOf((double) numOfPoints / numOfGames).setScale(3,
                    RoundingMode.CEILING);
            BigDecimal avgOppPointsPerGame = BigDecimal.valueOf((double) oppPoints / numOfGames).setScale(3,
                    RoundingMode.CEILING);
            BigDecimal margin = avgPointsPerGame.subtract(avgOppPointsPerGame).setScale(3, RoundingMode.CEILING);
            int scoringMarginSeason = numOfPoints - oppPoints;

            if (data[1].contains("\'"))
                data[1] = data[1].replace("\'", "");

            try {
                // System.out.println(data[1]);
                String query = "INSERT scoringMargin (YEAR, TEAM_NAME, NUM_GAMES, NUM_POINTS, NUM_OPPONENT_POINTS, SCORING_MARGIN_PER_GAME, SCORING_MARGIN_SEASON)"
                        + "VALUES(" + Integer.valueOf(year) + "," + "'" + data[1] + "'," + numOfGames + ","
                        + numOfPoints + "," + oppPoints + "," + margin + "," + scoringMarginSeason + ");" + "\n";
                Statement statement = dbConnection.createStatement();
                statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void fieldGoalPercent(Connection dbConnection, String year, ArrayList<String[]> csvData) {
        for (String[] data : csvData) {
            int numOfGames = Integer.valueOf(data[2]);
            int numOfFgMade = Integer.valueOf(data[4]);
            int numOfFgAttempts = Integer.valueOf(data[5]);
            BigDecimal numOfFgGame = BigDecimal.valueOf((double) numOfFgMade / numOfGames).setScale(3,
                    RoundingMode.CEILING);
            BigDecimal numOfFgAttemptsGame = BigDecimal.valueOf((double) numOfFgAttempts / numOfGames).setScale(3,
                    RoundingMode.CEILING);
            BigDecimal fgPercent = BigDecimal.valueOf((double) numOfFgMade / numOfFgAttempts).setScale(3,
                    RoundingMode.CEILING);

            if (data[1].contains("\'"))
                data[1] = data[1].replace("\'", "");

            try {
                // System.out.println(data[1]);
                String query = "INSERT fieldGoalPercent (YEAR, TEAM_NAME, NUM_GAMES, NUM_FG_MADE, NUM_FG_PER_GAME, NUM_FG_ATTEMPTS, NUM_FG_ATTEMPTS_PER_GAME, FG_PERCENTAGE)"
                        + "VALUES(" + Integer.valueOf(year) + "," + "'" + data[1] + "'," + numOfGames + ","
                        + numOfFgMade + "," + numOfFgGame + "," + numOfFgAttempts + "," + numOfFgAttemptsGame + ","
                        + fgPercent + ");" + "\n";
                Statement statement = dbConnection.createStatement();
                statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void fieldGoalPercentDefense(Connection dbConnection, String year, ArrayList<String[]> csvData) {
        for (String[] data : csvData) {
            int numOfGames = Integer.valueOf(data[2]);
            int numOfFgMade = Integer.valueOf(data[4]);
            int numOfFgAttempts = Integer.valueOf(data[5]);
            BigDecimal numOfFgGame = BigDecimal.valueOf((double) numOfFgMade / numOfGames).setScale(3,
                    RoundingMode.CEILING);
            BigDecimal numOfFgAttemptsGame = BigDecimal.valueOf((double) numOfFgAttempts / numOfGames).setScale(3,
                    RoundingMode.CEILING);
            BigDecimal fgPercent = BigDecimal.valueOf((double) numOfFgMade / numOfFgAttempts).setScale(3,
                    RoundingMode.CEILING);

            if (data[1].contains("\'"))
                data[1] = data[1].replace("\'", "");

            try {
                // System.out.println(data[1]);
                String query = "INSERT fieldGoalPercentDefense (YEAR, TEAM_NAME, NUM_GAMES, NUM_OPP_FG_MADE, NUM_OPP_FG_ATTEMPTS, NUM_OPP_FG_PER_GAME, NUM_OPP_FG_ATTEMPTS_PER_GAME, OPP_FG_PERCENTAGE)"
                        + "VALUES(" + Integer.valueOf(year) + "," + "'" + data[1] + "'," + numOfGames + ","
                        + numOfFgMade + "," + numOfFgAttempts + "," + numOfFgGame + "," + numOfFgAttemptsGame + ","
                        + fgPercent + ");" + "\n";
                Statement statement = dbConnection.createStatement();
                statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void threePointsPerGame(Connection dbConnection, String year, ArrayList<String[]> csvData) {
        for (String[] data : csvData) {
            int numOfGames = Integer.valueOf(data[2]);
            int numOfThreePointersMade = Integer.valueOf(data[4]);
            BigDecimal threePointsPerGame = BigDecimal.valueOf((double) numOfThreePointersMade / numOfGames).setScale(3,
                    RoundingMode.CEILING);

            if (data[1].contains("\'"))
                data[1] = data[1].replace("\'", "");

            try {
                // System.out.println(data[1]);
                String query = "INSERT threePointsPerGame (YEAR, TEAM_NAME, NUM_GAMES, NUM_3P_MADE, NUM_3P_PER_GAME)"
                        + "VALUES(" + Integer.valueOf(year) + "," + "'" + data[1] + "'," + numOfGames + ","
                        + numOfThreePointersMade + "," + threePointsPerGame + ");" + "\n";
                Statement statement = dbConnection.createStatement();
                statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void threePointPercentage(Connection dbConnection, String year, ArrayList<String[]> csvData) {
        for (String[] data : csvData) {
            int numOfGames = Integer.valueOf(data[2]);
            int numOfThreePointersMade = Integer.valueOf(data[4]);
            int numOfAttempts = Integer.valueOf(data[5]);
            BigDecimal threePointsPerGame = BigDecimal.valueOf((double) numOfThreePointersMade / numOfGames).setScale(3,
                    RoundingMode.CEILING);
            BigDecimal threePointsAttPerGame = BigDecimal.valueOf((double) numOfAttempts / numOfGames).setScale(3,
                    RoundingMode.CEILING);
            BigDecimal threePointsPct = BigDecimal.valueOf((double) numOfThreePointersMade / numOfAttempts).setScale(3,
                    RoundingMode.CEILING);

            if (data[1].contains("\'"))
                data[1] = data[1].replace("\'", "");

            try {
                // System.out.println(data[1]);
                String query = "INSERT threePointPercentage (YEAR, TEAM_NAME, NUM_GAMES, NUM_3P_MADE, NUM_3P_ATTEMPTS, NUM_3P_PER_GAME, NUM_3P_ATTEMPTS_PER_GAME, T3P_PERCENTAGE)"
                        + "VALUES(" + Integer.valueOf(year) + "," + "'" + data[1] + "'," + numOfGames + ","
                        + numOfThreePointersMade + "," + numOfAttempts + "," + threePointsPerGame + ","
                        + threePointsAttPerGame + "," + threePointsPct + ");" + "\n";
                Statement statement = dbConnection.createStatement();
                statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void threePointPercentageDefense(Connection dbConnection, String year, ArrayList<String[]> csvData) {
        for (String[] data : csvData) {
            int numOfGames = Integer.valueOf(data[2]);
            int numOfThreePointersMade = Integer.valueOf(data[5]);
            int numOfAttempts = Integer.valueOf(data[4]);
            BigDecimal threePointsPerGame = BigDecimal.valueOf((double) numOfThreePointersMade / numOfGames).setScale(3,
                    RoundingMode.CEILING);
            BigDecimal threePointsAttPerGame = BigDecimal.valueOf((double) numOfAttempts / numOfGames).setScale(3,
                    RoundingMode.CEILING);
            BigDecimal threePointsPct = BigDecimal.valueOf((double) numOfThreePointersMade / numOfAttempts).setScale(3,
                    RoundingMode.CEILING);

            if (data[1].contains("\'"))
                data[1] = data[1].replace("\'", "");

            try {
                // System.out.println(data[1]);
                String query = "INSERT threePointPercentageDefense (YEAR, TEAM_NAME, NUM_GAMES, NUM_OPP_3P_MADE, NUM_OPP_3P_ATTEMPTS, NUM_OPP_3P_PER_GAME, NUM_OPP_3P_ATTEMPTS_PER_GAME, OPP_3P_PERCENTAGE)"
                        + "VALUES(" + Integer.valueOf(year) + "," + "'" + data[1] + "'," + numOfGames + ","
                        + numOfThreePointersMade + "," + numOfAttempts + "," + threePointsPerGame + ","
                        + threePointsAttPerGame + "," + threePointsPct + ");" + "\n";
                Statement statement = dbConnection.createStatement();
                statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void freeThrowPercentage(Connection dbConnection, String year, ArrayList<String[]> csvData) {
        for (String[] data : csvData) {
            int numOfGames = Integer.valueOf(data[2]);
            int numOfFtMade = Integer.valueOf(data[4]);
            int numOfFtAttempts = Integer.valueOf(data[5]);
            BigDecimal numOfFtGame = BigDecimal.valueOf((double) numOfFtMade / numOfGames).setScale(3,
                    RoundingMode.CEILING);
            BigDecimal numOfFtAttemptsGame = BigDecimal.valueOf((double) numOfFtAttempts / numOfGames).setScale(3,
                    RoundingMode.CEILING);
            BigDecimal ftPercent = BigDecimal.valueOf((double) numOfFtMade / numOfFtAttempts).setScale(3,
                    RoundingMode.CEILING);

            if (data[1].contains("\'"))
                data[1] = data[1].replace("\'", "");

            try {
                // System.out.println(data[1]);
                String query = "INSERT freeThrowPercentage (YEAR, TEAM_NAME, NUM_GAMES, NUM_FT_MADE, NUM_FT_ATTEMPTS, NUM_FT_PER_GAME, NUM_FT_ATTEMPTS_PER_GAME, FT_PERCENTAGE)"
                        + "VALUES(" + Integer.valueOf(year) + "," + "'" + data[1] + "'," + numOfGames + ","
                        + numOfFtMade + "," + numOfFtAttempts + "," + numOfFtGame + "," + numOfFtAttemptsGame + ","
                        + ftPercent + ");" + "\n";
                Statement statement = dbConnection.createStatement();
                statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void reboundMargin(Connection dbConnection, String year, ArrayList<String[]> csvData) {
        for (String[] data : csvData) {
            int numOfGames = Integer.valueOf(data[2]);
            int numOfRebounds = Integer.valueOf(data[4]);
            int numOfOppRebounds = Integer.valueOf(data[6]);
            BigDecimal reboundsGame = BigDecimal.valueOf((double) numOfRebounds / numOfGames).setScale(3,
                    RoundingMode.CEILING);
            BigDecimal oppReboundsGame = BigDecimal.valueOf((double) numOfOppRebounds / numOfGames).setScale(3,
                    RoundingMode.CEILING);
            BigDecimal reboundMargin = reboundsGame.subtract(oppReboundsGame).setScale(3, RoundingMode.CEILING);

            if (data[1].contains("\'"))
                data[1] = data[1].replace("\'", "");

            try {
                // System.out.println(data[1]);
                String query = "INSERT reboundMargin (YEAR, TEAM_NAME, NUM_GAMES, NUM_REBOUNDS, REBOUNDS_PER_GAME, OPP_NUM_REBOUNDS, OPP_REBOUNDS_PER_GAME, REBOUND_MARGIN)"
                        + "VALUES(" + Integer.valueOf(year) + "," + "'" + data[1] + "'," + numOfGames + ","
                        + numOfRebounds + "," + reboundsGame + "," + numOfOppRebounds + "," + oppReboundsGame + ","
                        + reboundMargin + ");" + "\n";
                Statement statement = dbConnection.createStatement();
                statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void assistsPerGame(Connection dbConnection, String year, ArrayList<String[]> csvData) {
        for (String[] data : csvData) {
            int numOfGames = Integer.valueOf(data[2]);
            int numOfAssists = Integer.valueOf(data[4]);
            BigDecimal assistsPerGame = BigDecimal.valueOf((double) numOfAssists / numOfGames).setScale(3,
                    RoundingMode.CEILING);

            if (data[1].contains("\'"))
                data[1] = data[1].replace("\'", "");

            try {
                // System.out.println(data[1]);
                String query = "INSERT assistsPerGame (YEAR, TEAM_NAME, NUM_GAMES, NUM_ASSISTS, ASSISTS_PER_GAME)"
                        + "VALUES(" + Integer.valueOf(year) + "," + "'" + data[1] + "'," + numOfGames + ","
                        + numOfAssists + "," + assistsPerGame + ");" + "\n";
                Statement statement = dbConnection.createStatement();
                statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void assistTurnoverRatio(Connection dbConnection, String year, ArrayList<String[]> csvData) {
        for (String[] data : csvData) {
            int numOfGames = Integer.valueOf(data[2]);
            int numOfAssists = Integer.valueOf(data[4]);
            int numOfTurnovers = Integer.valueOf(data[5]);
            BigDecimal ratio = BigDecimal.valueOf((double) numOfAssists / numOfTurnovers).setScale(3,
                    RoundingMode.CEILING);

            if (data[1].contains("\'"))
                data[1] = data[1].replace("\'", "");

            try {
                // System.out.println(data[1]);
                String query = "INSERT assistTurnoverRatio (YEAR, TEAM_NAME, NUM_GAMES, NUM_ASSISTS, NUM_TURNOVERS, ATO_RATIO)"
                        + "VALUES(" + Integer.valueOf(year) + "," + "'" + data[1] + "'," + numOfGames + ","
                        + numOfAssists + "," + numOfTurnovers + "," + ratio + ");" + "\n";
                Statement statement = dbConnection.createStatement();
                statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void blocksPerGame(Connection dbConnection, String year, ArrayList<String[]> csvData) {
        for (String[] data : csvData) {
            int numOfGames = Integer.valueOf(data[2]);
            int numOfBlocks = Integer.valueOf(data[4]);
            BigDecimal blocksPerGame = BigDecimal.valueOf((double) numOfBlocks / numOfGames).setScale(3,
                    RoundingMode.CEILING);

            if (data[1].contains("\'"))
                data[1] = data[1].replace("\'", "");

            try {
                // System.out.println(data[1]);
                String query = "INSERT blocksPerGame (YEAR, TEAM_NAME, NUM_GAMES, NUM_BLOCKS, BLOCKS_PER_GAME)"
                        + "VALUES(" + Integer.valueOf(year) + "," + "'" + data[1] + "'," + numOfGames + ","
                        + numOfBlocks + "," + blocksPerGame + ");" + "\n";
                Statement statement = dbConnection.createStatement();
                statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void stealsPerGame(Connection dbConnection, String year, ArrayList<String[]> csvData) {
        for (String[] data : csvData) {
            int numOfGames = Integer.valueOf(data[2]);
            int numOfSteals = Integer.valueOf(data[4]);
            BigDecimal stealsPerGame = BigDecimal.valueOf((double) numOfSteals / numOfGames).setScale(3,
                    RoundingMode.CEILING);

            if (data[1].contains("\'"))
                data[1] = data[1].replace("\'", "");

            try {
                // System.out.println(data[1]);
                String query = "INSERT stealsPerGame (YEAR, TEAM_NAME, NUM_GAMES, NUM_STEALS, STEALS_PER_GAME)"
                        + "VALUES(" + Integer.valueOf(year) + "," + "'" + data[1] + "'," + numOfGames + ","
                        + numOfSteals + "," + stealsPerGame + ");" + "\n";
                Statement statement = dbConnection.createStatement();
                statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void turnoversPerGame(Connection dbConnection, String year, ArrayList<String[]> csvData) {
        for (String[] data : csvData) {
            int numOfGames = Integer.valueOf(data[2]);
            int numOfTurnovers = Integer.valueOf(data[4]);
            BigDecimal turnoversPerGame = BigDecimal.valueOf((double) numOfTurnovers / numOfGames).setScale(3,
                    RoundingMode.CEILING);

            if (data[1].contains("\'"))
                data[1] = data[1].replace("\'", "");

            try {
                // System.out.println(data[1]);
                String query = "INSERT turnoversPerGame (YEAR, TEAM_NAME, NUM_GAMES, NUM_TURNOVERS, TURNOVERS_PER_GAME)"
                        + "VALUES(" + Integer.valueOf(year) + "," + "'" + data[1] + "'," + numOfGames + ","
                        + numOfTurnovers + "," + turnoversPerGame + ");" + "\n";
                Statement statement = dbConnection.createStatement();
                statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void turnoverMargin(Connection dbConnection, String year, ArrayList<String[]> csvData) {
        for (String[] data : csvData) {
            int numOfGames = Integer.valueOf(data[2]);
            int numOfOppTurnovers = Integer.valueOf(data[4]);
            int numOfTurnovers = Integer.valueOf(data[5]);
            BigDecimal turnoversGame = BigDecimal.valueOf((double) numOfTurnovers / numOfGames).setScale(3,
                    RoundingMode.CEILING);
            BigDecimal oppTurnoversGame = BigDecimal.valueOf((double) numOfOppTurnovers / numOfGames).setScale(3,
                    RoundingMode.CEILING);
            BigDecimal turnoverMargin = turnoversGame.subtract(oppTurnoversGame).setScale(3, RoundingMode.CEILING);

            if (data[1].contains("\'"))
                data[1] = data[1].replace("\'", "");

            try {
                // System.out.println(data[1]);
                String query = "INSERT turnoverMargin (YEAR, TEAM_NAME, NUM_GAMES, NUM_TURNOVERS, TURNOVERS_PER_GAME, OPP_NUM_TURNOVERS, OPP_TURNOVERS_PER_GAME, TURNOVER_MARGIN)"
                        + "VALUES(" + Integer.valueOf(year) + "," + "'" + data[1] + "'," + numOfGames + ","
                        + numOfTurnovers + "," + turnoversGame + "," + numOfOppTurnovers + "," + oppTurnoversGame + ","
                        + turnoverMargin + ");" + "\n";
                Statement statement = dbConnection.createStatement();
                statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void foulsPerGame(Connection dbConnection, String year, ArrayList<String[]> csvData) {
        for (String[] data : csvData) {
            int numOfGames = Integer.valueOf(data[2]);
            int numOfFouls = Integer.valueOf(data[4]);
            int dq = Integer.valueOf(data[6]);
            BigDecimal foulsPerGame = BigDecimal.valueOf((double) numOfFouls / numOfGames).setScale(3,
                    RoundingMode.CEILING);

            if (data[1].contains("\'"))
                data[1] = data[1].replace("\'", "");

            try {
                // System.out.println(data[1]);
                String query = "INSERT foulsPerGame (YEAR, TEAM_NAME, NUM_GAMES, NUM_FOULS, FOULS_PER_GAME, NUM_DQ)"
                        + "VALUES(" + Integer.valueOf(year) + "," + "'" + data[1] + "'," + numOfGames + "," + numOfFouls
                        + "," + foulsPerGame + "," + dq + ");" + "\n";
                Statement statement = dbConnection.createStatement();
                statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    // ****************************************************************************
    //    END OF PARSING CSV FILES AND FILLING TABLES, GETTERS FOR TABLES BELOW
    //   THEY ALL RETURN A MAP<STRING,DOUBLE>, LOOK IN MAIN.JAVA FOR EXAMPLES
    // ****************************************************************************
    public Map<String, Double> getWinLoss(int year, String college) throws SQLException {
        Statement statement = null;
        Map<String, Double> returnList = new HashMap<String, Double>();

        String query = "SELECT NUM_WINS, NUM_LOSSES, WIN_PERCENTAGE " + 
                       "FROM winLossPercent " + 
                       "WHERE TEAM_NAME = '" + college + "' "
                     + "AND YEAR = " + year + ";\n";

        statement = dbConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            returnList.put("NUM_WINS", (double)rs.getInt("NUM_WINS"));
            returnList.put("NUM_LOSSES", (double)rs.getInt("NUM_LOSSES"));
            returnList.put("WIN_PERCENTAGE", rs.getDouble("WIN_PERCENTAGE"));                        
        }
        return returnList;
    }

    public Map<String, Double> getOffensiveScoring(int year, String college) throws SQLException {
        Statement statement = null;
        Map<String, Double> returnList = new HashMap<String, Double>();

        String query = "SELECT NUM_GAMES, NUM_POINTS, AVG_POINTS_PER_GAME " + 
                       "FROM offensiveScoring " + 
                       "WHERE TEAM_NAME = '" + college + "' "
                     + "AND YEAR = " + year + ";\n";

        statement = dbConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            returnList.put("NUM_GAMES", (double)rs.getInt("NUM_GAMES"));
            returnList.put("NUM_POINTS", (double)rs.getInt("NUM_POINTS"));
            returnList.put("AVG_POINTS_PER_GAME", rs.getDouble("AVG_POINTS_PER_GAME"));            
        }
        return returnList;
    }

    public Map<String, Double> getDefensiveScoring(int year, String college) throws SQLException {
        Statement statement = null;
        Map<String, Double> returnList = new HashMap<String, Double>();

        String query = "SELECT NUM_GAMES, NUM_OPPONENT_POINTS, AVG_OPPONENT_POINTS_PER_GAME " + 
                       "FROM defensiveScoring " + 
                       "WHERE TEAM_NAME = '" + college + "' "
                     + "AND YEAR = " + year + ";\n";

        statement = dbConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            returnList.put("NUM_GAMES", (double)rs.getInt("NUM_GAMES"));
            returnList.put("NUM_OPPONENT_POINTS", (double)rs.getInt("NUM_OPPONENT_POINTS"));
            returnList.put("AVG_OPPONENT_POINTS_PER_GAME", rs.getDouble("AVG_OPPONENT_POINTS_PER_GAME"));            
        }
        return returnList;
    }

    public Map<String, Double> getScoringMargin(int year, String college) throws SQLException {
        Statement statement = null;
        Map<String, Double> returnList = new HashMap<String, Double>();

        String query = "SELECT NUM_GAMES, NUM_POINTS, NUM_OPPONENT_POINTS, SCORING_MARGIN_PER_GAME, SCORING_MARGIN_SEASON " + 
                       "FROM scoringMargin " + 
                       "WHERE TEAM_NAME = '" + college + "' "
                     + "AND YEAR = " + year + ";\n";

        statement = dbConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            returnList.put("NUM_GAMES", (double)rs.getInt("NUM_GAMES"));
            returnList.put("NUM_POINTS", (double)rs.getInt("NUM_POINTS"));
            returnList.put("NUM_OPPONENT_POINTS", (double)rs.getInt("NUM_OPPONENT_POINTS"));
            returnList.put("SCORING_MARGIN_PER_GAME", rs.getDouble("SCORING_MARGIN_PER_GAME"));  
            returnList.put("SCORING_MARGIN_SEASON", (double)rs.getInt("SCORING_MARGIN_SEASON"));          
        }
        return returnList;
    }

    public Map<String, Double> getFieldGoalPercent(int year, String college) throws SQLException {
        Statement statement = null;
        Map<String, Double> returnList = new HashMap<String, Double>();

        String query = "SELECT NUM_GAMES, NUM_FG_MADE, NUM_FG_PER_GAME, NUM_FG_ATTEMPTS, NUM_FG_ATTEMPTS_PER_GAME, FG_PERCENTAGE " + 
                       "FROM fieldGoalPercent " + 
                       "WHERE TEAM_NAME = '" + college + "' "
                     + "AND YEAR = " + year + ";\n";

        statement = dbConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            returnList.put("NUM_GAMES", (double)rs.getInt("NUM_GAMES"));
            returnList.put("NUM_FG_MADE", (double)rs.getInt("NUM_FG_MADE"));
            returnList.put("NUM_FG_PER_GAME", rs.getDouble("NUM_FG_PER_GAME")); 
            returnList.put("NUM_FG_ATTEMPTS", (double)rs.getInt("NUM_FG_ATTEMPTS")); 
            returnList.put("NUM_FG_ATTEMPTS_PER_GAME", rs.getDouble("NUM_FG_ATTEMPTS_PER_GAME"));
            returnList.put("FG_PERCENTAGE", rs.getDouble("FG_PERCENTAGE"));                      
        }
        return returnList;
    }

    public Map<String, Double> getFieldGoalPercentDefense(int year, String college) throws SQLException {
        Statement statement = null;
        Map<String, Double> returnList = new HashMap<String, Double>();

        String query = "SELECT NUM_GAMES, NUM_OPP_FG_MADE, NUM_OPP_FG_ATTEMPTS, NUM_OPP_FG_PER_GAME, NUM_OPP_FG_ATTEMPTS_PER_GAME, OPP_FG_PERCENTAGE " + 
                       "FROM fieldGoalPercentDefense " + 
                       "WHERE TEAM_NAME = '" + college + "' "
                     + "AND YEAR = " + year + ";\n";

        statement = dbConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            returnList.put("NUM_GAMES", (double)rs.getInt("NUM_GAMES"));
            returnList.put("NUM_OPP_FG_MADE", (double)rs.getInt("NUM_OPP_FG_MADE"));
            returnList.put("NUM_OPP_FG_ATTEMPTS", (double)rs.getInt("NUM_OPP_FG_ATTEMPTS"));
            returnList.put("NUM_OPP_FG_PER_GAME", rs.getDouble("NUM_OPP_FG_PER_GAME"));  
            returnList.put("NUM_OPP_FG_ATTEMPTS_PER_GAME", rs.getDouble("NUM_OPP_FG_ATTEMPTS_PER_GAME"));
            returnList.put("OPP_FG_PERCENTAGEs", rs.getDouble("OPP_FG_PERCENTAGE"));                      
        }
        return returnList;
    }

    public Map<String, Double> getThreePointsPerGame(int year, String college) throws SQLException {
        Statement statement = null;
        Map<String, Double> returnList = new HashMap<String, Double>();

        String query = "SELECT NUM_GAMES, NUM_3P_MADE, NUM_3P_PER_GAME " + 
                       "FROM threePointsPerGame " + 
                       "WHERE TEAM_NAME = '" + college + "' "
                     + "AND YEAR = " + year + ";\n";

        statement = dbConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            returnList.put("NUM_GAMES", (double)rs.getInt("NUM_GAMES"));
            returnList.put("NUM_3P_MADE", (double)rs.getInt("NUM_3P_MADE"));
            returnList.put("NUM_3P_PER_GAME", rs.getDouble("NUM_3P_PER_GAME"));                       
        }
        return returnList;
    }

    public Map<String, Double> getThreePointPercentage(int year, String college) throws SQLException {
        Statement statement = null;
        Map<String, Double> returnList = new HashMap<String, Double>();

        String query = "SELECT NUM_GAMES, NUM_3P_MADE, NUM_3P_ATTEMPTS, NUM_3P_PER_GAME, NUM_3P_ATTEMPTS_PER_GAME, T3P_PERCENTAGE " + 
                       "FROM threePointPercentage " + 
                       "WHERE TEAM_NAME = '" + college + "' "
                     + "AND YEAR = " + year + ";\n";

        statement = dbConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            returnList.put("NUM_GAMES", (double)rs.getInt("NUM_GAMES"));
            returnList.put("NUM_3P_MADE", (double)rs.getInt("NUM_3P_MADE"));
            returnList.put("NUM_3P_ATTEMPTS", (double)rs.getInt("NUM_3P_ATTEMPTS"));
            returnList.put("NUM_3P_PER_GAME", rs.getDouble("NUM_3P_PER_GAME"));
            returnList.put("NUM_3P_ATTEMPTS_PER_GAME", rs.getDouble("NUM_3P_ATTEMPTS_PER_GAME"));
            returnList.put("T3P_PERCENTAGE", rs.getDouble("T3P_PERCENTAGE"));                       
        }
        return returnList;
    }

    public Map<String, Double> getThreePointPercentageDefense(int year, String college) throws SQLException {
        Statement statement = null;
        Map<String, Double> returnList = new HashMap<String, Double>();

        String query = "SELECT NUM_GAMES, NUM_OPP_3P_MADE, NUM_OPP_3P_ATTEMPTS, NUM_OPP_3P_PER_GAME, NUM_OPP_3P_ATTEMPTS_PER_GAME, OPP_3P_PERCENTAGE " + 
                       "FROM threePointPercentageDefense " + 
                       "WHERE TEAM_NAME = '" + college + "' "
                     + "AND YEAR = " + year + ";\n";

        statement = dbConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            returnList.put("NUM_GAMES", (double)rs.getInt("NUM_GAMES"));
            returnList.put("NUM_OPP_3P_MADE", (double)rs.getInt("NUM_OPP_3P_MADE"));
            returnList.put("NUM_OPP_3P_ATTEMPTS", (double)rs.getInt("NUM_OPP_3P_ATTEMPTS"));
            returnList.put("NUM_OPP_3P_PER_GAME", rs.getDouble("NUM_OPP_3P_PER_GAME"));
            returnList.put("NUM_OPP_3P_ATTEMPTS_PER_GAME", rs.getDouble("NUM_OPP_3P_ATTEMPTS_PER_GAME"));
            returnList.put("OPP_3P_PERCENTAGE", rs.getDouble("OPP_3P_PERCENTAGE"));                       
        }
        return returnList;
    }

    public Map<String, Double> getFreeThrowPercentage(int year, String college) throws SQLException {
        Statement statement = null;
        Map<String, Double> returnList = new HashMap<String, Double>();

        String query = "SELECT NUM_GAMES, NUM_FT_MADE, NUM_FT_ATTEMPTS, NUM_FT_PER_GAME, NUM_FT_ATTEMPTS_PER_GAME, FT_PERCENTAGE " + 
                       "FROM freeThrowPercentage " + 
                       "WHERE TEAM_NAME = '" + college + "' "
                     + "AND YEAR = " + year + ";\n";

        statement = dbConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            returnList.put("NUM_GAMES", (double)rs.getInt("NUM_GAMES"));
            returnList.put("NUM_FT_MADE", (double)rs.getInt("NUM_FT_MADE"));
            returnList.put("NUM_FT_ATTEMPTS", (double)rs.getInt("NUM_FT_ATTEMPTS"));
            returnList.put("NUM_FT_PER_GAME", rs.getDouble("NUM_FT_PER_GAME"));
            returnList.put("NUM_FT_ATTEMPTS_PER_GAME", rs.getDouble("NUM_FT_ATTEMPTS_PER_GAME"));
            returnList.put("FT_PERCENTAGE", rs.getDouble("FT_PERCENTAGE"));                       
        }
        return returnList;
    }

    public Map<String, Double> getReboundMargin(int year, String college) throws SQLException {
        Statement statement = null;
        Map<String, Double> returnList = new HashMap<String, Double>();

        String query = "SELECT NUM_GAMES, NUM_REBOUNDS, REBOUNDS_PER_GAME, OPP_NUM_REBOUNDS, OPP_REBOUNDS_PER_GAME, REBOUND_MARGIN " + 
                       "FROM reboundMargin " + 
                       "WHERE TEAM_NAME = '" + college + "' "
                     + "AND YEAR = " + year + ";\n";

        statement = dbConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            returnList.put("NUM_GAMES", (double)rs.getInt("NUM_GAMES"));
            returnList.put("NUM_REBOUNDS", (double)rs.getInt("NUM_REBOUNDS"));            
            returnList.put("REBOUNDS_PER_GAME", rs.getDouble("REBOUNDS_PER_GAME"));
            returnList.put("OPP_NUM_REBOUNDS", (double)rs.getInt("OPP_NUM_REBOUNDS"));
            returnList.put("OPP_REBOUNDS_PER_GAME", rs.getDouble("OPP_REBOUNDS_PER_GAME"));
            returnList.put("REBOUND_MARGIN", rs.getDouble("REBOUND_MARGIN"));                       
        }
        return returnList;
    }

    public Map<String, Double> getAssistsPerGame(int year, String college) throws SQLException {
        Statement statement = null;
        Map<String, Double> returnList = new HashMap<String, Double>();

        String query = "SELECT NUM_GAMES, NUM_ASSISTS, ASSISTS_PER_GAME " + 
                       "FROM assistsPerGame " + 
                       "WHERE TEAM_NAME = '" + college + "' "
                     + "AND YEAR = " + year + ";\n";

        statement = dbConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            returnList.put("NUM_GAMES", (double)rs.getInt("NUM_GAMES"));
            returnList.put("NUM_ASSISTS", (double)rs.getInt("NUM_ASSISTS"));            
            returnList.put("ASSISTS_PER_GAME", rs.getDouble("ASSISTS_PER_GAME"));                       
        }
        return returnList;
    }

    public Map<String, Double> getAssistTurnoverRatio(int year, String college) throws SQLException {
        Statement statement = null;
        Map<String, Double> returnList = new HashMap<String, Double>();

        String query = "SELECT NUM_GAMES, NUM_ASSISTS, NUM_TURNOVERS, ATO_RATIO " + 
                       "FROM assistTurnoverRatio " + 
                       "WHERE TEAM_NAME = '" + college + "' "
                     + "AND YEAR = " + year + ";\n";

        statement = dbConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            returnList.put("NUM_GAMES", (double)rs.getInt("NUM_GAMES"));
            returnList.put("NUM_ASSISTS", (double)rs.getInt("NUM_ASSISTS"));
            returnList.put("NUM_TURNOVERS", (double)rs.getInt("NUM_TURNOVERS"));             
            returnList.put("ATO_RATIO", rs.getDouble("ATO_RATIO"));                       
        }
        return returnList;
    }

    public Map<String, Double> getBlocksPerGame(int year, String college) throws SQLException {
        Statement statement = null;
        Map<String, Double> returnList = new HashMap<String, Double>();

        String query = "SELECT NUM_GAMES, NUM_BLOCKS, BLOCKS_PER_GAME " + 
                       "FROM blocksPerGame " + 
                       "WHERE TEAM_NAME = '" + college + "' "
                     + "AND YEAR = " + year + ";\n";

        statement = dbConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            returnList.put("NUM_GAMES", (double)rs.getInt("NUM_GAMES"));
            returnList.put("NUM_BLOCKS", (double)rs.getInt("NUM_BLOCKS"));             
            returnList.put("BLOCKS_PER_GAME", rs.getDouble("BLOCKS_PER_GAME"));                       
        }
        return returnList;
    }

    public Map<String, Double> getStealsPerGame(int year, String college) throws SQLException {
        Statement statement = null;
        Map<String, Double> returnList = new HashMap<String, Double>();

        String query = "SELECT NUM_GAMES, NUM_STEALS, STEALS_PER_GAME " + 
                       "FROM stealsPerGame " + 
                       "WHERE TEAM_NAME = '" + college + "' "
                     + "AND YEAR = " + year + ";\n";

        statement = dbConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            returnList.put("NUM_GAMES", (double)rs.getInt("NUM_GAMES"));
            returnList.put("NUM_STEALS", (double)rs.getInt("NUM_STEALS"));             
            returnList.put("STEALS_PER_GAME", rs.getDouble("STEALS_PER_GAME"));                       
        }
        return returnList;
    }

    public Map<String, Double> getTurnoversPerGame(int year, String college) throws SQLException {
        Statement statement = null;
        Map<String, Double> returnList = new HashMap<String, Double>();

        String query = "SELECT NUM_GAMES, NUM_TURNOVERS, TURNOVERS_PER_GAME " + 
                       "FROM turnoversPerGame " + 
                       "WHERE TEAM_NAME = '" + college + "' "
                     + "AND YEAR = " + year + ";\n";

        statement = dbConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            returnList.put("NUM_GAMES", (double)rs.getInt("NUM_GAMES"));
            returnList.put("NUM_TURNOVERS", (double)rs.getInt("NUM_TURNOVERS"));             
            returnList.put("TURNOVERS_PER_GAME", rs.getDouble("TURNOVERS_PER_GAME"));                       
        }
        return returnList;
    }

    public Map<String, Double> getTurnoverMargin(int year, String college) throws SQLException {
        Statement statement = null;
        Map<String, Double> returnList = new HashMap<String, Double>();

        String query = "SELECT NUM_GAMES, NUM_TURNOVERS, TURNOVERS_PER_GAME, OPP_NUM_TURNOVERS, OPP_TURNOVERS_PER_GAME, TURNOVER_MARGIN " + 
                       "FROM turnoverMargin " + 
                       "WHERE TEAM_NAME = '" + college + "' "
                     + "AND YEAR = " + year + ";\n";

        statement = dbConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            returnList.put("NUM_GAMES", (double)rs.getInt("NUM_GAMES"));
            returnList.put("NUM_TURNOVERS", (double)rs.getInt("NUM_TURNOVERS"));             
            returnList.put("TURNOVERS_PER_GAME", rs.getDouble("TURNOVERS_PER_GAME"));
            returnList.put("OPP_NUM_TURNOVERS", (double)rs.getInt("OPP_NUM_TURNOVERS"));
            returnList.put("OPP_TURNOVERS_PER_GAME", rs.getDouble("OPP_TURNOVERS_PER_GAME"));
            returnList.put("TURNOVER_MARGIN", rs.getDouble("TURNOVER_MARGIN"));                       
        }
        return returnList;
    }

    public Map<String, Double> getFoulsPerGame(int year, String college) throws SQLException {
        Statement statement = null;
        Map<String, Double> returnList = new HashMap<String, Double>();

        String query = "SELECT NUM_GAMES, NUM_FOULS, FOULS_PER_GAME, NUM_DQ " + 
                       "FROM foulsPerGame " + 
                       "WHERE TEAM_NAME = '" + college + "' "
                     + "AND YEAR = " + year + ";\n";

        statement = dbConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            returnList.put("NUM_GAMES", (double)rs.getInt("NUM_GAMES"));
            returnList.put("NUM_FOULS", (double)rs.getInt("NUM_FOULS"));             
            returnList.put("FOULS_PER_GAME", rs.getDouble("FOULS_PER_GAME"));
            returnList.put("NUM_DQ", (double)rs.getInt("NUM_DQ"));                       
        }
        return returnList;
    }    

    public ArrayList<String> getAllTeamsByYear(int year) throws SQLException {
        Statement statement = null;
        ArrayList<String> returnList = new ArrayList<String>();

        String query = "SELECT TEAM_NAME " + 
                       "FROM winLossPercent " + 
                       "WHERE YEAR = " + year + ";\n";

        statement = dbConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            returnList.add(rs.getString("TEAM_NAME"));                    
        }
        return returnList;
    }
}