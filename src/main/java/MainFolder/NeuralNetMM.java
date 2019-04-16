package MainFolder;

import java.util.List;

import org.neuroph.core.*;
import org.neuroph.nnet.*;
import org.neuroph.core.data.*;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.NeuralNetwork;

import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.nnet.learning.MomentumBackpropagation;


public class NeuralNetMM{
    
    private NeuralNetwork network;
    private DataSet trainingSet;
    private Double learningRate;
    private int [] numLayers;
    private int numOutputs;
    private int numInputs;
    private String networkName;
    

    public NeuralNetMM(String networkName, List<Integer> layers, int dataSetInputAmount, int dataSetOutputAmount){
        this.networkName = networkName;
        this.network = new MultiLayerPerceptron(layers);
        this.trainingSet = new DataSet(dataSetInputAmount, dataSetOutputAmount);
    }

    //setters
    public void setLearningRate(Double value){
        learningRate = value;
    }
    public void setNumLayers(int [] value){
        numLayers = value;
    }
    public void setNumInputs(int value){
        numInputs = value;
    }
    public void setNumOutputs(int value){
        numOutputs = value;
    }

    

    //expect a combined stirng array with name of the winner and then all of its double values
    public void trainingOnTwoInputs(Double [] value1, Double [] input2, Double winnerValue){
        

        
        
        
        // trainingSet. addRow (new DataSetRow (new String[]{0, 0}, new double[]{0}));    
    }



    public void trainingOnOneInput(String input){

    }

    //learning
    public void learnNetwork(DataSet trainingSet){

    }


    public void saveNetwork(){
        network.save(networkName);
    }

    public void openNetworkFromFile(String fileNetworkName){
        
        try{
            network = NeuralNetwork.createFromFile(fileNetworkName);
            networkName = fileNetworkName;
        } catch (Exception e){
            e.printStackTrace();
        }
        
    }



    public double [] getOutput(){
        if(!network.isEmpty()){
            return network.getOutput();
        }
        else{
            return null;
        }
}