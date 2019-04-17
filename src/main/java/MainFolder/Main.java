package MainFolder;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neuroph.core.*;
import org.neuroph.core.data.*;
import org.neuroph.nnet.*;

public class Main {
    public static void main( String[] args ) {
        
        AccessDatabase ad = new AccessDatabase();
        Map<String, Double> result, result2;
        Map<String, Map<String, Double>> all2015Info;
        List<String> teamNames;

        try { 
            //ad.fillTables();
            
            System.out.println("WL: " + ad.getWinLoss(2015, "Kentucky"));
            result = ad.getWinLoss(2015, "Kentucky");
            System.out.println(result.get("NUM_WINS"));
            System.out.println(result.get("NUM_LOSSES")); 
            System.out.println(result.get("WIN_PERCENTAGE"));
            System.out.println(result.values());

            System.out.println("\nOS: " + ad.getOffensiveScoring(2015, "Kentucky"));
            result2 = ad.getOffensiveScoring(2015, "Kentucky");
            System.out.println(result2.get("AVG_POINTS_PER_GAME"));
            System.out.println(result2.get("NUM_GAMES")); 
            System.out.println(result2.get("NUM_POINTS"));

            result.putAll(result2);
            System.out.println("MERGED: " + result);

            System.out.println("\nDS: " + ad.getDefensiveScoring(2015, "Kentucky"));
            System.out.println("\nSM: " + ad.getScoringMargin(2015, "Kentucky"));
            System.out.println("\nFGP: " + ad.getFieldGoalPercent(2015, "Kentucky"));
            System.out.println("\n-----------------------------------------------------------------------------------");
            
            //nested map
            all2015Info = new HashMap<String,Map<String,Double>>();

            //an ArrayList<String>, getAllTeamsByYear() returns an ArrayList<String>
            teamNames = ad.getAllTeamsByYear(2015);
            
            //iterate over every String in the above arraylist, recall it holds all the college names
            for (String college : teamNames) {    
                //get the winLoss of a college in the array list, store in a Map<String, Double>            
                result = ad.getWinLoss(2015, college);
                //merge the winLoss Map with the offensiveScoringMap, continue over every table type
                result.putAll(ad.getOffensiveScoring(2015, college));
                result.putAll(ad.getDefensiveScoring(2015, college));
                result.putAll(ad.getScoringMargin(2015, college));
                result.putAll(ad.getFieldGoalPercent(2015, college));
                result.putAll(ad.getFieldGoalPercentDefense(2015, college));
                result.putAll(ad.getThreePointsPerGame(2015, college));
                result.putAll(ad.getThreePointPercentage(2015, college));
                result.putAll(ad.getThreePointPercentageDefense(2015, college));
                result.putAll(ad.getFreeThrowPercentage(2015, college));
                result.putAll(ad.getReboundMargin(2015, college));
                result.putAll(ad.getAssistsPerGame(2015, college));
                result.putAll(ad.getAssistTurnoverRatio(2015, college));
                result.putAll(ad.getBlocksPerGame(2015, college));
                result.putAll(ad.getStealsPerGame(2015, college));
                result.putAll(ad.getTurnoversPerGame(2015, college));
                result.putAll(ad.getTurnoverMargin(2015, college));
                result.putAll(ad.getFoulsPerGame(2015, college));
                //put all the combined keys:values into a nested map. This maps key is the team/college
                //name, its value is another map that holds NUM_GAMES, NUM_POINTS, etc.
                all2015Info.put(college, result);
            }
            //Print the values for College "SIUE"
            System.out.println(all2015Info.get("SIUE").values());
            Double[] test = all2015Info.get("SIUE").values().toArray(new Double[all2015Info.get("SIUE").size()]);
            
            
        } catch (SQLException e) {}; 
        
        // // create new perceptron network
        // NeuralNetwork neuralNetwork = new Perceptron(2, 1);
        // // create training set
        // DataSet trainingSet = new DataSet(2, 1);
        // // add training data to training set (logical OR function)
        // trainingSet. addRow (new DataSetRow (new double[]{0, 0},
        // new double[]{0}));
        // trainingSet. addRow (new DataSetRow (new double[]{0, 1},
        // new double[]{1}));
        // trainingSet. addRow (new DataSetRow (new double[]{1, 0},
        // new double[]{1}));
        // trainingSet. addRow (new DataSetRow (new double[]{1, 1},
        // new double[]{1}));
        // // learn the training set
        // neuralNetwork.learn(trainingSet);
        // // save the trained network into file
        // neuralNetwork.save("or_perceptron.nnet");

        // // load the saved network
        // NeuralNetwork neuralNetwork2 = NeuralNetwork.createFromFile("or_perceptron.nnet");
        // // set network input
        // neuralNetwork2.setInput(1, 1);
        // // calculate network
        // neuralNetwork2.calculate();
        // // get network output
        // double[] networkOutput = neuralNetwork2.getOutput();       
    }
}
