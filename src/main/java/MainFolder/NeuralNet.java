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
    private Double learningRate;
    private int [] numLayers;
    private int numInputs;
    private int numOutputs;

    public NeuralNetMM(List<Integer> layers){
        network = new MultiLayerPerceptron(layers);
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


    //learning
    public void learnNetwork(DataSet trainingSet){

    }



}